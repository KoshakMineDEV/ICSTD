package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.entity.Player;

public interface PlayerTickFunction {
    void call(Player player, boolean isDead);
}
