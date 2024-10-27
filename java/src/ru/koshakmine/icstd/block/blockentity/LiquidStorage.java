package ru.koshakmine.icstd.block.blockentity;

import android.graphics.Rect;
import com.zhekasmirnov.apparatus.api.container.ItemContainer;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI;
import com.zhekasmirnov.innercore.api.mod.ui.container.Container;
import com.zhekasmirnov.innercore.api.runtime.saver.serializer.ScriptableSerializer;
import com.zhekasmirnov.innercore.mod.executable.Compiler;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.UniqueTag;
import ru.koshakmine.icstd.js.LiquidRegistry;

public class LiquidStorage {
    public ScriptableObject liquidAmounts = ScriptableObjectHelper.createEmpty();
    public ScriptableObject liquidLimits = ScriptableObjectHelper.createEmpty();
    public ScriptableObject tileEntity;

    public LiquidStorage(ScriptableObject jsTile){
        tileEntity = jsTile;
        AdaptedScriptAPI.Saver.registerObject(this, LiquidRegistry.liquidStorageSaverId);
    }

    public void fromJs(ScriptableObject tileEntity){
        final Object liquidStorage = tileEntity.get("liquidStorage", tileEntity);
        if(liquidStorage instanceof ScriptableObject){
            final ScriptableObject scriptableObjectLiquid = (ScriptableObject) liquidStorage;

            liquidAmounts = (ScriptableObject) scriptableObjectLiquid.get("liquidAmounts", scriptableObjectLiquid);
            liquidLimits = (ScriptableObject) scriptableObjectLiquid.get("liquidLimits", scriptableObjectLiquid);
        }
        this.tileEntity = tileEntity;
    }

    public void setParent(ScriptableObject parent){
        this.tileEntity = parent;
    }

    public ScriptableObject getParent(ScriptableObject parent){
        return this.tileEntity;
    }

    public boolean hasDataFor(String liquid){
        return liquidAmounts.has(liquid, liquidAmounts);
    }

    public void setLimit(String liquid, float limit){
        if(liquid != null){
            liquidLimits.put(liquid, liquidLimits, limit);
        }else{
            liquidLimits.put("__global", liquidLimits, limit);
        }
    }

    public float getLimit(String liquid){
        Object limit = liquidLimits.get(liquid);
        if(limit instanceof Number) return ((Number) limit).floatValue();
        limit = liquidLimits.get("__global");
        if(limit instanceof Number) return ((Number) limit).floatValue();
        return 99999999;
    }

    public float getAmount(String liquid){
        Object limit = liquidAmounts.get(liquid);
        if(limit instanceof Number) return ((Number) limit).floatValue();
        return 0;
    }

    public float getRelativeAmount(String liquid){
        return getAmount(liquid) / getLimit(liquid);
    }

    private Object getBinding(Object object, String scale, String name){
        if(object instanceof ItemContainer){
            return  ((ItemContainer) object).getBinding(scale,name);
        }else if(object instanceof Container){
            return ((Container) object).getBinding(scale, name);
        }

        return getBinding(((ru.koshakmine.icstd.ui.container.Container) object).getItemContainer(), scale, name);
    }

    private void setBinding(Object object, String scale, String name, Object value){
        if(object instanceof ItemContainer){
            ((ItemContainer) object).setBinding(scale,name, value);
        }else if(object instanceof Container){
            ((Container) object).setBinding(scale, name, value);
        }

        setBinding(((ru.koshakmine.icstd.ui.container.Container) object).getItemContainer(), scale, name, value);
    }

    public void _setContainerScale(Object container, String scale, String liquid, float val){
        final Rect size = (Rect) getBinding(container, scale, "element_rect");

        if(size != null){
            final String texture = LiquidRegistry.getLiquidUITexture(liquid, size.width(), size.height());
            setBinding(container, scale, "texture", texture);
            setBinding(container, scale, "value", val);
        }
    }

    public void updateUiScale(String scale, String liquid, Object container){
        if(container != null){
            _setContainerScale(container, scale, liquid, getRelativeAmount(liquid));
        }else if(tileEntity != null){
            final Object object = tileEntity.get("container", tileEntity);
            if(!(object instanceof UniqueTag)){
                updateUiScale(scale, liquid, object);
            }
        }
    }

    public void setAmount(String liquid, float amount){
        liquidAmounts.put(liquid, liquidAmounts, amount);
    }

    public String[] getLiquids(){
        final Object[] keys = liquidAmounts.getAllIds();
        final String[] res = new String[keys.length];
        for(int i = 0;i < keys.length;i++)
            res[i] = keys[i].toString();
        return res;
    }

    public String getLiquidStored(){
        for(String liquid : getLiquids()){
            if(getAmount(liquid) > 0){
                return liquid;
            }
        }

        return null;
    }

    public boolean isFull(String liquid){
        if(liquid != null){
            return getLimit(liquid) < getAmount(liquid);
        }else{
            for(String name : getLiquids()){
                if(!isFull(name))
                    return false;
            }
        }

        return true;
    }

    public boolean isEmpty(String liquid){
        if(liquid != null){
            return getAmount(liquid) <= 0;
        }else{
            for(String name : getLiquids()){
                if(!isEmpty(name))
                    return false;
            }
        }

        return true;
    }

    public float addLiquid(String liquid, float amount, boolean onlyFullAmount){
        final float limit = getLimit(liquid);
        final float stored = getAmount(liquid);
        final float result = stored + amount;
        final float left = result - Math.min(limit, result);

        if(!onlyFullAmount || left <= 0){
            setAmount(liquid, result - left);
            return Math.max(left, 0);
        }

        return amount;
    }

    public float addLiquid(String liquid, float amount){
        return addLiquid(liquid, amount, false);
    }

    private boolean getLiquid_flag = false;
    public float getLiquid(String liquid, float amount, boolean onlyFullAmount){
        float stored = getAmount(liquid);

        if(!getLiquid_flag && tileEntity != null && stored < amount){
            getLiquid_flag = true;
            Object function = tileEntity.get("requireMoreLiquid", tileEntity);
            if(function instanceof Function)
                ((Function) function).call(Compiler.assureContextForCurrentThread(), ((Function) function).getParentScope(), tileEntity, new Object[]{
                   liquid,
                   amount - stored
                });
            getLiquid_flag = false;
            stored = getAmount(liquid);
        }

        final float got = Math.min(stored, amount);
        if(!onlyFullAmount || got >= amount){
            setAmount(liquid, stored - got);
            return got;
        }

        return 0;
    }

    public float getLiquid(String liquid, float amount){
        return getLiquid(liquid, amount, false);
    }

    public JSONObject save() throws JSONException {
        final JSONObject json = new JSONObject();

        json.put("amounts", ScriptableSerializer.scriptableFromJson(liquidAmounts));
        json.put("limits", ScriptableSerializer.scriptableFromJson(liquidLimits));

        return json;
    }

    public void read(JSONObject json) throws JSONException {
        liquidAmounts = (ScriptableObject) ScriptableSerializer.scriptableToJson(json.get("amounts"), (e)-> {});
        liquidLimits = (ScriptableObject) ScriptableSerializer.scriptableToJson(json.get("liquidLimits"), (e)-> {});
    }
}
