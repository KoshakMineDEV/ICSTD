package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.blockentity.LocalBlockEntity;
import ru.koshakmine.icstd.block.blockentity.ticking.ITickingBlockEntity;
import ru.koshakmine.icstd.level.particle.Particle;
import ru.koshakmine.icstd.modloader.Mod;
import ru.koshakmine.icstd.render.animation.AnimationStaticItem;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.ui.IWindow;
import ru.koshakmine.icstd.ui.WindowStandard;
import ru.koshakmine.icstd.ui.elements.SlotElement;
import ru.koshakmine.icstd.ui.elements.TextElement;

public class TestLocalBlockEntity extends LocalBlockEntity implements ITickingBlockEntity {
    private static class TestParticle extends Particle {
        @Override
        public String getTexture() {
            return "test";
        }

        @Override
        public String getId() {
            return "test";
        }

        @Override
        public boolean isCollision() {
            return true;
        }

        @Override
        public Position getVelocity() {
            return new Position(0, 0.001f, 0);
        }
    }

    private static final Particle TEST = Mod.getFactory().addParticle(TestParticle::new);

    private AnimationStaticItem animation;

    public TestLocalBlockEntity(String type, int id, Position position, NetworkEntity network, JSONObject data) throws JSONException {
        super(type, id, position, network, data);
    }

    @Override
    public void onInit() {
        level.message("Init client block entity");

        animation = new AnimationStaticItem(x + .5f, y + 1.5f, z + .5f);
        animation.setItem(new ItemStack(264, 1));
        animation.load();
    }

    @Override
    public void onRemove() {
        level.message("Remove client block entity");

        animation.destroy();
    }

    @Override
    public void onTick() {
        level.spawnParticle(TEST, position.add(Math.random(), Math.random(), Math.random()));
    }

    private static final WindowStandard standard = new WindowStandard("Test standard gui");

    static {
        standard.putElement("text", new TextElement("Test text", 500+10, 15, 50));
        standard.putElement("slot1", new SlotElement(500+60, 80, 100)
                .setVisual(false));
        standard.putElement("slot2", new SlotElement(500+170, 80, 100)
                .setVisual(false));
    }

    @Override
    public IWindow getScreenByName(String name) {
        return standard;
    }
}
