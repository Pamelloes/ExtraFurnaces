package org.dyndns.pamelloes.SpoutFurnaces.data;

import org.dyndns.pamelloes.SpoutFurnaces.SpoutFurnaces;
import org.dyndns.pamelloes.SpoutFurnaces.data.OpenGUI.GUIType;
import org.getspout.spoutapi.io.AddonPacket;
import org.getspout.spoutapi.io.SpoutInputStream;
import org.getspout.spoutapi.io.SpoutOutputStream;
import org.getspout.spoutapi.player.SpoutPlayer;

public class OpenGUIServer extends AddonPacket {
	private GUIType type;
	
	public OpenGUIServer(GUIType type) {
		this.type=type;
	}
	
	public OpenGUIServer() {
		this(null);
	}
	
	public GUIType getType() {
		return type;
	}
	
	public void setType(int value) {
		type = GUIType.values()[value];	
	}
	
	@Override
	public void read(SpoutInputStream arg0) {
		int value = arg0.readInt();
		type = GUIType.values()[value];
	}

	@Override
	public void run(SpoutPlayer arg0) {
		if(!type.equals(GUIType.CloseGui)) return;
		SpoutFurnaces.map.get(arg0).onPlayerCloseFurnace(arg0);
		SpoutFurnaces.map.remove(arg0);
	}

	@Override
	public void write(SpoutOutputStream arg0) {
		arg0.writeInt(type.ordinal());
	}
}
