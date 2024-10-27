package ru.koshakmine.icstd.block.blockentity;

import com.zhekasmirnov.apparatus.api.container.ItemContainer;
import com.zhekasmirnov.apparatus.multiplayer.server.ConnectedClient;

import com.zhekasmirnov.innercore.api.runtime.saver.serializer.ScriptableSerializer;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.ui.container.Container;

public class BlockEntityContainer extends BlockEntity {
    protected Container container = newContainer(null);
    protected LiquidStorage liquidStorage = new LiquidStorage(getFakeTileEntity());

    static {
        ItemContainer.registerScreenFactory("icstd.container.block_entity", (container, name) -> {
            final String[] args = name.split(":");
            final String[] pos = args[0].split(",");

            if(args.length != 2 || pos.length != 3) return null;


            final BlockEntityLocal blockEntity = (BlockEntityLocal) BlockEntityLocal.getLocalManager().getBlockEntity(new Position(Float.valueOf(pos[0]), Float.valueOf(pos[1]), Float.valueOf(pos[2])), Level.getLocalLevel());
            return blockEntity.getScreenByName(args[1]).getWindow();
        });
    }

    public BlockEntityContainer(String type, String localType, int id, Position position, Level level) {
        super(type, localType, id, position, level);
    }

    public void onContainerOpen(ConnectedClient client, String data){}
    public void onContainerClose(ConnectedClient client){}

    public String getScreenName(Position position, ItemStack stack, Player player){
        return null;
    }

    @Override
    public void onClick(Position position, ItemStack stack, Player player) {
        final String name = getScreenName(position, stack, player);
        if(name != null && !player.isSneaking()){
            container.openFor(player, x + "," + y + "," + z + ":" + name);
        }
    }

    public void onInitContainer(Container container){
        container.addServerOpenListener((container1, client, data) -> onContainerOpen(client, data));
        container.addServerCloseListener((container1, client) -> onContainerClose(client));

        container.setClientContainerTypeName("icstd.container.block_entity");
    }

    public Container newContainer(Object slots){
        final Container itemContainer;
        if(slots == null)
            itemContainer = new Container(new ItemContainer());
        else{
            final com.zhekasmirnov.innercore.api.mod.ui.container.Container cont = new com.zhekasmirnov.innercore.api.mod.ui.container.Container();
            cont.slots = (ScriptableObject) ScriptableSerializer.scriptableFromJson(slots);
            itemContainer = new Container(new ItemContainer(cont));
        }
        onInitContainer(itemContainer);
        return itemContainer;
    }

    public Container getContainer() {
        return container;
    }

    @Override
    public void onLoad(JSONObject json) throws JSONException {
        container = newContainer(json.get("container"));
        liquidStorage.read(json.getJSONObject("liquidStorage"));
        super.onLoad(json);
    }

    @Override
    public void onSave(JSONObject json) throws JSONException {
        json.put("container", ScriptableSerializer.scriptableToJson(container.asLegacyContainer(false).slots, (e) -> {}));
        json.put("liquidStorage", liquidStorage.save());
        super.onSave(json);
    }

    @Override
    public void onRemove() {
        container.dropAt(level, x + .5f, y +.5f, z + .5f);
        super.onRemove();
    }

    @Override
    public ScriptableObject getFakeTileEntity() {
        final ScriptableObject fake = super.getFakeTileEntity();
        fake.put("container", fake, container.getItemContainer());
        fake.put("liquidStorage", fake, liquidStorage);
        return fake;
    }
}
