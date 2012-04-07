package org.dyndns.pamelloes.ExtraFurnaces.block;

import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.dyndns.pamelloes.ExtraFurnaces.ExtraFurnaces;
import org.dyndns.pamelloes.ExtraFurnaces.data.GoldFurnaceData;
import org.dyndns.pamelloes.ExtraFurnaces.gui.FurnaceGui;
import org.dyndns.pamelloes.ExtraFurnaces.gui.GoldFurnaceGui;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.SpoutBlock;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.player.SpoutPlayer;

public class GoldFurnace extends CustomFurnace {

	public GoldFurnace(ExtraFurnaces plugin, Texture texture, int[] ids, boolean on) {
		super(plugin, "Gold Furnace", texture, ids, on);
	}
	
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int changedId) { }

	@Override
    public void onBlockPlace(World world, int x, int y, int z) {
		if(((SpoutBlock)world.getBlockAt(x, y, z)).getCustomBlock() instanceof CustomFurnace) return;
		SpoutManager.getChunkDataManager().setBlockData("ExtraFurnaces", world, x, y, z, new GoldFurnaceData(world.getUID(),x,y,z));
	}

	@Override
    public void onEntityMoveAt(World world, int x, int y, int z, Entity entity) { }

	@Override
    public void onBlockClicked(World world, int x, int y, int z, SpoutPlayer player) { }

	@Override
    public boolean isProvidingPowerTo(World world, int x, int y, int z, BlockFace face) {
        return false;
    }

	@Override
    public boolean isIndirectlyProvidingPowerTo(World world, int x, int y, int z, BlockFace face) {
        return false;
    }

	@Override
	public FurnaceGui getGui(SpoutPlayer player) {
		return new GoldFurnaceGui(player);
	}
}
