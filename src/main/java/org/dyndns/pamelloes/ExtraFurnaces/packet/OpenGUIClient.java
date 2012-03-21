package org.dyndns.pamelloes.ExtraFurnaces.packet;

import org.dyndns.pamelloes.ExtraFurnaces.client.ExtraFurnacesClient;
import org.dyndns.pamelloes.ExtraFurnaces.gui.InventoryWidget;
import org.dyndns.pamelloes.ExtraFurnaces.packet.OpenGUI.GUIType;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.io.AddonPacket;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

public class OpenGUIClient extends AddonPacket {
	private GUIType type;
	
	public GUIType getType() {
		return type;
	}
	
	@Override
	public void read(SpoutInputStream arg0) {
		int value = arg0.readInt();
		type = GUIType.values()[value];
	}
	
	public void setType(int value) {
		type = GUIType.values()[value];	
	}

	@Override
	public void run() {
		Addon addon = Spoutcraft.getAddonManager().getAddon("ExtraFurnaces");
		if(addon==null) throw new RuntimeException("ExtraFurnaces is somehow not loaded... how is this code running?");
		if(!(addon instanceof ExtraFurnacesClient)) throw new RuntimeException("Extraurnaces is an imposter! Or this code is outdated. Possibly both. ;)");
		if(type == GUIType.CloseGui) SpoutClient.getHandle().displayPreviousScreen();
		else Spoutcraft.getActivePlayer().getMainScreen().attachPopupScreen(new InventoryWidget(type));
	}

	@Override
	public void write(SpoutOutputStream arg0) {
		arg0.writeInt(type.ordinal());
	}
}
