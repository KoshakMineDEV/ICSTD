package ru.koshakmine.icstd.item.tools;


import ru.koshakmine.icstd.js.ToolAPI;

public abstract class ItemSword extends ItemTool {
    @Override
    public void onInit() {
        ToolAPI.registerSword(getNumId(), this);
    }
}
