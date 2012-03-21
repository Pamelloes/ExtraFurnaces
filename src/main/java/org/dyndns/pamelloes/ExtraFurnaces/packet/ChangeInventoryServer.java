package org.dyndns.pamelloes.ExtraFurnaces.packet;

import org.bukkit.inventory.ItemStack;
import org.dyndns.pamelloes.ExtraFurnaces.ExtraFurnaces;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.io.AddonPacket;
import org.getspout.spoutapi.io.SpoutInputStream;
import org.getspout.spoutapi.io.SpoutOutputStream;
import org.getspout.spoutapi.material.Material;
import org.getspout.spoutapi.material.MaterialData;
import org.getspout.spoutapi.player.SpoutPlayer;

public class ChangeInventoryServer extends AddonPacket {
	private int slot;
	private Material type;
	private int amount;
	private short damage;
	
	public void setData(int slot, ItemStack is) {
		this.slot = slot;
		type = new SpoutItemStack(is).getMaterial();
		amount = is.getAmount();
		damage = is.getDurability();
	}
	
	@Override
	public void read(SpoutInputStream arg0) {
		slot = arg0.readInt();
		type = arg0.readMaterial();
		amount = arg0.readInt();
		damage = arg0.readShort();
	}

	@Override
	public void write(SpoutOutputStream arg0) {
		arg0.writeInt(slot);
		arg0.writeMaterial(type);
		arg0.writeInt(amount);
		arg0.writeShort(damage);
	}

	@Override
	public void run(SpoutPlayer arg0) {
		if(type.equals(MaterialData.air)) {
			if(slot < 36) arg0.getInventory().clear(slot);
			else ExtraFurnaces.map.get(arg0).setInventorySlotContents(slot - 36, null);
			return;
		}
		ItemStack is = new SpoutItemStack(type, amount);
		is.setDurability(damage);
		if(slot < 0) arg0.getWorld().dropItem(arg0.getLocation(), is);
		else if(slot < 36) arg0.getInventory().setItem(slot, is);
		else ExtraFurnaces.map.get(arg0).setInventorySlotContents(slot-36, is);
	}

}
