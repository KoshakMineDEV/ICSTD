package ru.koshakmine.icstd.entity;

import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.innercore.api.NativeAPI;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.entity.Effect;

public class Entity {
    private final long uid;

    public Entity(long uid){
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    public Level getRegion(){
        return Level.getForActor(uid);
    }

    public void addEffect(Effect effect, int duration, int level, boolean hideParticles, boolean hide, boolean animation){
        NativeAPI.addEffect(uid, effect.ordinal(), duration, level, hideParticles, hide, animation);
    }

    public void addEffect(Effect effect, int duration, int level, boolean hideParticles, boolean hide){
        addEffect(effect, duration, level, hideParticles, hide, false);
    }

    public void addEffect(Effect effect, int duration, int level, boolean hideParticles){
        addEffect(effect, duration, level, hideParticles, false, false);
    }

    public void addEffect(Effect effect, int duration, int level){
        addEffect(effect, duration, level, false);
    }

    public int getEffectLevel(Effect effect){
        return NativeAPI.getEffectLevel(uid, effect.ordinal());
    }

    public int getEffectDuration(Effect effect){
        return NativeAPI.getEffectDuration(uid, effect.ordinal());
    }
}
