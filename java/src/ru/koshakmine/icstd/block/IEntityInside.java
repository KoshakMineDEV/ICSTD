package ru.koshakmine.icstd.block;

import ru.koshakmine.icstd.entity.Entity;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.Position;

public interface IEntityInside {
    void onEntityInside(Position position, BlockData block, Entity entity);
}
