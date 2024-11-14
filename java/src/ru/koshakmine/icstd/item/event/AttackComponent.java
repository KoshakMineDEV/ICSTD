package ru.koshakmine.icstd.item.event;

import ru.koshakmine.icstd.entity.Entity;

public interface AttackComponent {
    void onAttack(Entity player, Entity entity);
}
