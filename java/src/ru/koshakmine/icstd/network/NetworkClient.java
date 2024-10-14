package ru.koshakmine.icstd.network;

import com.zhekasmirnov.apparatus.multiplayer.server.ConnectedClient;

public class NetworkClient {
    private final ConnectedClient client;

    public NetworkClient(ConnectedClient client){
        this.client = client;
    }

    public long getPlayerUid(){
        return client.getPlayerUid();
    }

    public ConnectedClient.ClientState getClientState(){
        return client.getClientState();
    }

    public <T extends NetworkPacket> void send(T data){
        client.send(data.getName(), data.buildPacket());
    }

    public void disconnect(String message){
        client.disconnect(message);
    }

    public void disconnect(){
        client.disconnect();
    }

    public ConnectedClient getClient() {
        return client;
    }
}
