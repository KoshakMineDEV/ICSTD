package ru.koshakmine.icstd.type.common;

import org.mozilla.javascript.Scriptable;

import java.util.Objects;

public class BlockPosition extends Position {
    public int side;
    public Position vec;
    public Position relative;

    public BlockPosition(Scriptable coords) {
        super(coords);

        this.side = ((Number) coords.get("side", coords)).intValue();
        this.vec = new Position((Scriptable) coords.get("vec", coords));
        this.relative = new Position((Scriptable) coords.get("relative", coords));
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BlockPosition that = (BlockPosition) object;
        return side == that.side && Objects.equals(vec, that.vec) && Objects.equals(relative, that.relative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(side, vec, relative);
    }
}
