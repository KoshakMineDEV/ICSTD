package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.apparatus.api.container.ItemContainer;
import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.blockentity.BlockEntity;
import ru.koshakmine.icstd.block.blockentity.ITickedBlockEntity;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.Position;

public class TestBlockEntity extends BlockEntity implements ITickedBlockEntity {
    public int data, tick;
    private ItemContainer container;

    public TestBlockEntity(String type, int id, Position position, NativeBlockSource region) {
        super(type, id, position, region);
    }

    @Override
    public void onInit() {
        Level.clientMessage("Init tile entity");
    }

    @Override
    public void onRemove() {
        Level.clientMessage("Remove tile entity");
    }

    @Override
    public void onLoad(JSONObject jsonObject) throws JSONException {
        data = jsonObject.getInt("data");
    }

    @Override
    public void onSave(JSONObject json) throws JSONException {
        json.put("data", data);
    }

    @Override
    public void onTick() {
        if(++tick % 100 == 0){
            region.spawnDroppedItem(x + .5f, y + 1.5f, z + .5f, 264, 1, 0, null);
        }
    }
}
