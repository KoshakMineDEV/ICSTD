package ru.koshakmine.icstd.item;

import com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState;
import com.zhekasmirnov.innercore.api.NativeAPI;
import ru.koshakmine.icstd.block.Block;
import ru.koshakmine.icstd.block.BlockPlantBase;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.item.event.ClickableComponent;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Texture;

public class PlantBaseItem extends Item implements ClickableComponent {
    private final BlockPlantBase plantBlock;

    public PlantBaseItem(Block block){
        if(block instanceof BlockPlantBase) this.plantBlock = ((BlockPlantBase) block).setPlantItem(this);
        else throw new RuntimeException("Not block plant");
    }

    @Override
    public Texture getTexture() {
        return new Texture(plantBlock.getTexture());
    }

    @Override
    public String getId() {
        return plantBlock.getId();
    }

    @Override
    public String getName() {
        return plantBlock.getName();
    }

    @Override
    public void onClick(BlockPosition position, ItemStack item, BlockData block, Player player) {
        final Level region = player.getRegion();
        final BlockState tile = region.getBlock(position.relative);
        final int id = region.getBlockId(position.relative.add(0, -1, 0));

        if(!NativeAPI.isDefaultPrevented() && Block.canTileBeReplaced(tile.id, tile.data) && plantBlock.canPlantStand(id)) {
            region.setBlock(position.relative, plantBlock.getNumId(), 0);
            player.setCarriedItem(item.decrease(1));
        }
    }
}
