package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.blockentity.LocalBlockEntity;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.render.animation.AnimationItem;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;

public class TestLocalBlockEntity extends LocalBlockEntity {
    private AnimationItem animation;

    public TestLocalBlockEntity(String type, int id, Position position, NetworkEntity network, JSONObject data) throws JSONException {
        super(type, id, position, network, data);
    }

    @Override
    public void onInit() {
        Level.clientMessage("Init client block entity");

        animation = new AnimationItem(x + .5f, y + 1.5f, z + .5f);
        animation.setItem(new ItemStack(264, 1));
        animation.load();
    }

    @Override
    public void onRemove() {
        Level.clientMessage("Remove client block entity");

        animation.destroy();
    }
}
