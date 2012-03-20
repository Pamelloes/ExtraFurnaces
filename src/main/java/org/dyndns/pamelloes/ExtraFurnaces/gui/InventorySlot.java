package org.dyndns.pamelloes.ExtraFurnaces.gui;

import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

public class InventorySlot {
	private int x,y;
	private ItemStack contents;
	private net.minecraft.src.ItemStack mccontents;
	private boolean readonly = false;
	
	public InventorySlot(int xpos, int ypos) {
		x = xpos;
		y = ypos;
		mccontents = new net.minecraft.src.ItemStack(0,1,0);
		contents = new ItemStack(MaterialData.air,1);
	}
	
	public void setContents(ItemStack contents) {
		this.contents = contents;
		mccontents.itemID = contents.getTypeId();
		mccontents.stackSize = contents.getAmount();
		mccontents.setItemDamage(contents.getDurability());
	}
	
	public ItemStack getContents() {
		return contents;
	}
	
	public void setContentsMC(net.minecraft.src.ItemStack mccontents) {
		this.mccontents = mccontents;
		contents.setAmount(mccontents.stackSize);
		contents.setDurability((short) mccontents.getItemDamage());
		contents.setTypeId(mccontents.itemID);
	}
	
	public net.minecraft.src.ItemStack getContentsMC() {
		return mccontents;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
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
}
