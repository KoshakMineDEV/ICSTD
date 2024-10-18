package ru.koshakmine.icstd.recipes.workbench;

public class PatternData {
    public final char symbol;
    public final int id, data;

    public PatternData(char symbol, int id, int data){
        this.symbol = symbol;
        this.id = id;
        this.data = data;
    }

    public PatternData(char symbol, int id){
        this(symbol, id, -1);
    }
}
