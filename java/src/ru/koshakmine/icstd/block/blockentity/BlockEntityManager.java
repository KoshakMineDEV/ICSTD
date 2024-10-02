package ru.koshakmine.icstd.block.blockentity;

import com.zhekasmirnov.innercore.api.runtime.other.PrintStacking;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.runtime.PostLevelLoaded;
import ru.koshakmine.icstd.runtime.Updatable;
import ru.koshakmine.icstd.runtime.saver.IRuntimeSaveObject;
import ru.koshakmine.icstd.runtime.saver.Saver;
import ru.koshakmine.icstd.type.common.Position;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockEntityManager {
    private final ConcurrentLinkedQueue<BlockEntityBase> allEntity = new ConcurrentLinkedQueue<>();

    interface IUpdateBlockEntity {
        void apply(BlockEntityBase entity);
    }

    private final NetworkSide side;

    public BlockEntityManager(String callbackName, IUpdateBlockEntity aboba, NetworkSide side){
        this.side = side;

        Event.onCall(Events.LevelLeft, args -> {
            allEntity.clear();
        }, -5);

        if(callbackName != null) {
            Event.onCall(callbackName, args -> {
                final Iterator<BlockEntityBase> it = allEntity.iterator();
                while (it.hasNext()) {
                    final BlockEntityBase entity = it.next();
                    aboba.apply(entity);
                    if (entity.canDestroyBlockEntity()) {
                        entity.removeBlockEntity();
                        it.remove();
                    }
                }
            });
        }
    }

    public boolean addBlockEntity(BlockEntityBase entity){
        final BlockEntityBase coordsEnitty = getBlockEntity(entity.getPosition(), entity.getLevel());

        if(coordsEnitty == null) {
            allEntity.add(entity);

            if(entity instanceof ITickingBlockEntity) Updatable.addUpdatable(entity);
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

    public BlockEntityBase getBlockEntity(Position position, Level region){
        final int dimension = region.getDimension();

        for(final BlockEntityBase entity : allEntity){
            if(position.equalsPosition(entity.getPosition()) && dimension == entity.dimension)
                return entity;
        }

        return null;
    }
}
