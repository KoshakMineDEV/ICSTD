package ru.koshakmine.icstd.type.common;

import org.mozilla.javascript.Scriptable;

public class EnchantData {
    public final int efficiency;
    public final int experience;
    public final int fortune;
    public final boolean silk;
    public final int unbreaking;

    private static int getInt(Scriptable scriptable, String name){
        return ((Number) scriptable.get(name, scriptable)).intValue();
    }

    public EnchantData(Scriptable scriptable){
        efficiency = getInt(scriptable, "efficiency");
        experience = getInt(scriptable, "experience");
        fortune = getInt(scriptable, "fortune");
        silk = ((Boolean) scriptable.get("silk", scriptable));
        unbreaking = getInt(scriptable, "unbreaking");
    }
}
