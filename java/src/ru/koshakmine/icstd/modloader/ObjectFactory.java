package ru.koshakmine.icstd.modloader;

import ru.koshakmine.icstd.block.Block;
import ru.koshakmine.icstd.item.Item;
import ru.koshakmine.icstd.level.particle.Particle;
import ru.koshakmine.icstd.recipes.workbench.WorkbenchRecipeBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ObjectFactory {
    public interface IFactory<T extends IBaseRegister> {
        T factory();
    }

    private final ArrayList<IBaseRegister> list = new ArrayList<>();
    private final HashMap<UUID, IBaseRegister> register = new HashMap<>();

    private boolean postFactory = false;

    public Block addBlock(IFactory<Block> factory){
        return add(factory);
    }

    public Item addItem(IFactory<Item> factory){
        return add(factory);
    }

    public Particle addParticle(IFactory<Particle> factory){
        return add(factory);
    }

    public WorkbenchRecipeBase addRecipe(IFactory<WorkbenchRecipeBase> factory){
        return add(factory);
    }

    public <T extends IBaseRegister>T add(IFactory<T> factory){
        return add(factory.factory());
    }

    public <T extends IBaseRegister>T add(T base){
        if(postFactory){
            base.onPreInit();
            base.factory();
            base.onInit();
            return base;
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPriority() < base.getPriority()) {
                list.add(i, base);
                register.put(base.getUUID(), base);

                return base;
            }
        }

        list.add(base);
        register.put(base.getUUID(), base);

        return base;
    }

    public IBaseRegister get(UUID id){
        return register.get(id);
    }

    public void build(){
        postFactory = true;

        for (IBaseRegister base : list) {
            base.onPreInit();
            base.factory();
            base.onInit();
        }
    }
}
