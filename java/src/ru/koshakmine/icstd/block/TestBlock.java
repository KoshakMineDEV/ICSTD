package ru.koshakmine.icstd.block;

public class TestBlock extends Block  {
    @Override
    public String getId() {
        return "testik";
    }

    @Override
    public String getName() {
        return "GGG";
    }

    @Override
    public String[] getTextures() {
        return new String[]{"gg"};
    }

    @Override
    public int getLightLevel() {
        return 15;
    }
}
