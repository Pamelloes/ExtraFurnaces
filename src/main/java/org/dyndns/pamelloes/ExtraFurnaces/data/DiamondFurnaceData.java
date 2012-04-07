package org.dyndns.pamelloes.ExtraFurnaces.data;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.dyndns.pamelloes.ExtraFurnaces.ExtraFurnaces;
import org.getspout.spoutapi.event.spout.ServerTickEvent;
import org.getspout.spoutapi.material.CustomBlock;

public class DiamondFurnaceData extends CustomFurnaceData {
	private static final long serialVersionUID = -3726681582102483401L;

	public DiamondFurnaceData(UUID world, int x, int y, int z) {
		super(23, world, x, y, z);
	}

	@Override
	protected int getFuelIndex() {
		return 13;
	}

	@Override
	protected int getBurnIndex() {
		return 6;
	}

	@Override
	protected int getResultIndex() {
		return 14;
	}
	
	@Override
	protected int getItemSmeltTime() {
		return 40;
	}

	@Override
	@EventHandler
	public void onServerTick(ServerTickEvent e) {
		boolean update = update();
		if(update) {
			if(furnaceItemStacks[getBurnIndex()] == null && furnaceItemStacks[getResultIndex()] != null) updateRange(getResultIndex() + 8, getResultIndex(), false);
			updateRange(getFuelIndex(), getFuelIndex() - 6);
			updateRange(getBurnIndex(), getBurnIndex() - 6);
	        updateInventory();
		}
		updateData();
	}

	@Override
	protected CustomBlock getBlock(boolean burning) {
		return burning ? ExtraFurnaces.diamondfurnaceon : ExtraFurnaces.diamondfurnaceoff;
	}

	public String getName() {
		return "Diamond Furnace";
	}
}
