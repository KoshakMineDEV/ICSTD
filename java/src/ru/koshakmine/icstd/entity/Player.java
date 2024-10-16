package ru.koshakmine.icstd.entity;

import com.zhekasmirnov.apparatus.mcpe.NativePlayer;
import com.zhekasmirnov.innercore.api.NativeAPI;
import com.zhekasmirnov.innercore.api.NativeItemInstanceExtra;
import ru.koshakmine.icstd.network.Network;
import ru.koshakmine.icstd.network.NetworkClient;
import ru.koshakmine.icstd.network.NetworkPacket;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.network.packets.ClientMessagePacket;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;

public class Player extends Entity {
    private final NativePlayer player;
    private final NetworkClient client;

    static {
        Network.registerPacket(NetworkSide.LOCAL, ClientMessagePacket::new);
    }

    public static Player getLocal() {
        return new Player(NativeAPI.getLocalPlayer());
    }

    public Player(long uid) {
        super(uid);

        this.player = new NativePlayer(uid);
        this.client = Network.getClientForPlayer(uid);
    }

    public NetworkClient getClient() {
        return client;
    }

    public <T extends NetworkPacket>boolean sendPacket(T packet){
        if(client != null){
            client.send(packet);
            return true;
        }
        return false;
    }

    public boolean message(String message){
        if(client != null){
            client.send(new ClientMessagePacket(message));
            return true;
        }
        return false;
    }

    public boolean disconnect(String reason){
        if(client != null){
            client.disconnect(reason);
            return true;
        }
        return false;
    }

    @Override
    public boolean isItemSpendingAllowed() {
        return player.getGameMode() != 1;
    }

    public int getGameMode() {
        return player.getGameMode();
    }

    public void addItemToInventory(ItemStack item, boolean dropItem) {
        player.addItemToInventory(item.id, item.count, item.data, item.extra, dropItem);
    }

    public void addExperience(int amount){
        player.addExperience(amount);
    }

    public float getExperience(){
        return player.getExperience();
    }

    public void setExperience(float value){
        player.setExperience(value);
    }

    public float getExperienceLevel(){
        return player.getLevel();
    }

    public void setExperienceLevel(float value){
        player.setLevel(value);
    }

    public float getExhaustion(){
        return player.getExhaustion();
    }

    public void setExhaustion(float value){
        player.setExhaustion(value);
    }

    public float getHunger(){
        return player.getHunger();
    }

    public void setHunger(float hunger){
        player.setHunger(hunger);
    }

    public float getSaturation(){
        return player.getSaturation();
    }

    public void setSaturation(float value){
        player.setSaturation(value);
    }

    public float getScore(){
        return player.getScore();
    }

    /*public void setScore(float value){
        player.setScore(value);
    }*/

    public ItemStack getInventorySlot(int slot){
        return new ItemStack(player.getInventorySlot(slot));
    }

    public void setInventorySlot(int slot, ItemStack item){
        player.setInventorySlot(slot, item.id, item.count, item.data, item.extra);
    }

    public int getItemUseDuration(){
        return player.getItemUseDuration();
    }

    public float getItemUseIntervalProgress(){
        return player.getItemUseIntervalProgress();
    }

    public float getItemUseStartupProgress(){
        return player.getItemUseStartupProgress();
    }

    public int getSelectedSlot(){
        return player.getSelectedSlot();
    }

    public void setSelectedSlot(int slot){
        player.setSelectedSlot(slot);
    }

    public void setRespawnCoords(int x, int y, int z){
        player.setRespawnCoords(x, y, z);
    }

    public void setRespawnCoords(Position position){
        player.setRespawnCoords((int) position.x, (int) position.y, (int) position.z);
    }

    public void spawnExpOrbs(float x, float y, float z, int value){
        player.spawnExpOrbs(x, y, z, value);
    }

    public void spawnExpOrbs(Position position, int value){
        spawnExpOrbs(position.x, position.y, position.z, value);
    }
}
