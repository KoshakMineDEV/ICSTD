package ru.koshakmine.icstd.ui.container;

import com.zhekasmirnov.apparatus.api.container.ItemContainer;
import com.zhekasmirnov.apparatus.api.container.ItemContainerSlot;
import com.zhekasmirnov.apparatus.multiplayer.server.ConnectedClient;
import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.network.NetworkClient;
import ru.koshakmine.icstd.network.NetworkPacket;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;

import java.nio.ByteBuffer;
import java.util.Base64;

public class Container {
    private final ItemContainer container;

    public Container(ItemContainer container){
        this.container = container;
    }

    public void addServerOpenListener(ItemContainer.ServerOnOpenListener listener){
        container.addServerOpenListener(listener);
    }

    public void addServerCloseListener(ItemContainer.ServerOnCloseListener listener){
        container.addServerCloseListener(listener);
    }

    public void openFor(Player player, String name){
        container.openFor(player.getClient().getClient(), name);
    }

    public void closeFor(Player player){
        container.closeFor(player.getClient().getClient());
    }

    public void setClientContainerTypeName(String name) {
        container.setClientContainerTypeName(name);
    }

    public com.zhekasmirnov.innercore.api.mod.ui.container.Container asLegacyContainer(boolean b) {
        return container.asLegacyContainer(b);
    }

    public void dropAt(Level level, float x, float y, float z) {
        container.dropAt(level.getRegion(), x, y, z);
    }

    public void dropAt(Level level, Position position){
        dropAt(level, position.x, position.y, position.z);
    }

    public ItemStack getSlot(String name){
        final ItemContainerSlot slot = container.getSlot(name);
        return new ItemStack(slot.id, slot.count, slot.data, slot.extra);
    }

    public void setSlot(String name, ItemStack item){
        container.setSlot(name, item.id, item.count, item.data, item.extra);
    }

    public void sendChanges() {
        container.sendChanges();
    }

    public ItemContainer getItemContainer() {
        return container;
    }

    public void clearSlot(String name){
        container.clearSlot(name);
    }

    public NetworkEntity getNetworkEntity(){
        return container.getNetworkEntity();
    }

    public String getText(String element){
        return container.getText(element);
    }

    interface IServerEventListener {
        void call(Container container, NetworkClient client, ContainerEventPacket packet);
    }

    public void addServerEventListener(String eventName, IServerEventListener listener){
        container.addServerEventListener(eventName, (itemContainer, connectedClient, data) -> {
            if(data instanceof String) {
                final NetworkClient client = new NetworkClient(connectedClient);

                ContainerEventPacket packet = new ContainerEventPacket();
                packet.setBuffer(ByteBuffer.wrap(Base64.getDecoder().decode((String) data)));
                listener.call(new Container(itemContainer), client, packet);
            }
        });
    }

    public void sendEvent(String eventName, ContainerEventPacket packet){
        container.sendEvent(eventName, packet.buildPacket());
    }

    public void setClientScale(String element, float scale){
        container.setClientScale(element, scale);
    }

    public void setClientText(String element, String text){
        container.setClientText(element, text);
    }
}
