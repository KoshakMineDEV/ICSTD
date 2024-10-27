package ru.koshakmine.icstd.item.event;

import ru.koshakmine.icstd.type.common.ItemStack;

public interface OverrideNameComponent {
    String onOverrideName(ItemStack item, String name, String translate);
}
