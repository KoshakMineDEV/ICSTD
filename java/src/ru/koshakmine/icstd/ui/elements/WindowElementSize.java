package ru.koshakmine.icstd.ui.elements;

import org.mozilla.javascript.ScriptableObject;

public class WindowElementSize extends WindowElement {
    protected int width, height;
    protected float scale = Float.MIN_VALUE;

    public WindowElementSize(String type, float x, float y, int width, int height) {
        super(type, x, y);

        this.width = width;
        this.height = height;
    }

    public WindowElementSize(String type, float x, float y, float scale){
        super(type, x, y);

        this.scale = scale;
    }

    @Override
    public ScriptableObject toElement() {
        final ScriptableObject desc = super.toElement();

        if(scale == Float.MIN_VALUE) {
            desc.put("width", desc, width);
            desc.put("height", desc, height);
        }else
            desc.put("scale", desc, scale);

        return desc;
    }
}
