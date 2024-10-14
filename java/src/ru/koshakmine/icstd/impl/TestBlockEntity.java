package ru.koshakmine.icstd.impl;

import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.blockentity.BlockEntityContainer;
import ru.koshakmine.icstd.block.blockentity.ticking.ITickingBlockEntity;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.ItemID;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.entity.Player;

public class TestBlockEntity extends BlockEntityContainer implements ITickingBlockEntity {
    public int data, tick;
    public boolean active;

    public TestBlockEntity(String type, int id, Position position, Level level) {
        super(type, type, id, position, level);
    }

    @Override
    public void onInit() {
        Level.clientMessage("Init block entity");
        super.onInit();
    }

    @Override
    public void onRemove() {
        Level.clientMessage("Remove block entity");
        super.onRemove();
    }

    @Override
    public void onLoad(JSONObject json) throws JSONException {
        data = json.getInt("data");
        active = json.getBoolean("active");

        super.onLoad(json);
    }

    @Override
    public void onSave(JSONObject json) throws JSONException {
        json.put("data", data);
        json.put("active", active);

        super.onSave(json);
    }

    @Override
    public void onClick(Position position, ItemStack stack, Player player) {
        if(stack.id == ItemID.STICK) {
            active = !active;
            Level.clientMessage("Change mode active: " + active);
        }

        super.onClick(position, stack, player);
    }

    @Override
    public String getScreenName(Position position, ItemStack stack, Player player) {
        return "main";
    }

    @Override
    public void onTick() {
        final ItemStack slot = container.getSlot("slot1");
        if(active && ++tick % 100 == 0 && !slot.isEmpty()){
            level.spawnDroppedItem(x + .5f, y + 1.5f, z + .5f, new ItemStack(slot.id, 1, slot.data, slot.extra));
            slot.decrease(1);
            container.setSlot("slot1", slot);
            container.sendChanges();
        }
    }

    @Override
    public int getHideDistance() {
        return 16;
    }
}
