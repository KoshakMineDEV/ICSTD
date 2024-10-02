package ru.koshakmine.icstd.impl;

import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.blockentity.BlockEntity;
import ru.koshakmine.icstd.block.blockentity.ITickingBlockEntity;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.entity.Player;

public class TestBlockEntity extends BlockEntity implements ITickingBlockEntity {
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
    }

    @Override
    public void onLoad(JSONObject jsonObject) throws JSONException {
        data = jsonObject.getInt("data");
        active = jsonObject.getBoolean("active");
    }

    @Override
    public void onSave(JSONObject json) throws JSONException {
        json.put("data", data);
        json.put("active", active);
    }

    @Override
    public void onClick(Position position, ItemStack stack, Player player) {
        active = !active;
        Level.clientMessage("Change mode active: "+active);
    }

    @Override
    public void onTick() {
        if(active && ++tick % 100 == 0){
            level.spawnDroppedItem(x + .5f, y + 1.5f, z + .5f, new ItemStack(264, 1, 0));
        }
    }

    @Override
    public int getHideDistance() {
        return 16;
    }
}
