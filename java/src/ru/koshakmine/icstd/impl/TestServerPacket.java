package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.innercore.api.NativeAPI;
import ru.koshakmine.icstd.network.NetworkClient;
import ru.koshakmine.icstd.network.NetworkPacket;

public class TestServerPacket extends NetworkPacket {
    private String message;

    public TestServerPacket(String message){
        this.message = message;
    }

    public TestServerPacket(){}

    @Override
    public String getName() {
        return "test_server_packet";
    }

    @Override
    protected void decode(NetworkClient client) {
        NativeAPI.clientMessage("[FOR CLIENT]"+getString());
    }

    @Override
    protected void encode() {
        putString(message);
    }
}
