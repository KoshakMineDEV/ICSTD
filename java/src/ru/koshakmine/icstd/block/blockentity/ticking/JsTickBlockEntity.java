package ru.koshakmine.icstd.block.blockentity.ticking;

import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.block.blockentity.BlockEntity;
import ru.koshakmine.icstd.block.blockentity.BlockEntityBase;
import ru.koshakmine.icstd.block.blockentity.IEnergyTile;
import ru.koshakmine.icstd.js.EnergyNetLib;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.Position;

public class JsTickBlockEntity extends BlockEntity {
    private final ScriptableObject tile, api;
    private final Function tickFunc;

    public JsTickBlockEntity(int id, int x, int y, int z, int dimension, ScriptableObject tile) {
        super("", "", id, new Position(x, y, z), Level.getForDimension(dimension));

        this.tile = tile;
        this.tickFunc = (Function) tile.get("update", tile);

        api = ScriptableObjectHelper.createEmpty();

        api.put("energyTick", api, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable parent, Scriptable self, Object[] args) {
                return apply(tile -> {
                    tile.energyTick(args[0].toString(), new EnergyNetLib.EnergyTileNode((ScriptableObject) args[1], (Function) args[2]));
                    return null;
                }, null);
            }
        });
        api.put("energyReceive", api, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable parent, Scriptable self, Object[] args) {
                return apply(tile -> tile.energyReceive(args[0].toString(), ((Number) args[1]).floatValue(), ((Number) args[2]).intValue()), 0);
            }
        });

        api.put("isConductor", api, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable parent, Scriptable self, Object[] args) {
                return apply(tile -> tile.isConductor(args[0].toString()), false);
            }
        });

        api.put("canReceiveEnergy", api, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable parent, Scriptable self, Object[] args) {
                return apply(tile -> tile.canReceiveEnergy(((Number) args[0]).intValue(), args[1].toString()), true);
            }
        });
        api.put("canExtractEnergy", api, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context ctx, Scriptable parent, Scriptable self, Object[] args) {
                return apply(tile -> tile.canExtractEnergy(((Number) args[0]).intValue(), args[1].toString()), true);
            }
        });
    }

    private IEnergyTile entity;
    public IEnergyTile getBlockEntity(){
        if(entity == null) {
            final BlockEntityBase base = BlockEntity.getManager().getBlockEntity(position, level);;
            if(base instanceof IEnergyTile)
                entity = (IEnergyTile) base;
        }
        return entity;
    }

    interface IApply<T> {
        T apply(IEnergyTile tile);
    }

    public <T>T apply(IApply<T> apply, T def){
        final IEnergyTile tile = getBlockEntity();
        if(tile != null)
            return apply.apply(tile);
        return def;
    }

    public ScriptableObject getApi() {
        return api;
    }

    public ScriptableObject getTile() {
        return tile;
    }

    public Function getTick() {
        return tickFunc;
    }
}
