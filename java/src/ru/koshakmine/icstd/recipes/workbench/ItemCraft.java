package ru.koshakmine.icstd.recipes.workbench;

import com.zhekasmirnov.innercore.api.NativeItemInstanceExtra;
import ru.koshakmine.icstd.type.ItemID;
import ru.koshakmine.icstd.type.block.BlockID;
import ru.koshakmine.icstd.type.common.ItemStack;

public class ItemCraft extends ItemStack {
    private final String itemId;

    public ItemCraft(String id, int count, int data, NativeItemInstanceExtra extra) {
        super(Integer.MIN_VALUE, count, data, extra);

        this.itemId = id;
    }

    public ItemCraft(String id, int count, int data) {
        super(Integer.MIN_VALUE, count, data);

        this.itemId = id;
    }

    public ItemCraft(String id, int data) {
        super(Integer.MIN_VALUE, data);

        this.itemId = id;
    }

    public ItemCraft(int id, int count, int data, NativeItemInstanceExtra extra) {
        super(id, count, data, extra);

        this.itemId = null;
    }

    public ItemCraft(int id, int count, int data) {
        super(id, count, data);

        this.itemId = null;
    }

    public ItemCraft(int id, int data) {
        super(id, data);

        this.itemId = null;
    }

    public ItemStack factory() {
        if(id == Integer.MIN_VALUE){
            final Integer numId = ItemID.getModId(itemId);
            if(numId != null)
                id = numId;
            else
                id = BlockID.getModId(itemId, 0);;
        }
        return this;
    }
}
