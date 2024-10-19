package ru.koshakmine.icstd.item;

import com.zhekasmirnov.innercore.api.NativeItem;
import com.zhekasmirnov.innercore.api.runtime.other.NameTranslation;
import ru.koshakmine.icstd.modloader.IBaseRegisterGameObject;

import java.util.ArrayList;
import java.util.UUID;

public abstract class ItemGroup implements IBaseRegisterGameObject {
    private ArrayList<Integer> ids = new ArrayList<>();
    private boolean postRegister = false;

    @Override
    public int getNumId() {return 0;}

    @Override
    public int getPriority() {
        return -1;
    }

    @Override
    public void onPreInit() {}

    @Override
    public void onInit() {}

    public void addItem(int id){
        if(postRegister){
            NativeItem.addToCreativeGroup(getId(), NameTranslation.translate(getName()), id);
            return;
        }
        ids.add(id);
    }

    @Override
    public void factory() {
        postRegister = true;
        for(int id : ids)
            NativeItem.addToCreativeGroup(getId(), NameTranslation.translate(getName()), id);
    }

    private final UUID uuid = UUID.randomUUID();
    @Override
    public UUID getUUID() {
        return uuid;
    }
}
