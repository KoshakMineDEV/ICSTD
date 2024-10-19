package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class EnchantData {
    public int efficiency;
    public int experience;
    public int fortune;
    public boolean silk;
    public int unbreaking;

    private static int getInt(Scriptable scriptable, String name, int def){
        try {
            return ((Number) scriptable.get(name, scriptable)).intValue();
        }catch (Exception ignore){}
        return def;
    }

    public EnchantData(int efficiency, int experience, int fortune, boolean silk, int unbreaking){
        this.efficiency = efficiency;
        this.experience = experience;
        this.fortune = fortune;
        this.silk = silk;
        this.unbreaking = unbreaking;
    }

    private static void putInt(Scriptable scriptable, String name, Object value){
        scriptable.put(name, scriptable, value);
    }

    public EnchantData(Scriptable scriptable){
        efficiency = getInt(scriptable, "efficiency", 0);
        experience = getInt(scriptable, "experience", 0);
        fortune = getInt(scriptable, "fortune", 0);
        try{
            silk = ((Boolean) scriptable.get("silk", scriptable));
        }catch (Exception ignore){}
        unbreaking = getInt(scriptable, "unbreaking", 0);
    }

    public Object toScriptable() {
        final ScriptableObject self = ScriptableObjectHelper.createEmpty();

        putInt(self, "efficiency", efficiency);
        putInt(self, "experience", experience);
        putInt(self, "fortune", fortune);
        putInt(self, "silk", silk);
        putInt(self, "unbreaking", unbreaking);

        return self;
    }
}
