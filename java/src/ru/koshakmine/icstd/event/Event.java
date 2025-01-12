package ru.koshakmine.icstd.event;

import com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState;
import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.innercore.api.commontypes.Coords;
import com.zhekasmirnov.innercore.api.commontypes.FullBlock;
import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.commontypes.ScriptableParams;
import com.zhekasmirnov.innercore.api.mod.util.ScriptableFunctionImpl;
import com.zhekasmirnov.innercore.api.runtime.Callback;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.entity.Entity;
import ru.koshakmine.icstd.entity.EntityItem;
import ru.koshakmine.icstd.entity.EntityProjectile;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.*;
import ru.koshakmine.icstd.event.function.*;
import ru.koshakmine.icstd.entity.Player;

import java.util.Random;

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
                function.call(new Position((Scriptable) objects[0]), (BlockState) objects[1], Player.from((long) objects[2]));
                return null;
            }
        }, 0);
    }

    public static void onDestroyBlockStart(DestroyBlockFunction function) {
        Callback.addCallback(Events.DestroyBlockStart, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Scriptable) objects[0]), (BlockState) objects[1], Player.from((long) objects[2]));
                return null;
            }
        }, 0);
    }

    public static void onDestroyBlockContinue(DestroyBlockContinueFunction function) {
        Callback.addCallback(Events.DestroyBlockContinue, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Scriptable) objects[0]), (BlockState) objects[1], (long) objects[2]);
                return null;
            }
        }, 0);
    }

    public static void onBuildBlock(BuildBlockFunction function) {
        Callback.addCallback(Events.BuildBlock, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Scriptable) objects[0]), (BlockState) objects[1], Player.from((long) objects[2]));
                return null;
            }
        }, 0);
    }

    public static void onBlockChanged(BlockChangedFunction function) {
        Callback.addCallback(Events.BlockChanged, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Scriptable) objects[0]), new BlockData((Scriptable) objects[1]), new BlockData((Scriptable) objects[2]), Level.getForRegion((NativeBlockSource) objects[3]));
                return null;
            }
        }, 0);
    }

    public static void onItemUse(ItemUseFunction function, int priority) {
        Callback.addCallback(Events.ItemUse, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new BlockPosition((Scriptable) objects[0]), new ItemStack((Scriptable) objects[1]), new BlockData((Scriptable) objects[2]), Player.from((long) objects[4]));
                return null;
            }
        }, priority);
    }

    public static void onItemUse(ItemUseFunction function) {
        onItemUse(function, 0);
    }

    public static void onItemUseNoTarget(ItemUseNoTargetFunction function) {
        Callback.addCallback(Events.ItemUseNoTarget, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new ItemStack((Scriptable) objects[0]), Player.from((long) objects[1]));
                return null;
            }
        }, 0);
    }

    public static void onItemUsingReleased(ItemUsingReleasedFunction function) {
        Callback.addCallback(Events.ItemUsingReleased, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new ItemStack((Scriptable) objects[0]), (int) objects[1], Player.from((long) objects[2]));
                return null;
            }
        }, 0);
    }

    public static void onItemUsingComplete(ItemUsingCompleteFunction function) {
        Callback.addCallback(Events.ItemUsingComplete, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new ItemStack((Scriptable) objects[0]), Player.from((long) objects[1]));
                return null;
            }
        }, 0);
    }

    public static void onFoodEaten(FoodEatenFunction function) {
        Callback.addCallback(Events.FoodEaten, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((int) objects[0], (float) objects[1], Player.from((long) objects[2]));
                return null;
            }
        }, 0);
    }

    public static void onExpAdd(ExpAddFunction function) {
        Callback.addCallback(Events.ExpAdd, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((int) objects[0], Player.from((long) objects[1]));
                return null;
            }
        }, 0);
    }

    public static void onExpLevelAdd(ExpLevelAddFunction function) {
        Callback.addCallback(Events.ExpLevelAdd, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((int) objects[0], Player.from((long) objects[1]));
                return null;
            }
        }, 0);
    }

    public static void onEntityInteract(EntityInteractFunction function) {
        Callback.addCallback(Events.EntityInteract, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(Entity.from((long) objects[0]), Player.from((long) objects[1]), new Position((Scriptable) objects[2]));
                return null;
            }
        }, 0);
    }

    public static void onPlayerAttack(PlayerAttackFunction function) {
        Callback.addCallback(Events.PlayerAttack, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(Entity.from((long) objects[0]), Player.from((long) objects[1]));
                return null;
            }
        }, 0);
    }

    public static void onEntityAdded(EntityAddedFunction function) {
        Callback.addCallback(Events.EntityAdded, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(Entity.from((long) objects[0]));
                return null;
            }
        }, 0);
    }

    public static void onEntityRemoved(EntityAddedFunction function) {
        Callback.addCallback(Events.EntityRemoved, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(Entity.from((long) objects[0]));
                return null;
            }
        }, 0);
    }

    public static void onEntityAddedLocal(EntityAddedFunction function) {
        Callback.addCallback(Events.EntityAddedLocal, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(Entity.from((long) objects[0]));;
                return null;
            }
        }, 0);
    }

    public static void onEntityRemovedLocal(EntityAddedFunction function) {
        Callback.addCallback(Events.EntityRemovedLocal, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(Entity.from((long) objects[0]));
                return null;
            }
        }, 0);
    }

    public static void onEntityDeath(EntityDeathFunction function) {
        Callback.addCallback(Events.EntityDeath, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(Entity.from((long) objects[0]), Entity.from((long) objects[1]), (int) objects[2]);
                return null;
            }
        }, 0);
    }

    public static void onEntityHurt(EntityHurtFunction function) {
        Callback.addCallback(Events.EntityHurt, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(Entity.from((long) objects[0]), Entity.from((long) objects[1]), (int) objects[2], (int) objects[3], (boolean) objects[4], (boolean) objects[5]);
                return null;
            }
        }, 0);
    }

    public static void onEntityPickUpDrop(EntityPickUpDropFunction function) {
        Callback.addCallback(Events.EntityPickUpDrop, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(Entity.from((long) objects[0]), (EntityItem) Entity.from((long) objects[1]), new ItemStack((Scriptable) objects[2]), (int) objects[3]);
                return null;
            }
        }, 0);
    }

    public static void onProjectileHit(ProjectileHitFunction function) {
        Callback.addCallback(Events.ProjectileHit, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new EntityProjectile((long) objects[0]), new ItemStack((Scriptable) objects[1]), new ProjectileHitTarget((ScriptableParams) objects[2]));
                return null;
            }
        }, 0);
    }

    public static void onServerPlayerTick(PlayerTickFunction function) {
        Callback.addCallback(Events.ServerPlayerTick, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(Player.from((long) objects[0]), (boolean) objects[1]);
                return null;
            }
        }, 0);
    }

    public static void onBlockEventEntityInside(BlockEventEntityInsideFunction function) {
        Callback.addCallback(Events.BlockEventEntityInside, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Scriptable) objects[0]), new BlockData((Scriptable) objects[1]), Entity.from((long) objects[2]));
                return null;
            }
        }, 0);
    }

    public static void onBlockEventEntityStepOn(BlockEventEntityInsideFunction function) {
        Callback.addCallback(Events.BlockEventEntityStepOn, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Scriptable) objects[0]), new BlockData((Scriptable) objects[1]), Entity.from((long) objects[2]));
                return null;
            }
        }, 0);
    }

    public static void onBlockEventNeighbourChange(BlockEventNeighbourChangeFunction function) {
        Callback.addCallback(Events.BlockEventNeighbourChange, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(new Position((Scriptable) objects[0]), new BlockData((Scriptable) objects[1]), new Position((Scriptable) objects[2]), Level.getForRegion((NativeBlockSource) objects[3]));
                return null;
            }
        }, 0);
    }

    public static void onLocalPlayerTick(PlayerTickFunction function) {
        Callback.addCallback(Events.LocalPlayerTick, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call(Player.from((long) objects[0]), (boolean) objects[1]);
                return null;
            }
        }, 0);
    }

    public static void onLevelSelected(LevelSelectedFunction function) {
        Callback.addCallback(Events.LevelSelected, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] objects) {
                function.call((String) objects[0], (String) objects[1]);
                return null;
            }
        }, 0);
    }

    public static void onGenerationChunk(String name, GenerateChunkFunction function){
        Callback.addCallback(name, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                final int dimensionId = ((Number) args[3]).intValue();
                function.call(((Number) args[0]).intValue(), ((Number) args[1]).intValue(), (Random) args[2],
                        dimensionId, ((Number) args[4]).longValue(), ((Number) args[5]).longValue(),
                        ((Number) args[6]).longValue(), Level.getGenRegion());
                return null;
            }
        }, 0);
    }

    public static void onBiomeMap(GenerateChunkFunction function){
        onGenerationChunk(Events.GenerateBiomeMap, function);
    }

    public static void onGenerateChunk(GenerateChunkFunction function){
        onGenerationChunk(Events.GenerateChunk, function);
    }

    public static void onCustomDimensionGenerateChunk(GenerateChunkFunction function){
        onGenerationChunk(Events.GenerateCustomDimensionChunk, function);
    }

    public static void onChunkLoadingStateChanged(IChunkLoadingStateChanged function){
        Callback.addCallback(Events.ChunkLoadingStateChanged, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                function.call(((Number) args[0]).intValue(), ((Number) args[1]).intValue(), ((Number) args[2]).intValue(),
                        ((Number) args[3]).intValue(), ((Number) args[4]).intValue(), (Boolean) args[5]);
                return null;
            }
        }, 0);
    }

    public static void onLocalChunkLoadingStateChanged(IChunkLoadingStateChanged function){
        Callback.addCallback(Events.LocalChunkLoadingStateChanged, new ScriptableFunctionImpl() {
            @Override
            public Object call(Context context, Scriptable scriptable, Scriptable scriptable1, Object[] args) {
                function.call(((Number) args[0]).intValue(), ((Number) args[1]).intValue(), ((Number) args[2]).intValue(),
                        ((Number) args[3]).intValue(), ((Number) args[4]).intValue(), (Boolean) args[5]);
                return null;
            }
        }, 0);
    }
}
