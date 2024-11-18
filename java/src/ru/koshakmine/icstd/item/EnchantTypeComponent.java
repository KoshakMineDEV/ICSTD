package ru.koshakmine.icstd.item;

public interface EnchantTypeComponent {
    int getEnchantType();
    default int getEnchantability() {
        return 14;
    }
}
