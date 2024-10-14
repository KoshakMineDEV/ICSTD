package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.Block;
import ru.koshakmine.icstd.block.BlockRotate;
import ru.koshakmine.icstd.block.blockentity.BlockEntity;
import ru.koshakmine.icstd.block.IBlockEntityHolder;
import ru.koshakmine.icstd.block.ILocalBlockEntityHolder;
import ru.koshakmine.icstd.block.blockentity.LocalBlockEntity;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.type.tools.BlockMaterials;

public class TestBlock extends BlockRotate implements IBlockEntityHolder, ILocalBlockEntityHolder {
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
    public BlockEntity createBlockEntity(Position position, Level level) {
        return new TestBlockEntity(getBlockEntityType(), getNativeBlock().getId(), position, level);
    }

    @Override
    public LocalBlockEntity createLocalBlockEntity(Position position, NetworkEntity entity, JSONObject data) throws JSONException {
        return new TestLocalBlockEntity(getBlockEntityType(), getNativeBlock().getId(), position, entity, data);
    }
}
