package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.innercore.api.NativeItemInstanceExtra;
import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;

public class ItemStack {
    public int id, count, data;
    public NativeItemInstanceExtra extra;

    public ItemStack(int id, int count, int data, NativeItemInstanceExtra extra) {
        this.id = id;
        this.count = count;
        this.data = data;
        this.extra = extra;
    }

    public ItemStack(int id, int count, int data) {
        this(id, count, data, null);
    }

    public ItemStack(ItemInstance instance) {
        this(instance.getId(), instance.getCount(), instance.getData(), instance.getExtra());
    }

    @Override
    public String toString() {
        return "ItemStack{" +
                "id=" + id +
                ", count=" + count +
                ", data=" + data +
                ", extra=" + extra +
                '}';
    }
}
