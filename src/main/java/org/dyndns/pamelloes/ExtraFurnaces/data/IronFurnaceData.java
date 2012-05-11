package org.dyndns.pamelloes.ExtraFurnaces.data;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.dyndns.pamelloes.ExtraFurnaces.ExtraFurnaces;
import org.getspout.spoutapi.event.spout.ServerTickEvent;
import org.getspout.spoutapi.material.CustomBlock;

public class IronFurnaceData extends CustomFurnaceData {
	private static final long serialVersionUID = -7918321645452718576L;

	public IronFurnaceData(UUID world, int x, int y, int z) {
		super(6, world, x, y, z);
	}

	@Override
	protected int getFuelIndex() {
		return 3;
	}

	@Override
	protected int getBurnIndex() {
		return 1;
	}

	@Override
	protected int getResultIndex() {
		return 4;
	}
	
	@Override
	protected int getItemSmeltTime() {
		return 150;
	}

	@Override
	@EventHandler
	public void onServerTick(ServerTickEvent e) {
		boolean update = false;
		if(update()) {
			updateRange(getFuelIndex(), getFuelIndex() - 1);
			updateRange(getBurnIndex(), getBurnIndex() - 1);
	        updateInventory();
			update = true;
		}
		if (adjustResult(2)) update = true;
		if (update) updateInventory();
		updateData();
	}

	@Override
	protected CustomBlock getBlock(boolean burning) {
		return burning ? ExtraFurnaces.ironfurnaceon : ExtraFurnaces.ironfurnaceoff;
	}

	public String getName() {
		return "Iron Furnace";
	}
}
