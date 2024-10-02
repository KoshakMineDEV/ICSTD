package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.Block;
import ru.koshakmine.icstd.block.blockentity.BlockEntity;
import ru.koshakmine.icstd.block.IBlockEntityHolder;
import ru.koshakmine.icstd.block.ILocalBlockEntityHolder;
import ru.koshakmine.icstd.block.blockentity.LocalBlockEntity;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.Position;

public class TestBlock extends Block implements IBlockEntityHolder, ILocalBlockEntityHolder {
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
        return new String[]{"gg"};
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public BlockEntity createBlockEntity(Position position, Level level) {
        return new TestBlockEntity(getId(), getNativeBlock().getId(), position, level);
    }

    @Override
    public LocalBlockEntity createLocalBlockEntity(Position position, NetworkEntity entity, JSONObject data) throws JSONException {
        return new TestLocalBlockEntity(getId(), getNativeBlock().getId(), position, entity, data);
    }
}
