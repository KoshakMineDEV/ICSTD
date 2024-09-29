package ru.koshakmine.icstd.impl;

import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.network.Network;
import ru.koshakmine.icstd.network.NetworkClient;
import ru.koshakmine.icstd.network.NetworkPacket;

import java.nio.ByteBuffer;

public class TestClientPacket extends NetworkPacket {
    private byte a1;
    private short a2;
    private int a3;
    private long a4;
    private float a5;
    private double a6;
    private String message;

    public TestClientPacket(byte a1, short a2, int a3, long a4, float a5, double a6, String message){
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = a4;
        this.a5 = a5;
        this.a6 = a6;
        this.message = message;
    }

    public TestClientPacket(){}

    @Override
    public String getName() {
        return "test_packet";
    }

    @Override
    protected void decode(NetworkClient client) {
        Level.clientMessage("byte: "+getByte()+", short: "+getShort()+", "+getInt()+", "+getLong()+", "+getFloat()+", "+getDouble());

        Network.sendToServer(new TestServerPacket(getString()));
    }

    @Override
    public void encode() {
        putByte(a1);
        putShort(a2);
        putInt(a3);
        putLong(a4);
        putFloat(a5);
        putDouble(a6);
        putString(message);
    }
}
