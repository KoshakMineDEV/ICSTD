package ru.koshakmine.icstd.ui.container;

import ru.koshakmine.icstd.network.NetworkClient;
import ru.koshakmine.icstd.network.NetworkPacket;

public class ContainerEventPacket extends NetworkPacket {
    @Override
    public String getName() {
        return "icstd.container_event_packet";
    }

    @Override
    protected void decode(NetworkClient client) {}

    @Override
    protected void encode() {}
}
