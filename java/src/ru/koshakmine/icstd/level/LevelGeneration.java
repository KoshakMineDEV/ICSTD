package ru.koshakmine.icstd.level;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.innercore.api.NativeAPI;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.utils.GenerationUtils;

public class LevelGeneration extends Level {
    protected LevelGeneration(NativeBlockSource region) {
        super(region);
    }

    public Position findSurface(int x, int z){
        return new Position(x, GenerationUtils.findSurface(x, z), z);
    }

    public void setBiomeMap(int x, int z, int biome){
        NativeAPI.setBiomeMap(x, z, biome);
    }
}
