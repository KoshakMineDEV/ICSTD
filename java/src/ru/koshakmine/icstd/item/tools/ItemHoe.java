package ru.koshakmine.icstd.item.tools;

import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.item.event.IClickable;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.tools.BlockMaterials;

public abstract class ItemHoe extends ItemTool implements IClickable {
    @Override
    public String[] getBlockMaterials() {
        return new String[] {BlockMaterials.PLANT};
    }

    @Override
    public void onClick(BlockPosition position, ItemStack item, BlockData block, Player player) {
        if ((block.id == 2 || block.id == 3) && position.side != 0) {
            final Level region = player.getRegion();

            if (region.getBlockId(position.add(0, 1, 0)) != 0)
                return;

            region.setBlock(position, 60, 0);
            region.playSound(position.add(.5f, 1, .5f) , "step.gravel", 1, 0.8f);

            item.applyDamage(1);
            player.setCarriedItem(item);
        }
    }
}
