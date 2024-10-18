package ru.koshakmine.icstd.modloader;

import java.util.UUID;

public interface IBaseRegister {
    int getPriority();

    void onPreInit();
    void onInit();
    void factory();

    UUID getUUID();
}
