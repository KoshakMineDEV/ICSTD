package ru.koshakmine.icstd.level.particle;

import com.zhekasmirnov.innercore.api.constants.ParticleType;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectWrapper;
import com.zhekasmirnov.innercore.api.particles.ParticleRegistry;
import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.modloader.IBaseRegisterGameObject;
import ru.koshakmine.icstd.type.common.Position;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

public abstract class Particle implements IBaseRegisterGameObject {
    private static final HashMap<String, Particle> particles = new HashMap<>();
    private static final HashMap<Integer, Particle> particlesIds = new HashMap<>();

    private static void register(Particle particle){
        particles.put(particle.getId(), particle);
        particlesIds.put(particle.getNumId(), particle);
    }

    private static class VanillaParticle extends Particle {
        private final String id;
        private final int numId;

        public VanillaParticle(String id, int numId){
            this.id = id;
            this.numId = numId;
        }

        @Override
        public String getTexture() {return "";}

        @Override
        public int getNumId() {
            return numId;
        }

        @Override
        public String getId() {
            return id;
        }
    };

    static {
        final Field[] fields = ParticleType.class.getFields();
        try {
            for(Field field : fields)
                register(new VanillaParticle(field.getName(), field.getInt(null)));
        }catch (Exception ignore){}
    }

    public static int getParticleIdByName(String type){
        final Particle particle = particles.get(type);
        if(particle == null)
            return ParticleType.flame;
        return particle.getNumId();
    }

    public static Particle getParticleById(int particle){
        return particlesIds.get(particle);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void onPreInit() {}
    @Override
    public void onInit() {}

    private ParticleRegistry.ParticleType type;

    public abstract String getTexture();

    public boolean isUsingBlockLight(){
        return true;
    }

    public int getRenderType(){
        return 1;
    }

    public int getRebuildDelay(){
        return 10;
    }

    public ParticleRegistry.ParticleType getParticleType() {
        return type;
    }

    public int[] getLifetime(){
        return new int[]{20, 20};
    }

    public int[] getSize(){
        return new int[]{1, 1};
    }

    public Position getVelocity(){
        return Position.EMPTY;
    }

    public Position getAcceleration(){
        return Position.EMPTY;
    }

    public boolean isCollision(){
        return false;
    }

    public boolean keepVelocityAfterImpact(){
        return false;
    }

    public int getAddLifetimeAfterImpact(){
        return 0;
    }

    public float[] getColor(){
        return new float[]{1, 1, 1, 1};
    }

    public float[] getColor2(){
        return getColor();
    }

    public boolean isFriction(){
        return false;
    }

    public float getFrictionAir(){
        return 1;
    }

    public float getFrictionBlock(){
        return 1;
    }

    public static abstract class ParticleAnimator {
        public int getPeriod() {
            return -1;
        }

        public float getStart() {
            return 0;
        }

        public float getEnd(){
            return 0;
        }

        public float getFadeIn() {
            return 0;
        }

        public float getFadeOut() {
            return 0;
        }

        private ParticleRegistry.ParticleAnimator toAnimator() {
            return new ParticleRegistry.ParticleAnimator(
                    getPeriod(),
                    getFadeIn(),
                    getStart(),
                    getFadeOut(),
                    getEnd()
            );
        }
    }

    public enum AnimatorType {
        ALPHA,
        SIZE,
        ICON,
        TEXTURE
    }

    public void setAnimator(AnimatorType type, ParticleAnimator animator){
        getParticleType().setAnimator(type.name().toLowerCase(), animator.toAnimator());
    }

    public class SubEmitter {
        public float getChance() {
            return 1;
        }

        public int getCount() {
            return 1;
        }

        public int getType() {
            return 0;
        }

        public int getData() {
            return 0;
        }

        public boolean canKeepVelocity() {
            return false;
        }

        public boolean canKeepEmitter() {
            return false;
        }

        public float getRandomize() {
            return 0;
        }

        private ParticleRegistry.ParticleSubEmitter toSubEmitter() {
            final ScriptableObjectWrapper wrapper = new ScriptableObjectWrapper();

            wrapper.put("chance", getChance());
            wrapper.put("count", getCount());
            wrapper.put("type", getType());
            wrapper.put("data", getData());

            wrapper.put("keepVelocity", canKeepVelocity());
            wrapper.put("keepEmitter", canKeepEmitter());
            wrapper.put("randomize", getRandomize());

            return new ParticleRegistry.ParticleSubEmitter(wrapper);
        }
    }

    public enum SubEmitterType {
        DEATH,
        IDLE,
        IMPACT
    }

    public void setSubEmitter(SubEmitterType type, SubEmitter emitter) {
        getParticleType().setSubEmitter(type.name().toLowerCase(), emitter.toSubEmitter());
    }

    @Override
    public void factory() {
        final ScriptableObject self = ScriptableObjectHelper.createEmpty();
        self.put("texture", self, getTexture());
        self.put("isUsingBlockLight", self, isUsingBlockLight());
        type = new ParticleRegistry.ParticleType(self);

        if(isFriction()){
            type.setFriction(getFrictionAir(), getFrictionBlock());
        }

        final float[] color = getColor(), color2 = getColor2();
        type.setColor(color[0], color[1], color[2], color[3], color2[0], color2[1], color2[2], color2[3]);

        final int[] lifetime = getLifetime();
        type.setLifetime(Math.min(lifetime[0], lifetime[1]), Math.max(lifetime[0], lifetime[1]));

        final int[] size = getSize();
        type.setSize(Math.min(size[0], size[1]), Math.max(size[0], size[1]));

        final Position velocity = getVelocity();
        type.setDefaultVelocity(velocity.x, velocity.y, velocity.z);

        final Position acceleration = getVelocity();
        type.setDefaultAcceleration(acceleration.x, acceleration.y, acceleration.z);

        type.setCollisionParams(isCollision(), keepVelocityAfterImpact(), getAddLifetimeAfterImpact());

        type.setRenderType(getRenderType());
        type.setRebuildDelay(getRebuildDelay());

        particles.put(getId(), this);
    }

    private final UUID uuid = UUID.randomUUID();
    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public int getNumId() {
        return type.getId();
    }

    @Override
    public final String getName() {
        return getId();
    }
}
