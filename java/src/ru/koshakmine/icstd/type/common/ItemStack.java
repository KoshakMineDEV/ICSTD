package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.apparatus.api.container.ItemContainerSlot;
import com.zhekasmirnov.innercore.api.NativeItem;
import com.zhekasmirnov.innercore.api.NativeItemInstance;
import com.zhekasmirnov.innercore.api.NativeItemInstanceExtra;
import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Wrapper;

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

    public static ItemStack fromPointer(long ptr){
        return new ItemStack(ptr);
    }

    private ItemStack(long ptr){
        build(new NativeItemInstance(ptr));
    }

    public ItemStack(ScriptableObject instance) {
        build(instance);
    }

    protected void build(ScriptableObject item){
        set(ScriptableObjectHelper.getIntProperty(item, "id", 0), ScriptableObjectHelper.getIntProperty(item, "count", 0),
                ScriptableObjectHelper.getIntProperty(item, "data", 0), (NativeItemInstanceExtra) ScriptableObjectHelper.getProperty(item, "extra", null));
    }

    protected void build(ItemContainerSlot slot){
        set(slot.getId(), slot.getCount(), slot.getData(), slot.getExtra());
    }

    protected void build(ItemInstance instance){
        set(instance.getId(), instance.getCount(), instance.getData(), instance.getExtra());
    }

    protected void build(NativeItemInstance instance){
        set(instance.id, instance.count, instance.data, instance.extra);
    }

    protected void build(com.zhekasmirnov.apparatus.adapter.innercore.game.item.ItemStack instance){
        set(instance.id, instance.count, instance.data, instance.extra);
    }

    protected void build(NativeArray array){
        NativeItemInstanceExtra extra  = null;
        if(array.size() == 4)
            extra = (NativeItemInstanceExtra) array.get(3, array);
        set(((Number) array.get(0, array)).intValue(), ((Number) array.get(1, array)).intValue(), ((Number) array.get(2, array)).intValue(), extra);
    }

    public ItemStack(Object item){
        if(item instanceof Wrapper)
            item = ((Wrapper) item).unwrap();

        if(item instanceof ItemContainerSlot) {
            build((ItemContainerSlot) item);
            return;
        }else if(item instanceof NativeArray){
            build((NativeArray) item);
            return;
        }else if(item instanceof NativeItemInstance){
            build(((NativeItemInstance) item));
            return;
        }else if(item instanceof com.zhekasmirnov.apparatus.adapter.innercore.game.item.ItemStack){
            build((com.zhekasmirnov.apparatus.adapter.innercore.game.item.ItemStack) item);
            return;
        }

        build((ScriptableObject) item);
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public void set(int id, int count, int data, NativeItemInstanceExtra extra){
        this.id = id;
        this.count = count;
        this.data = data;
        this.extra = extra;
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

    public int getMaxStack(){
        return NativeItem.getMaxStackForId(id, data);
    }

    public int getMaxData(){
        return NativeItem.getMaxDamageForId(id, data);
    }

    public int getArmorValue(){
        return NativeItem.getArmorValue(id);
    }

    public String getName(){
        return NativeItem.getNameForId(id, data);
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
