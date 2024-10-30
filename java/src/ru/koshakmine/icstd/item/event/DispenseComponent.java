package ru.koshakmine.icstd.item.event;

import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;

public interface DispenseComponent {
    void onDispense(BlockPosition position, ItemStack item, Level level, int slot);
}
