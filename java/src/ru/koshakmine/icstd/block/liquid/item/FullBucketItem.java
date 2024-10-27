package ru.koshakmine.icstd.block.liquid.item;

import com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.NativeTileEntity;
import com.zhekasmirnov.innercore.api.runtime.MainThreadQueue;
import ru.koshakmine.icstd.block.Block;
import ru.koshakmine.icstd.block.liquid.BlockLiquid;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.item.event.ClickableComponent;
import ru.koshakmine.icstd.item.event.DispenseComponent;
import ru.koshakmine.icstd.item.Item;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.CreativeCategory;
import ru.koshakmine.icstd.type.ItemID;
import ru.koshakmine.icstd.type.common.*;

public class FullBucketItem extends Item implements ClickableComponent, DispenseComponent {
    private final BlockLiquid liquid;

    public FullBucketItem(BlockLiquid liquid){
        this.liquid = liquid;
    }

    @Override
    public boolean isLiquidClip() {
        return true;
    }

    @Override
    public int getMaxStack() {
        return 1;
    }

    @Override
    public Texture getTexture() {
        return liquid.getFullBucketItem();
    }

    @Override
    public String getId() {
        return liquid.getId() + "_bucket";
    }

    @Override
    public String getName() {
        return "Bucket Of " + liquid.getName();
    }

    @Override
    public CreativeCategory getCreativeCategory() {
        return CreativeCategory.MATERIAL;
    }

    public boolean canReplaceWithLiquid(BlockData block){
        return block.id == liquid.getNumId() || block.id == liquid.getFlowing().getNumId() || Block.canTileBeReplaced(block.id, block.data);
    }

    @Override
    public void onClick(BlockPosition position, ItemStack item, BlockData block, Player player) {
        if (!player.isSneaking() && Block.doesVanillaTileHasUI(block.id)) return;
        Level level = player.getRegion();

        Position coords = position;

        if (!canReplaceWithLiquid(block)) {
            coords = position.relative;
            final BlockState state = level.getBlock(coords);
            block = new BlockData(state);

            if (!canReplaceWithLiquid(block)) {
                return;
            }
        }

        NativeAPI.preventDefault();
        level.setBlock(coords, liquid.getFlowing().getNumId(), 0);
        if (player.isItemSpendingAllowed()) {
            MainThreadQueue.serverThread.enqueue(() -> {
                player.setCarriedItem(new ItemStack(ItemID.BUCKET, 0));
            });
        }

        level.playSoundAtEntity(player, "bucket.fill_water", 1, 1);
    }

    @Override
    public void onDispense(BlockPosition position, ItemStack item, Level level, int slot) {
        final NativeTileEntity tileEntity = level.getNativeBlockEntity(position);
        if(tileEntity != null){
            final BlockState state = level.getBlock(position.relative);

            if(canReplaceWithLiquid(new BlockData(state))){
                level.setBlock(position.relative, liquid.getFlowing().getNumId(), 0);
                tileEntity.setSlot(slot, ItemID.BUCKET, 1, 0, null);
                level.playSound(position.relative.add(.5f, .5f, .5f), "bucket.empty_water", 1, 1);
            }
        }
    }
}
