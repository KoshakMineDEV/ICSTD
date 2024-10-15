package ru.koshakmine.icstd.block.blockentity.ticking;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.block.blockentity.BlockEntity;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.Position;

public class JsTickBlockEntity extends BlockEntity {
    private final ScriptableObject tile;
    private final Function tickFunc;

    public JsTickBlockEntity(int id, int x, int y, int z, int dimension, ScriptableObject tile) {
        super("", "", id, new Position(x, y, z), Level.getForDimension(dimension));

        this.tile = tile;
        this.tickFunc = (Function) tile.get("update", tile);
    }

    public ScriptableObject getTile() {
        return tile;
    }

    public Function getTick() {
        return tickFunc;
    }
}
