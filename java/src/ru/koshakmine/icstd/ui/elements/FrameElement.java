package ru.koshakmine.icstd.ui.elements;

import android.graphics.Color;
import com.zhekasmirnov.innercore.api.mod.ScriptableObjectHelper;
import org.mozilla.javascript.ScriptableObject;

public class FrameElement extends WindowElementSize {
    protected String bitmap;
    protected int color = Color.TRANSPARENT;
    protected ScriptableObject sides = ScriptableObjectHelper.createEmpty();

    public FrameElement(String bitmap, float x, float y, int width, int height) {
        super("frame", x, y, width, height);
        this.bitmap = bitmap;
        this.scale = 1f;
    }

    public FrameElement setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public FrameElement setColor(int color) {
        this.color = color;
        return this;
    }

    public FrameElement setSides(boolean down, boolean left, boolean right, boolean up){
        sides.put("down", sides, down);
        sides.put("left", sides, left);
        sides.put("right", sides, right);
        sides.put("up", sides, up);

        return this;
    }

    @Override
    public ScriptableObject toElement() {
        final ScriptableObject desc = super.toElement();

        desc.put("bitmap", desc, bitmap);
        desc.put("color", desc, color);
        desc.put("sides", desc, sides);

        return desc;
    }
}
