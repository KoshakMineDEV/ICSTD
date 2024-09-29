package ru.koshakmine.icstd.type.common;

import com.zhekasmirnov.innercore.api.commontypes.FullBlock;

public class BlockData {
    public int id, data;

    public BlockData(FullBlock block) {
        this.id = block.id;
        this.data = block.data;
    }

    @Override
    public String toString() {
        return "BlockData{id=" + id + ", data=" + data + "}";
    }
}
