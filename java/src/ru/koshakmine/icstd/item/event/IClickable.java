package ru.koshakmine.icstd.item.event;

import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;

public interface IClickable {
    void onClick(BlockPosition position, ItemStack item, BlockData block, Player player);
}
