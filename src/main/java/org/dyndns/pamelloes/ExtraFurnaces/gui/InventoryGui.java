package org.dyndns.pamelloes.ExtraFurnaces.gui;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.Tessellator;

import org.dyndns.pamelloes.ExtraFurnaces.packet.ChangeInventoryClient;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.client.gui.RenderItemCustom;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.gui.Keyboard;
import org.spoutcraft.spoutcraftapi.gui.MinecraftTessellator;
import org.spoutcraft.spoutcraftapi.gui.PopupScreen;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.inventory.PlayerInventory;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

public abstract class InventoryGui {
	protected final static MinecraftTessellator tessellator = Spoutcraft.getRenderDelegate().getTessellator();
	protected final RenderItemCustom render = new RenderItemCustom();
	
	private int xpos;
	private int ypos;
	private int width;
	private int height;
	
	private net.minecraft.src.ItemStack inventorySquare = new net.minecraft.src.ItemStack(0,0,0); //used to draw inventory.
	
	private PlayerInventory inventory;
	public PopupScreen parent;

	private boolean dragging;
	private net.minecraft.src.ItemStack dragmc = new net.minecraft.src.ItemStack(0,0,0);
	private ItemStack dragsc;
	private final ChangeInventoryClient cic = new ChangeInventoryClient();

	private long lastmousemove = 0;
	private double lastX, lastY;
	private boolean ldownp, rdownp;
	
	protected float zLevel = 0.0f;
	
	private List<InventorySlot> slots = new ArrayList<InventorySlot>();
	
	public InventoryGui(final PopupScreen parent, int width, int height) {
		this.parent = parent;
		this.width = width;
		this.height = height;
		inventory = Spoutcraft.getActivePlayer().getInventory();
		render.setRenderManager(RenderManager.instance);
	}
	
	public synchronized void drawScreen(int mouseX, int mouseY) {
		xpos = (Spoutcraft.getRenderDelegate().getScreenWidth() - width) / 2;
		ypos = (Spoutcraft.getRenderDelegate().getScreenHeight() - height) / 2;
		
		if(!ldownp && Mouse.isButtonDown(0)) toggleDrag(mouseX, mouseY, false);
		if(!rdownp && Mouse.isButtonDown(1)) toggleDrag(mouseX, mouseY, true);
		ldownp = Mouse.isButtonDown(0);
		rdownp = Mouse.isButtonDown(1);
		
		if(mouseX != lastX || mouseY != lastY) {
			lastmousemove = System.currentTimeMillis();
			lastX = mouseX;
			lastY = mouseY;
		}
		
		drawGuiContainerBackgroundLayer(mouseX, mouseY);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glTranslatef(xpos, ypos, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		int hoverslot = -1;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float)240 / 1.0F, (float)240 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		for(int slot = 0; slot < 36 + slots.size(); slot++)
		{
			Point pt = getItemLocation(slot);
			ItemStack is = getContents(slot);
			if(is.getTypeId() > 0) {
				render.zLevel = 100f;
				adaptStack(inventorySquare, is);
				render.renderItemIntoGUI(SpoutClient.getHandle().fontRenderer, SpoutClient.getHandle().renderEngine, inventorySquare, pt.x, pt.y);
				render.renderItemOverlayIntoGUI(SpoutClient.getHandle().fontRenderer, SpoutClient.getHandle().renderEngine, inventorySquare, pt.x, pt.y);
				render.zLevel = 0f;
			}
			
			if(getIsMouseOverSlot(slot, mouseX, mouseY)) {
				hoverslot = slot;
				GL11.glDisable(2896 /*GL_LIGHTING*/);
				GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
				drawGradientRect(pt.x, pt.y, pt.x + 16, pt.y + 16, 0x80ffffff, 0x80ffffff);
				GL11.glEnable(2896 /*GL_LIGHTING*/);
				GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
			}
		}

		//render item being dragged.
		if(dragging) {
			render.zLevel = 200F;
			render.renderItemIntoGUI(SpoutClient.getHandle().fontRenderer, SpoutClient.getHandle().renderEngine, dragmc, mouseX - 8 - xpos, mouseY - 8 - ypos);
			render.renderItemOverlayIntoGUI(SpoutClient.getHandle().fontRenderer, SpoutClient.getHandle().renderEngine, dragmc, mouseX - 8 - xpos, mouseY - 8 - ypos);
			render.zLevel = 0.0F;
		}
		
		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		drawGuiContainerForegroundLayer();
		
		if(!dragging && hoverslot >= 0 && getContents(hoverslot).getTypeId() > 0 && shouldShowTooltip()) {
			adaptStack(inventorySquare, getContents(hoverslot));
			@SuppressWarnings("unchecked")
			List<String> list = inventorySquare.getItemNameandInformation();
			org.spoutcraft.spoutcraftapi.material.Material item = MaterialData.getMaterial(inventorySquare.itemID, (short)(inventorySquare.getItemDamage()));
			String custom = item != null ? String.format(item.getName(), String.valueOf(inventorySquare.getItemDamage())) : null;
			if (custom != null && inventorySquare.itemID != MaterialData.potion.getRawId()) {
				list.set(0, custom);
			}
			if(list.size() > 0) {
				String tooltip = "";
				int lines = 0;
				for(int l4 = 0; l4 < list.size(); l4++) {
					String s = (String)list.get(l4);
					if(l4 == 0) {
						s = (new StringBuilder()).append("\247").append(Integer.toHexString(inventorySquare.getRarity().field_40535_e)).append(s).toString();
					} else {
						s = (new StringBuilder()).append("\2477").append(s).toString();
					}
					tooltip += s + "\n";
					lines++;
				}
				tooltip = tooltip.trim();
				drawTooltip(tooltip, (mouseX - xpos) + 8, mouseY - ypos - lines * 6);
				zLevel = 0.0F;
				render.zLevel = 0.0F;
			}
		}
		GL11.glPopMatrix();
		GL11.glEnable(2896 /*GL_LIGHTING*/);
		GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
	}
	
	protected abstract void drawGuiContainerBackgroundLayer(int mouseX, int mouseY);
	protected void drawGuiContainerForegroundLayer() { }
	
	/**
	 * This method returns an offset to the top-left corner of the standard 36 inventory slots.
	 */
	protected abstract Point getInventoryOffset();

	protected Point getItemLocation(int index) {
		if(index < 0 || index > 35 + slots.size()) throw new IllegalArgumentException("The inventory only takes up slots 0 to " + (35 + slots.size()) + "!");
		
		if(index < 36) {
			//calculate square in the inventory.
			int ix, iy;
			ix = index%9;
			if(index<9) iy=3;
			else iy = ( (index-ix) / 9) - 1;
			
			//calculate exact coordinates in the inventory.
			int ax,ay;
			ax = 18*ix;
			if(iy!=3) ay=18*iy;
			else ay = 18*3 + 4;
			Point result = getInventoryOffset();
			result.translate(ax, ay);
			return result;
		} else {
			InventorySlot slot = slots.get(index - 36);
			return new Point(slot.getX(), slot.getY());
		}
	}
	
	protected boolean getIsMouseOverSlot(int slot, double mouseX, double mouseY) {
		double mx = mouseX;
		double my = mouseY;
		mx -= xpos;
		my -= ypos;
		Point loc = getItemLocation(slot);
		return  mx >= loc.x - 1 && mx < loc.x + 16 + 1 && my >= loc.y - 1 && my < loc.y + 16 + 1;
	}
	
	protected int getSlotMouseIsOver(double mouseX, double mouseY) {
		for(int i = 0; i < 36 + slots.size(); i++) {
			if(getIsMouseOverSlot(i, mouseX, mouseY)) {
				return i;
			}
		}

		return -1;
	}
	
	public ItemStack getContents(int id) {
		if(id < 36) return inventory.getItem(id);
		return slots.get(id - 36).getContents();
	}
	
	public void setContents(int id, ItemStack contents) {
		setContents(id,contents,true);
	}
	
	/**
	 * Change an inventory slot.
	 * @param id The slot to update
	 * @param contents The contents of the slot to change
	 * @param update Whether or not to send notice to the server
	 */
	public void setContents(int id, ItemStack contents, boolean update) {
		if(id < 36) inventory.setItem(id, contents);
		else slots.get(id - 36).setContents(contents);
		if(!update) return;
    	cic.setData(id, contents);
		Spoutcraft.send(cic);
	}
	
	public void clearContents(int id) {
		clearContents(id,true);
	}
	
	/**
	 * Clears an inventory slot
	 * @param id The slot to update
	 * @param update Whether or not to send notice to the server
	 */
	public void clearContents(int id, boolean update) {
		if(id < 36) inventory.clear(id);
		else slots.get(id - 36).setContents(new ItemStack(MaterialData.air,1));
		if(!update) return;
		cic.setData(id, new ItemStack(MaterialData.air));
		Spoutcraft.send(cic);
	}
	
	public void ejectContents(ItemStack is) {
    	cic.setData(-1, is);
		Spoutcraft.send(cic);
	}

	protected boolean shouldShowTooltip() {
		return !ConfigReader.delayedTooltips || System.currentTimeMillis() - 500 > lastmousemove;
	}
	
    protected void drawGradientRect(int i, int j, int k, int l, int i1, int j1) {
        float f = (float)(i1 >> 24 & 0xff) / 255F;
        float f1 = (float)(i1 >> 16 & 0xff) / 255F;
        float f2 = (float)(i1 >> 8 & 0xff) / 255F;
        float f3 = (float)(i1 & 0xff) / 255F;
        float f4 = (float)(j1 >> 24 & 0xff) / 255F;
        float f5 = (float)(j1 >> 16 & 0xff) / 255F;
        float f6 = (float)(j1 >> 8 & 0xff) / 255F;
        float f7 = (float)(j1 & 0xff) / 255F;
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425 /*GL_SMOOTH*/);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBAFloat(f1, f2, f3, f);
        tessellator.addVertex(k, j, zLevel);
        tessellator.addVertex(i, j, zLevel);
        tessellator.setColorRGBAFloat(f5, f6, f7, f4);
        tessellator.addVertex(i, l, zLevel);
        tessellator.addVertex(k, l, zLevel);
        tessellator.draw();
        GL11.glShadeModel(7424 /*GL_FLAT*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
    }
	
	private void adaptStack(net.minecraft.src.ItemStack adabt, ItemStack from) {
		adabt.itemID = from.getTypeId();
		adabt.stackSize = from.getAmount();
		adabt.setItemDamage(from.getDurability());
	}
	
	/**
	 * Adds a new InventorySlot to the GUI. A slot can't be added twice.
	 * @param slot The InventorySlot to add.
	 * @return The Slot's ID for future reference.
	 */
	protected int addSlot(InventorySlot slot) {
		if(slots.contains(slot)) throw new IllegalArgumentException("You can't register a slot twice!");
		slots.add(slot);
		return slots.indexOf(slot) + 35;
	}
	
	protected boolean isReadOnly(int slot) {
		if(slot < 36) return false;
		return slots.get(slot - 36).isReadOnly();
	}
	
	public int getX() {
		return xpos;
	}
	public int getY() {
		return ypos;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(x + 0, y + height, zLevel, (float)(u + 0) * f, (float)(v + height) * f1);
		tessellator.addVertexWithUV(x + width, y + height, zLevel, (float)(u + width) * f, (float)(v + height) * f1);
		tessellator.addVertexWithUV(x + width, y + 0, zLevel, (float)(u + width) * f, (float)(v + 0) * f1);
		tessellator.addVertexWithUV(x + 0, y + 0, zLevel, (float)(u + 0) * f, (float)(v + 0) * f1);
		tessellator.draw();
	}

	protected void drawTooltip(String tooltip, int x, int y) {
		GL11.glPushMatrix();
		String lines[] = tooltip.split("\n");
		int tooltipWidth = 0;
		int tooltipHeight = 8 * lines.length + 3;
		for (String line : lines) {
			tooltipWidth = Math.max(SpoutClient.getHandle().fontRenderer.getStringWidth(line),
					tooltipWidth);
		}
		int offsetX = 0;
		if (x + tooltipWidth > width) {
			offsetX = -tooltipWidth - 11;
			if (offsetX + x < 0) {
				offsetX = -x;
			}
		}
		int offsetY = 0;
		if (y + tooltipHeight + 2 > height) {
			offsetY = -tooltipHeight;
			if (offsetY + y < 0) {
				offsetY = -y;
			}
		}

		x += 6;
		y -= 6;

		int j2 = 0;
		for (int k2 = 0; k2 < lines.length; k2++) {
			int i3 = SpoutClient.getHandle().fontRenderer.getStringWidth(lines[k2]);
			if (i3 > j2) {
				j2 = i3;
			}
		}

		int l2 = x + offsetX;
		int j3 = y + offsetY;
		int k3 = j2;
		int l3 = 8;
		if (lines.length > 1) {
			l3 += (lines.length - 1) * 10;
		}
		int i4 = 0xf0100010;
		drawGradientRect(l2 - 3, j3 - 4, l2 + k3 + 3, j3 - 3, i4, i4);
		drawGradientRect(l2 - 3, j3 + l3 + 3, l2 + k3 + 3, j3 + l3 + 4, i4, i4);
		drawGradientRect(l2 - 3, j3 - 3, l2 + k3 + 3, j3 + l3 + 3, i4, i4);
		drawGradientRect(l2 - 4, j3 - 3, l2 - 3, j3 + l3 + 3, i4, i4);
		drawGradientRect(l2 + k3 + 3, j3 - 3, l2 + k3 + 4, j3 + l3 + 3, i4, i4);
		int j4 = 0x505000ff;
		int k4 = (j4 & 0xfefefe) >> 1 | j4 & 0xff000000;
			drawGradientRect(l2 - 3, (j3 - 3) + 1, (l2 - 3) + 1, (j3 + l3 + 3) - 1, j4, k4);
			drawGradientRect(l2 + k3 + 2, (j3 - 3) + 1, l2 + k3 + 3, (j3 + l3 + 3) - 1, j4, k4);
			drawGradientRect(l2 - 3, j3 - 3, l2 + k3 + 3, (j3 - 3) + 1, j4, j4);
			drawGradientRect(l2 - 3, j3 + l3 + 2, l2 + k3 + 3, j3 + l3 + 3, k4, k4);

			GL11.glColor4f(1f, 1f, 1f, 1f);
			for (String line : lines) {
				Minecraft.theMinecraft.fontRenderer.drawStringWithShadow(line, l2, j3, -1);
				j3 += 10;
			}
			GL11.glPopMatrix();
	}
	
    private void toggleDrag(double mouseX, double mouseY, boolean right) {
    	//drop items
    	if(dragsc != null && (mouseX < xpos || mouseX > xpos + width || mouseY < ypos || mouseY > ypos + height)) {
    		ejectContents(dragsc);
    		dragsc = null;
    		dragging = false;
    		return;
    	}
    	int slot = getSlotMouseIsOver(mouseX,mouseY);
    	if(slot == -1) return;
    	//shift click
    	if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
	    	ItemStack move = getContents(slot);
	    	if(move.getTypeId()==0) return;
	    	int min,max;
	    	if(slot < 9 && slot >= 0) {
	    		min = 9;
	    		max = 36;
	    	} else if(slot < 36 && slot >=0) {
	    		min = 0;
	    		max = 9;
	    	} else {
	    		min = 9;
	    		max = 36;
	    	}
    		for(int i = min; i < max; i++) {
				ItemStack is = getContents(i);
				if(is.getTypeId() == 0) {
					if(move.getAmount() <= move.getMaxStackSize()) {
						setContents(i,move);
						clearContents(slot);
						return;
					} else {
						is = move.clone();
						is.setAmount(move.getMaxStackSize());
						setContents(i, is);
						move.setAmount(move.getAmount() - move.getMaxStackSize());
						if(move.getAmount()==0) return;
					}
				} else if(is.getType().equals(move.getType())) {
    				int total = is.getAmount() + move.getAmount();
    				if(total <= move.getMaxStackSize()) {
    					is.setAmount(total);
    					setContents(i,is);
    					clearContents(slot);
    					return;
    				} else {
    					is.setAmount(move.getMaxStackSize());
    					setContents(i,is);
						move.setAmount(total - move.getMaxStackSize());
						if(move.getAmount()==0) return;
    				}
    			}
    		}
    		setContents(slot,move);
    	//insert items
    	} else if(dragging) {
	    	ItemStack temp = getContents(slot);
	    	//air in slot
	    	if(temp.getTypeId() == 0) {
		    	if(isReadOnly(slot)) return;
		    	if(right) {
		    		temp = dragsc.clone();
		    		temp.setAmount(1);
		    		setContents(slot, temp);
		    		dragsc.setAmount(dragsc.getAmount()-1);
	    			dragmc.stackSize = dragsc.getAmount();
	    			if(dragsc.getAmount()==0) {
			    		dragsc = null;
			    		dragging = false;
	    			}
		    	} else {
		    		setContents(slot, dragsc);
		    		dragsc = null;
		    		dragging = false;
		    	}
			//same type in slot
	    	} else if (temp.getType().equals(dragsc.getType())) {
	    		if(isReadOnly(slot)) {
		    		int total = temp.getAmount() + dragsc.getAmount();
		    		if (total < dragsc.getMaxStackSize()) {
		    			dragsc.setAmount(total);
		    			dragmc.stackSize = dragsc.getAmount();
		    		}
	    		} else if(right) {
		    		int total = temp.getAmount() + 1;
		    		if (total <= dragsc.getMaxStackSize()) {
		    			temp.setAmount(total);
		    			setContents(slot, temp);
		    			dragsc.setAmount(dragsc.getAmount()-1);
		    			dragmc.stackSize = dragsc.getAmount();
		    			if(dragsc.getAmount()==0) {
				    		dragsc = null;
				    		dragging = false;
		    			}
		    		}
	    		} else {
		    		int total = temp.getAmount() + dragsc.getAmount();
		    		if (total <= dragsc.getMaxStackSize()) {
		    			temp.setAmount(total);
		    			setContents(slot, temp);
						dragsc = null;
						dragging = false;
		    		} else {
		    			temp.setAmount(dragsc.getMaxStackSize());
		    			setContents(slot, temp);
		    			dragsc.setAmount(total - dragsc.getMaxStackSize());
		    			dragmc.stackSize = dragsc.getAmount();
		    		}
	    		}
			//different type in slot
	    	} else {
	    		if(isReadOnly(slot)) return;
	    		setContents(slot, dragsc);
				dragsc = temp;
				adaptStack(dragmc, dragsc);
	    	}
	    //pick up items
	    } else {
	    	if(right) {
	    		dragsc = getContents(slot);
	    		if(dragsc.getTypeId() > 0) {
		    		ItemStack temp = dragsc.clone();
		    		temp.setAmount(temp.getAmount()/2);
		    		dragsc.setAmount(dragsc.getAmount()-temp.getAmount());
		    		setContents(slot, temp);
	    			adaptStack(dragmc, dragsc);
	    			dragging = true;
	    		} else {
	    			dragsc = null;
	    		}
	    	} else {
	    		dragsc = getContents(slot);
	    		if(dragsc.getTypeId() > 0) {
	    			adaptStack(dragmc, dragsc);
	    			clearContents(slot);
	    			dragging = true;
	    		} else {
	    			dragsc = null;
	    		}
	    	}
	    }
    }
    
    public void onClose() {
    	if(dragsc == null) return;
    	ejectContents(dragsc);
    }
}
