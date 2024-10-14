package ru.koshakmine.icstd.entity;

import com.zhekasmirnov.apparatus.mcpe.NativePlayer;
import com.zhekasmirnov.innercore.api.NativeAPI;
import ru.koshakmine.icstd.network.Network;
import ru.koshakmine.icstd.network.NetworkClient;
import ru.koshakmine.icstd.network.NetworkPacket;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.network.packets.ClientMessagePacket;

public class Player extends Entity {
    private final NativePlayer player;
    private final NetworkClient client;

    static {
        Network.registerPacket(NetworkSide.LOCAL, ClientMessagePacket::new);
    }

    public static Player getLocal() {
        return new Player(NativeAPI.getLocalPlayer());
    }

    public Player(long uid) {
        super(uid);

        this.player = new NativePlayer(uid);
        this.client = Network.getClientForPlayer(uid);
    }

    public NetworkClient getClient() {
        return client;
    }

    public <T extends NetworkPacket>boolean sendPacket(T packet){
        if(client != null){
            client.send(packet);
            return true;
        }
        return false;
    }

    public boolean message(String message){
        if(client != null){
            client.send(new ClientMessagePacket(message));
            return true;
        }
        return false;
    }

    public boolean disconnect(String reason){
        if(client != null){
            client.disconnect(reason);
            return true;
        }
        return false;
    }
}
