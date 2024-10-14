package ru.koshakmine.icstd;

import java.util.HashMap;

import android.graphics.Color;
import com.zhekasmirnov.apparatus.api.container.ItemContainer;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.impl.TestBlock;
import ru.koshakmine.icstd.impl.TestBlockLiquid;
import ru.koshakmine.icstd.impl.TestItem;
import ru.koshakmine.icstd.runtime.PostLevelLoaded;
import ru.koshakmine.icstd.type.BlockID;
import ru.koshakmine.icstd.type.ItemID;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.ui.Window;
import ru.koshakmine.icstd.ui.WindowStandard;
import ru.koshakmine.icstd.ui.elements.SlotElement;
import ru.koshakmine.icstd.ui.elements.TextElement;

/**
 * TODO LIST
 * частицы
 * тик BlockEntity
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

public class ICSTD {
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