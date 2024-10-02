package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.entity.Entity;

public interface EntityDeathFunction {
    void call(Entity entity, Entity attacker, int damageType);
}
