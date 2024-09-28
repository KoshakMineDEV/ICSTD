package ru.koshakmine.icstd.event.function;

import com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState;
import com.zhekasmirnov.innercore.api.commontypes.Coords;

public interface DestroyBlockFunction {
    void call(Coords coords, BlockState state, long playerId);
}
