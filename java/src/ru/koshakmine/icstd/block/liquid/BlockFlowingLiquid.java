package ru.koshakmine.icstd.block.liquid;

import com.zhekasmirnov.innercore.api.NativeBlock;
import ru.koshakmine.icstd.block.Block;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.item.event.ClickableComponent;

import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;

public class BlockFlowingLiquid extends Block implements ClickableComponent {
    private final BlockLiquid liquid;

    @Override
    public NativeBlock createBlock() {
        return this.getNativeBlock();
    }

    @Override
    public int getMaterial() {
        return 5;
    }

    @Override
    public int getRenderType() {
        return 4;
    }

    @Override
    public int getRenderLayer() {
        return 2;
    }

    public BlockFlowingLiquid(BlockLiquid liquid, NativeBlock block){
        this.liquid = liquid;
        this.block = block;
    }

    @Override
    public String[] getTextures() {
        return new String[0];
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void onClick(BlockPosition position, ItemStack item, BlockData block, Player player) {
        liquid.onClick(position, item, block, player);
    }

    @Override
    public void onPreInit() {
        addVariant(liquid.getName(), new String[]{liquid.getFlowingTexture()});
    }
}
