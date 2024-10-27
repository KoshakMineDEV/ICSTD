package ru.koshakmine.icstd.block;

import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.Position;

public interface AnimationTickingComponent {
    void onAnimationTick(Position position, BlockData block);
}
