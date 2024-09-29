package ru.koshakmine.icstd.block.blockentity;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.innercore.api.NativeAPI;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.runtime.Updatable;
import ru.koshakmine.icstd.runtime.saver.IRuntimeSaveObject;
import ru.koshakmine.icstd.runtime.saver.Saver;
import ru.koshakmine.icstd.type.LinkedList;
import ru.koshakmine.icstd.type.common.Position;

import java.util.HashMap;
import java.util.UUID;

public class BlockEntity extends Updatable implements IRuntimeSaveObject {
    private static final HashMap<String, IBlockEntityHolder> builders = new HashMap<>();

    public static void registerBlockEntity(String name, IBlockEntityHolder builder){
        builders.put(name, builder);
    }

    public final Position position;
    public final UUID uuid = UUID.randomUUID();
    public final String type;
    public int x, y, z, id;
    public final NativeBlockSource region;
    private boolean removed = false;

    private final Object lockTick = new Object();

    private LinkedList.Entry<BlockEntity> entry;

    public BlockEntity(String type, int id, Position position, NativeBlockSource region){
        this.position = position;
        this.type = type;
        this.x = (int) Math.floor(position.x);
        this.y = (int) Math.floor(position.y);
        this.z = (int) Math.floor(position.z);
        this.id = id;
        this.region = region;
    }

    // Events
    public void onInit(){}
    public void onRemove(){}
    public void onLoad(JSONObject jsonObject) throws JSONException {}
    public void onSave(JSONObject json) throws JSONException {}

    @Override
    public boolean update() {
        synchronized (lockTick){
            if(!canRemove())
                ((ITickedBlockEntity) this).onTick();
        }
        return canRemove();
    }

    public boolean canDestroyBlockEntity() {
        return region.getBlockId(x, y, z) != id;
    }

    public boolean canRemove(){
        return removed;
    }

    public final void removeBlockEntity(){
        synchronized (lockTick) {
            onRemove();

            removed = true;
            entry.remove();
            Saver.removeSaver(this);
        }
    }

    private static final LinkedList<BlockEntity> allEntity = new LinkedList<>();

    public static void addBlockEntity(BlockEntity entity){
        entity.entry = allEntity.add(entity);
        if(entity instanceof ITickedBlockEntity) Updatable.addUpdatable(entity);
        Saver.addSaver(entity);
        entity.onInit();
    }

    public static BlockEntity getBlockEntity(int x, int y, int z, NativeBlockSource region){
        final LinkedList.Entry<BlockEntity> blockEntityEntry = find(x, y, z, region);
        if(blockEntityEntry != null)
            return blockEntityEntry.self;
        return null;
    }

    private static LinkedList.Entry<BlockEntity> find(int x, int y, int z, NativeBlockSource region){
        LinkedList.Entry<BlockEntity> first = allEntity.getFirst();
        while (first != null){
            final BlockEntity entity = first.self;

            if(entity.position.x == x && entity.position.y == y && entity.position.z == z && region.getDimension() == entity.region.getDimension())
                return first;

            first = first.next;
        }

        return null;
    }

    @Override
    public final UUID getSaveId() {
        return uuid;
    }

    @Override
    public final String getName() {
        return "block_entity";
    }

    @Override
    public final JSONObject save() throws JSONException {
        final JSONObject json = new JSONObject();

        json.put("t", type);
        json.put("p", position.toJson());
        json.put("d", region.getDimension());

        onSave(json);

        return json;
    }

    static {
        Saver.registerRuntimeSaveObject("block_entity", jsonObject -> {
            final IBlockEntityHolder builder = builders.get(jsonObject.getString("t"));
            if(builder == null) return;
            final NativeBlockSource region = NativeBlockSource.getDefaultForDimension(jsonObject.getInt("d"));
            if(region != null) {
                final BlockEntity blockEntity = builder.createBlockEntity(new Position(jsonObject.getJSONObject("p")), region);
                blockEntity.onLoad(jsonObject);
                addBlockEntity(blockEntity);
            }
        });

        Event.onCall(Events.BreakBlock, args -> {
            if(NativeAPI.isDefaultPrevented()){
                return;
            }

            final Position position = new Position((ScriptableObject) args[1]);
            final NativeBlockSource region = (NativeBlockSource) args[0];

            final BlockEntity entity = BlockEntity.getBlockEntity((int) position.x, (int) position.y, (int) position.z, region);
            if(entity != null){
                entity.removeBlockEntity();
            }
        });

        Event.onCall(Events.LevelLeft, args -> {
            allEntity.clear();
        }, -5);

        Event.onCall(Events.tick, args -> {
            LinkedList.Entry<BlockEntity> first = allEntity.getFirst();
            while (first != null){
                final BlockEntity entity = first.self;

                if(entity.canDestroyBlockEntity())
                    entity.removeBlockEntity();

                first = first.next;
            }

            NativeAPI.tipMessage("BlockEntity: "+allEntity.size());
        });
    }
}
