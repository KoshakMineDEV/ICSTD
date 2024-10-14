package ru.koshakmine.icstd.ui.elements;

import org.mozilla.javascript.ScriptableObject;

public class ImageElement extends WindowElementSize {
    protected String bitmap;

    public ImageElement(String bitmap, int x, int y, int width, int height) {
        super("image", x, y, width, height);
        this.bitmap = bitmap;
    }

    @Override
    public ScriptableObject toElement() {
        final ScriptableObject desc = super.toElement();
        desc.put("bitmap", desc, bitmap);
        return desc;
    }
}
