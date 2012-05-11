package org.dyndns.pamelloes.ExtraFurnaces.gui;

import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.gui.GenericSlot;
import org.getspout.spoutapi.gui.Slot;

public class InventorySlot extends GenericSlot {
	private boolean readonly = false;
	private InventoryGui gui;
	private int timer = 0;
	
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
		return true;
	}
	
	@Override
	public boolean onItemPut(ItemStack i) {
		if(readonly) return false;
		return true;
	}
	
	@Override
	public boolean onItemExchange(ItemStack i, ItemStack j) {
		return true;
	}
	
	@Override
	public Slot setItem(ItemStack is) {
		return setItem(is, true);
	}
	
	public Slot setItem(ItemStack is, boolean update) {
		if(update) gui.updateContents(this, is);
		return super.setItem(is);
	}
	
	@Override
	public void onTick() {
		super.onTick();
		timer=(timer+1)%10;
		if(timer == 0) {
			int raw = gui.getIndex(this);
			int slot = gui.convertSlot(raw);
			ItemStack contents = null;
			if(slot == raw) contents = gui.getPlayer().getInventory().getItem(slot);
			else contents = gui.getBottomInventory().getItem(slot - 9);
			if(getItem().equals(contents)) return;
			setItem(contents);
		}
	}
}
