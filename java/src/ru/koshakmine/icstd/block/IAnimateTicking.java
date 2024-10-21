package ru.koshakmine.icstd.block;

import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.Position;

public interface IAnimateTicking {
    void onAnimateTick(Position position, BlockData block);
}
