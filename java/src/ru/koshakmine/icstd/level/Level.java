package ru.koshakmine.icstd.level;

import com.zhekasmirnov.apparatus.adapter.innercore.game.common.Vector3;
import com.zhekasmirnov.apparatus.adapter.innercore.game.entity.StaticEntity;
import com.zhekasmirnov.apparatus.mcpe.NativeBlockSource;
import com.zhekasmirnov.apparatus.util.Java8BackComp;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.commontypes.Coords;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.AdaptedScriptAPI;
import ru.koshakmine.icstd.entity.Entity;
import ru.koshakmine.icstd.entity.EntityItem;
import ru.koshakmine.icstd.entity.Player;
import ru.koshakmine.icstd.event.Event;
import ru.koshakmine.icstd.event.Events;
import ru.koshakmine.icstd.network.Network;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.network.packets.PlaySoundPacket;
import ru.koshakmine.icstd.type.common.BlockPosition;
import ru.koshakmine.icstd.type.common.ItemStack;

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

    public int getBlockId(Vector3 pos) {
        return getBlockId((int) pos.x, (int) pos.y, (int) pos.z);
    }

    public EntityItem spawnDroppedItem(float x, float y, float z, ItemStack stack) {
        return new EntityItem(region.spawnDroppedItem(x, y, z, stack.id, stack.count, stack.data, stack.extra));
    }

    public boolean isChunkLoaded(int x, int z) {
        return region.isChunkLoaded(x, z);
    }

    public void setBlock(Vector3 coords, int id, int data) {
        region.setBlock((int) coords.x, (int) coords.y, (int) coords.z, id, data);
    }

    public Player[] getPlayersForRadius(Vector3 pos, float radius){
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

    public void playSound(Vector3 pos, String sound, float volume, float pitch){
        final Player[] players = getPlayersForRadius(pos,volume * 2);
        final PlaySoundPacket packet = new PlaySoundPacket(pos.x, pos.y, pos.z, sound, volume, pitch);
        for (Player player : players) player.sendPacket(packet);
    }
}
