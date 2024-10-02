package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.apparatus.adapter.innercore.game.common.Vector3;
import com.zhekasmirnov.innercore.api.commontypes.Coords;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.ScriptableObject;

import java.util.Objects;

public class Position extends Vector3 {
    private static float getCoord(ScriptableObject coords, String name){
        final Object value = coords.get(name);

        if(value instanceof Number){
            return ((Number) value).floatValue();
        }

        throw new RuntimeException("Not coords!");
    }

    public Position(ScriptableObject coords){
        super(getCoord(coords, "x"), getCoord(coords, "y"), getCoord(coords, "z"));
    }

    public Position(float x, float y, float z){
        super(x, y, z);
    }

    public Position(float[] coords){
        super(coords);
    }

    public Position(JSONObject json) throws JSONException {
        this(json.getInt("x"), json.getInt("y"), json.getInt("z"));
    }

    public JSONObject toJson() throws JSONException {
        final JSONObject json = new JSONObject();
        json.put("x", x);
        json.put("y", y);
        json.put("z", z);
        return json;
    }

    public boolean equalsPosition(Object o) {
        Position position = (Position) o;
        return x == position.x && y == position.y && z == position.z;
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
