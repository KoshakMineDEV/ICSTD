package ru.koshakmine.icstd.recipes;

import com.zhekasmirnov.innercore.api.mod.recipes.furnace.FurnaceRecipeRegistry;

public class RecipeRegistry {
    public static void addFurnaceRecipe(int inputId, int inputData, int outputId, int outputData){
        FurnaceRecipeRegistry.addFurnaceRecipe(inputId, inputData, outputId, outputData, null);
    }

    public static void addFurnaceFuel(int inputId, int inputData, int burn){
        FurnaceRecipeRegistry.addFuel(inputId, inputData, burn);
    }
}
