package ru.koshakmine.icstd;

import java.util.HashMap;

import ru.koshakmine.icstd.modloader.Mod;
import ru.koshakmine.icstd.runtime.PostLevelLoaded;

public class ICSTD {
    public static void boot(HashMap<?, ?> args) {
        PostLevelLoaded.boot();
    }
}