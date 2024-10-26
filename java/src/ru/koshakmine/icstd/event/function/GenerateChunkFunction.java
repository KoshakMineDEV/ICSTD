package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.level.LevelGeneration;

import java.util.Random;

public interface GenerateChunkFunction {
    void call(int chunkX, int chunkZ, Random random, int dimensionId, long seed, long worldSeed, long dimensionSeed, LevelGeneration level);
}
