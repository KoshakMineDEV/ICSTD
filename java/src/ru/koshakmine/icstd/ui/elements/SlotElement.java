package ru.koshakmine.icstd.ui.elements;

import org.mozilla.javascript.ScriptableObject;
import ru.koshakmine.icstd.type.common.ItemStack;

public class SlotElement extends WindowElement {
    protected String bitmap;
    protected int size = 60;
    protected boolean isDarkenAtZero = true, visual = false;
    protected ItemStack source;

    public SlotElement(int x, int y, int size) {
        super("slot", x, y);

        this.size = size;
    }

    public SlotElement setVisual(boolean visual) {
        this.visual = visual;
        return this;
    }

    public SlotElement setSize(int size) {
        this.size = size;
        return this;
    }

    public SlotElement setSource(ItemStack source) {
        this.source = source;
        return this;
    }

    public SlotElement setDarkenAtZero(boolean darkenAtZero) {
        isDarkenAtZero = darkenAtZero;
        return this;
    }

    public SlotElement setBitmap(String bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    @Override
    public ScriptableObject toElement() {
        final ScriptableObject desc = super.toElement();

        if(bitmap != null)
            desc.put("bitmap", desc, bitmap);
        desc.put("size", desc, size);
        desc.put("isDarkenAtZero", desc, isDarkenAtZero);
        desc.put("visual", desc, visual);
        if(source != null)
            desc.put("source", desc, source.getItemInstance());

        return desc;
    }
}
