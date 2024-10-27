package ru.koshakmine.icstd.event.function;

public interface IChunkLoadingStateChanged {
    void call(int chunkX, int chunkZ, int dimension, int preState, int state, boolean discarded);
}
