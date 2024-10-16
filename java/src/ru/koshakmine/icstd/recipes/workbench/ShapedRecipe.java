package ru.koshakmine.icstd.recipes.workbench;

import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchField;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchRecipe;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchShapedRecipe;
import ru.koshakmine.icstd.type.common.ItemStack;

public class ShapedRecipe extends WorkbenchRecipeBase {
    private String[] pattern = new String[]{};

    public ShapedRecipe(ItemCraft item) {
        super(item);
    }

    @Override
    protected WorkbenchRecipe buildRecipe(ItemStack item) {
        final WorkbenchShapedRecipe recipe = new WorkbenchShapedRecipe(item.id, item.count, item.data, item.extra){
            @Override
            public ItemInstance provideRecipeForPlayer(WorkbenchField field, long player) {
                return ShapedRecipe.this.provideRecipeForPlayer(getResult(), field, player);
            }
        };
        recipe.setEntries(entries);
        recipe.setPattern(pattern);
        return recipe;
    }

    public WorkbenchRecipeBase setPattern(String[] pattern, PatternData[] datas) {
        setEntries(datas);
        this.pattern = pattern;
        return this;
    }
}
