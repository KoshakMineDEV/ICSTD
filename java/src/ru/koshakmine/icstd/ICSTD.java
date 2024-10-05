package ru.koshakmine.icstd;

import java.util.HashMap;

import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.impl.TestBlock;
import ru.koshakmine.icstd.impl.TestItem;
import ru.koshakmine.icstd.modloader.Mod;
import ru.koshakmine.icstd.runtime.PostLevelLoaded;

/**
 * TODO LIST
 * ИНСТРУМЕНТЫ
 * ЧАСТИЦЫ
 * ТИК BlockEntity
 * Прослойка совместимости для ванильных TileEntity
 * api для интерфейсов блоков
 * api для работы с StorageInterface
 * различные методы для классов Entity, Player
 * После добавления отправки массива байт в иннере, переписать etwork под это(более быстрая и экномная отправка)
 * Кеширование Player для более высокой производительности и экономии памяти
 * api для переводов
 * api для кастумных измерений
 * api для кастумных биомов
 * api для кастумных зачарований
 * api для рецептов
 * api для работы с файлами
 */

public class ICSTD {
    public static void boot(HashMap<?, ?> args) {
        Event.onCall(Events.ModsLoaded, arg -> {
            new TestBlock();
            new TestItem();
        });
        PostLevelLoaded.boot();
    }
}