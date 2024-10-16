package ru.koshakmine.icstd.js;

import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.block.blockentity.BlockEntity;
import ru.koshakmine.icstd.type.common.ItemStack;

public class StorageInterfaceLib {
    private static ScriptableObject StorageInterface;

    public static void init(ScriptableObject api){
        StorageInterface = api;
    }

    public static void checkHoppers(BlockEntity entity){
        JsHelper.callFunction(StorageInterface, "checkHoppers", entity.getFakeTileEntity());
    }

    public interface StorageDescriptor {
        String[] getInputSlots(int side);
        String[] getOutputSlots(int side);
        boolean isValidInput(ItemStack item, int side, BlockEntity entity);
        //int addItem(ItemStack item, int side, int maxCount);
    }

    public static void createInterface(int id, StorageDescriptor descriptor){
        final ScriptableObject desc = ScriptableObjectHelper.createEmpty();

        desc.put("getInputSlots", desc, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable parent, Scriptable self, Object[] args) {
                return new NativeArray(descriptor.getInputSlots(((Number) args[0]).intValue()));
            }
        });

        desc.put("getOutputSlots", desc, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable parent, Scriptable self, Object[] args) {
                return new NativeArray(descriptor.getOutputSlots(((Number) args[0]).intValue()));
            }
        });

        desc.put("isValidInput", desc, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable parent, Scriptable self, Object[] args) {
                return descriptor.isValidInput(new ItemStack(args[0]), ((Number) args[1]).intValue(), (BlockEntity) ScriptableObjectHelper.getProperty((ScriptableObject) args[2], "___fakeTile___", null));
            }
        });

        // Not work?
        /*desc.put("addItem", desc, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable parent, Scriptable self, Object[] args) {
                return descriptor.addItem(new ItemStack(args[0]), ((Number) args[1]).intValue(), ((Number) args[2]).intValue());
            }
        });*/

        JsHelper.callFunction(StorageInterface, "createInterface", id, desc);
    }
}
