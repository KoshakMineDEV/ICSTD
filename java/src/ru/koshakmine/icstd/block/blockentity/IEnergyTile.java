package ru.koshakmine.icstd.block.blockentity;

import ru.koshakmine.icstd.js.EnergyNetLib;

public interface IEnergyTile {
    void energyTick(String type, EnergyNetLib.EnergyTileNode node);
    float energyReceive(String type, float amount, int voltage);
    boolean isConductor(String type);
    boolean canReceiveEnergy(int side, String type);
    boolean canExtractEnergy(int side, String type);
}
