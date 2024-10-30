package ru.koshakmine.icstd.block;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.blockentity.BlockEntityLocal;
import ru.koshakmine.icstd.type.common.Position;

public interface BlockEntityLocalHolderComponent {


    BlockEntityLocal createLocalBlockEntity(Position position, NetworkEntity entity, JSONObject data) throws JSONException;
}
