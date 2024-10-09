package ru.koshakmine.icstd.utils;

import com.zhekasmirnov.horizon.util.FileUtils;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI;
import com.zhekasmirnov.innercore.api.runtime.other.NameTranslation;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class TranslationLoader {
    private static final HashMap<String, String> nameToKey = new HashMap<>();

    public static String getCurrentLang(){
        return NativeAPI.getGameLanguage();
    }

    public static void loadTranslation(String file, String defaultLang) {
        try{
            final List<String> files = FileUtils.getAllRelativePaths(new File(file), false);
            final HashMap<String, JSONObject> jsons = new HashMap<>();

            for (String path : files)
                jsons.put(path.split("\\\\")[path.split("\\\\").length - 1]
                        .split("/")[path.split("/").length - 1]
                        .split("\\.")[0], FileUtils.readJSON(new File(path)));

            final HashMap<String, ScriptableObject> translations = new HashMap<>();
            jsons.forEach((lang, json) -> {
                for(String key : jsons.keySet()){
                    try {
                        final String name = json.getString(key);
                        nameToKey.putIfAbsent(key, name);

                        ScriptableObject scriptable = translations.get(key);
                        if(scriptable == null) scriptable = ScriptableObjectHelper.createEmpty();

                        scriptable.put(lang, scriptable, name);

                        translations.put(key, scriptable);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            translations.forEach(AdaptedScriptAPI.Translation::addTranslation);
        }catch (Exception e){
            throw new RuntimeException("Error read lang");
        }

    }

    public static String translate(String key, Object... formats){
        String translate = NameTranslation.translate(key);
        if(key.equals(translate)){
            translate = nameToKey.getOrDefault(key, translate);
        }
        return String.format(translate, formats);
    }
}
