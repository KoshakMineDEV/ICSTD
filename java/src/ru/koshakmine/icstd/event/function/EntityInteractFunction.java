package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.type.common.Position;

public interface EntityInteractFunction {
    void call(long entity, long player, Position position);
}
