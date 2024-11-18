package ru.koshakmine.icstd.level.dimensions;

import com.zhekasmirnov.horizon.util.FileUtils;
import com.zhekasmirnov.innercore.api.dimensions.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.level.LevelGeneration;
import ru.koshakmine.icstd.modloader.IBaseRegisterGameObject;
import ru.koshakmine.icstd.type.block.BlockID;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.Position;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public abstract class CustomDimension implements IBaseRegisterGameObject {
    private com.zhekasmirnov.innercore.api.dimensions.CustomDimension dimension;

    protected abstract int getRegisterNumId();

    @Override
    public final int getNumId() {
        if(dimension != null)
            return dimension.id;
        return getRegisterNumId();
    }

    @Override
    public int getPriority() {
        return -2;
    }

    @Override
    public void onPreInit() {}

    @Override
    public void onInit() {}

    public float[] getBaseColor() {
        return null;
    }

    public float[] getSkyColor() {
        return getBaseColor();
    }

    public float[] getCloudColor() {
        return getBaseColor();
    }

    public float[] getFogColor() {
        return getBaseColor();
    }

    public float[] getSunsetColor() {
        return getBaseColor();
    }

    public float[] getFogDistance() {
        return new float[] {512, 512};
    }

    public String getGeneratorPath() {
        return null;
    }

    public boolean hasSkyLight() {
        return true;
    }

    public String getBaseGenerator() {
        return "overworld";
    }

    public boolean hasBuildVanillaSurfaces() {
        return false;
    }

    public boolean hasGenerateVanillaStructures() {
        return false;
    }

    public boolean hasGenerateCaves() {
        return false;
    }

    @Override
    public final String getName() {
        return getId();
    }

    protected Position onLoadVec3(Object json, Position position) {
        if(json == null) return position;

        if(json instanceof JSONArray) {
            final JSONArray list = (JSONArray) json;
            return new Position(
                    (float) list.optDouble(0, 0),
                    (float) list.optDouble(1, 0),
                    (float) list.optDouble(2, list.optDouble(0, 0))
            );
        } else if(json instanceof JSONObject) {
            final JSONObject object = (JSONObject) json;
            return new Position(
                    (float) object.optDouble("x", object.optDouble("r", 0)),
                    (float) object.optDouble("y", object.optDouble("g", 0)),
                    (float) object.optDouble("z", object.optDouble("x", object.optDouble("b", 0)))
            );
        } else if(json instanceof Number) {
            final Number num = (Number) json;
            return new Position(num.floatValue(), num.floatValue(), num.floatValue());
        }

        return position;
    }

    protected NoiseOctave onLoadNoiseOctave(JSONObject json) throws JSONException {
        final NoiseOctave octave = new NoiseOctave();
        if(json == null) return octave;

        final Position scale = onLoadVec3(json.opt("scale"), new Position(1, 1, 1));
        final Position translate = onLoadVec3("translate", new Position(0, 0, 0));
        final float weight = (float) json.optDouble("weight", 0);
        final int seed = json.optInt("seed", 0);

        if(json.has("conversion"))
            octave.setConversion(onLoadYConversion(json.get("conversion")));

        return octave
                .setScale(scale.x, scale.y, scale.z)
                .setTranslate(translate.x, translate.y, translate.z)
                .setWeight(weight)
                .setSeed(seed);
    }

    protected NoiseLayer onLoadNoiseLayer(JSONObject json) throws JSONException {
        final NoiseLayer layer = new NoiseLayer();
        if(json == null) return layer;

        if(json.has("octaves")) {
            final Object ocataves = json.get("octaves");

            if(ocataves instanceof JSONArray) {
                final JSONArray octaves_list = (JSONArray) ocataves;
                for(int i = 0;i < octaves_list.length();i++)
                    layer.addOctave(onLoadNoiseOctave(octaves_list.getJSONObject(i)));
            } else {
                final JSONObject octaves_object = (JSONObject) ocataves;

                final int count = octaves_object.optInt("count", 1);
                final int default_scale = 1 << count;

                final Position scale_factor = onLoadVec3(octaves_object.opt("scale_factor"), new Position(2, 2, 2));
                final Position scale = onLoadVec3(octaves_object.opt("scale"), new Position(default_scale, default_scale, default_scale));

                final float seed = (float) octaves_object.optDouble("seed", 0);
                final float weight = (float) octaves_object.optDouble("weight", 1);
                final float weight_factor = (float) octaves_object.optDouble("weight_factor", 2);
                float mul = 2 * ((1 << count) - 1) / ((float) (1 << count));

                if(count <= 0) {
                    throw new RuntimeException("NoiseLayer: octave count is missing or invalid: " + json);
                }

                for(int i = 0;i < count;i++) {
                    final JSONObject octave = new JSONObject();

                    final JSONArray scale_list = new JSONArray();
                    scale_list.put(1 / scale.x);
                    scale_list.put(1 / scale.y);
                    scale_list.put(1 / scale.z);
                    octave.put("scale", scale_list);

                    octave.put("weight", weight / mul);
                    octave.put("seed", seed + i);

                    scale.divide(scale_factor);
                    mul *= weight_factor;

                    layer.addOctave(onLoadNoiseOctave(octave));
                }

                if(octaves_object.has("conversion")) {
                    layer.setConversion(onLoadYConversion(octaves_object.get("conversion")));
                }
            }
        } else {
            layer.addOctave(onLoadNoiseOctave(json));
        }

        final int grid_size = json.optInt("grid", -1);
        if(grid_size > 0)
            layer.setGridSize(grid_size);

        return layer;
    }

    protected NoiseGenerator onLoadNoiseGenerator(JSONObject json) throws JSONException {
        final NoiseGenerator noise = new NoiseGenerator();
        if(json == null) return noise;

        if(json.has("layers")) {
            final JSONArray layers = json.getJSONArray("layers");
            for (int i = 0; i < layers.length(); i++) {
                noise.addLayer(onLoadNoiseLayer(layers.getJSONObject(i)));
            }

            if (json.has("conversion")) {
                noise.setConversion(onLoadYConversion(json.get("conversion")));
            }
        } else {
            final NoiseLayer layer = onLoadNoiseLayer(json);
            noise.addLayer(layer);
        }

        return noise;
    }

    protected static class BlockDataWidth extends BlockData {
        public final int width;

        public BlockDataWidth(int id, int data, int width) {
            super(id, data);
            this.width = width;
        }
    }

    protected int getIdByBlock(Object id) {
        if(id instanceof Number) {
            return ((Number) id).intValue();
        } else if(id instanceof String) {
            final String stringId = (String) id;
            if(stringId.startsWith("BlockID.")){
                return BlockID.getModId(stringId.replace("BlockID.", ""), 1);
            }

            try {
                return BlockID.class.getField(stringId.replace("VanillaBlockID.", "").toUpperCase())
                        .getInt(null);
            } catch (NoSuchFieldException | IllegalAccessException ignore) {}
        }

        return 1;
    }

    protected BlockDataWidth onLoadMaterialBlockData(Object json, BlockDataWidth default_data) {
        if(json == null) return default_data;

        if(json instanceof JSONArray) {
            final JSONArray list = (JSONArray) json;
            return new BlockDataWidth(
                    getIdByBlock(list.optInt(0, 0)),
                    list.optInt(1, 0),
                    list.optInt(2, 1)
            );
        } else if(json instanceof JSONObject) {
            final JSONObject object = (JSONObject) json;
            return new BlockDataWidth(
                    getIdByBlock(object.optInt("id", 0)),
                    object.optInt("data", 0),
                    object.optInt("width", 1)
            );
        }

        if(json instanceof Number) {
            return new BlockDataWidth(((Number) json).intValue(), 0, 1);
        }

        if(json instanceof String) {
            return new BlockDataWidth(getIdByBlock(json), 0, 1);
        }

        return default_data;
    }

    protected JSONArray buildConversion(float x, float y) throws JSONException {
        final JSONArray list = new JSONArray();

        list.put(x);
        list.put(y);

        return list;
    }

    protected NoiseConversion onLoadYConversion(Object json) throws JSONException {
        final NoiseConversion conversion = new NoiseConversion();
        if(json == null) return conversion;

        if(json.equals("identity")) {
            final JSONArray list = new JSONArray();
            list.put(buildConversion(0, 0));
            list.put(buildConversion(1, 1));
            json = list;
        }

        if(json.equals("inverse")) {
            final JSONArray list = new JSONArray();
            list.put(buildConversion(0, 1));
            list.put(buildConversion(1, 0));
            json = list;
        }

        if(json instanceof JSONArray) {
            final JSONArray list = (JSONArray) json;
            for(int i =0;i < list.length();i++) {
                final Position node = onLoadVec3(list.get(i), new Position(0, 0, 0));
                conversion.addNode(node.x, node.y);
            }
        }

        return conversion;
    }

    protected void onLoadTerrainMaterial(TerrainMaterial material, JSONObject json) {
        if(json == null) return;

        final BlockDataWidth base = onLoadMaterialBlockData(json.opt("base"), new BlockDataWidth(0, 0, 1));
        final BlockDataWidth cover = onLoadMaterialBlockData(json.opt("cover"), new BlockDataWidth(0, 0, 1));
        final BlockDataWidth surface = onLoadMaterialBlockData(json.opt("surface"), new BlockDataWidth(0, 0, 1));
        final BlockDataWidth filling = onLoadMaterialBlockData(json.opt("filling"), new BlockDataWidth(0, 0, 1));
        final float diffuse = (float) json.optDouble("diffuse", 1);

        if(base.id == 0) {
            throw new RuntimeException("TerrainMaterial: base must be defined: " + json);
        }

        material.setBase(base.id, base.data);
        if(cover.id != 0)
            material.setCover(cover.id, cover.data);
        if(surface.id != 0)
            material.setSurface(surface.width, surface.id, surface.data);
        if(filling.id != 0)
            material.setFilling(filling.width, filling.id, filling.data);

        material.setDiffuse(diffuse);
    }


    protected TerrainLayer onLoadTerrainLayerMono(MonoBiomeTerrainGenerator terrain, JSONObject json) throws JSONException {
        if(json == null) return null;
        final TerrainLayer layer = terrain.addTerrainLayer(json.optInt("minY", 0), json.optInt("maxY", 256));

        if(json.has("noise"))
            layer.setMainNoise(onLoadNoiseGenerator(json.getJSONObject("noise")));

        if(json.has("heightmap"))
            layer.setHeightmapNoise(onLoadNoiseGenerator(json.getJSONObject("heightmap")));

        if(json.has("yConversion"))
            layer.setYConversion(onLoadYConversion(json.get("yConversion")));

        onLoadTerrainMaterial(layer.getMainMaterial(), json.optJSONObject("material"));

        if(json.has("materials")) {
            final JSONArray materials = json.getJSONArray("materials");
            for(int i = 0;i < materials.length();i++) {
                final JSONObject material = materials.getJSONObject(i);
                if(material.isNull("noise")) {
                    throw new RuntimeException("TerrainLayer: material missing noise: " + json);
                }
                onLoadTerrainMaterial(layer.addNewMaterial(onLoadNoiseGenerator(material.getJSONObject("noise")), 0),  material);
            }
        }

        return layer;
    }

    protected MonoBiomeTerrainGenerator onLoadMonoBiomeTerrainGenerator(JSONObject json) throws JSONException {
        final MonoBiomeTerrainGenerator terrain = new MonoBiomeTerrainGenerator();
        if(json == null) return terrain;

        if(json.has("layers")) {
            final JSONArray layers = json.getJSONArray("layers");
            for(int i = 0;i < layers.length();i++) {
                onLoadTerrainLayerMono(terrain, layers.getJSONObject(i));
            }
        }

        if(json.has("biome"))
            terrain.setBaseBiome(json.getInt("biome"));

        return terrain;
    }

    protected CustomDimensionGenerator onLoadGenerator(JSONObject json) throws IOException, JSONException {
        final CustomDimensionGenerator generator = new CustomDimensionGenerator(json.optString("base", "overworld"));

        if(json.has("buildVanillaSurfaces"))
            generator.setBuildVanillaSurfaces(json.getBoolean("buildVanillaSurfaces"));

        if(json.has("generateVanillaStructures"))
            generator.setGenerateVanillaStructures(json.getBoolean("generateVanillaStructures"));

        if(json.has("generateCaves"))
            generator.setGenerateCaves(json.getBoolean("generateCaves"), json.optBoolean("generateUnderwaterCaves", false));

        if(json.has("modWorldgenDimension"))
            generator.setModGenerationBaseDimension(json.getInt("modWorldgenDimension"));

        final String type = json.optString("type", "mono");

        switch (type) {
            case "mono":
                generator.setTerrainGenerator(onLoadMonoBiomeTerrainGenerator(json));
                return generator;
        }
        throw new RuntimeException("invalid generator type: " + type + ", " + json);
    }

    @Override
    public void factory() {
        dimension = new com.zhekasmirnov.innercore.api.dimensions.CustomDimension(getName(), getNumId());

        float[] color = getSkyColor();
        if(color != null) dimension.setSkyColor(color[0], color[1], color[2]);

        color = getCloudColor();
        if(color != null) dimension.setCloudColor(color[0], color[1], color[2]);

        color = getFogColor();
        if(color != null) dimension.setFogColor(color[0], color[1], color[2]);

        color = getSunsetColor();
        if(color != null) dimension.setSunsetColor(color[0], color[1], color[2]);

        final float[] distance = getFogDistance();
        dimension.setFogDistance(distance[0], distance[1]);

        dimension.setHasSkyLight(hasSkyLight());

        try {
            dimension.setGenerator(onLoadGenerator(FileUtils.readJSON(new File(getGeneratorPath()))));
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }


        Event.onCustomDimensionGenerateChunk((chunkX, chunkZ, random, dimensionId, seed, worldSeed, dimensionSeed, level) -> {
            if(dimensionId == dimension.id)
                onGenerationChunk(chunkX, chunkZ, random, seed, worldSeed, dimensionSeed, level);
        });
    }

    public void onGenerationChunk(int chunkX, int chunkZ, Random random, long seed, long worldSeed, long dimensionSeed, LevelGeneration level) {}

    private final UUID uuid = UUID.randomUUID();
    @Override
    public UUID getUUID() {
        return uuid;
    }
}
