package ru.koshakmine.icstd.item;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.innercore.api.NativeItem;
import com.zhekasmirnov.innercore.api.commontypes.Coords;
import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI;
import com.zhekasmirnov.innercore.api.unlimited.IDRegistry;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.item.event.IClickable;
import ru.koshakmine.icstd.item.event.IDispense;
import ru.koshakmine.icstd.item.event.IOverrideIcon;
import ru.koshakmine.icstd.item.event.IOverrideName;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.modloader.Mod;
import ru.koshakmine.icstd.modloader.ObjectFactory;
import ru.koshakmine.icstd.recipes.RecipeRegistry;
import ru.koshakmine.icstd.type.CreativeCategory;
import ru.koshakmine.icstd.modloader.IBaseRegisterGameObject;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Texture;

import java.util.HashMap;
import java.util.UUID;

public abstract class Item implements IBaseRegisterGameObject {
    protected static final ObjectFactory FACTORY = Mod.getFactory();

    private static final HashMap<Integer, IUsableItem> using = new HashMap<>();
    private static final HashMap<Integer, IClickable> clickable = new HashMap<>();
    private static final HashMap<Integer, IDispense> dispenses = new HashMap<>();
    private static final HashMap<Integer, IOverrideName> overrideName = new HashMap<>();
    private static final HashMap<Integer, IOverrideIcon> overrideIcon = new HashMap<>();

    public static void registerUsing(int id, IUsableItem usableItem){
        using.put(id, usableItem);
    }

    public static void registerClick(int id, IClickable click){
        clickable.put(id, click);
    }

    public static void registerDispense(int id, IDispense dispense){
        dispenses.put(id, dispense);
    }

    public static void registerOverrideName(int id, IOverrideName item){
        overrideName.put(id, item);
    }

    public static void registerOverrideIcon(int id, IOverrideIcon item){
        overrideIcon.put(id, item);
    }

    static {
        Event.onItemUse((position, item, block, player) -> {
            final IClickable clickableItem = clickable.get(item.id);
            if (clickableItem != null) {
                clickableItem.onClick(position, item, block, player);
            }
        });

        Event.onCall(Events.ItemUsingComplete, (args) -> {
            final ItemStack item = new ItemStack((ItemInstance) args[0]);
            final IUsableItem usingItem = using.get(item.id);

            if (usingItem != null) {
                usingItem.onItemUsingComplete(item, new Player((long) args[1]));
            }
        });

        Event.onCall(Events.ItemDispensed, (args) -> {
            final ItemStack item = new ItemStack((ItemInstance) args[1]);
            final IDispense dispense = dispenses.get(item.id);

            if(dispense != null){
                dispense.onDispense(new BlockPosition((Coords) args[0]), item, Level.getForRegion((NativeBlockSource) args[2]), ((Number) args[3]).intValue());
            }
        });

        Event.onCall(Events.ItemNameOverride, (args -> {
            final ItemStack item = new ItemStack((ItemInstance) args[0]);
            final IOverrideName override = overrideName.get(item.id);

            if(override != null){
                String name = override.onOverrideName(item, args[1].toString(), args[2].toString());
                if(name != null){
                    AdaptedScriptAPI.Item.overrideCurrentName(name);
                }
            }
        }));

        Event.onCall(Events.ItemIconOverride, (args -> {
            final ItemStack item = new ItemStack(args[0]);
            final IOverrideIcon override = overrideIcon.get(item.id);

            if(override != null){
                override.onOverrideIcon(item, (Boolean) args[1]);
            }
        }));
    }

    private NativeItem item;

    @Override
    public int getNumId() {
        if(item == null)
            return IDRegistry.genItemID(getId());
        return item.id;
    }

    public boolean isGlint(){
        return false;
    }

    public int getDurability(){
        return 0;
    }

    public int getMaxStack(){
        return 64;
    }

    public boolean isLiquidClip(){
        return false;
    }

    public ItemGroup getCreativeItemGroup(){
        return null;
    }

    public CreativeCategory getCreativeCategory(){
        return null;
    }

    public boolean isArmorDamageable(){
        return false;
    }

    public boolean isExplodable(){
        return true;
    }

    public boolean isShouldDespawn(){
        return true;
    }

    public boolean isFireResistant(){
        return false;
    }

    public boolean isToolRender(){
        return false;
    }

    @Override
    public void onPreInit() {}

    @Override
    public void onInit() {}

    @Override
    public int getPriority() {
        return 0;
    }

    private final UUID uuid = UUID.randomUUID();
    @Override
    public UUID getUUID() {
        return uuid;
    }

    public abstract Texture getTexture();

    protected void createItem(){
        Texture texture = getTexture();
        if(texture == null) texture = Texture.EMPTY;
        final int id = IDRegistry.genItemID(getId());

        if(this instanceof IFoodItem) {
            // Food is created differently on the server core and on the client
            this.item = AdaptedScriptAPI.Item.createFoodItem(id, getId(), getName(), texture.texture, texture.meta, ((IFoodItem) this).getFood());
        } else if (this instanceof IArmorItem) {
            final IArmorItem armorItem = (IArmorItem) this;
            this.item = NativeItem.createArmorItem(id, getId(), getName(), texture.texture, texture.meta, armorItem.getArmorPlayerTexture(),
                    armorItem.getSlot().ordinal(), armorItem.getDefense(), armorItem.getDuration(), armorItem.getKnockbackResist());
        } else if (this instanceof IThrowableItem) {
            this.item = NativeItem.createThrowableItem(id, getId(), getName(), texture.texture, texture.meta);
        } else {
            this.item = NativeItem.createItem(id, getId(), getName(), texture.texture, texture.meta);
        }
    }

    public static void registerEvents(IBaseRegisterGameObject self){
        final int id = self.getNumId();

        if (self instanceof IClickable) {
            clickable.put(id, (IClickable) self);
        }

        if (self instanceof IFurnaceBurn) {
            RecipeRegistry.addFurnaceFuel(id, -1, ((IFurnaceBurn) self).getFuelBurn());
        }

        if(self instanceof IOverrideName) {
            registerOverrideName(id, (IOverrideName) self);
        }

        if(self instanceof IOverrideIcon) {
            registerOverrideIcon(id, (IOverrideIcon) self);
        }
    }

    @Override
    public void factory() {
        createItem();

        final int id = IDRegistry.genItemID(getId());

        if (this instanceof IUsableItem) {
            final IUsableItem usingItem = (IUsableItem) this;

            item.setMaxUseDuration(usingItem.getUsingDuration());
            item.setUseAnimation(usingItem.getType().ordinal());

            using.put(id, usingItem);
        }

        registerEvents(this);

        item.setGlint(isGlint());
        item.setMaxDamage(getDurability());
        item.setMaxStackSize(getMaxStack());
        item.setLiquidClip(isLiquidClip());

        item.setArmorDamageable(isArmorDamageable());
        item.setExplodable(isExplodable());
        item.setShouldDespawn(isShouldDespawn());
        item.setFireResistant(isFireResistant());
        item.setHandEquipped(isToolRender());

        final CreativeCategory category = getCreativeCategory();
        if (category != null) {
            NativeItem.addToCreative(getNumId(), 1, 0, null);
            item.setCreativeCategory(category.ordinal());
            final ItemGroup group = getCreativeItemGroup();
            if(group != null)
                group.addItem(getNumId());
        }
    }

    public NativeItem getItem() {
        return item;
    }

    public ItemStack getStack(int count, int data) {
        return new ItemStack(getNumId(), count, data);
    }

    public ItemStack getStack(int count) {
        return getStack(count, 0);
    }

    public ItemStack getStack() {
        return getStack(1, 0);
    }
}
