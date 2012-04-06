package org.dyndns.pamelloes.ExtraFurnaces.gui;

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.Label;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.gui.Texture;
import org.getspout.spoutapi.gui.Widget;
import org.getspout.spoutapi.gui.WidgetAnchor;
import org.getspout.spoutapi.player.SpoutPlayer;


public abstract class FurnaceGui extends InventoryGui{
	
    /** The number of ticks that the furnace will keep burning */
    public int furnaceBurnTime;

    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    public int currentItemBurnTime;

    /** The number of ticks that the current item has been cooking for */
    public int furnaceCookTime;
    
    public int itemBurnTime;

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    public int getCookProgressScaled(int par1) {
    	return (furnaceCookTime * par1) / itemBurnTime;
    }

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    public int getBurnTimeRemainingScaled(int par1) {
        if (currentItemBurnTime == 0) {
            currentItemBurnTime = itemBurnTime;
        }

        return (furnaceBurnTime * par1) / currentItemBurnTime;
    }

    /**
     * Returns true if the furnace is currently burning
     */
    public boolean isBurning() {
        return furnaceBurnTime > 0;
    }
    
    private Texture arrow, flame;
    private Label name, inv;
    
    protected abstract int getFlameX();
    protected abstract int getFlameY();
    protected abstract int getArrowX();
    protected abstract int getArrowY();
    protected abstract String getName();

	public FurnaceGui(SpoutPlayer sp, int width, int height) {
		super(sp, width, height);
		flame = getBackground();
		flame.setLeft(width).setTop(12).setWidth(14).setHeight(2).setFixed(true);
		flame.setY(getFlameY() + 12).setX(getFlameX()).setPriority(RenderPriority.Lowest).setAnchor(WidgetAnchor.TOP_LEFT);
		arrow = getBackground();
		arrow.setLeft(width).setTop(14).setWidth(1).setHeight(16).setFixed(true);
		arrow.setX(getArrowX()).setY(getArrowY()).setPriority(RenderPriority.Lowest).setAnchor(WidgetAnchor.TOP_LEFT);
		name = new GenericLabel(getName());
		name.setTextColor(new Color(64,64,64)).setShadow(false).setX(60).setY(6).setWidth(-1).setHeight(-1).setPriority(RenderPriority.Low);
		inv = new GenericLabel("Inventory");
		inv.setTextColor(new Color(64,64,64)).setShadow(false).setX(8).setY(height - 94).setWidth(-1).setHeight(-1).setPriority(RenderPriority.Low);
	}
    
	@Override
	public void onTick() {
		if (isBurning()) {
			flame.setVisible(true);
			int l = getBurnTimeRemainingScaled(12);
			flame.setTop(12 - l).setHeight(l + 2).setY(getFlameY() - (getHeight()/2) + 12 - l);
		} else {
			flame.setVisible(false);
		}
		arrow.setWidth(getCookProgressScaled(24) + 1);
	}
	
	@Override
	public Widget[] getWidgets() {
		return new Widget[]{ name, inv, flame, arrow };
	}

	/*@Override
	protected void drawGuiContainerForegroundLayer() {
		Spoutcraft.getMinecraftFont().drawString(getName(), 60, 6, 0x404040);
		Spoutcraft.getMinecraftFont().drawString("Inventory", 8, (getHeight() - 96) + 2, 0x404040);
	}*/

}
