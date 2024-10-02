package ru.koshakmine.icstd.level;

import com.zhekasmirnov.apparatus.adapter.innercore.game.entity.StaticEntity;
import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.apparatus.util.Java8BackComp;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI;
import ru.koshakmine.icstd.entity.EntityItem;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.type.common.ItemStack;

import java.util.HashMap;
import java.util.function.Function;

public class Level {
    public static void clientMessage(String message){
        NativeAPI.clientMessage(message);
    }

    private final NativeBlockSource region;

    private Level(NativeBlockSource region){
        this.region = region;
    }

    private static final HashMap<Integer, Level> levels = new HashMap<>();
    private static Level localLevel;

    static {
        Event.onCall(Events.LevelLeft, args -> {
            levels.clear();
            localLevel = null;
        });
    }

    public static Level getLocalLevel(){
        if(localLevel == null){
            localLevel = new Level(NativeBlockSource.getCurrentClientRegion());
        }
        return localLevel;
    }

    public static Level getForDimension(int dimension){
        return Java8BackComp.computeIfAbsent(levels, dimension, (Function<Integer, Level>) id -> new Level(NativeBlockSource.getDefaultForDimension(id)));
    }

    public static Level getForActor(long entity){
        return getForDimension(StaticEntity.getDimension(entity));
    }

    public static Level getForRegion(NativeBlockSource object) {
        return getForDimension(object.getDimension());
    }

    public int getDimension(){
        return region.getDimension();
    }

    public int getBlockId(int x, int y, int z) {
        return region.getBlockId(x, y, z);
    }

    public EntityItem spawnDroppedItem(float x, float y, float z, ItemStack stack) {
        return new EntityItem(region.spawnDroppedItem(x, y, z, stack.id, stack.count, stack.data, stack.extra));
    }
}
