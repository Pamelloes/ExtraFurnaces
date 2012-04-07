package org.dyndns.pamelloes.ExtraFurnaces.block;

import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.inventory.FurnaceInventory;
import org.dyndns.pamelloes.ExtraFurnaces.data.CustomFurnaceData;
import org.getspout.spoutapi.SpoutManager;

public class CustomFurnaceBlockState extends CraftBlockState implements Furnace {
    private final CustomFurnaceData furnace;
    
	public CustomFurnaceBlockState(Block block) {
		super(block);
		furnace = (CustomFurnaceData) SpoutManager.getChunkDataManager().getBlockData("ExtraFurnaces", getWorld(), getX(), getY(), getZ());
	}

	public FurnaceInventory getInventory() {
		return furnace;
	}

    public short getBurnTime() {
        return (short) furnace.furnaceBurnTime;
    }

    public void setBurnTime(short burnTime) {
        furnace.furnaceBurnTime = burnTime;
    }

    public short getCookTime() {
        return (short) furnace.furnaceCookTime;
    }

    public void setCookTime(short cookTime) {
        furnace.furnaceCookTime = cookTime;
    }
}
