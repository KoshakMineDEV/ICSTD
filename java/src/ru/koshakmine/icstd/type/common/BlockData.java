package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState;
import com.zhekasmirnov.innercore.api.commontypes.FullBlock;

public class BlockData {
    public int id, data;

    public BlockData(FullBlock block) {
        this.id = block.id;
        this.data = block.data;
    }

    public BlockData(int id, int data){
        this.id = id;
        this.data = data;
    }

    public BlockData(BlockState state) {
        this(state.id, state.data);
    }

    @Override
    public String toString() {
        return "BlockData{id=" + id + ", data=" + data + "}";
    }
}
