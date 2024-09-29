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
import ru.koshakmine.icstd.type.common.BlockData;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.event.function.*;

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
                function.call(new Position((Coords) objects[0]), new BlockData((FullBlock) objects[1]), new BlockData((FullBlock) objects[2]), (NativeBlockSource) objects[3]);
                return null;
            }
        }, 0);
    }

    public static void onItemUse(ItemUseFunction function) {
        Callback.addCallback(Events.ItemUse, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new BlockPosition((Coords) objects[0]), new ItemStack((ItemInstance) objects[1]), new BlockData((FullBlock) objects[2]), (long) objects[4]);
                return null;
            }
        }, 0);
    }

}
