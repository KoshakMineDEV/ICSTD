package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState;
import org.mozilla.javascript.Scriptable;

import java.util.Objects;

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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BlockData blockData = (BlockData) object;
        return id == blockData.id && data == blockData.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data);
    }
}
