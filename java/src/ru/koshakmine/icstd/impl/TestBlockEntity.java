package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.innercore.api.constants.ParticleType;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.blockentity.BlockEntityContainer;
import ru.koshakmine.icstd.block.blockentity.IEnergyTile;
import ru.koshakmine.icstd.block.blockentity.ticking.ITickingBlockEntity;
import ru.koshakmine.icstd.js.EnergyNetLib;
import ru.koshakmine.icstd.js.StorageInterfaceLib;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.level.particle.Particle;
import ru.koshakmine.icstd.level.particle.ParticleGroup;
import ru.koshakmine.icstd.level.particle.ParticleGroupCache;
import ru.koshakmine.icstd.type.ItemID;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.entity.Player;

public class TestBlockEntity extends BlockEntityContainer implements ITickingBlockEntity, IEnergyTile {
    public int data, tick;
    public boolean active;

    public TestBlockEntity(String type, int id, Position position, Level level) {
        super(type, type, id, position, level);
    }

    @Override
    public void onInit() {
        level.message("Init block entity %s", "testFormat");
        super.onInit();
    }

    @Override
    public void onRemove() {
        level.message("Remove block entity");
        super.onRemove();
    }

    @Override
    public void onLoad(JSONObject json) throws JSONException {
        data = json.getInt("data");
        active = json.getBoolean("active");

        super.onLoad(json);
    }

    @Override
    public void onSave(JSONObject json) throws JSONException {
        json.put("data", data);
        json.put("active", active);

        super.onSave(json);
    }

    @Override
    public void onClick(Position position, ItemStack stack, Player player) {
        if(stack.id == ItemID.STICK) {
            active = !active;
            player.message("Change mode active: " + active);
        }

        super.onClick(position, stack, player);
    }

    @Override
    public String getScreenName(Position position, ItemStack stack, Player player) {
        if(stack.id == ItemID.STICK)
            return null;
        return "main";
    }

    private static final ParticleGroupCache cacheGroup = new ParticleGroupCache()
            .add(Particle.getParticleById(ParticleType.flame), new Position(0, 1, 0))
            .add(Particle.getParticleById(ParticleType.flame), new Position(1, 1, 1))
            .add(Particle.getParticleById(ParticleType.flame), new Position(2, 1, 2))
            .add(Particle.getParticleById(ParticleType.flame), new Position(3, 1, 3));

    @Override
    public void onTick() {
        StorageInterfaceLib.checkHoppers(this);

        final ItemStack slot = container.getSlot("slot1");
        if(active && ++tick % 100 == 0 && !slot.isEmpty()){
            level.spawnDroppedItem(x + .5f, y + 1.5f, z + .5f, new ItemStack(slot.id, 1, slot.data, slot.extra));
            slot.decrease(1);
            container.setSlot("slot1", slot);
        }else
            level.spawnParticle(Particle.getParticleById(ParticleType.flame), position.add(.5, 1.5, .5));

        cacheGroup.send(level, position);

        final ParticleGroup group = new ParticleGroup();
        for(int i = 0;i < 30;i++){
            group.add(Particle.getParticleById(ParticleType.redstone), position.add(Math.random(), Math.random(), Math.random()));
        }

        group.send(level);

        container.sendChanges();
    }

    @Override
    public int getHideDistance() {
        return 16;
    }

    @Override
    public void energyTick(String type, EnergyNetLib.EnergyTileNode node) {
        if(active)
            node.add(16, 32);
    }

    @Override
    public float energyReceive(String type, float amount, int voltage) {
        if(!active)
            return Math.min(amount, 16);
        return 0;
    }

    @Override
    public boolean isConductor(String type) {
        return false;
    }

    @Override
    public boolean canReceiveEnergy(int side, String type) {
        return true;
    }

    @Override
    public boolean canExtractEnergy(int side, String type) {
        return true;
    }
}
