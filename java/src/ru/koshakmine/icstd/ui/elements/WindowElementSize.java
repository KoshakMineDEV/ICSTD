package ru.koshakmine.icstd.ui.elements;

import org.mozilla.javascript.ScriptableObject;

public class WindowElementSize extends WindowElement {
    protected int width, height;

    public WindowElementSize(String type, int x, int y, int width, int height) {
        super(type, x, y);

        this.width = width;
        this.height = height;
    }

    @Override
    public ScriptableObject toElement() {
        final ScriptableObject desc = super.toElement();

        desc.put("width", desc, width);
        desc.put("height", desc, height);

        return desc;
    }
}
