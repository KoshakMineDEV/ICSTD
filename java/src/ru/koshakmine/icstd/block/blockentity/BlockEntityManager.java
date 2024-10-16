package ru.koshakmine.icstd.block.blockentity;

import ru.koshakmine.icstd.ICSTD;
import ru.koshakmine.icstd.block.blockentity.ticking.ITickingBlockEntity;
import ru.koshakmine.icstd.block.blockentity.ticking.TickingSystemBlockEntity;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.runtime.PostLevelLoaded;
import ru.koshakmine.icstd.runtime.saver.IRuntimeSaveObject;
import ru.koshakmine.icstd.runtime.saver.Saver;
import ru.koshakmine.icstd.type.common.Position;

import java.util.Iterator;
import java.util.concurrent.*;

public class BlockEntityManager {
    public interface IUpdateBlockEntity {
        void apply(BlockEntityBase entity);
    }

    private final ConcurrentLinkedQueue<BlockEntityBase> allEntity = new ConcurrentLinkedQueue<>();
    private final NetworkSide side;
    private final TickingSystemBlockEntity SYSTEM;
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    boolean isEnd = false;

    public BlockEntityManager(IUpdateBlockEntity aboba, NetworkSide side){
        this.side = side;
        SYSTEM = new TickingSystemBlockEntity(side == NetworkSide.SERVER);

        Event.onCall(Events.LevelLeft, args -> {
            allEntity.clear();
        }, -5);

        final String name = side == NetworkSide.SERVER ? Events.tick : Events.LocalTick;

        Event.onCall(name, args -> {
            isEnd = false;
            ICSTD.onMultiThreadRun(executor, () -> {
                final Iterator<BlockEntityBase> it = allEntity.iterator();

                while (it.hasNext()) {
                    final BlockEntityBase entity = it.next();
                    aboba.apply(entity);
                    if (entity.canDestroyBlockEntity()) {
                        if(entity instanceof ITickingBlockEntity) SYSTEM.removeBlockEntity(entity);
                        entity.removeBlockEntity();
                        it.remove();
                    }
                }

                isEnd = true;
            });

        }, 10);


        Event.onCall(name, (args) -> {
            try {
                while (ICSTD.MULTI_THREAD && !isEnd){
                    Thread.sleep(1L);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, -10);
    }

    public void removeBlockEntity(BlockEntityBase entity){
        final Iterator<BlockEntityBase> it = allEntity.iterator();
        while (it.hasNext()) {
            final BlockEntityBase base = it.next();
            if(entity == base){
                if(entity instanceof ITickingBlockEntity) SYSTEM.removeBlockEntity(entity);
                entity.removeBlockEntity();
                it.remove();
                return;
            }
        }
    }

    public boolean addBlockEntity(BlockEntityBase entity){
        final BlockEntityBase coordsEnitty = getBlockEntity(entity.getPosition(), entity.getLevel());

        if(coordsEnitty == null) {
            allEntity.add(entity);

            if(entity instanceof ITickingBlockEntity) SYSTEM.addBlockEntity(entity);
            if(entity instanceof IRuntimeSaveObject) Saver.addSaver((IRuntimeSaveObject) entity);

            if(this.side == NetworkSide.LOCAL) {
                PostLevelLoaded.LOCAL.run(() -> {
                    if(!entity.canRemove()) {
                        entity.onInit();
                        entity.fullInit = true;
                    }
                });
            }else{
                PostLevelLoaded.SERVER.run(() -> {
                    if(!entity.canRemove()) {
                        entity.onInit();
                        entity.fullInit = true;
                    }
                });
            }

            return true;
        }

        return false;
    }

    public BlockEntityBase getBlockEntity(int x, int y, int z, int dimension){
        return getBlockEntity(new Position(x, y, z), Level.getForDimension(dimension));
    }

    public BlockEntityBase getBlockEntity(Position position, Level region){
        final int dimension = region.getDimension();

        for(final BlockEntityBase entity : allEntity){
            if(position.equalsPosition(entity.getPosition()) && dimension == entity.dimension)
                return entity;
        }

        return null;
    }
}
