package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.innercore.api.constants.ChatColor;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.item.IArmorItem;
import ru.koshakmine.icstd.item.IClickableItem;
import ru.koshakmine.icstd.item.IUsingItem;
import ru.koshakmine.icstd.item.Item;
import ru.koshakmine.icstd.type.AnimationType;
import ru.koshakmine.icstd.type.ArmorSlot;
import ru.koshakmine.icstd.type.CreativeCategory;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Texture;

public class TestItem extends Item implements IArmorItem, IUsingItem, IClickableItem {
    @Override
    public String getArmorPlayerTexture() {
        return "";
    }

    @Override
    public ArmorSlot getSlot() {
        return ArmorSlot.CHESTPLATE;
    }

    @Override
    public int getDefense() {
        return 1;
    }

    @Override
    public int getDuration() {
        return 16;
    }

    @Override
    public double getKnockbackResist() {
        return 1.0;
    }

    @Override
    public AnimationType getType() {
        return AnimationType.TRIDENT;
    }

    @Override
    public int getUsingDuration() {
        return 20;
    }

    @Override
    public Texture getTexture() {
        return new Texture("stick");
    }

    @Override
    public String getId() {
        return "test_stick";
    }

    @Override
    public String getName() {
        return "Test my item";
    }

    @Override
    public CreativeCategory getCreativeCategory() {
        return CreativeCategory.MATERIAL;
    }

    @Override
    public void onItemUsingComplete(ItemStack item, Player player) {
        player.message(ChatColor.GOLD + "End using test item");
    }

    @Override
    public void onClick(BlockPosition position, ItemStack itemStack, BlockData blockData, Player player) {
        player.message(ChatColor.GREEN + "Click item");
    }
}
