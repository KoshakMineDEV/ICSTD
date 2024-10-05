package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.Position;

public interface BlockEventNeighbourChangeFunction {
    void call(Position position, BlockData block, Position neighbourPosition, Level blockSource);
}
