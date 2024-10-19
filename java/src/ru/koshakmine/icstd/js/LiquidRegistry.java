package ru.koshakmine.icstd.js;

import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.ScriptableObject;

public class LiquidRegistry {
    private static ScriptableObject scriptable;
    public static int liquidStorageSaverId;

    public static void init(ScriptableObject scriptable){
        LiquidRegistry.scriptable = scriptable;
        liquidStorageSaverId = ScriptableObjectHelper.getIntProperty(scriptable, "liquidStorageSaverId", -1);
        //scriptable.put("");
    }

    public static void registerLiquid(String liquidName, String name, String[] textureUi){
        final NativeArray array = ScriptableObjectHelper.createEmptyArray();
        for (int i = 0;i < textureUi.length;i++) {
            array.put(i, array, textureUi[i]);
        }
        JsHelper.callFunction(scriptable, "registerLiquid", liquidName, name, array);
    }

    public static void registerBlock(String liquidName, int blockId, boolean isDynamic){
        JsHelper.callFunction(scriptable, "registerBlock", liquidName, blockId, isDynamic);
    }

    public static String getLiquidUITexture(String liquid, int width, int height){
        return JsHelper.callFunction(scriptable, "getLiquidUITexture", liquid, width, height).toString();
    }

    public static void registerItem(String liquidName, int emptyBucket, int emptyBucketData, int fullBucket, int fullBucketData){
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        empty.put("id", empty, emptyBucket);
        empty.put("data", empty, emptyBucketData);

        final ScriptableObject full = ScriptableObjectHelper.createEmpty();
        full.put("id", full, fullBucket);
        full.put("data", full, fullBucketData);

        JsHelper.callFunction(scriptable, "registerItem", liquidName, empty, full);
    }
}
