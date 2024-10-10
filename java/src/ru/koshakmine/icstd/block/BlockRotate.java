package ru.koshakmine.icstd.block;

import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.*;

public abstract class BlockRotate extends Block implements IPlaceBlock, IDropBlock {
    @Override
    public void onInit() {
        final String[] td = getTextures();
        final int[] metas = new int[td.length];
        final String[][] rotated = new String[][]{
                new String[]{td[0], td[1], td[3], td[2], td[5], td[4]},
                new String[]{td[0], td[1], td[5], td[4], td[2], td[3]},
                new String[]{td[0], td[1], td[4], td[5], td[3], td[2]},
        };
        for (String[] strings : rotated) {
            getNativeBlock().addVariant(getName(), strings, metas);
        }
    }

    @Override
    public ItemStack[] getDrop(Position position, BlockData block, Level level, int diggingLevel, EnchantData enchant, ItemStack item) {
        return new ItemStack[]{new ItemStack(getNumId(), 0)};
    }

    @Override
    public Position onPlace(BlockPosition position, ItemStack item, BlockData block, Player player, Level level) {
        int yaw = (int) Math.floor((player.getYaw() - 45) / 90);

        while (yaw < 0){
            yaw += 4;
        }
        while (yaw > 3){
            yaw -= 4;
        }

        final int meta = new int[]{2, 0, 3, 1}[yaw];

        if(canTileBeReplaced(level.getBlockId(position.relative))){
            level.setBlock(position.relative, item.id, meta);
        }

        return position.relative;
    }
}
