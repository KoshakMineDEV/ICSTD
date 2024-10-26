package ru.koshakmine.icstd.level;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.particles.ParticleRegistry;
import com.zhekasmirnov.innercore.api.runtime.other.NameTranslation;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.level.particle.Particle;
import ru.koshakmine.icstd.runtime.PostLevelLoaded;
import ru.koshakmine.icstd.type.common.Position;

public class LocalLevel extends Level {
    static {
        Event.onEntityAddedLocal((entity -> Level.getLocalLevel().entities.add(entity.getUid())));
        Event.onEntityRemovedLocal((entity -> Level.getLocalLevel().entities.remove(entity.getUid())));
    }

    protected LocalLevel(NativeBlockSource region) {
        super(region);
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public void message(String message, String... formats) {
        NativeAPI.clientMessage(String.format(NameTranslation.translate(message), (Object[]) formats));
    }

    @Override
    public void spawnParticle(Particle particle, Position position, Position vector) {
        if(PostLevelLoaded.LOCAL.isLevelLoaded()) {
            ParticleRegistry.addParticle(particle.getNumId(),
                    position.x, position.y, position.z,
                    vector.x, vector.y, vector.z,
                    0);
        }
    }
}
