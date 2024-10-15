package ru.koshakmine.icstd.block.blockentity.ticking;

import com.zhekasmirnov.apparatus.util.Java8BackComp;
import ru.koshakmine.icstd.ICSTD;
import ru.koshakmine.icstd.block.blockentity.BlockEntityBase;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.ChunkPos;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class TickingSystemBlockEntity {
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>>> dimensions = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public TickingSystemBlockEntity(Boolean isServer){
        Event.onCall(Events.LevelLeft, (args) -> dimensions.clear());
        if(isServer != null) Event.onCall(isServer ? Events.tick : Events.LocalTick, (args) -> {
            try {
                AtomicInteger count = new AtomicInteger();

                dimensions.forEach((dimension, chunks) -> {
                    final Level level = Level.getForDimension(dimension);

                    chunks.forEach(((pos, list) -> {
                        if(level.isChunkLoaded(pos.x, pos.z) &&
                                level.isChunkLoaded(pos.x - 1, pos.z - 1) && level.isChunkLoaded(pos.x - 1, pos.z + 1) &&
                                level.isChunkLoaded(pos.x + 1, pos.z - 1) && level.isChunkLoaded(pos.x + 1, pos.z + 1)){
                            count.getAndIncrement();
                            ICSTD.onMultiThreadRun(executor, () -> {
                                onTickChunk(list);
                                count.addAndGet(-1);
                            });
                        }
                    }));
                });

                while (ICSTD.MULTI_THREAD && count.get() != 0){
                    Thread.sleep(1L);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected void onTickChunk(ConcurrentLinkedDeque<BlockEntityBase> list){
        final Iterator<BlockEntityBase> it = list.iterator();
        while (it.hasNext()) {
            final BlockEntityBase entity = it.next();
            if(!entity.canRemove() && entity.canInitialization())
                ((ITickingBlockEntity) entity).onTick();
        }
    }

    public ConcurrentLinkedDeque<BlockEntityBase> getTiles(Level level, int x, int z){
        final ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>> chunks = Java8BackComp.computeIfAbsent(dimensions, level.getDimension(), (Function<Integer, ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>>>) integer -> new ConcurrentHashMap<>());
        return Java8BackComp.computeIfAbsent(chunks, new ChunkPos(x / 16, z / 16), (Function<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>>) chunkPos -> new ConcurrentLinkedDeque<>());
    }

    public void addBlockEntity(BlockEntityBase entity){
        getTiles(entity.getLevel(), entity.x, entity.z).push(entity);
    }

    public BlockEntityBase getBlockEntity(Level level, int x, int y, int z){
        final Iterator<BlockEntityBase> it = getTiles(level, x, z).iterator();
        while (it.hasNext()) {
            final BlockEntityBase entity = it.next();
            if (entity.x == x && entity.y == y && entity.z == z) {
                return entity;
            }
        }
        return null;
    }

    public void removeBlockEntity(BlockEntityBase removed){
        final Iterator<BlockEntityBase> it = getTiles(removed.getLevel(), removed.x, removed.z).iterator();
        while (it.hasNext()){
            final BlockEntityBase entity = it.next();
            if(entity == removed){
                it.remove();
                return;
            }
        }
    }
}
