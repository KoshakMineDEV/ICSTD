package ru.koshakmine.icstd.impl;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity;
import org.json.JSONException;
import org.json.JSONObject;
import ru.koshakmine.icstd.block.blockentity.LocalBlockEntity;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.render.animation.AnimationStaticItem;
import ru.koshakmine.icstd.type.common.ItemStack;
import ru.koshakmine.icstd.type.common.Position;
import ru.koshakmine.icstd.ui.IWindow;
import ru.koshakmine.icstd.ui.WindowStandard;
import ru.koshakmine.icstd.ui.elements.SlotElement;
import ru.koshakmine.icstd.ui.elements.TextElement;

public class TestLocalBlockEntity extends LocalBlockEntity {
    private AnimationStaticItem animation;

    public TestLocalBlockEntity(String type, int id, Position position, NetworkEntity network, JSONObject data) throws JSONException {
        super(type, id, position, network, data);
    }

    @Override
    public void onInit() {
        Level.clientMessage("Init client block entity");

        animation = new AnimationStaticItem(x + .5f, y + 1.5f, z + .5f);
        animation.setItem(new ItemStack(264, 1));
        animation.load();
    }

    @Override
    public void onRemove() {
        Level.clientMessage("Remove client block entity");

        animation.destroy();
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
