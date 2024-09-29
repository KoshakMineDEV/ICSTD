package ru.koshakmine.icstd.commontypes;

import com.zhekasmirnov.apparatus.adapter.innercore.game.common.Vector3;
import com.zhekasmirnov.innercore.api.commontypes.Coords;

public class Position extends Vector3 {
    private static float getCoord(Coords coords, String name){
        final Object value = coords.get(name);

        if(value instanceof Number){
            return ((Number) value).floatValue();
        }

        throw new RuntimeException("Not coords!");
    }

    public Position(Coords coords){
        super(getCoord(coords, "x"), getCoord(coords, "y"), getCoord(coords, "z"));
    }

    public Position(float x, float y, float z){
        super(x, y, z);
    }

    public Position(float[] coords){
        super(coords);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
