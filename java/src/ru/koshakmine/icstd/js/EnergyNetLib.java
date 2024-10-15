package ru.koshakmine.icstd.js;

import com.zhekasmirnov.innercore.mod.executable.Compiler;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;

public class EnergyNetLib {
    private static ScriptableObject EnergyNet, EnergyTypeRegistry, EnergyTileRegistry, api;

    public static void init(ScriptableObject EnergyNet, ScriptableObject EnergyTypeRegistry, ScriptableObject EnergyTileRegistry, ScriptableObject api){
        EnergyNetLib.EnergyNet = EnergyNet;
        EnergyNetLib.EnergyTypeRegistry = EnergyTypeRegistry;
        EnergyNetLib.EnergyTileRegistry = EnergyTileRegistry;
        EnergyNetLib.api = api;
    }

    public static class EnergyType {
        private final ScriptableObject self;

        private EnergyType(ScriptableObject self){
            this.self = self;
        }

        private ScriptableObject getSelf() {
            return self;
        }

        public void registerWire(int wireId, int maxEnergyPacket){
            JsHelper.callFunction(self, "registerWire", wireId, maxEnergyPacket);
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

    public static void addEnergyTileTypeForId(int blockId, EnergyType type){
        JsHelper.callFunction(EnergyTileRegistry, "addEnergyTypeForId", blockId, type.getSelf());
    }

    public static void registerTile(int id){
        JsHelper.callFunction(api, "registerTile", id);
    }
}
