package org.dyndns.pamelloes.ExtraFurnaces.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftChunk;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.getspout.spoutapi.block.SpoutBlock;

public class CraftSpecialBlock extends CraftBlock {

	public CraftSpecialBlock(Block b) {
		super((CraftChunk) b.getChunk(), b.getX(), b.getY(), b.getZ());
		if(((SpoutBlock) b).getCustomBlock() == null) throw new IllegalArgumentException("The given Block must have an associated CustomBlock!");
		if(!(((SpoutBlock) b).getCustomBlock() instanceof CustomFurnace)) throw new IllegalArgumentException("The given Block's CustomBlock must be a CustomFurnace!");
	}

	@Override
	public BlockState getState() {
		return new CustomFurnaceBlockState(this);
	}
}
