package ru.koshakmine.icstd.event.function;

import ru.koshakmine.icstd.entity.Entity;
import ru.koshakmine.icstd.entity.EntityItem;
import ru.koshakmine.icstd.type.common.ItemStack;

public interface EntityPickUpDropFunction {
    void call(Entity entity, EntityItem item, ItemStack itemStack, int count);
}
