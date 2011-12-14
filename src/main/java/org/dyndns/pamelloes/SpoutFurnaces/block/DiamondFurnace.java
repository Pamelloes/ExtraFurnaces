package org.dyndns.pamelloes.SpoutFurnaces.block;

import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.dyndns.pamelloes.SpoutFurnaces.SpoutFurnaces;
import org.dyndns.pamelloes.SpoutFurnaces.gui.CustomFurnaceGUI;
import org.dyndns.pamelloes.SpoutFurnaces.gui.DiamondFurnaceGUI;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.player.SpoutPlayer;

public class DiamondFurnace extends CustomFurnace {

	public DiamondFurnace(SpoutFurnaces plugin, Texture texture, int[] designon, int[] designoff) {
		super(plugin, "Diamond Furnace", texture, designon, designoff);
	}
	
	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int changedId) { }

	@Override
    public void onBlockPlace(World world, int x, int y, int z) { }

	@Override
    public void onBlockDestroyed(World world, int x, int y, int z) { }

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
	public CustomFurnaceGUI getGUI() {
		return new DiamondFurnaceGUI(plugin);
	}
}
