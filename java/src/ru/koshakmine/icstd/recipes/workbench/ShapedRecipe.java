package ru.koshakmine.icstd.recipes.workbench;

import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchField;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchRecipe;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchShapedRecipe;
import ru.koshakmine.icstd.type.common.ItemStack;

public class ShapedRecipe extends WorkbenchRecipeBase {
    public ShapedRecipe(ItemStack item) {
        super(item);
    }

    @Override
    protected WorkbenchRecipe buildRecipe(ItemStack item) {
        return new WorkbenchShapedRecipe(item.id, item.count, item.data, item.extra){
            @Override
            public ItemInstance provideRecipeForPlayer(WorkbenchField field, long player) {
                return ShapedRecipe.this.provideRecipeForPlayer(getResult(), field, player);
            }
        };
    }

    public WorkbenchRecipeBase setPattern(String[] pattern, PatternData[] datas) {
        setEntries(datas);
        ((WorkbenchShapedRecipe) recipe).setPattern(pattern);
        return this;
    }
}
