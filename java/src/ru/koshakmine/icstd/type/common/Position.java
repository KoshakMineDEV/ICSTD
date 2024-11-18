package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.apparatus.adapter.innercore.game.common.Vector3;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.Scriptable;

public class Position extends Vector3 {
    public static final Position EMPTY = new Position(0, 0, 0);

    private static float getCoord(Scriptable coords, String name){
        final Object value = coords.get(name, coords);

        if(value instanceof Number){
            return ((Number) value).floatValue();
        }

        return 0;
    }

    public Position(Scriptable coords){
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

    public Position add(float x, float y, float z){
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    public Position add(Position position){
        return add(position.x, position.y, position.z);
    }

    public Position add(double x, double y, double z){
        return add((float) x, (float) y, (float) z);
    }

    public Position subtract(float x, float y, float z){
        return add(-x, -y, -z);
    }

    public Position subtract(Position position){
        return subtract(position.x, position.y, position.z);
    }

    public Position subtract(double x, double y, double z){
        return subtract((float) x, (float) y, (float) z);
    }

    public Position multiply(float x, float y, float z){
        return new Position(this.x * x, this.y * y, this.z * z);
    }

    public Position multiply(Position position){
        return multiply(position.x, position.y, position.z);
    }

    public Position multiply(double x, double y, double z){
        return multiply((float) x, (float) y, (float) z);
    }

    public Position multiply(float number) {
        return multiply(number, number, number);
    }

    public Position divide(float x, float y, float z){
        return new Position(this.x / x, this.y / y, this.z / z);
    }

    public Position divide(Position position){
        return divide(position.x, position.y, position.z);
    }

    public Position divide(double x, double y, double z){
        return divide((float) x, (float) y, (float) z);
    }

    public Position divide(float number) {
        return divide(number, number, number);
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
