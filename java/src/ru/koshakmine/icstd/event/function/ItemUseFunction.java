package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;

public interface ItemUseFunction {
    void call(BlockPosition position, ItemStack itemStack, BlockData blockData, long player);
}
