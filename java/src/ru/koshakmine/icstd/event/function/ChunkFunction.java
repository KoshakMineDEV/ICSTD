package ru.koshakmine.icstd.event.function;

public interface ChunkFunction {
    void call(int dimension, int chunkX, int chunkZ, boolean isServer);
}
