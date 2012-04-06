package org.dyndns.pamelloes.ExtraFurnaces.gui;

import java.awt.Point;

import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.player.SpoutPlayer;


public class GoldFurnaceGui extends FurnaceGui {

	public GoldFurnaceGui(SpoutPlayer sp) {
		super(sp, 216, 166);
		//input
		addSlot(new InventorySlot(8,17,this));
		addSlot(new InventorySlot(26,17,this));
		addSlot(new InventorySlot(44,17,this));
		addSlot(new InventorySlot(62,17,this));
		//fuel
		addSlot(new InventorySlot(8,53,this));
		addSlot(new InventorySlot(26,53,this));
		addSlot(new InventorySlot(44,53,this));
		addSlot(new InventorySlot(62,53,this));
		//output
		addSlot(new InventorySlot(116,35,this).setReadOnly(true));
		addSlot(new InventorySlot(138,39,this).setReadOnly(true));
		addSlot(new InventorySlot(156,39,this).setReadOnly(true));
		addSlot(new InventorySlot(174,39,this).setReadOnly(true));
		addSlot(new InventorySlot(192,39,this).setReadOnly(true));
		itemBurnTime = 80;
	}

	@Override
	protected int getFlameX() {
		return 63;
	}

	@Override
	protected int getFlameY() {
		return 36;
	}

	@Override
	protected int getArrowX() {
		return 79;
	}

	@Override
	protected int getArrowY() {
		return 34;
	}

	@Override
	protected String getName() {
		return "Gold Furnace";
	}
	

	@Override
	protected Texture getBackground() {
		Texture t = new GenericTexture("plugins/ExtraFurnaces/goldfurnace.png");
		t.setLeft(0).setTop(0).setWidth(216).setHeight(166);
		return t;
	}

	@Override
	public Point getInventoryOffset() {
		return new Point(28, 84);
	}
	
}
