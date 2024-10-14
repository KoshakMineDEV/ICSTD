package ru.koshakmine.icstd;

import java.util.HashMap;
import java.util.concurrent.Executor;

import android.graphics.Color;
import com.zhekasmirnov.apparatus.modloader.LegacyInnerCoreMod;
import com.zhekasmirnov.innercore.mod.build.Config;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.impl.TestBlock;
import ru.koshakmine.icstd.impl.TestBlockLiquid;
import ru.koshakmine.icstd.impl.TestItem;
import ru.koshakmine.icstd.modloader.Mod;
import ru.koshakmine.icstd.runtime.PostLevelLoaded;
import ru.koshakmine.icstd.type.ItemID;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.ui.Window;
import ru.koshakmine.icstd.ui.elements.SlotElement;

/**
 * TODO LIST
 * частицы
 * Прослойка совместимости для ванильных TileEntity
 * различные методы для классов Entity, Player
 * После добавления отправки массива байт в иннере, переписать network под это(более быстрая и экономная отправка)
 * Кеширование Player для более высокой производительности и экономии памяти
 * api для работы с StorageInterface
 * api для кастумных измерений
 * api для кастумных биомов
 * api для кастумных зачарований
 * api для рецептов
 * api для комманд
 */

public class ICSTD extends Mod {
    // The game client does not work well with a lot of traffic, there may be crashes!
    public static boolean MULTI_THREAD = false;

    public static void onMultiThreadRun(Executor executor, Runnable runnable){
        if(MULTI_THREAD) executor.execute(runnable);
        else runnable.run();
    }

    public ICSTD(String dir, LegacyInnerCoreMod mod) {
        super(dir, mod);
    }

    @Override
    public void runMod() {
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
        Event.onCall(Events.ModsLoaded, arg -> {
            new TestBlock();
            new TestItem();
            new TestBlockLiquid();
        });

        final Window testWindow = new Window();

        testWindow.setBackgroundColor(Color.TRANSPARENT);
        testWindow.setAsGameOverlay(true);
        testWindow.setBlockingBackground(false);
        testWindow.setTouchable(false);

        Event.onCall(Events.LocalTick, (arg) -> {
            final ItemStack stack = Player.getLocal().getCarriedItem();

            if(stack.id == ItemID.STONE){
                testWindow.putElement("slot", new SlotElement(10, 10, 60)
                        .setVisual(true)
                        .setSource(stack));

                testWindow.open();
            }else
                testWindow.close();
        });

        PostLevelLoaded.boot();
    }


}