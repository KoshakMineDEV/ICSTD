package ru.koshakmine.icstd.level;

import com.zhekasmirnov.innercore.api.NativeAPI;

public class Level {
    public static void clientMessage(String message){
        NativeAPI.clientMessage(message);
    }
}
