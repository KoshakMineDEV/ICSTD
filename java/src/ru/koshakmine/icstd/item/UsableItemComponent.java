package ru.koshakmine.icstd.item;

import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.type.AnimationType;
import ru.koshakmine.icstd.type.common.ItemStack;

public interface UsableItemComponent {
    AnimationType getType();
    int getUsingDuration();

    void onItemUsingComplete(ItemStack item, Player player);
}
