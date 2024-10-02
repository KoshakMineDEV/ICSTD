package ru.koshakmine.icstd.event;

import com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState;
import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.innercore.api.commontypes.Coords;
import com.zhekasmirnov.innercore.api.commontypes.FullBlock;
import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import com.zhekasmirnov.innercore.api.runtime.Callback;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.event.function.*;
import ru.koshakmine.icstd.entity.Player;

public class Event {
    public static void onCall(String name, EventFunction function, int priority) {
        Callback.addCallback(name, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(objects);
                return null;
            }
        }, priority);
    }

    public static void onCall(String name, EventFunction function) {
        onCall(name, function, 0);
    }

    public static void onDestroyBlock(DestroyBlockFunction function) {
        Callback.addCallback(Events.DestroyBlock, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Coords) objects[0]), (BlockState) objects[1], (long) objects[2]);
                return null;
            }
        }, 0);
    }

    public static void onDestroyBlockStart(DestroyBlockFunction function) {
        Callback.addCallback(Events.DestroyBlockStart, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Coords) objects[0]), (BlockState) objects[1], (long) objects[2]);
                return null;
            }
        }, 0);
    }

    public static void onDestroyBlockContinue(DestroyBlockContinueFunction function) {
        Callback.addCallback(Events.DestroyBlockContinue, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Coords) objects[0]), (BlockState) objects[1], (long) objects[2]);
                return null;
            }
        }, 0);
    }

    public static void onBuildBlock(BuildBlockFunction function) {
        Callback.addCallback(Events.BuildBlock, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Coords) objects[0]), (BlockState) objects[1], (long) objects[2]);
                return null;
            }
        }, 0);
    }

    public static void onBlockChanged(BlockChangedFunction function) {
        Callback.addCallback(Events.BlockChanged, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Coords) objects[0]), new BlockData((FullBlock) objects[1]), new BlockData((FullBlock) objects[2]), Level.getForRegion((NativeBlockSource) objects[3]));
                return null;
            }
        }, 0);
    }

    public static void onItemUse(ItemUseFunction function) {
        Callback.addCallback(Events.ItemUse, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new BlockPosition((Coords) objects[0]), new ItemStack((ItemInstance) objects[1]), new BlockData((FullBlock) objects[2]), new Player((long) objects[4]));
                return null;
            }
        }, 0);
    }

    public static void onFoodEaten(FoodEatenFunction function) {
        Callback.addCallback(Events.FoodEaten, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((int) objects[0], (float) objects[1], (long) objects[2]);
                return null;
            }
        }, 0);
    }

    public static void onExpAdd(ExpAddFunction function) {
        Callback.addCallback(Events.ExpAdd, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((int) objects[0], (long) objects[1]);
                return null;
            }
        }, 0);
    }

    public static void onExpLevelAdd(ExpLevelAddFunction function) {
        Callback.addCallback(Events.ExpLevelAdd, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((int) objects[0], (long) objects[1]);
                return null;
            }
        }, 0);
    }

    public static void onEntityInteract(EntityInteractFunction function) {
        Callback.addCallback(Events.EntityInteract, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((long) objects[0], (long) objects[1], new Position((Coords) objects[2]));
                return null;
            }
        }, 0);
    }

    public static void onEntityAdded(EntityAddedFunction function) {
        Callback.addCallback(Events.EntityAdded, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((long) objects[0]);
                return null;
            }
        }, 0);
    }

    public static void onEntityRemoved(EntityAddedFunction function) {
        Callback.addCallback(Events.EntityRemoved, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((long) objects[0]);
                return null;
            }
        }, 0);
    }

    public static void onEntityAddedLocal(EntityAddedFunction function) {
        Callback.addCallback(Events.EntityAddedLocal, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((long) objects[0]);
                return null;
            }
        }, 0);
    }

    public static void onEntityRemovedLocal(EntityAddedFunction function) {
        Callback.addCallback(Events.EntityRemovedLocal, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((long) objects[0]);
                return null;
            }
        }, 0);
    }

    public static void onEntityDeath(EntityDeathFunction function) {
        Callback.addCallback(Events.EntityDeath, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((long) objects[0], (long) objects[1], (int) objects[2]);
                return null;
            }
        }, 0);
    }

    public static void onEntityHurt(EntityHurtFunction function) {
        Callback.addCallback(Events.EntityHurt, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((long) objects[0], (long) objects[1], (int) objects[2], (int) objects[3], (boolean) objects[4], (boolean) objects[5]);
                return null;
            }
        }, 0);
    }

    public static void onServerPlayerTick(PlayerTickFunction function) {
        Callback.addCallback(Events.ServerPlayerTick, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((long) objects[0], (boolean) objects[1]);
                return null;
            }
        }, 0);
    }

    public static void onLocalPlayerTick(PlayerTickFunction function) {
        Callback.addCallback(Events.LocalPlayerTick, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((long) objects[0], (boolean) objects[1]);
                return null;
            }
        }, 0);
    }

}
