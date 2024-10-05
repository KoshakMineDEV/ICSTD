package ru.koshakmine.icstd.item;

import com.zhekasmirnov.innercore.api.NativeItem;
import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI;
import com.zhekasmirnov.innercore.api.unlimited.IDRegistry;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.type.CreativeCategory;
import ru.koshakmine.icstd.type.common.IBaseRegister;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Texture;

import java.util.HashMap;

public abstract class Item implements IBaseRegister {
    private static final HashMap<Integer, IUsableItem> using = new HashMap<>();
    private static final HashMap<Integer, IClickableItem> clickable = new HashMap<>();

    static {
        Event.onItemUse((position, item, block, player) -> {
            final IClickableItem clickableItem = clickable.get(item.id);
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
    }

    private final NativeItem item;

    @Override
    public int getNumId() {
        return item.id;
    }

    public boolean isGlint(){
        return false;
    }

    public int getDamage(){
        return 0;
    }

    public int getMaxStack(){
        return 64;
    }

    public boolean isLiquidClip(){
        return false;
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

    public abstract Texture getTexture();

    public Item(){
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

        if(this instanceof IUsableItem){
            final IUsableItem usingItem = (IUsableItem) this;

            item.setMaxUseDuration(usingItem.getUsingDuration());
            item.setUseAnimation(usingItem.getType().ordinal());

            using.put(id, usingItem);
        }

        if(this instanceof IClickableItem){
            clickable.put(id, (IClickableItem) this);
        }

        item.setGlint(isGlint());
        item.setMaxDamage(getDamage());
        item.setMaxStackSize(getMaxStack());
        item.setLiquidClip(isLiquidClip());

        item.setArmorDamageable(isArmorDamageable());
        item.setExplodable(isExplodable());
        item.setShouldDespawn(isShouldDespawn());
        item.setFireResistant(isFireResistant());
        item.setHandEquipped(isToolRender());

        final CreativeCategory category = getCreativeCategory();
        if(category != null){
            item.setCreativeCategory(category.ordinal());
        }
    }

    public NativeItem getItem() {
        return item;
    }
}
