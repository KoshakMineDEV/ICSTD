declare class IJsTickBlockEntity {
    constructor(id: number, x: number, y: number, z: number, dimension: number, tiel: any);
}

declare class ITickingSystemBlockEntity {
    constructor(isServer: boolean);

    public addBlockEntity(entity: IJsTickBlockEntity): void;
    public removeBlockEntity(entity: IJsTickBlockEntity): void;
}

let JsTickBlockEntity: typeof IJsTickBlockEntity = WRAP_JAVA("ru.koshakmine.icstd.block.blockentity.ticking.JsTickBlockEntity");
let TickingSystemBlockEntity: typeof ITickingSystemBlockEntity = WRAP_JAVA("ru.koshakmine.icstd.block.blockentity.ticking.JsTickingSystemBlockEntity");

let System = new TickingSystemBlockEntity(true);

TileEntity.createTileEntityForPrototype = function(Prototype: any, addToUpdate?: boolean): any {
    var tileEntity: any = {};

    for (let property in Prototype) {
        tileEntity[property] = Prototype[property];
    }

    tileEntity.data = {};

    for (let property in Prototype.defaultValues) {
        tileEntity.data[property] = Prototype.defaultValues[property];
    }

    tileEntity.networkData = new SyncedNetworkData();
    tileEntity.container = Prototype.useNetworkItemContainer ? new ItemContainer() : new UI.Container(tileEntity);
    tileEntity.container.setParent(tileEntity);
    tileEntity.liquidStorage = new LiquidRegistry.Storage(tileEntity);

    if (addToUpdate) {
        if (tileEntity.saverId && tileEntity.saverId != -1) {
            Saver.registerObject(tileEntity, tileEntity.saverId);
        }

        // Updatable.addUpdatable(tileEntity);

        tileEntity.remove = false;
        tileEntity.isLoaded = true;
    }

    return tileEntity;
}

function optiTile(tileEntity){
    let fakeBlockEntity = new JsTickBlockEntity(tileEntity.blockID, tileEntity.x, tileEntity.y, tileEntity.z, tileEntity.dimension, tileEntity);

    tileEntity.___fakeBlockEntity___ = fakeBlockEntity;

    let func = tileEntity.destroy || function(){}
    tileEntity.destroy = function(){
        System.removeBlockEntity(fakeBlockEntity);
        return func();
    }

    tileEntity.update = function(){
        if (this.isLoaded) {
            if (!this.__initialized) {
                if (!this._runInit()) {
                    this.noupdate = true;
                    System.removeBlockEntity(fakeBlockEntity);
                    return;
                }
            }
            if (this.tick) {
                this.tick();
            }
        }
    }

    System.addBlockEntity(fakeBlockEntity);
}

TileEntity.addTileEntity = function(x: number, y: number, z: number, blockSource: BlockSource): any {
    if (this.getTileEntity(x, y, z, blockSource)) {
        return null;
    }
    var tile = blockSource ? blockSource.getBlockId(x, y, z) : World.getBlockID(x, y, z);
    var Prototype = this.getPrototype(tile);
    if (Prototype) {
        var tileEntity = this.createTileEntityForPrototype(Prototype, true);
        
        tileEntity.x = x;
        tileEntity.y = y;
        tileEntity.z = z;
        tileEntity.dimension = blockSource ? blockSource.getDimension() : Player.getDimension();

        optiTile(tileEntity);

        this.tileEntityList.push(tileEntity);
        this.tileEntityCacheMap[x + "," + y + "," + z + "," + tileEntity.dimension] = tileEntity;
        tileEntity.created();
        Callback.invokeCallback("TileEntityAdded", tileEntity, true);
        return tileEntity;
    }
    return null;
}

Saver.addSavesScope("_tiles", function read(data){
    let tiles = TileEntity["tileEntityList"] = data || [];
    for(let i in tiles)
        optiTile(tiles[i]);
}, function save(){
    return TileEntity["tileEntityList"];
});