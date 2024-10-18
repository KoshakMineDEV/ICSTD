package ru.koshakmine.icstd.level.particle;

import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.network.Network;
import ru.koshakmine.icstd.network.NetworkPacket;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.network.packets.SpawnParticleGroupPacket;
import ru.koshakmine.icstd.type.common.Position;

import java.util.ArrayList;

public abstract class AbstractParticleGroup<T extends AbstractParticleGroup> {
    static {
        Network.registerPacket(NetworkSide.LOCAL, SpawnParticleGroupPacket::new);
    }

    public static class ParticleData {
        public final Particle particle;
        public final Position position, vector;


        public ParticleData(Particle particle, Position position, Position vector) {
            this.particle = particle;
            this.position = position;
            this.vector = vector;
        }
    }

    public AbstractParticleGroup(){}

    protected final ArrayList<ParticleData> particles = new ArrayList<>();

    public T add(Particle particle, Position position, Position vector){
        particles.add(new ParticleData(particle, position, vector));
        return (T) this;
    }

    public T add(Particle particle, Position position){
        return add(particle, position, Position.EMPTY);
    }

    public abstract Position getPosition();

    protected void spawn(Level level){
        final Player[] players = level.getPlayersForRadius(getPosition());
        final SpawnParticleGroupPacket packet = new SpawnParticleGroupPacket(this);

        for(Player player : players){
            player.sendPacket(packet);
        }
    }

    public void encode(NetworkPacket packet) {
        packet.putShort((short) particles.size());
        for(AbstractParticleGroup.ParticleData data : particles){
            packet.putPosition(data.position);
            packet.putPosition(data.vector);
            packet.putString(data.particle.getId());
        }
    }
}
