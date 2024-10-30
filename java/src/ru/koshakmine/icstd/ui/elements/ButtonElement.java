package ru.koshakmine.icstd.ui.elements;

import org.mozilla.javascript.ScriptableObject;

public class ButtonElement extends WindowElement {
    protected String bitmap1, bitmap2;
    protected float scale;

    protected ButtonElement(String type, float x, float y, String bitmap, float scale) {
        super(type, x, y);

        this.bitmap1 = bitmap;
        this.bitmap2 = bitmap;
        this.scale = scale;
    }

    public ButtonElement(float x, float y, String bitmap, float scale){
        this("button", x, y, bitmap, scale);
    }

    public ButtonElement(float x, float y, String bitmap){
        this("button", x, y, bitmap, 1f);
    }

    public ButtonElement setBitmapPost(String bitmap2) {
        this.bitmap2 = bitmap2;
        return this;
    }

    @Override
    public ScriptableObject toElement() {
        final ScriptableObject desc = super.toElement();

        desc.put("bitmap", desc, bitmap1);
        desc.put("bitmap2", desc, bitmap2);
        desc.put("scale", desc, scale);

        return desc;
    }
}
