package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import com.zhekasmirnov.innercore.api.NativeAPI;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.blockentity.ITickingBlockEntity;
import ru.koshakmine.icstd.block.blockentity.LocalBlockEntity;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.Position;

public class TestLocalBlockEntity extends LocalBlockEntity implements ITickingBlockEntity {
    public TestLocalBlockEntity(String type, int id, Position position, NetworkEntity network, JSONObject data) throws JSONException {
        super(type, id, position, network, data);
    }

    @Override
    public void onInit() {
        Level.clientMessage("Init client block entity");
    }

    @Override
    public void onRemove() {
        Level.clientMessage("Remove client block entity");
    }

    @Override
    public void onTick() {
        NativeAPI.addBreakingItemParticle(5, 3, x + Math.random(), y + Math.random() + 1, z + Math.random());
    }
}
