package ru.koshakmine.icstd.ui.elements;

public class CloseButtonElement extends ButtonElement {
    public CloseButtonElement(float x, float y, String bitmap, float scale){
        super("closeButton", x, y, bitmap, scale);
    }

    public CloseButtonElement(float x, float y, String bitmap){
        super("closeButton", x, y, bitmap, 1f);
    }
}
