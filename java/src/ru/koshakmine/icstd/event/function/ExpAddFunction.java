package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.entity.Player;

public interface ExpAddFunction {
    void call(int experience, Player player);
}
