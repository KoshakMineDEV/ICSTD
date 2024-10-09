package ru.koshakmine.icstd.js;

import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.item.tools.ToolMaterial;

public class ToolAPI {
    private static Scriptable scriptable;

    public static void init(Scriptable scriptable){
        ToolAPI.scriptable = scriptable;
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
        JsHelp.callFunction(scriptable, "addBlockMaterial", name, breakingMultiplier);
    }

    public static void addToolMaterial(String name, ToolMaterial material){
        JsHelp.callFunction(scriptable, "addToolMaterial", name, toMaterialScriptable(material));
    }

    public static double getBlockDestroyLevel(int id){
        Object arg = JsHelp.callFunction(scriptable, "getBlockDestroyLevel", id);
        if(arg instanceof Number)
            return ((Number) arg).doubleValue();
        throw new RuntimeException("Not js");
    }

    public static String getBlockMaterialName(int id){
        Object arg = JsHelp.callFunction(scriptable, "getBlockMaterialName", id);
        if(arg instanceof CharSequence)
            return arg.toString();
        throw new RuntimeException("Not js");
    }

    public static int getToolLevel(int itemId){
        Object arg = JsHelp.callFunction(scriptable, "getBlockDestroyLevel", itemId);
        if(arg instanceof Number)
            return ((Number) arg).intValue();
        throw new RuntimeException("Not js");
    }

    public static double getToolLevelViaBlock(int itemId, int blockId){
        Object arg = JsHelp.callFunction(scriptable, "getToolLevelViaBlock", itemId, blockId);
        if(arg instanceof Number)
            return ((Number) arg).doubleValue();
        throw new RuntimeException("Not js");
    }

    private static void registerSword(int id, Object material){
        JsHelp.callFunction(scriptable, "registerSword", id, material);
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
        JsHelp.callFunction(scriptable, "registerTool", id, material, array);
    }

    public static void registerTool(int id, String material, String[] blockBreakerMaterial){
        registerTool(id, (Object) material, blockBreakerMaterial);
    }

    public static void registerTool(int id, ToolMaterial material, String[] blockBreakerMaterial){
        registerTool(id, toMaterialScriptable(material), blockBreakerMaterial);
    }

    public static void registerBlockMaterial(int id, String blockMaterial, int level){
        JsHelp.callFunction(scriptable, "registerBlockMaterial", id, blockMaterial, level);
    }
}
