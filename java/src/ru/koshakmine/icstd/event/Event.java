package ru.koshakmine.icstd.event;

import com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState;
import com.zhekasmirnov.innercore.api.commontypes.Coords;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import com.zhekasmirnov.innercore.api.runtime.Callback;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import ru.koshakmine.icstd.event.function.BuildBlockFunction;
import ru.koshakmine.icstd.event.function.DestroyBlockFunction;
import ru.koshakmine.icstd.event.function.EventFunction;

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
                function.call((Coords) objects[0], (BlockState) objects[1], (long) objects[2]);
                return null;
            }
        }, 0);
    }

    public static void onBuildBlock(BuildBlockFunction function) {
        Callback.addCallback(Events.BuildBlock, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((Coords) objects[0], (BlockState) objects[1], (long) objects[2]);
                return null;
            }
        }, 0);
    }

}
