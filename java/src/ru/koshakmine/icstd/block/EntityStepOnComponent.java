package ru.koshakmine.icstd.block;

import ru.koshakmine.icstd.entity.Entity;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.Position;

public interface EntityStepOnComponent {
    void onEntityStepOn(Position position, BlockData block, Entity entity);
}
