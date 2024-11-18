package ru.koshakmine.icstd.type.common;

public class Texture {
    public final String texture;
    public final int meta;

    public Texture(String texture, int meta){
        this.texture = texture;
        this.meta = meta;
    }

    public Texture(String texture){
        this(texture, 0);
    }

    @Override
    public String toString() {
        return "Texture{" +
                "texture='" + texture + '\'' +
                ", meta=" + meta +
                '}';
    }

    public static final Texture EMPTY = new Texture("missing");
}
