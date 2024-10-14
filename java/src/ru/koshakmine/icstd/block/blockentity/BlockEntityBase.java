package ru.koshakmine.icstd.block.blockentity;

import ru.koshakmine.icstd.block.blockentity.ticking.ITickingBlockEntity;
import ru.koshakmine.icstd.level.Level;
import ru.koshakmine.icstd.network.NetworkSide;
import ru.koshakmine.icstd.runtime.Updatable;
import ru.koshakmine.icstd.type.common.Position;

public abstract class BlockEntityBase {
    protected final Position position;
    protected final String type;
    public int x, y, z, id, dimension;
    protected final Level level;

    // Not use!
    protected boolean removed = false, fullInit = false;

    public BlockEntityBase(Position position, Level level, String type, int id){
        this.position = position;
        this.level = level;

        this.type = type;
        this.id = id;

        this.x = (int) Math.floor(position.x);
        this.y = (int) Math.floor(position.y);
        this.z = (int) Math.floor(position.z);
        this.dimension = level.getDimension();
    }

    // Life events
    public void onInit(){}
    public void onRemove(){}

    // Methods
    public boolean canInitialization() {
        return fullInit;
    }

    public boolean canRemove(){
        return removed;
    }

    public Position getPosition() {
        return position;
    }

    public Level getLevel() {
        return level;
    }

    public int getDimension() {
        return dimension;
    }

    public boolean canDestroyBlockEntity() {
        return (level.isChunkLoaded(x / 16, z / 16) && level.getBlockId(x, y, z) != id) || canRemove();
    }

    public String getType() {
        return type;
    }

    public boolean removeBlockEntity(){
        if(!canRemove()) {
            if(canInitialization()) {
                onRemove();
            }

            removed = true;
            return true;
        }

        return false;
    }

    public abstract NetworkSide getSide();
}
