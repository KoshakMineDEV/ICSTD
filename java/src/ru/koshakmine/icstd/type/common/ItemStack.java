package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.innercore.api.NativeItem;
import com.zhekasmirnov.innercore.api.NativeItemInstanceExtra;
import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import org.mozilla.javascript.NativeArray;

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

    public ItemStack(int id, int data) {
        this(id, 1, data);
    }

    public ItemStack(ItemInstance instance) {
        this(instance.getId(), instance.getCount(), instance.getData(), instance.getExtra());
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public void reset(){
        id = 0;
        data = 0;
        count = 0;
        extra = null;
    }

    public void applyDamage(int damage){
        data += damage;
        if(data >= NativeItem.getMaxDamageForId(id, 0)){
            reset();
        }
    }

    public ItemStack decrease(int count){
        this.count -= count;
        if(this.count <= 0)
            this.reset();
        return this;
    }

    public NativeArray toDrop(){
        final NativeArray drop = ScriptableObjectHelper.createEmptyArray();

        drop.put(0, drop, id);
        drop.put(1, drop, count);
        drop.put(2, drop, data);
        if(extra != null)
            drop.put(3, drop, id);

        return drop;
    }

    public ItemInstance getItemInstance(){
        return new ItemInstance(id, count, data, extra);
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
