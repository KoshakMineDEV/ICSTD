package ru.koshakmine.icstd.impl;

import ru.koshakmine.icstd.item.tools.ItemHoe;
import ru.koshakmine.icstd.modloader.Mod;
import ru.koshakmine.icstd.recipes.workbench.PatternData;
import ru.koshakmine.icstd.recipes.workbench.ShapedRecipe;
import ru.koshakmine.icstd.type.CreativeCategory;
import ru.koshakmine.icstd.type.ItemID;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Texture;

public class TestItem extends ItemHoe {
    @Override
    public Texture getTexture() {
        return new Texture("stick");
    }

    @Override
    public void onInit() {
        super.onInit();

        Mod.getFactory().add(new ShapedRecipe(new ItemStack(getNumId(), 8))
                .setPattern(new String[]{
                        "aaa"
                }, new PatternData[]{
                        new PatternData('a', ItemID.DIAMOND, 0)
                }));
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
