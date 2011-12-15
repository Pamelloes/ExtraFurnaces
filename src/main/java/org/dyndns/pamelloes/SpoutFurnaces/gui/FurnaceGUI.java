package org.dyndns.pamelloes.SpoutFurnaces.gui;

import java.awt.Point;

import org.getspout.spoutapi.player.SpoutPlayer;

public interface FurnaceGUI {
	public void attach(SpoutPlayer player);
	
	public String getTextureURL();
	public int getTextureWidth();
	public int getTextureHeight();
	
	public Point getItemLocation(int index);
}
