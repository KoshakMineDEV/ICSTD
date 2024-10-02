package ru.koshakmine.icstd.block;

import ru.koshakmine.icstd.block.blockentity.BlockEntity;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.Position;

public interface IBlockEntityHolder {
    BlockEntity createBlockEntity(Position position, Level region);
}
