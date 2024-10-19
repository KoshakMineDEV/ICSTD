package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import com.zhekasmirnov.innercore.api.NativeICRender;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.Block;
import ru.koshakmine.icstd.block.BlockRotate;
import ru.koshakmine.icstd.block.blockentity.BlockEntity;
import ru.koshakmine.icstd.block.IBlockEntityHolder;
import ru.koshakmine.icstd.block.ILocalBlockEntityHolder;
import ru.koshakmine.icstd.block.blockentity.LocalBlockEntity;
import ru.koshakmine.icstd.js.EnergyNetLib;
import ru.koshakmine.icstd.js.StorageInterfaceLib;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.CreativeCategory;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.type.tools.BlockMaterials;
import ru.koshakmine.icstd.type.tools.ToolLevel;

public class TestBlock extends BlockRotate implements IBlockEntityHolder, ILocalBlockEntityHolder, StorageInterfaceLib.StorageDescriptor {
    @Override
    public String getId() {
        return "testik";
    }

    @Override
    public String getName() {
        return "GGG";
    }

    @Override
    public String[] getTextures() {
        return new String[]{"stone", "stone", "stone", "planks", "stone", "stone"};
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public String getBlockMaterial() {
        return BlockMaterials.DIRT;
    }

    @Override
    public int getToolLevel() {
        return ToolLevel.IRON;
    }

    @Override
    public String getSoundType() {
        return super.getSoundType();
    }

    @Override
    public void onInit() {
        super.onInit();

        NativeICRender.getGroup("ic-wire").add(getNumId(), -1);
        EnergyNetLib.addEnergyTileTypeForId(getNumId(), EnergyNetLib.assureEnergyType("Eu", 1));
    }

    @Override
    public BlockEntity createBlockEntity(Position position, Level level) {
        return new TestBlockEntity(getBlockEntityType(), getNativeBlock().getId(), position, level);
    }

    @Override
    public LocalBlockEntity createLocalBlockEntity(Position position, NetworkEntity entity, JSONObject data) throws JSONException {
        return new TestLocalBlockEntity(getBlockEntityType(), getNativeBlock().getId(), position, entity, data);
    }

    @Override
    public String[] getInputSlots(int side) {
        return new String[]{"slot1"};
    }

    @Override
    public String[] getOutputSlots(int side) {
        return new String[]{"slot1"};
    }

    @Override
    public boolean isValidInput(ItemStack item, int side, BlockEntity entity) {
        return true;
    }

    @Override
    public CreativeCategory getCreativeCategory() {
        return CreativeCategory.NATURE;
    }

    /*@Override
    public int addItem(ItemStack item, int side, int maxCount) {
        return Math.min(maxCount, 1);
    }*/
}
