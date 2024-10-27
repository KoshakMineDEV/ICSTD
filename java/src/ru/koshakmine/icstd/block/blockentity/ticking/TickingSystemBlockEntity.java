package ru.koshakmine.icstd.block.blockentity.ticking;

import com.zhekasmirnov.apparatus.util.Java8BackComp;
import com.zhekasmirnov.innercore.api.NativeAPI;
import ru.koshakmine.icstd.ICSTD;
import ru.koshakmine.icstd.block.blockentity.BlockEntityBase;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.ChunkPos;

import java.util.Iterator;
import java.util.concurrent.*;
import java.util.function.Function;

public class TickingSystemBlockEntity {
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>>> dimensions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>>> loadedTiles = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public TickingSystemBlockEntity(Boolean isServer){
        Event.onCall(Events.LevelLeft, (args) -> dimensions.clear());
        if(isServer != null) {
            NativeAPI.setChunkStateChangeCallbackEnabled(-1, true);
            NativeAPI.setChunkStateChangeCallbackEnabled(9, true);

            if(isServer){
                Event.onChunkLoadingStateChanged(((chunkX, chunkZ, dimension, preState, state, discarded) -> {
                    if(state == 9){
                        this.onChunkLoaded(chunkX, chunkZ, dimension);
                    }
                }));
                Event.onChunkLoadingStateChanged(((chunkX, chunkZ, dimension, preState, state, discarded) -> {
                    if(discarded){
                        this.onChunkLoaded(chunkX, chunkZ, dimension);
                    }
                }));
            } else {
                Event.onLocalChunkLoadingStateChanged(((chunkX, chunkZ, dimension, preState, state, discarded) -> {
                    if(state == 9){
                        this.onChunkLoaded(chunkX, chunkZ, dimension);
                    }
                }));
                Event.onLocalChunkLoadingStateChanged(((chunkX, chunkZ, dimension, preState, state, discarded) -> {
                    if(discarded){
                        this.onChunkLoaded(chunkX, chunkZ, dimension);
                    }
                }));
            }
            Event.onCall(isServer ? Events.tick : Events.LocalTick, (args) -> {
                try {
                    loadedTiles.forEach((dimension, chunks) -> {
                        final Level level = Level.getForDimension(dimension);

                        chunks.forEach(((pos, list) -> {
                            ICSTD.onMultiThreadRun(executor, () -> {
                                onTickChunk(list, pos.x, pos.z, level);
                            });
                        }));
                    });

                    if(ICSTD.MULTI_THREAD){
                        executor.shutdown();
                        while (!executor.awaitTermination(1L, TimeUnit.MILLISECONDS)){}
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>> getChunks(int dimension, ConcurrentHashMap<Integer, ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>>> aboby){
        return Java8BackComp.computeIfAbsent(aboby, dimension, (Function<Integer, ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>>>) integer -> new ConcurrentHashMap<>());
    }

    public ConcurrentLinkedDeque<BlockEntityBase> geTilesOrCreate(ChunkPos hash, ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>> aboby){
        return Java8BackComp.computeIfAbsent(aboby, hash, (Function<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>>) _hash -> new ConcurrentLinkedDeque<>());

    }

    private void onChunkLoaded(int chunkX, int chunkZ, int dimension) {
        synchronized (loadedTiles){
            final ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>> loadedChunks = getChunks(dimension, loadedTiles);
            final ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>> allChunks = getChunks(dimension, dimensions);

            final ChunkPos hash = new ChunkPos(chunkX, chunkZ);
            loadedChunks.put(hash, geTilesOrCreate(hash, allChunks));
        }
    }

    private void onChunkDiscarded(int chunkX, int chunkZ, int dimension) {
        synchronized (loadedTiles){
            final ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>> chunks = getChunks(dimension, loadedTiles);
            chunks.remove(new ChunkPos(chunkX, chunkZ));
        }
    }


    protected void onTickChunk(ConcurrentLinkedDeque<BlockEntityBase> list, int chunkX, int chunkZ, Level level){
        final Iterator<BlockEntityBase> it = list.iterator();

        while (it.hasNext() && level.isChunkLoaded(chunkX, chunkZ)) {
            final BlockEntityBase entity = it.next();
            if(!entity.canRemove() && entity.canInitialization() )
                ((TickingBlockEntityComponent) entity).onTick();
        }
    }

    public ConcurrentLinkedDeque<BlockEntityBase> getTiles(Level level, int x, int z){
        final ConcurrentHashMap<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>> chunks = getChunks(level.getDimension(), dimensions);
        return Java8BackComp.computeIfAbsent(chunks, new ChunkPos(x, z), (Function<ChunkPos, ConcurrentLinkedDeque<BlockEntityBase>>) hash -> new ConcurrentLinkedDeque<>());
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
