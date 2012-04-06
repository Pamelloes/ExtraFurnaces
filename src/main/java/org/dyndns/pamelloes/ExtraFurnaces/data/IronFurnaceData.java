package org.dyndns.pamelloes.ExtraFurnaces.data;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.dyndns.pamelloes.ExtraFurnaces.ExtraFurnaces;
import org.getspout.spoutapi.event.spout.ServerTickEvent;
import org.getspout.spoutapi.material.CustomBlock;

public class IronFurnaceData extends CustomFurnaceData {
	private static final long serialVersionUID = -3726681582102483401L;

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
		boolean update = update();
		if(update) {
			if(furnaceItemStacks[getBurnIndex()] == null && furnaceItemStacks[getResultIndex()] != null) updateRange(getResultIndex() + 1, getResultIndex(), false);
			updateRange(getFuelIndex(), getFuelIndex() - 1);
			updateRange(getBurnIndex(), getBurnIndex() - 1);
	        updateInventory();
		}
		updateData();
	}

	@Override
	protected CustomBlock getBlock(boolean burning) {
		return burning ? ExtraFurnaces.ironfurnaceon : ExtraFurnaces.ironfurnaceoff;
	}
}
