package org.dyndns.pamelloes.SpoutFurnaces.block;

import org.dyndns.pamelloes.SpoutFurnaces.SpoutFurnaces;
import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;

public class CustomFurnace extends GenericCubeCustomBlock {
	private GenericCubeBlockDesign desoff, deson;
	
	public CustomFurnace(SpoutFurnaces plugin, String name, GenericCubeBlockDesign designoff, GenericCubeBlockDesign designon) {
		super(plugin, name, true, designoff);
		desoff=designoff;
		deson=designon;
	}
	
	public void turnOn() {
		setBlockDesign(deson);
	}
	
	public void turnOff() {
		setBlockDesign(desoff);
	}
}
