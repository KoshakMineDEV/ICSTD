package ru.koshakmine.icstd.item;

import ru.koshakmine.icstd.type.ArmorSlot;

public interface IArmorItem {
    String getArmorPlayerTexture();
    ArmorSlot getSlot();
    int getDefense();
    int getDuration();
    double getKnockbackResist();
}
