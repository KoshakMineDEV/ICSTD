package ru.koshakmine.icstd.level;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.particles.ParticleRegistry;
import ru.koshakmine.icstd.level.particle.Particle;
import ru.koshakmine.icstd.type.common.Position;

public class LocalLevel extends Level {
    protected LocalLevel(NativeBlockSource region) {
        super(region);
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public void message(String message) {
        NativeAPI.clientMessage(message);
    }

    @Override
    public void spawnParticle(Particle particle, Position position, Position vector) {
        ParticleRegistry.addParticle(particle.getNumId(),
                position.x, position.y, position.z,
                vector.x, vector.y, vector.z,
                0);
    }
}
