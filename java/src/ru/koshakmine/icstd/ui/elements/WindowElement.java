package ru.koshakmine.icstd.ui.elements;

import com.zhekasmirnov.apparatus.api.container.ItemContainer;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.ui.type.OnClickComponent;

public class WindowElement {
    protected String type;
    protected int x, y;
    protected Integer z = null;
    protected ScriptableObject clicker = null;

    public WindowElement(String type, int x, int y){
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    private void setClicker(String name, OnClickComponent click){
        if(clicker == null)
            clicker = ScriptableObjectHelper.createEmpty();
        clicker.put(name, clicker, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                click.onClick((ItemContainer) args[0], new Position((ScriptableObject) args[3]));
                return null;
            }
        });
    }

    public void setOnClick(OnClickComponent click){
        setClicker("onClick", click);
    }

    public void setOnLongClick(OnClickComponent click){
        setClicker("onLongClick", click);
    }

    public ScriptableObject toElement(){
        final ScriptableObject desc = ScriptableObjectHelper.createEmpty();

        desc.put("type", desc, type);
        desc.put("x", desc, x);
        desc.put("y", desc, y);
        if(z != null)
            desc.put("z", desc, z);

        if(clicker != null)
            desc.put("clicker", desc, clicker);

        return desc;
    }
}
