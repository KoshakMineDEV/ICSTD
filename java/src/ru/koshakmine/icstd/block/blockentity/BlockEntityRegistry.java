package ru.koshakmine.icstd.block.blockentity;

import java.util.HashMap;

public class BlockEntityRegistry<T> {
    private final HashMap<String, T> builders = new HashMap<>();
    private final HashMap<Integer, T> buildersForBlocks = new HashMap<>();

    public void registerBlockEntity(String type, T builder){
        builders.put(type, builder);
    }

    public void registerBlockEntity(String type, int id){
        buildersForBlocks.put(id, builders.get(type));
    }

    public T get(String type){
        return builders.get(type);
    }

    public T get(int blockId){
        return buildersForBlocks.get(blockId);
    }
}
