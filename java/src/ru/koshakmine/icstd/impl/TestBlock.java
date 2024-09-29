package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import ru.koshakmine.icstd.block.Block;
import ru.koshakmine.icstd.block.blockentity.BlockEntity;
import ru.koshakmine.icstd.block.blockentity.IBlockEntityHolder;
import ru.koshakmine.icstd.type.common.Position;

public class TestBlock extends Block implements IBlockEntityHolder {
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
    public BlockEntity createBlockEntity(Position position, NativeBlockSource region) {
        return new TestBlockEntity(getId(), getNativeBlock().getId(), position, region);
    }
}
