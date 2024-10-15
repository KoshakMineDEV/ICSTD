package ru.koshakmine.icstd.modloader;

import com.zhekasmirnov.apparatus.modloader.ApparatusMod;
import com.zhekasmirnov.apparatus.modloader.ApparatusModLoader;
import com.zhekasmirnov.apparatus.modloader.LegacyInnerCoreMod;
import com.zhekasmirnov.horizon.runtime.logger.Logger;
import com.zhekasmirnov.innercore.api.log.DialogHelper;
import com.zhekasmirnov.innercore.api.log.ICLog;
import com.zhekasmirnov.innercore.mod.build.Config;
import com.zhekasmirnov.innercore.utils.FileTools;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public abstract class Mod {
    private final String dir;
    private final LegacyInnerCoreMod mod;

    public Mod(String dir, LegacyInnerCoreMod mod){
        this.dir = dir;
        this.mod = mod;
    }

    public final String getDir() {
        return dir;
    }

    public final String getName(){
        return mod.getLegacyModInstance().getName();
    }

    public final Config getConfig(){
        return mod.getLegacyModInstance().getConfig();
    }

    public abstract void runMod(ObjectFactory factory);

    private static final ArrayList<Mod> mods = new ArrayList<>();
    private static final ObjectFactory factory = new ObjectFactory();

    public static ObjectFactory getFactory() {
        return factory;
    }

    public static ArrayList<Mod> getMods() {
        return mods;
    }

    public static void runMods(){
        final List<ApparatusMod> mods = ApparatusModLoader.getSingleton().getAllMods();
        for(final ApparatusMod mod : mods) {
            if (!(mod instanceof LegacyInnerCoreMod)) continue;

            final String path = mod.getInfo().getProperty("directory_root", String.class, null);

            try{
                final JSONArray json = FileTools.readJSONArray(path+"icstd.json");

                for(int i = 0;i < json.length();i++){
                    try{
                        final String classPath = json.getString(i);
                        Class<? extends Mod> clazz = (Class<? extends Mod>) Class.forName(classPath);
                        Mod.mods.add(clazz.getConstructor(String.class, LegacyInnerCoreMod.class).newInstance(path, (LegacyInnerCoreMod) mod));
                    }catch (Exception e){
                        Logger.error(ICLog.getStackTrace(e));
                    }
                }
            }catch (Exception ignore){
            }
        }

        for(Mod mod : Mod.mods) {
            try{
                mod.runMod(factory);
            } catch (Exception e){
                final String errorText = ICLog.getStackTrace(e);
                DialogHelper.openFormattedDialog(errorText, mod.getName());
                Logger.error(ICLog.getStackTrace(e));
            }
        }
        factory.build();
    }
}
