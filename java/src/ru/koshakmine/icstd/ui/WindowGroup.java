package ru.koshakmine.icstd.ui;

import com.zhekasmirnov.innercore.api.mod.ui.window.UIWindowGroup;

import java.util.Collection;

public class WindowGroup implements IWindow {
    protected final UIWindowGroup group;

    protected WindowGroup(UIWindowGroup group){
        this.group = group;
    }

    public WindowGroup(){
        this(new UIWindowGroup());
    }

    public void removeWindow(String name){
        group.removeWindow(name);
    }

    public void addWindowInstance(String name, Window _window){
        group.addWindowInstance(name, _window.getWindow());
    }

    public Window getWindow(String name){
        return new Window(group.getWindow(name));
    }

    public Collection<String> getWindowNames() {
        return group.getWindowNames();
    }

    public void refreshWindow(String name) {
        group.refreshWindow(name);
    }

    public void refreshAll() {
        group.refreshAll();
    }

    @Override
    public void open() {
        group.open();
    }

    @Override
    public void close() {
        group.close();
    }

    @Override
    public boolean isOpened(){
        return group.isOpened();
    }

    @Override
    public com.zhekasmirnov.innercore.api.mod.ui.window.IWindow getWindow() {
        return group;
    }
}
