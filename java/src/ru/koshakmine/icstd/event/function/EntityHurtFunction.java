package ru.koshakmine.icstd.event.function;

public interface EntityHurtFunction {
    void call(long entity, long attacker, int damageType, int damageValue, boolean someBool1, boolean someBool2);
}
