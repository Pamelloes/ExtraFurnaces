package org.dyndns.pamelloes.ExtraFurnaces.gui;

import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.gui.PopupScreen;


public abstract class FurnaceGui extends InventoryGui{
	public static FurnaceGui current = null;
	
    /** The number of ticks that the furnace will keep burning */
    public int furnaceBurnTime;

    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    public int currentItemBurnTime;

    /** The number of ticks that the current item has been cooking for */
    public int furnaceCookTime;
    
    public int itemBurnTime;
    
    private String id;

	public FurnaceGui(PopupScreen parent, int width, int height, String id) {
		super(parent, width, height);
		this.id = id;
	}

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
    
    protected abstract int getFlameX();
    protected abstract int getFlameY();
    protected abstract int getArrowX();
    protected abstract int getArrowY();

	@Override
	protected void drawGuiContainerBackgroundLayer(int mouseX, int mouseY) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, TextureUtils.getInstance(id).getId());
		int j = getX();
		int k = getY();
		drawTexturedModalRect(j, k, 0, 0, getWidth(), getHeight());

		if (isBurning()) {
			int l = getBurnTimeRemainingScaled(12);
			drawTexturedModalRect(j + getFlameX(), (k + getFlameY() + 12) - l, getWidth(), 12 - l, 14, l + 2);
		}

		int i1 = getCookProgressScaled(24);
		drawTexturedModalRect(j + getArrowX(), k + getArrowY(), getWidth(), 14, i1 + 1, 16);
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		Spoutcraft.getMinecraftFont().drawString("Furnace", 60, 6, 0x404040);
		Spoutcraft.getMinecraftFont().drawString("Inventory", 8, (getHeight() - 96) + 2, 0x404040);
	}

}
