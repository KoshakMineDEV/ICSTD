package ru.koshakmine.icstd.recipes.workbench;

import com.zhekasmirnov.innercore.api.NativeItemInstanceExtra;
import com.zhekasmirnov.innercore.api.unlimited.IDRegistry;
import ru.koshakmine.icstd.type.common.ItemStack;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ItemCraft extends ItemStack {
    private static final HashMap<String, Integer> itemIdShortcut;
    private static final HashMap<String, Integer> blockIdShortcut;


    static {
        final Class<IDRegistry> idRegistryClass = IDRegistry.class;

        try {
            final Field itemIdShortcutField = idRegistryClass.getDeclaredField("itemIdShortcut");
            itemIdShortcutField.setAccessible(true);
            itemIdShortcut = (HashMap<String, Integer>) itemIdShortcutField.get(null);

            final Field blockIdShortcutField = idRegistryClass.getDeclaredField("blockIdShortcut");
            blockIdShortcutField.setAccessible(true);
            blockIdShortcut = (HashMap<String, Integer>) blockIdShortcutField.get(null);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

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
            if(itemIdShortcut.containsKey(itemId))
                id = itemIdShortcut.get(itemId);
            else
                id = blockIdShortcut.getOrDefault(itemId, 0);
        }
        return this;
    }
}
