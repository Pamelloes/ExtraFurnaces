package org.dyndns.pamelloes.SpoutFurnaces.gui;

import java.awt.Point;

import org.dyndns.pamelloes.SpoutFurnaces.SpoutFurnaces;
import org.getspout.spoutapi.gui.Container;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericItemWidget;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.ItemWidget;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutPlayerInventory;
import org.getspout.spoutapi.player.SpoutPlayer;

public abstract class CustomFurnaceGUI implements FurnaceGUI {
	private SpoutFurnaces plugin;
	
	private Texture bg;
	private Container cont;
	private PopupScreen popup;
	private int gox, goy;
	
	public CustomFurnaceGUI(SpoutFurnaces plugin) {
		this.plugin=plugin;
		
		bg = new GenericTexture(getTextureURL());
		bg.setX(0).setY(0);
		bg.setWidth(getTextureWidth()).setHeight(getTextureHeight());
		bg.setFixed(true);
		bg.setPriority(RenderPriority.Highest);
		bg.setAnchor(WidgetAnchor.CENTER_CENTER);
		/*ont = new GenericContainer();//bg);
		cont.setLayout(ContainerType.OVERLAY);
		cont.setAnchor(WidgetAnchor.CENTER_CENTER).setX(0).setY(0);
		cont.setHeight(bg.getHeight()).setWidth(bg.getWidth());
		cont.shiftXPos(-(cont.getWidth()/2)).shiftYPos(-(cont.getHeight()/2));*/
		gox = -(bg.getWidth()/2);
		goy = -(bg.getHeight()/2);
		bg.shiftXPos(gox).shiftYPos(goy);
		popup = new GenericPopup();
		popup.setBgVisible(false);
		//popup.attachWidget(plugin, cont);
		popup.attachWidget(plugin, bg);
	}
	
	public void attach(SpoutPlayer player) {
		player.getMainScreen().attachPopupScreen(popup);
		loadInventory(player);
	}

	private void loadInventory(SpoutPlayer player) {
		SpoutPlayerInventory inv = (SpoutPlayerInventory) player.getInventory();
		for(int i=0;i<35;i++) {
			SpoutItemStack sis = new SpoutItemStack(inv.getItem(i));
			if(sis.getTypeId()==0 && !sis.isCustomItem()) continue;
			ItemWidget iw = new GenericItemWidget(sis);
			iw.setWidth(8).setHeight(8);//iw.setWidth(16).setHeight(16);
			iw.setFixed(true);
			iw.setAnchor(WidgetAnchor.CENTER_CENTER);//.TOP_LEFT);
			iw.setX(0).setY(0);
			Point pt = getItemLocation(i);
			iw.shiftXPos((int) pt.getX()).shiftYPos((int) pt.getY());
			iw.shiftXPos(gox).shiftYPos(goy);
			//cont.addChild(iw);
			popup.attachWidget(plugin, iw);
		}
		/*SpoutItemStack sis = new SpoutItemStack(inv.getItem(0));
		ItemWidget iw = new GenericItemWidget(sis);
		Point pt = getItemLocation(0);
		iw.setX((int) pt.getX()).setY((int) pt.getY());
		iw.setWidth(16).setHeight(16);
		iw.setAnchor(WidgetAnchor.TOP_LEFT);
		cont.addChild(iw);*/
		
	}
	
	public abstract Point getInventoryOffset();

	public Point getItemLocation(int index) {
		if(index < 0 || index > 35) throw new IllegalArgumentException("The inventory only takes up slots 0 to 35");
		
		//calculate square in the inventory.
		int ix, iy;
		ix = index%9;
		if(index<9) iy=3;
		else iy = ( (index-ix) / 9) - 1;
		
		//calculate exact coordinates in the inventory.
		int ax,ay;
		ax = 18*ix;
		if(iy!=3) ay=18*iy;
		else ay = 18*3 + 4;
		//System.out.println("X:"+ax+"\nY:"+ay);
		Point result = getInventoryOffset();
		result.translate(ax, ay);
		return result;
	}
}
