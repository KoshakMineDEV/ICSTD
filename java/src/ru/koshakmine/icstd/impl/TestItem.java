package ru.koshakmine.icstd.impl;

import ru.koshakmine.icstd.item.tools.ItemHoe;
import ru.koshakmine.icstd.recipes.workbench.ItemCraft;
import ru.koshakmine.icstd.recipes.workbench.PatternData;
import ru.koshakmine.icstd.recipes.workbench.ShapedRecipe;
import ru.koshakmine.icstd.recipes.workbench.WorkbenchRecipeBase;
import ru.koshakmine.icstd.type.CreativeCategory;
import ru.koshakmine.icstd.type.ItemID;
import ru.koshakmine.icstd.type.common.Texture;

public class TestItem extends ItemHoe {
    private static final WorkbenchRecipeBase RECIPE = FACTORY.add(new ShapedRecipe(new ItemCraft("test_stick", 8))
            .setPattern(new String[]{
                    "aaa"
            }, new PatternData[]{
                    new PatternData('a', ItemID.DIAMOND, 0)
            }));

    @Override
    public Texture getTexture() {
        return new Texture("stick");
    }

    @Override
    public void onInit() {
        super.onInit();
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

    @Override
    public CreativeCategory getCreativeCategory() {
        return CreativeCategory.CONSTRUCTION;
    }
}
