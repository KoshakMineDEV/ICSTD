package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.innercore.api.commontypes.Coords;
import org.mozilla.javascript.ScriptableObject;

public class BlockPosition extends Position {
    public int side;
    public Position vec;
    public Position relative;

    public BlockPosition(Coords coords) {
        super(coords);

        this.side = (int) coords.get("side", coords);
        this.vec = new Position((ScriptableObject) coords.get("vec", coords));
        this.relative = new Position((ScriptableObject) coords.get("relative", coords));
    }
}
