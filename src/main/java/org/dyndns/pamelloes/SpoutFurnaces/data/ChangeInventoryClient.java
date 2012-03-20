package org.dyndns.pamelloes.SpoutFurnaces.data;

import org.dyndns.pamelloes.SpoutFurnaces.gui.FurnaceGui;
import org.spoutcraft.client.inventory.CraftItemStack;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.io.AddonPacket;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;
import org.spoutcraft.spoutcraftapi.material.Material;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

public class ChangeInventoryClient extends AddonPacket {
	private int slot;
	private Material type;
	private int amount;
	private short damage;
	
	public void setData(int slot, ItemStack is) {
		this.slot = slot;
		type = is.getType();
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
	public void run() {
		if(FurnaceGui.current == null) return;
		if(type.equals(MaterialData.air)) FurnaceGui.current.clearContents(slot, false);
		else FurnaceGui.current.setContents(slot, new CraftItemStack(new net.minecraft.src.ItemStack(type.getRawId(), amount, damage)), false);
	}

	@Override
	public void write(SpoutOutputStream arg0) {
		arg0.writeInt(slot);
		arg0.writeMaterial(type);
		arg0.writeInt(amount);
		arg0.writeShort(damage);
	}

}
