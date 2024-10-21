package ru.koshakmine.icstd.item.event;

import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Texture;

public interface IOverrideIcon {
    Texture onOverrideIcon(ItemStack stack, boolean isMod);
}
