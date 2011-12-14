package org.dyndns.pamelloes.SpoutFurnaces.gui;

import org.dyndns.pamelloes.SpoutFurnaces.SpoutFurnaces;

public class IronFurnaceGUI extends CustomFurnaceGUI {

	public IronFurnaceGUI(SpoutFurnaces plugin) {
		super(plugin);
	}

	public String getTextureURL() {
		return "plugins/SpoutFurnaces/ironfurnace.png";
	}
	
	public int getTextureWidth() {
		return 176;
	}
	
	public int getTextureHeight() {
		return 166;
	}

}
