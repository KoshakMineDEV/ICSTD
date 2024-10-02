package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.entity.Entity;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.type.common.Position;

public interface EntityInteractFunction {
    void call(Entity entity, Player player, Position position);
}
