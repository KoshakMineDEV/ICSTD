package ru.koshakmine.icstd.impl;

import ru.koshakmine.icstd.block.liquid.BlockLiquid;

public class TestBlockLiquid extends BlockLiquid {
    @Override
    public String getFlowingTexture() {
        return "water_steam";
    }

    @Override
    public String getStillTexture() {
        return getFlowingTexture();
    }

    @Override
    public String[] getTextureUi() {
        return new String[] {"water_steam"};
    }

    @Override
    public String getId() {
        return "test_liquid";
    }

    @Override
    public String getName() {
        return "Test liquid";
    }
}
