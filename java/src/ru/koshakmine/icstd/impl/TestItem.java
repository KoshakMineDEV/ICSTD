package ru.koshakmine.icstd.impl;

import ru.koshakmine.icstd.item.tools.ItemHoe;
import ru.koshakmine.icstd.type.CreativeCategory;
import ru.koshakmine.icstd.type.common.Texture;

public class TestItem extends ItemHoe {
    @Override
    public Texture getTexture() {
        return new Texture("stick");
    }

    @Override
    public String getId() {
        return "test_stick";
    }

    @Override
    public String getName() {
        return "Test my item";
    }

    @Override
    public CreativeCategory getCreativeCategory() {
        return CreativeCategory.MATERIAL;
    }

    @Override
    public int getToolDurability() {
        return 16;
    }

    @Override
    public int getToolLevel() {
        return 4;
    }

    @Override
    public int getToolEfficiency() {
        return 15;
    }
}
