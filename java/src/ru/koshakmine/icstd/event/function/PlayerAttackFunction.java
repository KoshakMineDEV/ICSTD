package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.entity.Entity;
import ru.koshakmine.icstd.entity.Player;

public interface PlayerAttackFunction {
    void call(Entity entity, Player attacker);
}
