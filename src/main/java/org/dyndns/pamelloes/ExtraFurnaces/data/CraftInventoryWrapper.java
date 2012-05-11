package org.dyndns.pamelloes.ExtraFurnaces.data;

import org.bukkit.craftbukkit.inventory.CraftInventory;

/**
 * Because the serialization process requires the first un-serializable class in its hierarchy to have a no-argument constructor,
 * CustomFurnaceData can not directly extends CraftInventory, so this class CraftInventoryWrapper provides a subclass of CraftInventory
 * with a no-argument constructor.
 */
public class CraftInventoryWrapper extends CraftInventory {

	public CraftInventoryWrapper() {
		super(null);
	}
}
