package ru.koshakmine.icstd.event.function;

public interface EntityDeathFunction {
    void call(long entity, long attacker, int damageType);
}
