package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.innercore.api.commontypes.Coords;

public class BlockPosition extends Position {
    public int side;

    public BlockPosition(Coords coords) {
        super(coords);
        this.side = (int) coords.get("side", coords);
    }
}
