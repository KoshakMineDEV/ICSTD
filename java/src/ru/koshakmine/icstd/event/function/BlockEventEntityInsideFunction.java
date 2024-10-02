package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.entity.Entity;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.Position;

public interface BlockEventEntityInsideFunction {
    void call(Position position, BlockData block, Entity entity);
}
