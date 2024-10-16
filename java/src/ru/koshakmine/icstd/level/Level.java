package ru.koshakmine.icstd.level;

import com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState;
import com.zhekasmirnov.apparatus.adapter.innercore.game.entity.StaticEntity;
import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.apparatus.util.Java8BackComp;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.NativeTileEntity;
import ru.koshakmine.icstd.entity.Entity;
import ru.koshakmine.icstd.entity.EntityItem;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.network.Network;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.network.packets.PlaySoundPacket;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;

import java.util.HashMap;
import java.util.function.Function;

public class Level {
    public static void clientMessage(String message){
        NativeAPI.clientMessage(message);
    }

    private final NativeBlockSource region;

    private Level(NativeBlockSource region){
        this.region = region;
    }

    public NativeBlockSource getRegion() {
        return region;
    }

    private static final HashMap<Integer, Level> levels = new HashMap<>();
    private static Level localLevel;

    static {
        Event.onCall(Events.LevelLeft, args -> {
            levels.clear();
            localLevel = null;
        });
    }

    public static Level getLocalLevel(){
        if(localLevel == null){
            localLevel = new Level(NativeBlockSource.getCurrentClientRegion());
        }
        return localLevel;
    }

    public static Level getForDimension(int dimension){
        return Java8BackComp.computeIfAbsent(levels, dimension, (Function<Integer, Level>) id -> {
            final NativeBlockSource region = NativeBlockSource.getDefaultForDimension(id);
            if(region != null)
                return new Level(region);
            return null;
        });
    }

    public static Level getForActor(long entity){
        return getForDimension(StaticEntity.getDimension(entity));
    }

    public static Level getForRegion(NativeBlockSource object) {
        return getForDimension(object.getDimension());
    }

    public int getDimension(){
        return region.getDimension();
    }

    public int getBlockId(int x, int y, int z) {
        return region.getBlockId(x, y, z);
    }

    public int getBlockId(Position pos) {
        return getBlockId((int) pos.x, (int) pos.y, (int) pos.z);
    }

    public int getBlockData(int x, int y, int z) {
        return region.getBlockData(x, y, z);
    }

    public int getBlockData(Position pos) {
        return getBlockData((int) pos.x, (int) pos.y, (int) pos.z);
    }

    public EntityItem spawnDroppedItem(float x, float y, float z, ItemStack stack) {
        return new EntityItem(region.spawnDroppedItem(x, y, z, stack.id, stack.count, stack.data, stack.extra));
    }

    public boolean isChunkLoaded(int chunkX, int chunkZ) {
        return region.isChunkLoaded(chunkX, chunkZ);
    }

    public boolean isChunkLoadedAt(int x, int z) {
        return region.isChunkLoadedAt(x, z);
    }

    public Entity[] listEntityInAABB(Position pos1, Position pos2, int type, boolean blackList){
        final long[] entitys = region.fetchEntitiesInAABB(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z, type, blackList);

        final Entity[] result = new Entity[entitys.length];
        for (int i = 0;i < result.length;i++) {
            result[i] = Entity.from(entitys[i]);
        }

        return result;
    }

    public Player[] getPlayersForRadius(Position pos, float radius){
        final long[] entitys = region.fetchEntitiesInAABB(
                pos.x - radius, pos.y - radius, pos.z - radius,
                pos.x + radius, pos.y + radius, pos.z + radius,
                63, false);
        final Player[] players = new Player[entitys.length];
        for (int i = 0;i < players.length;i++) {
            players[i] = new Player(entitys[i]);
        }
        return players;
    }

    static {
        Network.registerPacket(NetworkSide.LOCAL, PlaySoundPacket::new);
    }

    public void playSound(Position pos, String sound, float volume, float pitch){
        final Player[] players = getPlayersForRadius(pos,volume * 2);
        final PlaySoundPacket packet = new PlaySoundPacket(pos.x, pos.y, pos.z, sound, volume, pitch);
        for (Player player : players) player.sendPacket(packet);
    }

    public void playSoundAtEntity(Player player, String sound, float volume, float pitch) {
        playSound(player.getPosition(), sound, volume, pitch);
    }

    public BlockState getBlock(int x, int y, int z) {
        return region.getBlock(x, y, z);
    }

    public BlockState getBlock(Position position) {
        return getBlock((int) position.x, (int) position.y, (int) position.z);
    }

    public void setBlock(Position coords, int id, int data) {
        region.setBlock((int) coords.x, (int) coords.y, (int) coords.z, id, data);
    }

    public void setBlock(Position coords, BlockState state){
        region.setBlock((int) coords.x, (int) coords.y, (int) coords.z, state);
    }

    public NativeTileEntity getNativeBlockEntity(int x, int y, int z){
        return region.getBlockEntity(x, y, z);
    }

    public NativeTileEntity getNativeBlockEntity(Position position){
        return getNativeBlockEntity((int) position.x, (int) position.y, (int) position.z);
    }

    public boolean canSeeSky(int x, int y, int z){
        return region.canSeeSky(x, y, z);
    }

    public boolean canSeeSky(Position position){
        return region.canSeeSky((int) position.x, (int) position.y, (int) position.z);
    }

    public void destroyBlock(int x, int y, int z, boolean drop){
        region.destroyBlock(x, y, z, drop);
    }

    public void destroyBlock(Position position, boolean drop){
        destroyBlock((int) position.x, (int) position.y, (int) position.z, drop);
    }

    public void explode(float x, float y, float z, int power, boolean fire){
        region.explode(x, y, z, power, fire);
    }

    public void explode(Position position, int power, boolean fire){
        explode(position.x, position.y, position.z, power, fire);
    }

    public int getBiome(int x, int z){
        return region.getBiome(x, z);
    }

    public void setBiome(int x, int z, int biomeId){
        region.setBiome(x, z, biomeId);
    }

    public float getBiomeDownfallAt(int x, int y, int z){
        return region.getBiomeDownfallAt(x, y, z);
    }

    public float getBiomeTemperatureAt(int x, int y, int z){
        return region.getBiomeTemperatureAt(x, y, z);
    }

    public BlockState getExtraBlock(int x, int y, int z) {
        return region.getExtraBlock(x, y, z);
    }

    public BlockState getExtraBlock(Position position) {
        return getExtraBlock((int) position.x, (int) position.y, (int) position.z);
    }

    public void setExtraBlock(Position coords, int id, int data) {
        region.setExtraBlock((int) coords.x, (int) coords.y, (int) coords.z, id, data);
    }

    public void setExtraBlock(Position coords, BlockState state){
        region.setExtraBlock((int) coords.x, (int) coords.y, (int) coords.z, state);
    }

    public int getLightLevel(int x, int y, int z){
        return region.getLightLevel(x, y, z);
    }

    public int getLightLevel(Position position){
        return getLightLevel((int) position.x, (int) position.y, (int) position.z);
    }

    public void setDestroyParticlesEnabled(boolean enabled){
        region.setDestroyParticlesEnabled(enabled);
    }

    public Entity spawnEntity(float x, float y, float z, String type){
        return Entity.from(region.spawnEntity(x, y, z, type));
    }

    public Entity spawnEntity(Position position, String type){
        return spawnEntity(position.x, position.y, position.z, type);
    }

    public Entity spawnEntity(float x, float y, float z, int type){
        return Entity.from(region.spawnEntity(x, y, z, type));
    }

    public Entity spawnEntity(Position position, int type){
        return spawnEntity(position.x, position.y, position.z, type);
    }

    public void spawnExpOrbs(float x, float y, float z, int amount){
        region.spawnExpOrbs(x, y, z, amount);
    }

    public void spawnExpOrbs(Position position, int amount){
        spawnExpOrbs(position.x, position.y, position.z, amount);
    }
}
