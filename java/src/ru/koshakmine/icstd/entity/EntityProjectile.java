package ru.koshakmine.icstd.entity;

import com.zhekasmirnov.innercore.api.NativeAPI;
import ru.koshakmine.icstd.type.common.ItemStack;

public class EntityProjectile extends Entity {
    public EntityProjectile(long uid) {
        super(uid);
    }

    public ItemStack getDroppedItem(){
        return ItemStack.fromPointer(NativeAPI.getItemFromProjectile(uid));
    }
}
