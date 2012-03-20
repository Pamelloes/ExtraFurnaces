package org.dyndns.pamelloes.ExtraFurnaces.packet;

import org.getspout.spoutapi.io.AddonPacket;
import org.getspout.spoutapi.io.SpoutInputStream;
import org.getspout.spoutapi.io.SpoutOutputStream;
import org.getspout.spoutapi.player.SpoutPlayer;

public class ChangeDataServer extends AddonPacket {
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
		throw new RuntimeException("The ChangeData packet should never be sent from the client.");
	}

	@Override
	public void run(SpoutPlayer p) {
		throw new RuntimeException("The ChangeData packet should never be sent from the client.");
	}

	@Override
	public void write(SpoutOutputStream out) {
		out.writeInt(furnaceBurnTime);
		out.writeInt(currentItemBurnTime);
		out.writeInt(furnaceCookTime);
	}

}
