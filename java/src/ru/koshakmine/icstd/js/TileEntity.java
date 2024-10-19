package ru.koshakmine.icstd.js;

import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.Position;

public class TileEntity {
    private static ScriptableObject TileEntity;

    public static void init(ScriptableObject api){
        TileEntity = api;
    }

    public static void addTileEntity(int x, int y, int z, Level level){
        JsHelper.callFunction(TileEntity, "addTileEntity", x, y, z, level.getRegion());
    }

    public static void addTileEntity(Position position, Level level){
        addTileEntity((int) position.x, (int) position.y, (int) position.z, level);
    }

    public static ScriptableObject getTileEntity(int x, int y, int z, Level level){
        Object res = JsHelper.callFunction(TileEntity, "getTileEntity", x, y, z, level.getRegion());
        if(res instanceof ScriptableObject)
            return (ScriptableObject) res;
        return null;
    }

    public static ScriptableObject getTileEntity(Position position, Level level){
        return getTileEntity((int) position.x, (int) position.y, (int) position.z, level);
    }

    public static void registerPrototype(int id, ScriptableObject tile){
        JsHelper.callFunction(TileEntity, "registerPrototype", id, tile);
    }
}
