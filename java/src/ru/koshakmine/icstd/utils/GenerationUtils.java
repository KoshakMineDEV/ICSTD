package ru.koshakmine.icstd.utils;

import com.zhekasmirnov.innercore.api.NativeGenerationUtils;
import ru.koshakmine.icstd.type.common.Position;

public class GenerationUtils {
    public static int findSurface(int x, int z){
        return NativeGenerationUtils.findSurface(x, 64, z);
    }

    public static double getPerlinNoise(double x, double y, double z, int seed, double scale, int numOctaves){
        return NativeGenerationUtils.getPerlinNoise(x, y, z, seed, scale, numOctaves);
    }

    public static double getPerlinNoise(Position position, int seed, double scale, int numOctaves){
        return getPerlinNoise(position.x, position.y, position.z, seed, scale, numOctaves);
    }
}
