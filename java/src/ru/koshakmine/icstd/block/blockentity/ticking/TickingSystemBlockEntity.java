package ru.koshakmine.icstd.block.blockentity.ticking;

import com.zhekasmirnov.apparatus.util.Java8BackComp;
import ru.koshakmine.icstd.ICSTD;
import ru.koshakmine.icstd.block.blockentity.BlockEntityBase;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.ChunkPos;

import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class TickingSystemBlockEntity {
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<Long, ConcurrentLinkedDeque<BlockEntityBase>>> dimensions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<Long, ConcurrentLinkedDeque<BlockEntityBase>>> loadedTiles = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public TickingSystemBlockEntity(Boolean isServer){
        Event.onCall(Events.LevelLeft, (args) -> dimensions.clear());
        if(isServer != null) {
            if(isServer){
                Event.onChunkLoaded((this::onChunkLoaded));
                Event.onChunkDiscarded(this::onChunkDiscarded);
            } else {
                Event.onLocalChunkLoaded((this::onChunkLoaded));
                Event.onLocalChunkDiscarded(this::onChunkDiscarded);
            }
            Event.onCall(isServer ? Events.tick : Events.LocalTick, (args) -> {
                try {
                    final AtomicInteger count = new AtomicInteger();

                    loadedTiles.forEach((dimension, chunks) -> {
                        chunks.forEach(((hash, list) -> {
                            count.getAndIncrement();
                            ICSTD.onMultiThreadRun(executor, () -> {
                                onTickChunk(list);
                                count.addAndGet(-1);
                            });
                            /*if(level.isChunkLoaded(pos.x, pos.z) &&
                                    level.isChunkLoaded(pos.x - 1, pos.z - 1) && level.isChunkLoaded(pos.x - 1, pos.z + 1) &&
                                    level.isChunkLoaded(pos.x + 1, pos.z - 1) && level.isChunkLoaded(pos.x + 1, pos.z + 1))*/
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
    }

    private static Long hashChunkPos(int x, int z){
        return (((long)x) << 32) | (z & 0xffffffffL);
    }

    public ConcurrentHashMap<Long, ConcurrentLinkedDeque<BlockEntityBase>> getChunks(int dimension, ConcurrentHashMap<Integer, ConcurrentHashMap<Long, ConcurrentLinkedDeque<BlockEntityBase>>> aboby){
        return Java8BackComp.computeIfAbsent(aboby, dimension, (Function<Integer, ConcurrentHashMap<Long, ConcurrentLinkedDeque<BlockEntityBase>>>) integer -> new ConcurrentHashMap<>());
    }

    public ConcurrentLinkedDeque<BlockEntityBase> geTilesOrCreate(Long hash, ConcurrentHashMap<Long, ConcurrentLinkedDeque<BlockEntityBase>> aboby){
        return Java8BackComp.computeIfAbsent(aboby, hash, (Function<Long, ConcurrentLinkedDeque<BlockEntityBase>>) _hash -> new ConcurrentLinkedDeque<>());

    }

    private void onChunkLoaded(int chunkX, int chunkZ, int dimension) {
        synchronized (loadedTiles){
            final ConcurrentHashMap<Long, ConcurrentLinkedDeque<BlockEntityBase>> loadedChunks = getChunks(dimension, loadedTiles);
            final ConcurrentHashMap<Long, ConcurrentLinkedDeque<BlockEntityBase>> allChunks = getChunks(dimension, dimensions);

            final Long hash = hashChunkPos(chunkX, chunkZ);
            loadedChunks.put(hash, geTilesOrCreate(hash, allChunks));
        }
    }

    private void onChunkDiscarded(int chunkX, int chunkZ, int dimension) {
        synchronized (loadedTiles){
            final ConcurrentHashMap<Long, ConcurrentLinkedDeque<BlockEntityBase>> chunks = getChunks(dimension, loadedTiles);
            chunks.remove(hashChunkPos(chunkX, chunkZ));
        }
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
        final ConcurrentHashMap<Long, ConcurrentLinkedDeque<BlockEntityBase>> chunks = getChunks(level.getDimension(), dimensions);
        return Java8BackComp.computeIfAbsent(chunks, hashChunkPos(x / 16, z / 16), (Function<Long, ConcurrentLinkedDeque<BlockEntityBase>>) hash -> new ConcurrentLinkedDeque<>());
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
