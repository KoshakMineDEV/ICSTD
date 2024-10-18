package ru.koshakmine.icstd.level.particle;

import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.common.Position;

public class ParticleGroup extends AbstractParticleGroup<ParticleGroup> {
    private float maxX = Float.MIN_VALUE, minX = Float.MAX_VALUE;
    private float maxY = Float.MIN_VALUE, minY = Float.MAX_VALUE;
    private float maxZ = Float.MIN_VALUE, minZ = Float.MAX_VALUE;

    @Override
    public ParticleGroup add(Particle particle, Position position, Position vector) {
        this.maxX = Math.max(position.x, maxX);
        this.minX = Math.min(position.x, minX);

        this.maxY = Math.max(position.y, maxY);
        this.minY = Math.min(position.y, minY);

        this.maxZ = Math.max(position.z, maxZ);
        this.minZ = Math.min(position.z, minZ);

        return super.add(particle, position, vector);
    }

    @Override
    public Position getPosition() {
        return new Position((maxX + minX) / 2f, (maxY + minY) / 2f, (maxZ + minZ) / 2f);
    }

    public void send(Level level){
        spawn(level);
    }
}
