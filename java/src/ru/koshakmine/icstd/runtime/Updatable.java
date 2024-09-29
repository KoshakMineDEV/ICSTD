package ru.koshakmine.icstd.runtime;

import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.network.NetworkSide;

public abstract class Updatable implements IUpdate {
    private final ScriptableObject self;

    public Updatable(){
        self = ScriptableObjectHelper.createEmpty();

        self.put("update", self, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                final boolean remove = update();
                if(remove){
                    self.put("remove", self, true);
                }

                return null;
            }
        });
    }

    public static void addUpdatable(NetworkSide networkSide, Updatable updatable){
        if(networkSide == NetworkSide.SERVER){
            com.zhekasmirnov.innercore.api.runtime.Updatable.getForServer().addUpdatable(updatable.self);
        }else{
            com.zhekasmirnov.innercore.api.runtime.Updatable.getForClient().addUpdatable(updatable.self);
        }
    }

    public static void addUpdatable(Updatable updatable){
        addUpdatable(NetworkSide.SERVER, updatable);
    }

    public static void addUpdatable(NetworkSide networkSide, IUpdate update){
        addUpdatable(networkSide, new Updatable() {
            @Override
            public boolean update() {
                return update.update();
            }
        });
    }

    public static void addUpdatable(IUpdate update){
        addUpdatable(NetworkSide.SERVER, update);
    }
}
