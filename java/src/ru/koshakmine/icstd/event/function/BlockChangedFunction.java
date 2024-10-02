package ru.koshakmine.icstd.event.function;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.Position;

public interface BlockChangedFunction {
    void call(Position coords, BlockData oldState, BlockData newState, Level source);
}
