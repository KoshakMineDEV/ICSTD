package ru.koshakmine.icstd.recipes.workbench;

import com.zhekasmirnov.apparatus.mcpe.NativeWorkbenchContainer;
import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.RecipeEntry;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchField;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchFieldAPI;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchRecipeRegistry;
import com.zhekasmirnov.innercore.api.runtime.Callback;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.modloader.IBaseRegister;
import ru.koshakmine.icstd.type.common.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public abstract class WorkbenchRecipeBase implements IBaseRegister {
    protected final ItemCraft item;
    protected HashMap<Character, RecipeEntry> entries = new HashMap<>();

    public WorkbenchRecipeBase(ItemCraft item){
        this.item = item;
    }

    protected abstract com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchRecipe buildRecipe(ItemStack item);

    public WorkbenchRecipeBase setEntries(PatternData[] datas){
        final HashMap<Character, RecipeEntry> entries = new HashMap<>();

        for (final PatternData data : datas)
            entries.put(data.symbol, new RecipeEntry(data.id, data.data));

        this.entries = entries;

        return this;
    }

    protected ItemInstance provideRecipeForPlayer(ItemInstance result, WorkbenchField field, long player){
        WorkbenchFieldAPI api = new WorkbenchFieldAPI(field);
        Callback.invokeCallback("CraftRecipePreProvided", this, field, player);

        onCraft(api, new Player(((NativeWorkbenchContainer) field).getPlayer()));

        Callback.invokeCallback("CraftRecipeProvided", this, field, api.isPrevented(), player);
        return api.isPrevented() ? null : result;
    }

    public void onCraft(WorkbenchFieldAPI container, Player player){
        for (int i = 0; i < 9; i++) {
            container.decreaseFieldSlot(i);
        }
    }

    private final UUID uuid = UUID.randomUUID();
    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void factory() {
        final com.zhekasmirnov.innercore.api.mod.recipes.workbench.WorkbenchRecipe recipe = buildRecipe(item.factory());

        recipe.setEntries(entries);
        recipe.setPrefix("");
        recipe.setVanilla(false);

        WorkbenchRecipeRegistry.addRecipe(recipe);
    }

    @Override
    public int getPriority() {
        return -5;
    }

    @Override
    public void onPreInit() {}
    @Override
    public void onInit() {}
}
