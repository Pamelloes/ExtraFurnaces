package org.dyndns.pamelloes.ExtraFurnaces.gui;

import java.awt.Point;

import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.player.SpoutPlayer;


public class IronFurnaceGui extends FurnaceGui {

	public IronFurnaceGui(SpoutPlayer sp) {
		super(sp, 176, 166);
		InventorySlot in = new InventorySlot(56,17,this);
		InventorySlot fuel = new InventorySlot(56,53,this);
		InventorySlot out = new InventorySlot(116,35,this);
		out.setReadOnly(true);
		InventorySlot in2 = new InventorySlot(38,17,this);
		InventorySlot fuel2 = new InventorySlot(38,53,this);
		InventorySlot out2 = new InventorySlot(138,39,this);
		out2.setReadOnly(true);
		addSlot(in2);
		addSlot(in);
		addSlot(fuel2);
		addSlot(fuel);
		addSlot(out);
		addSlot(out2);
		itemBurnTime = 150;
	}

	@Override
	public Point getInventoryOffset() {
		return new Point(8, 84);
	}

	@Override
	protected int getFlameX() {
		return 56;
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
		return "Iron Furnace";
	}

	@Override
	protected Texture getBackground() {
		Texture t = new GenericTexture("plugins/ExtraFurnaces/ironfurnace.png");
		t.setLeft(0).setTop(0).setWidth(176).setHeight(166);
		return t;
	}
	
}
