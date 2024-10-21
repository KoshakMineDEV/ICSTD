package ru.koshakmine.icstd.block;

import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.Position;

public interface IRandomTicking {
    void onRandomTick(Position position, BlockData block, Level level);
}
