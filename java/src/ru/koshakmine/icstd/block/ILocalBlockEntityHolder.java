package ru.koshakmine.icstd.block;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.blockentity.LocalBlockEntity;
import ru.koshakmine.icstd.type.common.Position;

public interface ILocalBlockEntityHolder {


    LocalBlockEntity createLocalBlockEntity(Position position, NetworkEntity entity, JSONObject data) throws JSONException;
}
