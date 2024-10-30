package ru.koshakmine.icstd.block;

import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.Position;

public interface PopResourcesComponent {
    void onPopResources(Position position, BlockData block, Level level, double explosionRadius);
}
