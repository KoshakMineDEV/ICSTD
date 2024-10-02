package ru.koshakmine.icstd.entity;

import com.zhekasmirnov.apparatus.mcpe.NativePlayer;

public class Player extends Entity {
    private final NativePlayer player;

    public Player(long uid) {
        super(uid);

        this.player = new NativePlayer(uid);
    }


}
