package ru.koshakmine.icstd.block;

import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.EnchantData;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;

public interface IDropBlock {
    ItemStack[] getDrop(Position position, BlockData block, Level level, int diggingLevel, EnchantData enchant, ItemStack item);
}
