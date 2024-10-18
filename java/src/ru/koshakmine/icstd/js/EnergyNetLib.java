package ru.koshakmine.icstd.js;

import com.zhekasmirnov.apparatus.util.Java8BackComp;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.mod.executable.Compiler;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.ScriptableObject;

import java.util.HashMap;

public class EnergyNetLib {
    private static ScriptableObject EnergyTypeRegistry;

    public static void init(ScriptableObject EnergyTypeRegistry){
        EnergyNetLib.EnergyTypeRegistry = EnergyTypeRegistry;
    }

    public static class EnergyType {
        private final ScriptableObject self;

        private EnergyType(ScriptableObject self){
            this.self = self;
        }

        public ScriptableObject getSelf() {
            return self;
        }

        public void registerWire(int wireId, int maxEnergyPacket){
            JsHelper.callFunction(self, "registerWire", wireId, maxEnergyPacket);
        }

        public String getName(){
            return ScriptableObjectHelper.getStringProperty(self, "name", null);
        }
    }

    public static class EnergyTileNode {
        private final ScriptableObject self;
        private final Function add;

        public EnergyTileNode(ScriptableObject self, Function add){
            this.self = self;
            this.add = add;
        }

        public void add(float amount, int power){
            add.call(Compiler.assureContextForCurrentThread(), add.getParentScope(), self, new Object[]{amount, power});
        }
    }

    public static EnergyType assureEnergyType(String name, float energyValue){
        return new EnergyType((ScriptableObject) JsHelper.callFunction(EnergyTypeRegistry, "assureEnergyType", name, energyValue));
    }

    public static final HashMap<Integer, ScriptableObject> energyTypes = new HashMap<>();

    public static void addEnergyTileTypeForId(int blockId, EnergyType type){
        final ScriptableObject types = Java8BackComp.computeIfAbsent(energyTypes, blockId, (java.util.function.Function<Integer, ScriptableObject>) integer -> ScriptableObjectHelper.createEmpty());
        types.put(type.getName(), types, type);

        TileEntity.registerPrototype(blockId, ScriptableObjectHelper.createEmpty());
    }
}
