package ru.koshakmine.icstd.block.blockentity;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import ru.koshakmine.icstd.type.common.Position;

public interface IBlockEntityHolder {
    BlockEntity createBlockEntity(Position position, NativeBlockSource region);
}
