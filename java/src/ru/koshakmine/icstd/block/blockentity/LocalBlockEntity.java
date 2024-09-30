package ru.koshakmine.icstd.block.blockentity;


import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntityType;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.type.common.Position;

public class LocalBlockEntity extends BlockEntityBase {
    protected final NetworkEntity network;

    public LocalBlockEntity(String type, int id, Position position, NetworkEntity network, JSONObject data) throws JSONException {
        super(position, Level.getLocalLevel(), type, id);

        this.network = network;
    }

    @Override
    public NetworkSide getSide() {
        return NetworkSide.LOCAL;
    }

    @Override
    public boolean canDestroyBlockEntity() {
        return canRemove();
    }

    private static final BlockEntityManager LOCAL_MANAGER = new BlockEntityManager(Events.LocalTick, entity -> {});
    private static final BlockEntityRegistry<ILocalBlockEntityHolder> LOCAL_REGISTRY = new BlockEntityRegistry<>();
    public static final NetworkEntityType TYPE;

    public static BlockEntityManager getLocalManager() {
        return LOCAL_MANAGER;
    }

    public static BlockEntityRegistry<ILocalBlockEntityHolder> getRegistry() {
        return LOCAL_REGISTRY;
    }

    static {
        TYPE = new NetworkEntityType("icstd.block_entity");

        // SERVER SIDE
        TYPE.setClientAddPacketFactory((o, networkEntity, connectedClient) -> {
            final BlockEntity entity = (BlockEntity) o;
            final JSONObject send = new JSONObject();
            try {

                send.put("p", entity.getPosition().toJson());
                send.put("d", entity.buildPacketLocal());
                send.put("t", entity.getType());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            return send;
        });
        TYPE.setClientListSetupListener((list, o, networkEntity) -> {
            final BlockEntity entityLocal = (BlockEntity) o;
            list.setupDistancePolicy(entityLocal.x, entityLocal.y, entityLocal.z, entityLocal.dimension, entityLocal.getHideDistance());
        });

        // CLIENT SIDE
        TYPE.setClientEntityAddedListener((networkEntity, o) -> {
            final JSONObject data = (JSONObject) o;

            try {
                final ILocalBlockEntityHolder holder = LOCAL_REGISTRY.get(data.getString("t"));
                if(holder != null){
                    final LocalBlockEntity local = holder.createLocalBlockEntity(new Position(data.getJSONObject("p")), networkEntity, data.getJSONObject("d"));
                    LOCAL_MANAGER.addBlockEntity(local);
                    return local;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
        TYPE.setClientEntityRemovedListener((o, networkEntity, o1) -> {
            ((LocalBlockEntity) o).removeBlockEntity();
        });
    }
}
