package org.dyndns.pamelloes.SpoutFurnaces.gui;

import java.awt.Point;

import org.dyndns.pamelloes.SpoutFurnaces.SpoutFurnaces;

public class GoldFurnaceGUI extends CustomFurnaceGUI {

	public GoldFurnaceGUI(SpoutFurnaces plugin) {
		super(plugin);
	}

	public String getTextureURL() {
		return "plugins/SpoutFurnaces/goldfurnace.png";
	}
	
	public int getTextureWidth() {
		return 216;
	}
	
	public int getTextureHeight() {
		return 166;
	}

	@Override
	public Point getInventoryOffset() {
		return new Point(28,84);
	}

}
