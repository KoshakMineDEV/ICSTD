IMPORT("EnergyNet");

WRAP_JAVA("ru.koshakmine.icstd.js.EnergyNetLib")
    .init(EnergyNet, {
        assureEnergyType(type, value){
            return EnergyTypeRegistry.assureEnergyType(String(type), Number(value));
        }
    }, {
        addEnergyTypeForId(id, type){
            EnergyTileRegistry.addEnergyTypeForId(Number(id), type);
        }
    }, {
        registerTile(id){
            TileEntity.registerPrototype(Number(id), {
                getApi(): ITileApi {
                    return this.__api__;
                },

                energyTick(type: string, node: EnergyTileNode): void {
                    this.getApi().energyTick(type, node, node.add);
                },
                energyReceive(type: string, amount: number, voltage: number): number {
                    return Number(this.getApi().energyReceive(type, amount, voltage));
                },

                isConductor(type: string): boolean {
                    return this.getApi().isConductor(type);
                },
                canReceiveEnergy(side: number, type: string): boolean {
                    return this.getApi().canReceiveEnergy(side, type);
                },
                canExtractEnergy(side: number, type: string): boolean {
                    return this.getApi().canExtractEnergy(side, type);;
                }
            });
        }
    });