package ru.koshakmine.icstd.item.tools;

import ru.koshakmine.icstd.type.tools.BlockMaterials;

public abstract class ItemShovel extends ItemTool {
    @Override
    public String[] getBlockMaterials() {
        return new String[]{BlockMaterials.DIRT};
    }
}
