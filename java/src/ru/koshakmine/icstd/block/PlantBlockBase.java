package ru.koshakmine.icstd.block;

import com.zhekasmirnov.innercore.api.NativeBlockRenderer;
import com.zhekasmirnov.innercore.api.NativeICRender;
import com.zhekasmirnov.innercore.api.NativeItemModel;
import com.zhekasmirnov.innercore.api.NativeRenderMesh;
import ru.koshakmine.icstd.item.PlantBaseItem;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.block.BlockID;
import ru.koshakmine.icstd.type.block.SoundType;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.EnchantData;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.type.tools.BlockMaterials;
import ru.koshakmine.icstd.type.tools.ToolLevel;

public abstract class PlantBlockBase extends Block implements INeighbourChanged, IDropBlock {
    protected PlantBaseItem plantItem = null;

    public abstract String getTexture();

    @Override
    public final String[] getTextures() {
        return new String[]{getTexture()};
    }

    @Override
    public float getDestroyTime() {
        return 1/20f;
    }

    @Override
    public String getSoundType() {
        return SoundType.GRASS;
    }

    @Override
    public int getMaterialBase() {
        return BlockID.LEAVES;
    }

    @Override
    public int getToolLevel() {
        return ToolLevel.ARM;
    }

    @Override
    public String getBlockMaterial() {
        return BlockMaterials.PLANT;
    }

    @Override
    public ItemStack[] getDrop(Position position, BlockData block, Level level, int diggingLevel, EnchantData enchant, ItemStack item) {
        if(plantItem != null)
            return new ItemStack[]{plantItem.getStack()};
        return new ItemStack[]{new ItemStack(block.id, block.data)};
    }

    @Override
    public void onNeighbourChanged(Position position, Position changePosition, BlockData block, Level level) {
        if (position.y - 1 == changePosition.y){
            level.destroyBlock(position);
            if(plantItem != null) level.spawnDroppedItem(position.add(.5, .5, .5), plantItem.getStack());
        }
    }

    private static final float[][] PLANT_VERTEX = new float[][]{
            new float[]{0.15f, 0, 0.15f, 1, 1},
            new float[]{0.85f, 0, 0.85f, 0, 1},
            new float[]{0.85f, 1, 0.85f, 0, 0},
            new float[]{0.15f, 0, 0.15f, 1, 1},
            new float[]{0.15f, 1, 0.15f, 1, 0},
            new float[]{0.85f, 1, 0.85f, 0, 0},
            new float[]{0.15f, 0, 0.85f, 1, 1},
            new float[]{0.85f, 0, 0.15f, 0, 1},
            new float[]{0.85f, 1, 0.15f, 0, 0},
            new float[]{0.15f, 0, 0.85f, 1, 1},
            new float[]{0.15f, 1, 0.85f, 1, 0},
            new float[]{0.85f, 1, 0.15f, 0, 0}
    };

    protected NativeICRender.Model getModel(float x_offset, float z_offset) {
        final NativeRenderMesh mesh = new NativeRenderMesh();
        mesh.setBlockTexture(getTexture(), 0);

        for(int i = 0; i < 12; i++){
            final float[] poly = PLANT_VERTEX[i];
            mesh.addVertex(poly[0] - x_offset, poly[1], poly[2] - z_offset, poly[3], poly[4]);
        }
        for(int i = 11; i >= 0; i--){
            final float[] poly = PLANT_VERTEX[i];
            mesh.addVertex(poly[0] - x_offset, poly[1], poly[2] - z_offset, poly[3], poly[4]);
        }

        final NativeICRender.Model render = new NativeICRender.Model();
        render.addEntry(mesh);
        return render;
    }

    @Override
    public void onInit() {
        final NativeICRender.CollisionShape shape = new NativeICRender.CollisionShape();
        shape.addEntry().addBox(7 / 8f, 1, 7 / 8f, 1 / 8f, 0, 1 / 8f);

        NativeBlockRenderer.setCustomCollisionShape(getNumId(), -1, shape);
        NativeBlockRenderer.setStaticICRender(getNumId(), -1, getModel(0, 0));
    }

    public PlantBlockBase setPlantItem(PlantBaseItem plantItem) {
        this.plantItem = plantItem;
        return this;
    }
}
