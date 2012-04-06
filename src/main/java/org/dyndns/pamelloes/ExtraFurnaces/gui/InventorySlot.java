package org.dyndns.pamelloes.ExtraFurnaces.gui;

import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.gui.GenericSlot;

public class InventorySlot extends GenericSlot {
	private boolean readonly = false;
	private InventoryGui gui;
	
	public InventorySlot(int xpos, int ypos, InventoryGui gui) {
		this.gui = gui;
		
		setMargin(ypos, 0, 0, xpos);//for containers
		setX(xpos).setY(ypos);//for everything else
		setFixed(true).setWidth(16).setHeight(16);
	}
	
	public boolean isReadOnly() {
		return readonly;
	}
	
	/**
	 * If a slot is read only, items can not be dragged into it. setContents() still
	 * works.
	 */
	public InventorySlot setReadOnly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}
	
	@Override
	public boolean onItemTake(ItemStack i) {
		gui.clearContents(this);
		return true;
	}
	
	@Override
	public boolean onItemPut(ItemStack i) {
		if(readonly) return false;
		gui.setContents(this, i);
		return true;
	}
	
	@Override
	public boolean onItemExchange(ItemStack i, ItemStack j) {
		gui.setContents(this, j);
		return true;
	}
}
