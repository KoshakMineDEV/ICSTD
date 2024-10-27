package ru.koshakmine.icstd.level;

import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.modloader.IBaseRegisterGameObject;
import ru.koshakmine.icstd.type.block.BlockID;
import ru.koshakmine.icstd.type.common.BlockData;

import java.util.Random;
import java.util.UUID;

public abstract class CustomBiome implements IBaseRegisterGameObject {
    private com.zhekasmirnov.innercore.api.biomes.CustomBiome biome;

    @Override
    public int getNumId() {
        if(biome != null)
            return biome.id;
        return -1;
    }

    @Override
    public final String getName() {return getId();}

    @Override
    public int getPriority() {
        return -1;
    }

    @Override
    public void onPreInit() {}

    @Override
    public void onInit() {}

    public float[] getBaseColor(){
        return new float[]{0, 135f/255f, 0};
    }

    public float[] getGrassColor(){
        return getBaseColor();
    }

    public float[] getSkyColor(){
        return getBaseColor();
    }

    public float[] getFoliageColor(){
        return getBaseColor();
    }

    public float[] getWaterColor(){
        return getBaseColor();
    }

    public float getTemperature(){
        return .5f;
    }

    public float getDownfall(){
        return .5f;
    }

    public BlockData getCoverBlock(){
        return new BlockData(BlockID.GRASS);
    }

    public BlockData getSurfaceBlock(){
        return new BlockData(BlockID.DIRT);
    }

    public BlockData getFillingBlock(){
        return new BlockData(BlockID.STONE);
    }

    public BlockData getSeaFloorBlock(){
        return new BlockData(BlockID.GRAVEL);
    }

    public int getSeaFloorDepth(){
        return 1;
    }

    public com.zhekasmirnov.innercore.api.biomes.CustomBiome getBiome(){
        return biome;
    }

    @Override
    public void factory() {
        biome = new com.zhekasmirnov.innercore.api.biomes.CustomBiome(getId());

        biome.setTemperatureAndDownfall(getTemperature(), getDownfall());

        float[] color = getGrassColor();
        biome.setGrassColor(color[0], color[1], color[2]);

        color = getSkyColor();
        biome.setSkyColor(color[0], color[1], color[2]);

        color = getFoliageColor();
        biome.setFoliageColor(color[0], color[1], color[2]);

        color = getWaterColor();
        biome.setWaterColor(color[0], color[1], color[2]);

        BlockData block = getCoverBlock();
        biome.setCoverBlock(block.id, block.data);

        block = getSurfaceBlock();
        biome.setSurfaceBlock(block.id, block.data);

        block = getFillingBlock();
        biome.setFillingBlock(block.id, block.data);

        block = getSeaFloorBlock();
        biome.setSeaFloorBlock(block.id, block.data);

        biome.setSeaFloorDepth(getSeaFloorDepth());

        Event.onBiomeMap(this::onBiomeMap);
    }

    public abstract void onBiomeMap(int chunkX, int chunkZ, Random random, int dimensionId, long seed, long worldSeed, long dimensionSeed, LevelGeneration level);

    private final UUID uuid = UUID.randomUUID();
    @Override
    public UUID getUUID() {
        return uuid;
    }
}
