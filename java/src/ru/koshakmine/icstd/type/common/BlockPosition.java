package ru.koshakmine.icstd.type.common;

import org.mozilla.javascript.ScriptableObject;

public class BlockPosition extends Position {
    public int side;
    public Position vec;
    public Position relative;

    public BlockPosition(ScriptableObject coords) {
        super(coords);

        this.side = (int) coords.get("side", coords);
        this.vec = new Position((ScriptableObject) coords.get("vec", coords));
        this.relative = new Position((ScriptableObject) coords.get("relative", coords));
    }

    @Override
    public String toString() {
        return "BlockPosition{" +
                "side=" + side +
                ", vec=" + vec +
                ", relative=" + relative +
                ", x=" + x +
                ", z=" + z +
                ", y=" + y +
                '}';
    }
}
