package ru.koshakmine.icstd.type.item;

public class EnchantType {
    public static final int AXE = 512;
    public static final int ALL = 16383;
    public static final int BOW = 32;
    public static final int FISHING_ROD = 4096;
    public static final int HOE = 64;
    public static final int PICKAXE = 1024;
    public static final int SHOVEL = 2048;
    public static final int WEAPON = 16;
    public static final int SHEARS = 128;

    public static final int HELMET = 0;
    public static final int CHESTPLATE = 8;
    public static final int LEGGINS = 2;
    public static final int BOOTS = 4;

    public static final int TOOL = AXE | HOE | SHOVEL | SHEARS | PICKAXE;
    public static final int ARMOR = HELMET | CHESTPLATE | LEGGINS | BOOTS;
    public static final int WEAPONS = WEAPON | AXE;
}
