package ru.koshakmine.icstd.block;

import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;

public interface PlaceComponent {
    Position onPlace(BlockPosition position, ItemStack item, BlockData block, Player player, Level level);
}
