package ru.koshakmine.icstd;

import java.io.IOException;
import java.util.HashMap;


import ru.koshakmine.icstd.impl.*;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.network.Network;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.runtime.Updatable;

public class ICSTD {
    //TODO: remove this class completely cuz it is used for internal tests

    static {
        Event.onCall("ModsLoaded", args -> {
            TestBlock block = new TestBlock();
        });

        Network.registerPacket(NetworkSide.LOCAL, TestClientPacket::new);
        Network.registerPacket(NetworkSide.SERVER, TestServerPacket::new);

        /*Event.onDestroyBlock((position, block, player) -> {
            Updatable.addUpdatable(new TestUpdatable(position));
            Network.sendToClient(player, new TestClientPacket((byte) 100, (short) 666, 1000, 2000L, 2.3f, 1.5d, position.toString()));
        });*/
    }

    public static void boot(HashMap<?, ?> args) throws IOException {

    }
}