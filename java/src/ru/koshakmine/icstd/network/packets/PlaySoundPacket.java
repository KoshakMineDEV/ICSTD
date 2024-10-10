package ru.koshakmine.icstd.network.packets;

import com.zhekasmirnov.innercore.api.NativeAPI;
import ru.koshakmine.icstd.network.NetworkClient;
import ru.koshakmine.icstd.network.NetworkPacket;

public class PlaySoundPacket extends NetworkPacket {
    private float x, y, z, volume, pitch;
    private String name;

    public PlaySoundPacket(){
    }

    public PlaySoundPacket(float x, float y, float z, String name, float volume, float pitch){
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public String getName() {
        return "icstd.play_sound";
    }

    @Override
    protected void decode(NetworkClient client) {
        NativeAPI.playSound(getString(), getFloat(), getFloat(), getFloat(), getFloat(), getFloat());
    }

    @Override
    protected void encode() {
        putString(name);
        putFloat(x);
        putFloat(y);
        putFloat(z);
        putFloat(volume);
        putFloat(pitch);
    }
}
