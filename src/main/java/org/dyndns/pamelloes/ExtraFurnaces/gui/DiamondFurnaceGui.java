package org.dyndns.pamelloes.ExtraFurnaces.gui;

import java.awt.Point;

import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.player.SpoutPlayer;


public class DiamondFurnaceGui extends FurnaceGui {

	public DiamondFurnaceGui(SpoutPlayer sp) {
		super(sp, 216, 202);
		//input
		addSlot(new InventorySlot(8,17,this));
		addSlot(new InventorySlot(26,17,this));
		addSlot(new InventorySlot(44,17,this));
		addSlot(new InventorySlot(8,35,this));
		addSlot(new InventorySlot(26,35,this));
		addSlot(new InventorySlot(44,35,this));
		addSlot(new InventorySlot(62,35,this));
		//fuel
		addSlot(new InventorySlot(8,89,this));
		addSlot(new InventorySlot(26,89,this));
		addSlot(new InventorySlot(44,89,this));
		addSlot(new InventorySlot(8,71,this));
		addSlot(new InventorySlot(26,71,this));
		addSlot(new InventorySlot(44,71,this));
		addSlot(new InventorySlot(62,71,this));
		//output
		addSlot(new InventorySlot(116,53,this).setReadOnly(true));
		addSlot(new InventorySlot(138,39,this).setReadOnly(true));
		addSlot(new InventorySlot(156,39,this).setReadOnly(true));
		addSlot(new InventorySlot(174,39,this).setReadOnly(true));
		addSlot(new InventorySlot(192,39,this).setReadOnly(true));
		addSlot(new InventorySlot(138,57,this).setReadOnly(true));
		addSlot(new InventorySlot(156,57,this).setReadOnly(true));
		addSlot(new InventorySlot(174,57,this).setReadOnly(true));
		addSlot(new InventorySlot(192,57,this).setReadOnly(true));
		itemBurnTime = 40;
	}

	@Override
	protected int getFlameX() {
		return 63;
	}

	@Override
	protected int getFlameY() {
		return 54;
	}

	@Override
	protected int getArrowX() {
		return 79;
	}

	@Override
	protected int getArrowY() {
		return 52;
	}

	@Override
	protected String getName() {
		return "Diamond Furnace";
	}

	@Override
	protected Texture getBackground() {
		Texture t = new GenericTexture("plugins/ExtraFurnaces/diamondfurnace.png");
		t.setLeft(0).setTop(0).setWidth(216).setHeight(202);
		return t;
	}

	@Override
	public Point getInventoryOffset() {
		return new Point(28, 120);
	}
}
