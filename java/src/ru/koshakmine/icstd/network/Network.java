package ru.koshakmine.icstd.network;

import com.zhekasmirnov.apparatus.multiplayer.ThreadTypeMarker;
import com.zhekasmirnov.apparatus.multiplayer.server.ConnectedClient;
import com.zhekasmirnov.apparatus.multiplayer.server.ModdedServer;

import java.nio.ByteBuffer;
import java.util.Base64;

public class Network {
    private static final com.zhekasmirnov.apparatus.multiplayer.Network network = com.zhekasmirnov.apparatus.multiplayer.Network.getSingleton();

    public static NetworkSide getCurrentNetworkSide(){
        return ThreadTypeMarker.isClientThread() ? NetworkSide.LOCAL : NetworkSide.SERVER;
    }

    public static NetworkClient getClientForPlayer(long player){
        final ConnectedClient client = network.getServer().getConnectedClientForPlayer(player);
        if(client != null)
            return new NetworkClient(client);
        return null;
    }

    public interface BuilderPacket {
        NetworkPacket create();
    }

    public static void registerPacket(NetworkSide side, BuilderPacket builder){
        final String namePacket = builder.create().getName();

        if(side == NetworkSide.LOCAL) {
            network.addClientPacket(namePacket, (data, meta, type) -> {
                if (data instanceof byte[]) {
                    NetworkPacket packet = builder.create();
                    packet.setBuffer(ByteBuffer.wrap((byte[]) data));
                    packet.decode(null);
                    return;
                }

                throw new RuntimeException("Not support");
            });
        }else{
            network.addServerPacket(namePacket, new ModdedServer.OnPacketReceivedListener() {
                @Override
                public void onPacketReceived(ConnectedClient client, Object data, String meta, Class<?> type) {
                    if (data instanceof byte[]) {
                        NetworkPacket packet = builder.create();
                        packet.setBuffer(ByteBuffer.wrap((byte[]) data));
                        packet.decode(new NetworkClient(client));
                        return;
                    }

                    throw new RuntimeException("Not support");
                }
            });
        }
    }

    public static boolean sendToClient(long player, NetworkPacket packet){
        final NetworkClient client = Network.getClientForPlayer(player);

        if(client != null){
            client.send(packet);
            return true;
        }

        return false;
    }

    public static void sendToServer(NetworkPacket packet){
        network.getClient().send(packet.getName(), packet.buildPacket());
    }
}
