package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.entity.EntityProjectile;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.ProjectileHitTarget;

public interface ProjectileHitFunction {
    void call(EntityProjectile projectile, ItemStack item, ProjectileHitTarget target);
}
