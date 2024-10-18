package ru.koshakmine.icstd.network.packets;

import com.zhekasmirnov.innercore.api.NativeAPI;
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
        NativeAPI.clientMessage(getString());
    }

    @Override
    protected void encode() {
        putString(message);
    }
}
