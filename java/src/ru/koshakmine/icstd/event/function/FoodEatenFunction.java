package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.entity.Player;

public interface FoodEatenFunction {
    void call(int food, float ratio, Player player);
}
