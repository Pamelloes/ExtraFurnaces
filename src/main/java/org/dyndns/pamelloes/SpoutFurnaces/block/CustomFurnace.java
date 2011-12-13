package org.dyndns.pamelloes.SpoutFurnaces.block;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.dyndns.pamelloes.SpoutFurnaces.SpoutFurnaces;
import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;
import org.getspout.spoutapi.player.SpoutPlayer;

public class CustomFurnace extends GenericCubeCustomBlock {
	private SpoutFurnaces plugin;
	private Texture texture;
	private int[] idson, idsoff;
	
	private GenericCubeBlockDesign desoff, deson;
	
	private boolean on = false;
	
	public CustomFurnace(SpoutFurnaces plugin, String name, Texture tex, int[] idson,int[] idsoff) {
		super(plugin, name, true, new GenericCubeBlockDesign(plugin, tex, idsoff));
		desoff=(GenericCubeBlockDesign) getBlockDesign();
		deson=new GenericCubeBlockDesign(plugin, tex, idson);
		this.idson=idson;
		this.idsoff=idsoff;
		this.plugin=plugin;
		texture=tex;
	}
	
	public void turnOn() {
		if(on) return;
		setBlockDesign(deson);
		on=true;
	}
	
	public void turnOff() {
		if(!on) return;
		setBlockDesign(desoff);
		on=false;
	}
	
	public boolean isOn() {
		return on;
	}
	

	@Override
    public void onBlockPlace(World world, int x, int y, int z, LivingEntity living) {
		System.out.println("BLOCK PLACE!");
		int[][] positions = {
				{x+1,z},
				{x,z-1},
				{x-1,z},
				{x,z+1}
		};
		int[] pos  = {living.getLocation().getBlockX(),living.getLocation().getBlockZ()};
		
		int face = 0;
		double dist = -1;
		for(int i=0; i<4;i++) {
			double di = Math.sqrt(Math.pow(positions[i][0]+pos[0], 2) + Math.pow(positions[i][1] + pos[1], 2));
			if(dist<0 || di<dist) {
				dist = di;
				face = i;
			}
		}
		int critid = idson[1];
		int normid = idson[2];
		idson[1]=normid;
		idson[face+1] = critid;
		
		critid = idsoff[1];
		normid = idsoff[2];
		idsoff[1]=normid;
		idsoff[face+1] = critid;
		
		desoff = new GenericCubeBlockDesign(plugin, texture, idsoff);
		deson = new GenericCubeBlockDesign(plugin, texture, idson);
		
		if(isOn()) setBlockDesign(deson);
		else setBlockDesign(desoff);
	}
	

	@Override
    public boolean onBlockInteract(World world, int x, int y, int z, SpoutPlayer player) {
        //player.getMainScreen().attachPopupScreen(
		System.out.println("BLOCK INTERACT!");
		if(isOn()) turnOff();
		else turnOn();
		return true;
    }
}
