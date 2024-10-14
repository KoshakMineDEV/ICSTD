package ru.koshakmine.icstd.ui;

public interface IWindow {
    void open();
    void close();
    boolean isOpened();
    com.zhekasmirnov.innercore.api.mod.ui.window.IWindow getWindow();
}
