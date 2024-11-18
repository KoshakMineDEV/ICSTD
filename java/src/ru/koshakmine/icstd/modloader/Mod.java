package ru.koshakmine.icstd.modloader;

import com.zhekasmirnov.apparatus.modloader.ApparatusMod;
import com.zhekasmirnov.apparatus.modloader.ApparatusModLoader;
import com.zhekasmirnov.apparatus.modloader.LegacyInnerCoreMod;
import com.zhekasmirnov.horizon.runtime.logger.Logger;
import com.zhekasmirnov.innercore.api.log.DialogHelper;
import com.zhekasmirnov.innercore.api.log.ICLog;
import com.zhekasmirnov.innercore.api.runtime.Callback;
import com.zhekasmirnov.innercore.mod.build.Config;
import com.zhekasmirnov.innercore.utils.FileTools;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.javascript.Scriptable;
import ru.koshakmine.icstd.ICSTD;
import ru.koshakmine.icstd.event.Event;

import java.lang.reflect.InvocationTargetException;
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

    public String[] getIntegration(){
        return new String[]{};
    }

    public abstract void runMod(ObjectFactory factory);
    public void onLoadIntegration(String name, Scriptable api) {}

    public int getPriority() {
        return 0;
    }

    public boolean isClient() {
        return false;
    }

    private static final ArrayList<Mod> mods = new ArrayList<>();
    private static final ObjectFactory factory = new ObjectFactory();

    public static ObjectFactory getFactory() {
        return factory;
    }

    public static ArrayList<Mod> getMods() {
        return mods;
    }

    protected static void addMainClass(String modPath, LegacyInnerCoreMod legacyInnerCoreMod, String classPath) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends Mod> clazz = (Class<? extends Mod>) ICSTD.loader.loadClass(classPath);
        final Mod mod_ = clazz.getConstructor(String.class, LegacyInnerCoreMod.class).newInstance(modPath, legacyInnerCoreMod);

        if(mod_.getConfig().getBool("enabled")) {
            boolean added = true;
            for (int a = 0; a < Mod.mods.size(); a++) {
                if (Mod.mods.get(a).getPriority() < mod_.getPriority()) {
                    Mod.mods.add(a, mod_);
                    added = false;
                    break;
                }
            }

            final com.zhekasmirnov.innercore.mod.build.Mod legacyMod = legacyInnerCoreMod.getLegacyModInstance();
            legacyMod.configureMultiplayer(legacyMod.getName(), legacyMod.getVersion(), mod_.isClient());

            if (added) Mod.mods.add(mod_);
            Logger.debug("Register loaded mod: " + mod_.getName() + ", path: " + mod_.getDir());

            String[] integrations = mod_.getIntegration();
            for (String name : integrations) {
                Event.onCall("API:" + name, (args -> {
                    mod_.onLoadIntegration(name, (Scriptable) args[0]);
                }));
            }
        }
    }

    public static void runMods(){
        Callback.invokeCallbackV("ICSTD-PRE-MODS", new Object[0]);

        final List<ApparatusMod> mods = ApparatusModLoader.getSingleton().getAllMods();
        for(final ApparatusMod mod : mods) {
            if (!(mod instanceof LegacyInnerCoreMod)) continue;
            final LegacyInnerCoreMod legacyInnerCoreMod = (LegacyInnerCoreMod) mod;

            final String modPath = mod.getInfo().getProperty("directory_root", String.class, null);

            try{
                final JSONArray json = FileTools.readJSONArray(modPath+"icstd.json");

                for(int i = 0;i < json.length();i++){
                    try{
                        final Object element = json.get(i);

                        if(element instanceof String) {
                            addMainClass(modPath, legacyInnerCoreMod, (String) element);
                        } else if(element instanceof JSONObject) {
                            final JSONObject description = (JSONObject) element;
                            addMainClass(modPath, legacyInnerCoreMod, description.getString("main"));
                        } else {
                            throw new RuntimeException("not support format icstd.json, mod: " + legacyInnerCoreMod.getLegacyModInstance().getName());
                        }

                    }catch (Exception e){
                        Logger.error(ICLog.getStackTrace(e), "ERROR-ICSTD");
                    }
                }
            }catch (Exception ignore){
            }
        }

        Callback.invokeCallbackV("ICSTD-PRE-MODS-RUN", new Object[0]);

        for(Mod mod : Mod.mods) {
            try{
                mod.runMod(factory);
            } catch (Exception e){
                final String errorText = ICLog.getStackTrace(e);
                DialogHelper.openFormattedDialog(errorText, mod.getName());
                Logger.error(errorText);
            }
        }

        Callback.invokeCallbackV("ICSTD-END-MODS-RUN", new Object[0]);

        factory.build();

        Callback.invokeCallbackV("ICSTD-POST-MODS", new Object[0]);
    }
}
