package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.entity.Entity;

public interface EntityHurtFunction {
    void call(Entity entity, Entity attacker, int damageType, int damageValue, boolean someBool1, boolean someBool2);
}
