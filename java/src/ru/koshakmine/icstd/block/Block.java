package ru.koshakmine.icstd.block;

import com.zhekasmirnov.apparatus.minecraft.enums.GameEnums;
import com.zhekasmirnov.innercore.api.NativeBlock;
import com.zhekasmirnov.innercore.api.NativeItem;
import com.zhekasmirnov.innercore.api.NativeItemModel;
import com.zhekasmirnov.innercore.api.unlimited.BlockRegistry;
import com.zhekasmirnov.innercore.api.unlimited.BlockVariant;
import com.zhekasmirnov.innercore.api.unlimited.IDRegistry;

public abstract class Block {
    private NativeBlock block;

    public abstract String getId();
    public abstract String getName();
    public abstract String[] getTextures();

    public int getMaterial() {
        return 3;
    }

    public int getMaterialBase() {
        return 0;
    }

    public String getSoundType() {
        return "";
    }

    public boolean isSolid() {
        return true;
    }

    public boolean canContainLiquid() {
        return false;
    }

    public boolean canBeExtraBlock() {
        return false;
    }

    public boolean renderAllFaces() {
        return false;
    }

    public int getRenderType() {
        return 0;
    }

    public int getRenderLayer() {
        return GameEnums.getInt(GameEnums.getSingleton().getEnum("block_render_layer", "alpha"));
    }

    public int getLightLevel() {
        return 0;
    }

    public int getLightOpacity() {
        return 0;
    }

    public float getExplosionResistance() {
        return 3.0f;
    }

    public float getFriction() {
        return 0.6f;
    }

    public float getDestroyTime() {
        return 1.0f;
    }

    public float getTranslucency() {
        return 1.0f;
    }

    public int getMapColor() {
        return 0;
    }

    public boolean addToCreativeInventory() {
        return true;
    }

    public Block() {
        this.block = NativeBlock.createBlock(IDRegistry.genBlockID(getId()), getId(), "blank", 0);
        this.block.addVariant(getName(), getTextures(), new int[getTextures().length]);

        NativeBlock.setMaterial(block.getId(), getMaterial());
        NativeBlock.setMaterialBase(block.getId(), getMaterialBase());
        NativeBlock.setSoundType(block.getId(), getSoundType());
        NativeBlock.setSolid(block.getId(), isSolid());
        NativeBlock.setCanContainLiquid(block.getId(), canContainLiquid());
        NativeBlock.setCanBeExtraBlock(block.getId(), canBeExtraBlock());
        NativeBlock.setRenderAllFaces(block.getId(), renderAllFaces());
        NativeBlock.setRenderType(block.getId(), getRenderType());
        NativeBlock.setRenderLayer(block.getId(), getRenderLayer());
        NativeBlock.setLightLevel(block.getId(), getLightLevel());
        NativeBlock.setLightOpacity(block.getId(), getLightOpacity());
        NativeBlock.setExplosionResistance(block.getId(), getExplosionResistance());
        NativeBlock.setFriction(block.getId(), getFriction());
        NativeBlock.setDestroyTime(block.getId(), getDestroyTime());
        NativeBlock.setTranslucency(block.getId(), getTranslucency());
        NativeBlock.setMapColor(block.getId(), getMapColor());

        if(this instanceof IShapedBlock) {
            IShapedBlock shapedBlock = (IShapedBlock) this;
            shapedBlock.getShape().setToBlock(block.getId(), 0);

            BlockVariant variant = BlockRegistry.getBlockVariant(block.getId(), 0);

            if (variant != null) {
                variant.shape = shapedBlock.getShape();
                NativeItemModel.getFor(block.getId(), 0).updateForBlockVariant(variant);
            }
        }

        if(addToCreativeInventory()) {
            NativeItem.addToCreative(block.getId(), 1, 0, null);
        }

    }

    public NativeBlock getNativeBlock() {
        return block;
    }

}
