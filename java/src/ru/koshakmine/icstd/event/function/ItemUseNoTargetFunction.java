package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.type.common.ItemStack;

public interface ItemUseNoTargetFunction {
    void call(ItemStack itemStack, Player player);
}
