package ru.koshakmine.icstd.ui.type;

import com.zhekasmirnov.apparatus.api.container.ItemContainer;
import ru.koshakmine.icstd.type.common.Position;

public interface IOnClick {
    void onClick(ItemContainer container, Position position);
}
