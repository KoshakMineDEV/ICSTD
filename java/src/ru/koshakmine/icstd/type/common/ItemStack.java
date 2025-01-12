package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.apparatus.api.container.ItemContainerSlot;
import com.zhekasmirnov.innercore.api.NativeItem;
import com.zhekasmirnov.innercore.api.NativeItemInstance;
import com.zhekasmirnov.innercore.api.NativeItemInstanceExtra;
import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Wrapper;

import java.util.Objects;

public class ItemStack implements Cloneable {
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

    public ItemStack set(int id, int count, int data, NativeItemInstanceExtra extra){
        this.id = id;
        this.count = count;
        this.data = data;
        this.extra = extra;
        return this;
    }

    public ItemStack reset(){
        id = 0;
        data = 0;
        count = 0;
        extra = null;
        return this;
    }

    public ItemStack applyDamage(int damage){
        data += damage;
        if(data >= getMaxData())
            reset();
        return this;
    }

    public ItemStack decrease(int count){
        this.count -= count;
        if(this.count <= 0)
            this.reset();
        return this;
    }

    public ItemStack setId(int id) {
        this.id = id;
        return this;
    }

    public ItemStack setCount(int count) {
        this.count = count;
        return this;
    }

    public ItemStack setData(int data) {
        this.data = data;
        return this;
    }

    public ItemStack setExtra(NativeItemInstanceExtra extra) {
        this.extra = extra;
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

    public static JSONObject extraToJson(NativeItemInstanceExtra extra) throws JSONException {
        final JSONObject json = new JSONObject();

        json.put("c", extra.getAllCustomData());
        json.put("cn", extra.getCustomName());

        final JSONArray rawEnchantsJson = new JSONArray();
        final int[][] rawEnchants = extra.getRawEnchants();

        for(int i = 0;i < rawEnchants[0].length;i++) {
            final JSONArray enchantJson = new JSONArray();
            enchantJson.put(rawEnchants[0][i]);//id
            enchantJson.put(rawEnchants[1][i]);//level
            rawEnchantsJson.put(enchantJson);
        }

        json.put("e", rawEnchantsJson);

        return json;
    }

    public static NativeItemInstanceExtra extraFromJson(JSONObject json) throws JSONException {
        final NativeItemInstanceExtra extra = new NativeItemInstanceExtra();

        extra.setAllCustomData(json.getString("c"));
        extra.setCustomName(json.getString("cn"));

        final JSONArray rawEnchantsJson = new JSONArray();
        for(int i = 0;i < rawEnchantsJson.length();i++) {
            final JSONArray enchant = rawEnchantsJson.getJSONArray(i);
            extra.addEnchant(enchant.getInt(0), enchant.getInt(1));
        }

        return extra;
    }

    public JSONObject toJson() throws JSONException {
        final JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("count", count);
        json.put("data", data);
        if(extra != null) json.put("extra", extraToJson(extra));
        return json;
    }

    public static ItemStack fromJson(JSONObject json) throws JSONException {
        NativeItemInstanceExtra extra = null;
        if(!json.isNull("extra"))
            extra = extraFromJson(json.getJSONObject("extra"));
        return new ItemStack(json.getInt("id"), json.getInt("count"), json.getInt("data"), extra);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ItemStack stack = (ItemStack) object;
        return id == stack.id && count == stack.count && data == stack.data && Objects.equals(extra, stack.extra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count, data, extra);
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

    @Override
    public ItemStack clone() {
        try {
            ItemStack clone = (ItemStack) super.clone();
            clone.id = id;
            clone.count = count;
            clone.data = data;
            clone.extra = extra;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
