package ru.koshakmine.icstd.entity;

import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.commontypes.ItemInstance;
import com.zhekasmirnov.innercore.api.constants.EntityType;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI;
import com.zhekasmirnov.innercore.api.nbt.NativeCompoundTag;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.type.ArmorSlot;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.type.entity.Effect;
import ru.koshakmine.icstd.type.entity.LookAngle;
import ru.koshakmine.icstd.utils.MathUtils;

public class Entity {
    protected final long uid;

    protected Entity(long uid){
        this.uid = uid;
    }

    public static Entity from(long uid){
        if(uid == -1)
            return null;

        final int type = NativeAPI.getEntityType(uid);
        if(type == EntityType.ITEM)
            return new EntityItem(uid);
        if(type == EntityType.PLAYER)
            return new Player(uid);
        return new Entity(uid);
    }

    public static long getUid(Entity entity) {
        return entity != null ? entity.uid : -1;
    }

    public long getUid() {
        return uid;
    }

    public String getEntityType() {
        return NativeAPI.getEntityTypeName(uid);
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

    public boolean hasEffect(Effect effect){
        return getEffectLevel(effect) != 0;
    }

    public int getEffectLevel(Effect effect){
        return NativeAPI.getEffectLevel(uid, effect.ordinal());
    }

    public int getEffectDuration(Effect effect){
        return NativeAPI.getEffectDuration(uid, effect.ordinal());
    }

    public ItemStack getCarriedItem(){
        return new ItemStack(new ItemInstance(NativeAPI.getEntityCarriedItem(uid)));
    }

    public void setCarriedItem(ItemStack item){
        AdaptedScriptAPI.Entity.setCarriedItem(uid, item.id, item.count, item.data, item.extra);
    }

    public boolean isSneaking(){
        return NativeAPI.isSneaking(uid);
    }

    public void setSneaking(boolean value){
        NativeAPI.setSneaking(uid, value);
    }

    public double getYaw(){
        final float[] rotation = new float[2];
        NativeAPI.getRotation(uid, rotation);
        return rotation[0];
    }

    public double getPitch(){
        final float[] rotation = new float[2];
        NativeAPI.getRotation(uid, rotation);
        return rotation[1];
    }

    public void setRotation(double yaw, double pitch){
        NativeAPI.setRotation(uid, (float) yaw, (float) pitch);
    }

    public boolean isItemSpendingAllowed(){
        return true;
    }

    public Position getPosition() {
        final float[] pos = new float[3];
        NativeAPI.getPosition(uid, pos);
        return new Position(pos[0], pos[1], pos[2]);
    }

    public void setPosition(float x, float y, float z){
        NativeAPI.setPosition(uid, x, y, z);
    }

    public void setPosition(Position position){
        setPosition(position.x, position.y, position.z);
    }

    public ItemStack getArmorSlot(ArmorSlot slot){
        return ItemStack.fromPointer(NativeAPI.getEntityArmor(uid, slot.ordinal()));
    }

    public void setArmorSlot(ArmorSlot slot, ItemStack item){
        AdaptedScriptAPI.Entity.setArmorSlot(uid, slot.ordinal(), item.id, item.count, item.data, item.extra);
    }

    public NativeCompoundTag getCompoundTag(){
        return new NativeCompoundTag(NativeAPI.getEntityCompoundTag(uid));
    }

    public void setCompoundTag(NativeCompoundTag tag){
        NativeAPI.setEntityCompoundTag(uid, tag.pointer);
    }

    public int getDimension(){
        return NativeAPI.getEntityDimension(uid);
    }

    public int getHealth(){
        return NativeAPI.getHealth(uid);
    }

    public int getMaxHealth(){
        return NativeAPI.getMaxHealth(uid);
    }

    public void setHealth(int health){
        NativeAPI.setHealth(uid, health);
    }

    public void setMaxHealth(int health){
        NativeAPI.setMaxHealth(uid, health);
    }

    public void healEntity(int heal){
        int health = getHealth() + heal;
        int maxHealth = getMaxHealth();

        setHealth(Math.min(health, maxHealth));
    }

    public ItemStack getOffhandItem(){
        return new ItemStack(new ItemInstance(NativeAPI.getEntityOffhandItem(uid)));
    }

    public void setOffhandItem(ItemStack item){
        AdaptedScriptAPI.Entity.setOffhandItem(uid, item.id, item.count, item.data, item.extra);
    }

    public Entity getRider(){
        return from(NativeAPI.getRider(uid));
    }

    public Entity getRiding(){
        return from(NativeAPI.getRider(uid));
    }

    public Entity getTarget(){
        return from(NativeAPI.getTarget(uid));
    }

    public void setTarget(Entity target){
        NativeAPI.setTarget(uid, getUid(target));
    }

    public int getType(){
        return NativeAPI.getEntityType(uid);
    }

    public Position getVelocity() {
        final float[] pos = new float[3];
        NativeAPI.getVelocity(uid, pos);
        return new Position(pos[0], pos[1], pos[2]);
    }

    public void setVelocity(float x, float y, float z){
        NativeAPI.setVelocity(uid, x, y, z);
    }

    public void setVelocity(Position position){
        setVelocity(position.x, position.y, position.z);
    }

    public boolean isValid(){
        return NativeAPI.isValidEntity(uid);
    }

    public void remove(){
        NativeAPI.removeEntity(uid);
    }

    public void rideAnimal(Entity rider){
        NativeAPI.rideAnimal(uid, getUid(rider));
    }

    public int getFire(){
        return NativeAPI.getFireTicks(uid);
    }

    public void setFire(int fire, boolean force){
        NativeAPI.setFireTicks(uid, fire, force);
    }

    public void setFire(int fire){
        setFire(fire, false);
    }

    public boolean isImmobile(){
        return NativeAPI.isImmobile(uid);
    }

    public void setImmobile(boolean value){
        NativeAPI.setImmobile(uid, value);
    }

    public String getNameTag(){
        return NativeAPI.getNameTag(uid);
    }

    public void setNameTag(String tag){
        NativeAPI.setNameTag(uid, tag);
    }

    public LookAngle getLookAngle() {
        return new LookAngle(MathUtils.degreesToRad(-getPitch()), MathUtils.degreesToRad(getYaw()));
    }

    public Position getLookVectorByAngle(LookAngle angle) {
        return new Position(
                (float) (-Math.sin(angle.yaw) * Math.cos(angle.pitch)),
                (float) Math.sin(angle.pitch),
                (float) (Math.cos(angle.yaw) * Math.cos(angle.pitch))
        );
    }

    public Position getLookVector() {
        return getLookVectorByAngle(getLookAngle());
    }

    public void damage(int damage) {
        damage(damage, 0);
    }

    public void damage(int damage, int cause) {
        damage(damage, cause, null);
    }

    public void damage(int damage, int cause, Entity attacker) {
        damage(damage, cause, attacker, false);
    }

    public void damage(int damage, int cause, Entity attacker, boolean bool1) {
        damage(damage, cause, attacker, bool1, false);
    }

    public void damage(int damage, int cause, Entity attacker, boolean bool1, boolean bool2) {
        NativeAPI.dealDamage(uid, damage, cause, getUid(attacker), bool1, bool2);
    }
}
