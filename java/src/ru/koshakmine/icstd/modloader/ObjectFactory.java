package ru.koshakmine.icstd.modloader;

import com.zhekasmirnov.innercore.api.runtime.other.PrintStacking;
import ru.koshakmine.icstd.block.Block;
import ru.koshakmine.icstd.item.Item;

import java.util.HashMap;

public class ObjectFactory {
    public interface IFactory<T extends IBaseRegister> {
        T factory();
    }

    private final HashMap<String, IBaseRegister> register = new HashMap<>();

    public Block addBlock(IFactory<Block> factory){
        return add(factory);
    }

    public Item addItem(IFactory<Item> factory){
        return add(factory);
    }

    public <T extends IBaseRegister>T add(IFactory<T> factory){
        return add(factory.factory());
    }

    public <T extends IBaseRegister>T add(T base){
        register.put(base.getId(), base);
        return base;
    }

    public IBaseRegister get(String id){
        return register.get(id);
    }

    public void build(){
        for (IBaseRegister base : register.values()) {
            base.factory();
        }
    }
}
