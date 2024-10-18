package ru.koshakmine.icstd.utils;

import com.zhekasmirnov.horizon.util.FileUtils;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.runtime.other.NameTranslation;
import com.zhekasmirnov.innercore.api.runtime.other.PrintStacking;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.util.Arrays;
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
                        .split("\\.")[0], FileUtils.readJSON(new File(file + "/" +path)));

            final HashMap<String, ScriptableObject> translations = new HashMap<>();
            jsons.forEach((lang, json) -> {
                try {
                    final JSONArray names = json.names();
                    for(int i = 0;i < names.length();i++){
                        final String key = names.getString(i);
                        final String name = json.getString(key);
                        nameToKey.putIfAbsent(key, name);

                        ScriptableObject scriptable = translations.get(key);
                        if(scriptable == null) scriptable = ScriptableObjectHelper.createEmpty();

                        scriptable.put(lang, scriptable, name);

                        translations.put(key, scriptable);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            translations.forEach(NameTranslation::addTranslation);
        }catch (Exception e){
            throw new RuntimeException(e);
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
