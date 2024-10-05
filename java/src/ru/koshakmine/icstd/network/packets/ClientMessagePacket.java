package ru.koshakmine.icstd.network.packets;

import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.network.NetworkClient;
import ru.koshakmine.icstd.network.NetworkPacket;

public class ClientMessagePacket extends NetworkPacket {
    private final String message;

    public ClientMessagePacket(String message){
        this.message = message;
    }

    public ClientMessagePacket(){
        this.message = "";
    }

    @Override
    public String getName() {
        return "icstd.client_message";
    }

    @Override
    protected void decode(NetworkClient client) {
        Level.clientMessage(getString());
    }

    @Override
    protected void encode() {
        putString(message);
    }
}
