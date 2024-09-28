package ru.koshakmine.icstd;

import java.io.IOException;
import java.util.HashMap;

import com.zhekasmirnov.innercore.api.runtime.other.PrintStacking;
import ru.koshakmine.icstd.impl.TestBlock;
import ru.koshakmine.icstd.event.Event;

public class ICSTD {
    //TODO: remove this class completely cuz it is used for internal tests

    static {
        Event.onCall("ModsLoaded", args -> {
            TestBlock block = new TestBlock();
        });

        Event.onDestroyBlock((coords, block, player) -> {
            PrintStacking.print("Coords: " + coords + ", Block: " + block + ", Player: " + player);
        });
    }

    public static void boot(HashMap<?, ?> args) throws IOException {

    }
}