package ru.koshakmine.icstd.entity;

import com.zhekasmirnov.apparatus.adapter.innercore.game.entity.StaticEntity;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.NativeItemInstanceExtra;
import ru.koshakmine.icstd.type.common.ItemStack;

public class EntityItem extends Entity {
    public EntityItem(long uid) {
        super(uid);
    }

    public ItemStack getDroppedItem(){
        return new ItemStack(StaticEntity.getDroppedItem(uid));
    }

    public void setDroppedItem(ItemStack item){
        NativeAPI.setItemToDrop(uid, item.id, item.count, item.data, NativeItemInstanceExtra.unwrapValue(item.extra));
    }
}
