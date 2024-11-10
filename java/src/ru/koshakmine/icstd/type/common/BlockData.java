package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState;
import org.mozilla.javascript.Scriptable;

public class BlockData {
    public int id, data;

    public BlockData(Scriptable block) {
        this.id = ((Number) block.get("id", block)).intValue();
        this.data = ((Number) block.get("data", block)).intValue();
    }

    public BlockData(int id, int data){
        this.id = id;
        this.data = data;
    }

    public BlockData(int id){
        this(id, 0);
    }

    public BlockData(BlockState state) {
        this(state.id, state.data);
    }

    @Override
    public String toString() {
        return "BlockData{id=" + id + ", data=" + data + "}";
    }
}
