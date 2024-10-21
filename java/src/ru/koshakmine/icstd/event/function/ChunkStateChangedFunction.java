package ru.koshakmine.icstd.event.function;

public interface ChunkStateChangedFunction {
    void call(int chunkX, int chunkZ, int dimension, int preState, int state);
}
