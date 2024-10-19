package ru.koshakmine.icstd.js;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.horizon.runtime.logger.Logger;
import com.zhekasmirnov.innercore.api.commontypes.Coords;
import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import com.zhekasmirnov.innercore.mod.executable.Compiler;
import org.mozilla.javascript.*;
import ru.koshakmine.icstd.block.IDropBlock;
import ru.koshakmine.icstd.item.tools.ToolMaterial;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.EnchantData;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;

public class ToolAPI {
    private static ScriptableObject scriptable;
    private static ScriptableObject dropFunctions;

    public static void init(ScriptableObject scriptable, ScriptableObject dropFunctions){
        ToolAPI.scriptable = scriptable;
        ToolAPI.dropFunctions = dropFunctions;
    }

    public static ScriptableObject toMaterialScriptable(ToolMaterial material) {
        final ScriptableObject scriptable = ScriptableObjectHelper.createEmpty();

        scriptable.put("damage", scriptable, material.getToolDamage());
        scriptable.put("durability", scriptable, material.getToolDurability());
        scriptable.put("efficiency", scriptable, material.getToolEfficiency());
        scriptable.put("level", scriptable, material.getToolLevel());

        return scriptable;
    }

    public static void addBlockMaterial(String name, double breakingMultiplier){
        JsHelper.callFunction(scriptable, "addBlockMaterial", name, breakingMultiplier);
    }

    public static void addToolMaterial(String name, ToolMaterial material){
        JsHelper.callFunction(scriptable, "addToolMaterial", name, toMaterialScriptable(material));
    }

    public static double getBlockDestroyLevel(int id){
        Object arg = JsHelper.callFunction(scriptable, "getBlockDestroyLevel", id);
        if(arg instanceof Number)
            return ((Number) arg).doubleValue();
        throw new RuntimeException("Not js");
    }

    public static String getBlockMaterialName(int id){
        Object arg = JsHelper.callFunction(scriptable, "getBlockMaterialName", id);
        if(arg instanceof CharSequence)
            return arg.toString();
        throw new RuntimeException("Not js");
    }

    public static int getToolLevel(int itemId){
        Object arg = JsHelper.callFunction(scriptable, "getBlockDestroyLevel", itemId);
        if(arg instanceof Number)
            return ((Number) arg).intValue();
        throw new RuntimeException("Not js");
    }

    public static double getToolLevelViaBlock(int itemId, int blockId){
        Object arg = JsHelper.callFunction(scriptable, "getToolLevelViaBlock", itemId, blockId);
        if(arg instanceof Number)
            return ((Number) arg).doubleValue();
        throw new RuntimeException("Not js");
    }

    private static void registerSword(int id, Object material){
        JsHelper.callFunction(scriptable, "registerSword", id, material);
    }

    public static void registerSword(int id, String material){
        registerSword(id, (Object) material);
    }

    public static void registerSword(int id, ToolMaterial material){
        registerSword(id, toMaterialScriptable(material));
    }

    private static void registerTool(int id, Object material, String[] blockBreakerMaterial){
        final NativeArray array = ScriptableObjectHelper.createEmptyArray();
        for (String str : blockBreakerMaterial)
            array.put(array.size(), array, str);
        JsHelper.callFunction(scriptable, "registerTool", id, material, array);
    }

    public static void registerTool(int id, String material, String[] blockBreakerMaterial){
        registerTool(id, (Object) material, blockBreakerMaterial);
    }

    public static void registerTool(int id, ToolMaterial material, String[] blockBreakerMaterial){
        registerTool(id, toMaterialScriptable(material), blockBreakerMaterial);
    }

    public static void registerBlockMaterial(int id, String blockMaterial, int level){
        JsHelper.callFunction(scriptable, "registerBlockMaterial", id, blockMaterial, level);
    }

    public static void registerDropFunction(int id, IDropBlock dropFunc, int level){
        JsHelper.callFunction(scriptable, "registerDropFunction", id, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable self, Scriptable parent, Object[] args) {
                final ItemStack[] items = dropFunc.getDrop(
                        new Position((Coords) args[0]), new BlockData(((Number) args[1]).intValue(), ((Number) args[2]).intValue()),
                        Level.getForRegion((NativeBlockSource) args[6]),
                        ((Number) args[3]).intValue(),
                        new EnchantData((Scriptable) args[4]),
                        new ItemStack((ItemInstance) args[5])
                );

                final NativeArray array = ScriptableObjectHelper.createEmptyArray();
                for (int i = 0;i < items.length;i++) {
                    array.put(i, array, items[i].toDrop());
                }

                return array;
            }
        }, level);
    }

    public static ItemStack[] getBlockDrop(Position position, BlockData block, Level level, int levelDigging, EnchantData data, ItemStack item){
        final Object obj = dropFunctions.get(block.id);
        if(obj instanceof Function){
            final Function function = (Function) obj;
            final Object result = function.call(Compiler.assureContextForCurrentThread(), function.getParentScope(), dropFunctions, new Object[]{
                    new Coords(position.x, position.y, position.z),
                    block.id,
                    block.data,
                    levelDigging,
                    data.toScriptable(),
                    item.getItemInstance(),
                    level.getRegion()
            });

            if(result instanceof NativeArray){
                final NativeArray list = (NativeArray) result;
                final ItemStack[] stacks = new ItemStack[list.size()];

                for (int i = 0;i < stacks.length;i++)
                    stacks[i] = new ItemStack(list.get(i, list));

                return stacks;
            }

        }

        return new ItemStack[]{new ItemStack(block.id, 1, block.data)};
    }
}
