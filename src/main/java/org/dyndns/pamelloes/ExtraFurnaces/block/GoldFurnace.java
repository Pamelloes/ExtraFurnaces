package org.dyndns.pamelloes.ExtraFurnaces.block;

import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.dyndns.pamelloes.ExtraFurnaces.SpoutFurnaces;
import org.dyndns.pamelloes.ExtraFurnaces.data.OpenGUI.GUIType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.player.SpoutPlayer;

public class GoldFurnace extends CustomFurnace {

	public GoldFurnace(SpoutFurnaces plugin, Texture texture, int[] designon, int[] designoff) {
		super(plugin, "Gold Furnace", texture, designon, designoff);
	}
	
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int changedId) { }

	@Override
    public void onBlockPlace(World world, int x, int y, int z) {
		SpoutManager.getChunkDataManager().setBlockData("SpoutFurnaces", world, x, y, z, new GoldFurnaceData(world.getUID(),x,y,z));
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
	public GUIType getGUIType() {
		return GUIType.GoldFurnace;
	}
}
