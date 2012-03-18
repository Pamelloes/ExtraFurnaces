package org.dyndns.pamelloes.SpoutFurnaces.gui;

import org.dyndns.pamelloes.SpoutFurnaces.data.OpenGUIClient;
import org.dyndns.pamelloes.SpoutFurnaces.data.OpenGUI.GUIType;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.event.Listener;
import org.spoutcraft.spoutcraftapi.event.Order;
import org.spoutcraft.spoutcraftapi.event.screen.ScreenCloseEvent;
import org.spoutcraft.spoutcraftapi.gui.GenericPopup;
import org.spoutcraft.spoutcraftapi.gui.WidgetAnchor;

public class InventoryWidget extends GenericPopup {
	private InventoryGui gui;
	
	private static Listener<ScreenCloseEvent> listener = null;

	public InventoryWidget(GUIType type) {
		if(listener == null) {
			listener = new Listener<ScreenCloseEvent>() {

				public void onEvent(ScreenCloseEvent arg0) {
					if (!(arg0.getScreen() instanceof InventoryWidget)) return;
					((InventoryWidget) arg0.getScreen()).onClose();
					FurnaceGui.current = null;
					OpenGUIClient ogc = new OpenGUIClient();
					ogc.setType(GUIType.CloseGui.ordinal());
					Spoutcraft.send(ogc);
				}
				
			};
			ScreenCloseEvent.handlers.register(listener, Order.Default, getAddon());
		}
		switch(type) {
		case IronFurnace:
			gui = new IronFurnaceGui(this);
			break;
		case GoldFurnace:
			gui = new GoldFurnaceGui(this);
			break;
		case DiamondFurnace:
			gui = new DiamondFurnaceGui(this);
			break;
		default:
			throw new RuntimeException("The server requested a GUI with an invalid id opened. The server or client is outdated.");
		}
		FurnaceGui.current = (FurnaceGui) gui;
		setBgVisible(false);
		setAnchor(WidgetAnchor.CENTER_CENTER);
	}

	public void render() {
		super.render();
		gui.drawScreen(getMouseX(), getMouseY());
	}
	
	public void onClose() {
		gui.onClose();
	}

}
