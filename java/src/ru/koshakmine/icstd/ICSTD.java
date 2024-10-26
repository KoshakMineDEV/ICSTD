package ru.koshakmine.icstd;

import java.util.HashMap;
import java.util.concurrent.Executor;

import com.zhekasmirnov.apparatus.modloader.LegacyInnerCoreMod;
import com.zhekasmirnov.horizon.runtime.logger.Logger;
import com.zhekasmirnov.innercore.mod.build.Config;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.modloader.Mod;
import ru.koshakmine.icstd.modloader.ObjectFactory;
import ru.koshakmine.icstd.runtime.PostLevelLoaded;

/**
 * TODO LIST
 * Кеширование Player для более высокой производительности и экономии памяти
 * api для кастумных измерений
 * api для кастумных зачарований
 * api для комманд
 */

public class ICSTD extends Mod {
    // The game client does not work well with a lot of traffic, there may be crashes!
    public static boolean MULTI_THREAD = false;
    public static ClassLoader loader;

    public static void onMultiThreadRun(Executor executor, Runnable runnable){
        if(MULTI_THREAD) executor.execute(runnable);
        else runnable.run();
    }

    public ICSTD(String dir, LegacyInnerCoreMod mod) {
        super(dir, mod);
    }

    @Override
    public void runMod(ObjectFactory factory) {
        final Config config = getConfig();
        try{
            final JSONObject json = new JSONObject();

            json.put("enabled", true);
            json.put("multi_thread", false);

            config.checkAndRestore(json);
        }catch (JSONException ignore){}

        MULTI_THREAD = config.getBool("multi_thread");
    }


    public static void boot(HashMap<?, ?> args) {
        Logger.debug("Loaded mod", "ICSTD");
        loader = ICSTD.class.getClassLoader();
        PostLevelLoaded.boot();
    }
}