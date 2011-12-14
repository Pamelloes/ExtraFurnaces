package org.dyndns.pamelloes.SpoutFurnaces.gui;

import org.dyndns.pamelloes.SpoutFurnaces.SpoutFurnaces;
import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.inventory.SpoutPlayerInventory;
import org.getspout.spoutapi.player.SpoutPlayer;

public abstract class CustomFurnaceGUI implements FurnaceGUI {
	private Texture bg;
	private PopupScreen popup;
	
	public CustomFurnaceGUI(SpoutFurnaces plugin) {
		bg = new GenericTexture(getTextureURL());
		bg.setX(0).setY(0);
		bg.setLeft(0).setTop(0).setWidth(getTextureWidth()).setHeight(getTextureHeight());
		Container cont = new GenericContainer(bg);
		cont.setAnchor(WidgetAnchor.CENTER_CENTER).setX(0).setY(0);
		cont.setHeight(bg.getHeight()).setWidth(bg.getWidth());
		cont.shiftXPos(-(cont.getWidth()/2)).shiftYPos(-(cont.getHeight()/2));
		popup = new GenericPopup();
		popup.setBgVisible(false);
		popup.attachWidget(plugin, bg);
	}
	
	public void attach(SpoutPlayer player) {
		player.getMainScreen().attachPopupScreen(popup);
		loadInventory(player);
	}

	private void loadInventory(SpoutPlayer player) {
		SpoutPlayerInventory inv = (SpoutPlayerInventory) player.getInventory();
		
	}

}
