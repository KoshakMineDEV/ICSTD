package ru.koshakmine.icstd.impl;

import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.runtime.IUpdate;

public class TestUpdatable implements IUpdate {
    private int tick;

    public TestUpdatable(Position position){
        Level.getLocalLevel().message("Init updatable" + position);
    }

    @Override
    public boolean update() {
        Level.getLocalLevel().message("Test tick: "+(++tick));
        return tick > 5;
    }
}
