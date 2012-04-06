package org.dyndns.pamelloes.ExtraFurnaces.gui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.dyndns.pamelloes.ExtraFurnaces.ExtraFurnaces;
import org.getspout.spoutapi.gui.ContainerType;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;

public abstract class InventoryGui extends GenericContainer {
	private SpoutPlayer sp;
	
	private int width;
	private int height;
	
	private PlayerInventory inventory;
	
	private List<InventorySlot> slots = new ArrayList<InventorySlot>();
	
	public InventoryGui(SpoutPlayer p, int width, int height) {
		this.width = width;
		this.height = height;
		sp = p;
		inventory = p.getInventory();
		for(int i = 0; i < 36; i++) {
			Point loc = getSlotLocation(i);
			InventorySlot s = new InventorySlot((int) loc.getX(), (int) loc.getY(), this);
			s.setItem(inventory.getItem(i));
			addSlot(s);
		}
	}
	
	public void makeGui() {
		setLayout(ContainerType.OVERLAY).setAnchor(WidgetAnchor.CENTER_CENTER).setWidth(width).setHeight(height).setX(-width/2).setY(-height/2);
		addChild(getBackground().setPriority(RenderPriority.High));
		for(int i = 0; i < slots.size(); i++) addChild(slots.get(i));
		for(Widget w : getWidgets()) addChild(w);
		sp.getMainScreen().attachPopupScreen((PopupScreen) new GenericPopup() {
			@Override
			public void handleItemOnCursor(ItemStack is) {
				if(is.getTypeId() == 0 || is.getAmount() == 0) return;
				eject(is);
			}
		}.attachWidget(Bukkit.getPluginManager().getPlugin("ExtraFurnaces"), this));
	}
	
	protected abstract Texture getBackground();
	
	/**
	 * This method returns an offset to the top-left corner of the standard 36 inventory slots.
	 */
	protected abstract Point getInventoryOffset();

	private Point getSlotLocation(int index) {
		if(index < 0 || index > 36) throw new IllegalArgumentException("The inventory only takes up slots 0 to " + (35 + slots.size()) + "!");
		
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
		Point result = getInventoryOffset();
		result.translate(ax, ay);
		return result;
	}
	
	public ItemStack getContents(int id) {
		if(id < 36) return inventory.getItem(id);
		return slots.get(id - 36).getItem();
	}
	
	/**
	 * Change an inventory slot.
	 * @param id The slot to update
	 * @param contents The contents of the slot to change
	 */
	public void setContents(InventorySlot slot, ItemStack contents) {
		int id = slots.indexOf(slot);
		if(id >= 0) setContents(id, contents);
	}
	
	public void setContents(int id, ItemStack contents) {
		setContents(id, contents, true);
	}
	
	public void setContents(int id, ItemStack contents, boolean update) {
		slots.get(id).setItem(contents);
		if(!update) return;
		if(id < 36) {
			inventory.setItem(id, contents);
		} else {
			ExtraFurnaces.datamap.get(sp).setInventorySlotContents(id - 36, contents);
		}
	}
	
	/**
	 * Clears an inventory slot
	 * @param id The slot to update
	 */
	public void clearContents(InventorySlot slot) {
		int id = slots.indexOf(slot);
		if(id >= 0) clearContents(id);
	}
	
	public void clearContents(int id) {
		clearContents(id, true);
	}
	
	public void clearContents(int id, boolean update) {
		slots.get(id).setItem(new ItemStack(0));
		if(!update) return;
		if(id < 36) inventory.clear(id);
		else ExtraFurnaces.datamap.get(sp).setInventorySlotContents(id - 36, null);
	}
	
	public void eject(ItemStack is) {
		Location l = sp.getLocation();
		l.getWorld().dropItem(l, is);
	}
	
	/**
	 * Adds a new InventorySlot to the GUI. A slot can't be added twice.
	 * @param slot The InventorySlot to add.
	 * @return The Slot's ID for future reference.
	 */
	protected int addSlot(InventorySlot slot) {
		if(slots.contains(slot)) throw new IllegalArgumentException("You can't register a slot twice!");
		slots.add(slot);
		return slots.indexOf(slot) + 35;
	}
	
	protected boolean isReadOnly(int slot) {
		if(slot < 36) return false;
		return slots.get(slot - 36).isReadOnly();
	}
	
	public int getGuiWidth() {
		return width;
	}
	public int getGuiHeight() {
		return height;
	}
	
	public void onClose() {
		ExtraFurnaces.datamap.remove(sp);
		ExtraFurnaces.guimap.remove(sp);
	}
	
	public Widget[] getWidgets() { return new Widget[0]; }
}
