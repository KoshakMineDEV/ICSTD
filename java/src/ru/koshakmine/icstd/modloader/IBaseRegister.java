package ru.koshakmine.icstd.modloader;

public interface IBaseRegister {
    int getNumId();
    String getId();
    String getName();
    void onPreInit();
    void onInit();
}
