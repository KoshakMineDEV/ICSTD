package ru.koshakmine.icstd.level.particle;

import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.network.NetworkPacket;
import ru.koshakmine.icstd.type.common.Position;

public class ParticleGroupCache extends AbstractParticleGroup<ParticleGroupCache> {
    public Position position;

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void encode(NetworkPacket packet) {
        packet.putShort((short) particles.size());
        for(AbstractParticleGroup.ParticleData data : particles){
            packet.putPosition(data.position.add(position));
            packet.putPosition(data.vector);
            packet.putString(data.particle.getId());
        }
    }

    public void send(Level level, Position position){
        this.position = position;
        super.spawn(level);
        this.position = null;
    }
}
