package ru.koshakmine.icstd.ui.elements;

import android.graphics.Color;
import com.zhekasmirnov.innercore.api.mod.ui.types.Font;
import org.mozilla.javascript.ScriptableObject;

public class TextElement extends WindowElement {
    protected String text;
    protected Font font;
    protected boolean multiline;

    public TextElement(String text, int x, int y, int size, int shadow) {
        super("text", x, y);

        this.text = text;
        this.font = new Font(Color.BLACK, size, shadow);
    }

    public TextElement(String text, int x, int y, int size){
        this(text, x, y, size, 0);
    }

    public TextElement setFont(boolean isBold, boolean isCursive, boolean isUnderlined) {
        font.isBold = isBold;
        font.isCursive = isCursive;
        font.isUnderlined = isUnderlined;
        return this;
    }

    public TextElement setMultiline(boolean multiline) {
        this.multiline = multiline;
        return this;
    }

    @Override
    public ScriptableObject toElement() {
        final ScriptableObject desc = super.toElement();

        desc.put("text", desc, text);
        desc.put("font", desc, font.asScriptable());
        desc.put("multiline", desc, multiline);

        return desc;
    }
}
