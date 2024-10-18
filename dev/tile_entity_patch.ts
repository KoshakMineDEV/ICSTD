interface ITileApi {
    energyTick(type: string, node: EnergyTileNode): void;
    energyReceive(type: string, amount: number, voltage: number): number;

    isConductor(type: string): boolean;
    canReceiveEnergy(side: number, type: string): boolean;
    canExtractEnergy(side: number, type: string): boolean;
}

declare class IBlockEntity {
    public getFakeTileEntity(): any;
}

declare class IJsTickBlockEntity extends IBlockEntity {
    constructor(id: number, x: number, y: number, z: number, dimension: number, tiel: any);
}

declare class ITickingSystemBlockEntity {
    public addBlockEntity(entity: IJsTickBlockEntity): void;
    public removeBlockEntity(entity: IJsTickBlockEntity): void;
}

declare class IBlockEntityManager {
    public removeBlockEntity(entity: IBlockEntity): void;
    public addBlockEntity(entity: IBlockEntity): void;
    public getBlockEntity(x: number, y: number, z: number, dimension: number): IBlockEntity;
}

let JsTickBlockEntity: typeof IJsTickBlockEntity = WRAP_JAVA("ru.koshakmine.icstd.block.blockentity.ticking.JsTickBlockEntity");
let System: ITickingSystemBlockEntity = WRAP_JAVA("ru.koshakmine.icstd.block.blockentity.ticking.JsTickingSystemBlockEntity")
    .getInstance();
let Manager: IBlockEntityManager = WRAP_JAVA("ru.koshakmine.icstd.block.blockentity.BlockEntity").getManager();

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

let getTileEntityOriginal = TileEntity.getTileEntity;
TileEntity.getTileEntity = function(x: number, y: number, z:number, region?: BlockSource): any {
    let result = getTileEntityOriginal.apply(this, arguments);
    if(!result){
        let tile = Manager.getBlockEntity(x, y, z, region ? region.getDimension() : Player.getDimension());
        return tile ? tile.getFakeTileEntity() : undefined;
    }
    return result;
}

function optiTile(tileEntity){
    tileEntity.update = function(){
        if (this.isLoaded) {
            if (!this.__initialized) {
                !this.tick && System.removeBlockEntity(tileEntity.___fakeBlockEntity___);
                if (!this._runInit()) {
                    this.noupdate = true;
                    return;
                }
            }

            this.tick && this.tick();
        }
    }

    tileEntity.___fakeBlockEntity___ = new JsTickBlockEntity(tileEntity.blockID, tileEntity.x, tileEntity.y, tileEntity.z, tileEntity.dimension, tileEntity);
    let fakeTile = tileEntity.___fakeBlockEntity___.getFakeTileEntity();
    for(let key in fakeTile){
        tileEntity[key] = fakeTile[key];
    }

    // Хрен пойми откуда rhinon получает add
    if(fakeTile.energyTick){
        tileEntity.energyTick = function(name, node){
            fakeTile.energyTick(name, node, node.add);
        }
    }



    let func = tileEntity.destroy || function(){}
    tileEntity.destroy = function(){
        System.removeBlockEntity(tileEntity.___fakeBlockEntity___);
        return func();
    }

    System.addBlockEntity(tileEntity.___fakeBlockEntity___);
}

TileEntity.addTileEntity = function(x: number, y: number, z: number, blockSource: BlockSource): any {
    let tileGet = this.getTileEntity(x, y, z, blockSource);
    if (tileGet && !tileGet.___fakeTile___) {
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
        
        if(tileGet && tileGet.container){
            tileEntity.container = tileGet.container;
        }

        optiTile(tileEntity);

        this.tileEntityList.push(tileEntity);
        this.tileEntityCacheMap[x + "," + y + "," + z + "," + tileEntity.dimension] = tileEntity;
        tileEntity.created();
        Callback.invokeCallback("TileEntityAdded", tileEntity, true);
        return tileEntity;
    }
    return tileGet;
}

TileEntity.addUpdatableAsTileEntity = function(updatable){
    updatable.remove = false;
    updatable.isLoaded = true;
    if (updatable.saverId && updatable.saverId !== -1) {
        Saver.registerObject(updatable, updatable.saverId);
    }
    optiTile(updatable);
    this.tileEntityList.push(updatable);
    this.tileEntityCacheMap[updatable.x + "," + updatable.y + "," + updatable.z + "," + updatable.dimension] = updatable;
    Callback.invokeCallback("TileEntityAdded", updatable, false);
}

let AbobaDeclarations: any = TileEntity;

Saver.addSavesScope("_tiles", function read(data){
    AbobaDeclarations.tileEntityList = data || [];
}, function save(){
    return TileEntity["tileEntityList"];
});

WRAP_JAVA("ru.koshakmine.icstd.js.LiquidRegistry").init(LiquidRegistry);
WRAP_JAVA("ru.koshakmine.icstd.js.TileEntity").init(TileEntity);