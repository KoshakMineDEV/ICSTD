package ru.koshakmine.icstd.impl;

import ru.koshakmine.icstd.commontypes.Position;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.runtime.IUpdate;

public class TestUpdatable implements IUpdate {
    private int tick;

    public TestUpdatable(Position position){
        Level.clientMessage("Init updatable" + position);
    }

    @Override
    public boolean update() {
        Level.clientMessage("Test tick: "+(++tick));
        return tick > 5;
    }
}
