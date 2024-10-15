package ru.koshakmine.icstd.block.blockentity;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import com.zhekasmirnov.innercore.api.NativeAPI;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.block.IBlockEntityHolder;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.runtime.PostLevelLoaded;
import ru.koshakmine.icstd.runtime.saver.IRuntimeSaveObject;
import ru.koshakmine.icstd.runtime.saver.Saver;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.entity.Player;

import java.util.UUID;

public class BlockEntity extends BlockEntityBase implements IRuntimeSaveObject {
    private static final BlockEntityManager SERVER_MANAGER = new BlockEntityManager(entity -> {
        final NetworkEntity networkEntity = ((BlockEntity) entity).network;
        if(networkEntity != null)
            networkEntity.refreshClients();
    }, NetworkSide.SERVER);
    private static final BlockEntityRegistry<IBlockEntityHolder> SERVER_REGISTRY = new BlockEntityRegistry<>();

    public static BlockEntityManager getManager() {
        return SERVER_MANAGER;
    }

    public static BlockEntityRegistry<IBlockEntityHolder> getRegistry() {
        return SERVER_REGISTRY;
    }

    static {
        Saver.registerRuntimeSaveObject("block_entity", jsonObject -> {
            PostLevelLoaded.SERVER.run(() -> {
                try{
                    final IBlockEntityHolder builder = SERVER_REGISTRY.get(jsonObject.getString("t"));

                    if(builder == null) return;
                    final Level level = Level.getForDimension(jsonObject.getInt("d"));

                    if(level != null) {
                        try{
                            final BlockEntity blockEntity = builder.createBlockEntity(new Position(jsonObject.getJSONObject("p")), level);
                            blockEntity.onLoad(jsonObject);
                            SERVER_MANAGER.addBlockEntity(blockEntity);
                        }catch (JSONException e){}
                    }
                }catch (JSONException e){}
            });
        });

        Event.onCall(Events.BreakBlock, args -> {
            if(NativeAPI.isDefaultPrevented()){
                return;
            }

            final Position position = new Position((ScriptableObject) args[1]);

            final BlockEntity entity = (BlockEntity) SERVER_MANAGER.getBlockEntity(position, Level.getForRegion((NativeBlockSource) args[0]));
            if(entity != null){
                SERVER_MANAGER.removeBlockEntity(entity);
            }
        });

        Event.onItemUse(((position, itemStack, blockData, player) -> {
            final Level region = player.getRegion();
            final BlockEntity entity = (BlockEntity) SERVER_MANAGER.getBlockEntity(position, region);

            if(entity != null){
                entity.onClick(position, itemStack, player);
            }else{
                IBlockEntityHolder holder = SERVER_REGISTRY.get(blockData.id);
                if(holder != null){
                    SERVER_MANAGER.addBlockEntity(holder.createBlockEntity(position, region));
                }

                holder = SERVER_REGISTRY.get(itemStack.id);
                if(holder != null){
                    SERVER_MANAGER.addBlockEntity(holder.createBlockEntity(position.relative, region));
                }
            }
        }));
    }

    public final UUID uuid = UUID.randomUUID();
    protected NetworkEntity network;
    protected String localType;

    public BlockEntity(String type, String localType, int id, Position position, Level level){
        super(position, level, type, id);

        this.localType = localType;
    }

    @Override
    public void onInit() {
        if(LocalBlockEntity.getRegistry().get(localType) != null){
            network = new NetworkEntity(LocalBlockEntity.TYPE, this);
        }else network = null;
    }

    public void onLoad(JSONObject json) throws JSONException {}
    public void onSave(JSONObject json) throws JSONException {}

    // Minecraft events
    public void onClick(Position position, ItemStack stack, Player player) {}


    public NetworkEntity getNetwork() {
        return network;
    }

    public String getLocalType() {
        return localType;
    }

    public JSONObject buildPacketLocal() {
        return new JSONObject();
    }

    public int getHideDistance(){
        return 128;
    }

    @Override
    public final NetworkSide getSide() {
        return NetworkSide.SERVER;
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
        if(canRemove())
            return null;

        final JSONObject json = new JSONObject();

        json.put("t", type);
        json.put("p", position.toJson());
        json.put("d", level.getDimension());

        onSave(json);

        return json;
    }

    @Override
    public final boolean removeBlockEntity() {
        if(super.removeBlockEntity()){
            if(network != null){
                network.remove();
            }
            Saver.removeSaver(this);
            return true;
        }
        return false;
    }
}
