package org.dyndns.pamelloes.SpoutFurnaces.data;

import org.dyndns.pamelloes.SpoutFurnaces.gui.FurnaceGui;
import org.spoutcraft.spoutcraftapi.io.AddonPacket;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

public class ChangeDataClient extends AddonPacket {
    /** The number of ticks that the furnace will keep burning */
    public int furnaceBurnTime;

    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    public int currentItemBurnTime;

    /** The number of ticks that the current item has been cooking for */
    public int furnaceCookTime;
	
	@Override
	public void read(SpoutInputStream in) {
		furnaceBurnTime = in.readInt();
		currentItemBurnTime = in.readInt();
		furnaceCookTime = in.readInt();
	}

	@Override
	public void run() {
		FurnaceGui.current.currentItemBurnTime=currentItemBurnTime;
		FurnaceGui.current.furnaceBurnTime=furnaceBurnTime;
		FurnaceGui.current.furnaceCookTime=furnaceCookTime;
	}

	@Override
	public void write(SpoutOutputStream out) {
		throw new RuntimeException("The ChangeData packet should never be sent from the client.");
	}
}
