package org.dyndns.pamelloes.ExtraFurnaces.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.minecraft.server.Block;
import net.minecraft.server.FurnaceRecipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.dyndns.pamelloes.ExtraFurnaces.ExtraFurnaces;
import org.dyndns.pamelloes.ExtraFurnaces.block.CraftSpecialBlock;
import org.dyndns.pamelloes.ExtraFurnaces.block.CustomFurnaceBlockState;
import org.dyndns.pamelloes.ExtraFurnaces.gui.FurnaceGui;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.ServerTickEvent;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.player.SpoutPlayer;

@SuppressWarnings("serial")
public abstract class CustomFurnaceData extends CraftInventory implements Serializable, Listener, FurnaceInventory {
    protected transient ItemStack furnaceItemStacks[];
	protected transient List<SpoutPlayer> players = new ArrayList<SpoutPlayer>();

    /** The number of ticks that the furnace will keep burning */
    public int furnaceBurnTime;

    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    public int currentItemBurnTime;

    /** The number of ticks that the current item has been cooking for */
    public int furnaceCookTime;
    
    private int x,y,z;
    private UUID world;

    public CustomFurnaceData(int slots, UUID world, int x, int y, int z) {
    	super(null);
        furnaceItemStacks = new ItemStack[slots];
        furnaceBurnTime = 0;
        currentItemBurnTime = 0;
        furnaceCookTime = 0;
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        Bukkit.getPluginManager().getPlugin("ExtraFurnaces").getServer().getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("ExtraFurnaces"));
    }

    /**
     * Returns the number of slots in the inventory.
     */
	@Override
    public int getSize() {
        return furnaceItemStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1) {
        return furnaceItemStacks[par1];
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int slot, ItemStack is) {
        furnaceItemStacks[slot] = is;

        if (is != null && is.getAmount() > 64) is.setAmount(64);
        if (is != null && (is.getTypeId() == 0 || is.getAmount() == 0)) furnaceItemStacks[slot] = null;
        
        updateInventory();
    }

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    public int getCookProgressScaled(int par1) {
        return (furnaceCookTime * par1) / 200;
    }

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    public int getBurnTimeRemainingScaled(int par1) {
        if (currentItemBurnTime == 0) {
            currentItemBurnTime = 200;
        }

        return (furnaceBurnTime * par1) / currentItemBurnTime;
    }

    /**
     * Returns true if the furnace is currently burning
     */
    public boolean isBurning() {
        return furnaceBurnTime > 0;
    }
    
    protected abstract int getFuelIndex();
    protected abstract int getBurnIndex();
    protected abstract int getResultIndex();
    protected int getItemSmeltTime() {
    	return 200;//1600 = coal so 1 coal = 8
    	//150 = iron furnace
    	//80 = gold furnace
    	//40 = diamond furnace
    }
    protected abstract CustomBlock getBlock(boolean burning);
    
    /**
     * Updates the furnace.
     * @return true if there has been a change in the inventory.
     */
    public boolean update() {
    	if(Bukkit.getWorld(world).getBlockAt(x, y, z).getData() != 3) Bukkit.getWorld(world).getBlockAt(x, y, z).setData((byte) 3);
        boolean flag = furnaceBurnTime > 0;
        boolean flag1 = false;

        if (furnaceBurnTime > 0) {
            furnaceBurnTime--;
		}

		if (isBurning() && canSmelt()) {
			furnaceCookTime++;

			if (furnaceCookTime == getItemSmeltTime()) {
				furnaceCookTime = 0;
				smeltItem();
				flag1 = true;
			}
		} else {
			furnaceCookTime = 0;
		}
        
		if (furnaceBurnTime <= 0 && canSmelt() && furnaceItemStacks[getFuelIndex()] != null) {
            FurnaceBurnEvent furnaceBurnEvent = new FurnaceBurnEvent(new CraftSpecialBlock(Bukkit.getWorld(world).getBlockAt(x, y, z)), furnaceItemStacks[getFuelIndex()], getItemBurnTime(furnaceItemStacks[getFuelIndex()]));
            Bukkit.getPluginManager().callEvent(furnaceBurnEvent);

            if (furnaceBurnEvent.isCancelled()) {
                return flag1;
            }
            
			currentItemBurnTime = furnaceBurnEvent.getBurnTime();
			furnaceBurnTime += currentItemBurnTime;
			if (furnaceBurnTime > 0) {
				flag1 = true;
				if (furnaceItemStacks[getFuelIndex()] != null) {
					furnaceItemStacks[getFuelIndex()].setAmount(furnaceItemStacks[getFuelIndex()].getAmount()-1);

					if (furnaceItemStacks[getFuelIndex()].getAmount() == 0) furnaceItemStacks[getFuelIndex()] = null;
				}
			}
		}

		if (flag != (furnaceBurnTime > 0)) {
			flag1 = true;
			CustomBlock block = getBlock(isBurning());
			Bukkit.getWorld(world).getBlockAt(x, y, z).setTypeIdAndData(block.getBlockId(), (byte) block.getBlockData(), true);
			SpoutManager.getMaterialManager().overrideBlock(Bukkit.getWorld(world), x, y, z, block);
    		SpoutManager.getChunkDataManager().setBlockData("ExtraFurnaces", Bukkit.getWorld(world), x, y, z, CustomFurnaceData.this);
		}

        return flag1;
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt() {
        if (furnaceItemStacks[getBurnIndex()] == null) {
            return false;
        } else {
            ItemStack itemstack = getResult(furnaceItemStacks[getBurnIndex()]);
            return itemstack == null ? false : (furnaceItemStacks[getResultIndex()] == null ? true : (!furnaceItemStacks[getResultIndex()].getType().equals(itemstack.getType()) ? false : (furnaceItemStacks[getResultIndex()].getAmount() + itemstack.getAmount() <= 64 && furnaceItemStacks[getResultIndex()].getAmount() < furnaceItemStacks[getResultIndex()].getMaxStackSize() ? true : furnaceItemStacks[getResultIndex()].getAmount() + itemstack.getAmount() <= itemstack.getMaxStackSize())));
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem() {
        if (!canSmelt()) return;

        ItemStack result = getResult(furnaceItemStacks[getBurnIndex()]);
        
        FurnaceSmeltEvent furnaceSmeltEvent = new FurnaceSmeltEvent(new CraftSpecialBlock(Bukkit.getWorld(world).getBlockAt(x, y, z)), furnaceItemStacks[getBurnIndex()], result);
        Bukkit.getPluginManager().callEvent(furnaceSmeltEvent);

        if (furnaceItemStacks[getResultIndex()] == null) furnaceItemStacks[getResultIndex()] = result.clone();
        else if (furnaceItemStacks[getResultIndex()].getType().equals(result.getType())) furnaceItemStacks[getResultIndex()].setAmount(furnaceItemStacks[getResultIndex()].getAmount() + 1);

        furnaceItemStacks[getBurnIndex()].setAmount(furnaceItemStacks[getBurnIndex()].getAmount()-1);

        if (furnaceItemStacks[getBurnIndex()].getAmount() <= 0) furnaceItemStacks[getBurnIndex()] = null;
    }

	/**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    protected int getItemBurnTime(ItemStack item) {
        if (item == null) return 0;
        int id = item.getTypeId();
        if (id < 256 && Block.byId[id].material == net.minecraft.server.Material.WOOD) return 300;
        if (id == Material.STICK.getId()) return 100;
        if (id == Material.COAL.getId()) return 1600;
        if (id == Material.LAVA_BUCKET.getId()) return 20000;
        if (id == Material.SAPLING.getId()) return 100;
        if (id == Material.BLAZE_ROD.getId()) return 2400;
        return 0;
    }
    
    protected ItemStack getResult(ItemStack in) {
    	Method m1 = null;
    	Method m2 = null;
    	try {
    		m1 = FurnaceRecipes.class.getDeclaredMethod("getResult",int.class);
    	} catch(Exception e) { }
    	try {
			m2 = FurnaceRecipes.class.getDeclaredMethod("getResult",net.minecraft.server.ItemStack.class);
		} catch (Exception e) { }
		if(m1 != null) {
			try {
				net.minecraft.server.ItemStack is = (net.minecraft.server.ItemStack) m1.invoke(FurnaceRecipes.getInstance(), in.getTypeId());
				return is == null ? null : new CraftItemStack(is);
			} catch (Exception e) { }
		}
		if(m2 != null) {
			try {
				net.minecraft.server.ItemStack is = (net.minecraft.server.ItemStack) m2.invoke(FurnaceRecipes.getInstance(), new CraftItemStack(in).getHandle());
				return is == null ? null : new CraftItemStack(is);
			} catch (Exception e) { }
		}
		return null;
    }
    
    public void onPlayerOpenFurnace(SpoutPlayer player) {
    	players.add(player);
    	updateInventory();
    }
    
    public void onPlayerCloseFurnace(SpoutPlayer player) {
    	players.remove(player);
    }
    
    @EventHandler
    public abstract void onServerTick(ServerTickEvent e);
    
    private void writeObject(ObjectOutputStream out) throws IOException {
    	out.defaultWriteObject();
    	out.writeInt(furnaceItemStacks.length);
    	for(int i = 0; i < furnaceItemStacks.length; i++) {
    		if(furnaceItemStacks[i]!=null) {
	    		out.writeInt(furnaceItemStacks[i].getTypeId());
	    		out.writeShort(furnaceItemStacks[i].getDurability());
	    		out.writeShort((short)furnaceItemStacks[i].getAmount());
    		} else {
	    		out.writeInt(0);
	    		out.writeShort(0);
	    		out.writeShort(0);
    		}
    	}
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    	in.defaultReadObject();
    	int amount = in.readInt();
    	furnaceItemStacks = new ItemStack[amount];
    	for(int i = 0; i < furnaceItemStacks.length; i++) {
    		furnaceItemStacks[i] = new ItemStack(Material.AIR);
    		furnaceItemStacks[i].setTypeId(in.readInt());
    		furnaceItemStacks[i].setDurability(in.readShort());
    		furnaceItemStacks[i].setAmount(in.readShort());
    		if(furnaceItemStacks[i].getTypeId()==0) furnaceItemStacks[i]=null;
    	}
    	players = new ArrayList<SpoutPlayer>();
        Bukkit.getPluginManager().getPlugin("Spout").getServer().getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("Spout"));
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Spout"), new Runnable() {
        	public void run() {
        		SpoutManager.getChunkDataManager().setBlockData("ExtraFurnaces", Bukkit.getWorld(world), x, y, z, CustomFurnaceData.this);
        	}
        }, 1L);
    }
	
    protected void updateRange(int max, int min) {
    	updateRange(max,min, true);
    }
    
	protected void updateRange(int max, int min, boolean fillLast) {
		for(int t = 0; fillLast ? (t < max - min) && (furnaceItemStacks[max] == null) : t < 1; t++) {
			for(int i = max; i > min; i--) {
				if(furnaceItemStacks[i] == null && furnaceItemStacks[i-1] != null) {
					furnaceItemStacks[i] = furnaceItemStacks[i-1];
					furnaceItemStacks[i-1] = null;
				}
			}
		}
	}
	
	protected void updateData() {
		for(SpoutPlayer p : players) {
			FurnaceGui gui = ExtraFurnaces.guimap.get(p);
			if(gui==null) continue;
			gui.currentItemBurnTime=currentItemBurnTime;
			gui.furnaceBurnTime=furnaceBurnTime;
			gui.furnaceCookTime=furnaceCookTime;
		}
	}
	
	protected void updateInventory() {
		for(SpoutPlayer p : players) {
			FurnaceGui gui = ExtraFurnaces.guimap.get(p);
			if(gui==null) continue;
			for(int i = 0; i < furnaceItemStacks.length; i++) {
				if (furnaceItemStacks[i] == null) gui.clearContents(i + 36, false);
				else gui.setContents(i + 36, furnaceItemStacks[i], false);
			}
		}
	}

	@Override
	public int getMaxStackSize() {
		return 64;
	}

	@Override
	public void setMaxStackSize(int maxStackSize) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<HumanEntity> getViewers() {
		return new ArrayList<HumanEntity>() {{
			addAll(players);
		}};
	}

	@Override
	public InventoryType getType() {
		return InventoryType.FURNACE;
	}

	@Override
	public Furnace getHolder() {
		return new CustomFurnaceBlockState(Bukkit.getWorld(world).getBlockAt(x, y, z));
	}
	
	//This method has to be overriden because in the original implementation, it uses a few
	//private methods that need to be changed (which, as I'm sure you know, is impossible)
	@Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        /* TODO: some optimization
         *  - Create a 'firstPartial' with a 'fromIndex'
         *  - Record the lastPartial per Material
         *  - Cache firstEmpty result
         */

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            while (true) {
                // Do we already have a stack of it?
                int firstPartial = firstPartial(item);

                // Drat! no partial stack
                if (firstPartial == -1) {
                    // Find a free spot!
                    int firstFree = firstEmpty();

                    if (firstFree == -1) {
                        // No space at all!
                        leftover.put(i, item);
                        break;
                    } else {
                        // More than a single stack!
                        if (item.getAmount() > getMaxStackSize()) {
                            CraftItemStack stack = new CraftItemStack(item.getTypeId(), getMaxStackSize(), item.getDurability());
                            stack.addUnsafeEnchantments(item.getEnchantments());
                            setItem(firstFree, stack);
                            item.setAmount(item.getAmount() - getMaxStackSize());
                        } else {
                            // Just store it
                            setItem(firstFree, item);
                            break;
                        }
                    }
                } else {
                    // So, apparently it might only partially fit, well lets do just that
                    ItemStack partialItem = getItem(firstPartial);

                    int amount = item.getAmount();
                    int partialAmount = partialItem.getAmount();
                    int maxAmount = partialItem.getMaxStackSize();

                    // Check if it fully fits
                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        break;
                    }

                    // It fits partially
                    partialItem.setAmount(maxAmount);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }
        return leftover;
    }

	@Override
	public String getTitle() {
		return getName();
	}
	
	@Override
	public ItemStack getItem(int id) {
		return furnaceItemStacks[id];
	}
	
	@Override
	public void setItem(int id, ItemStack is) {
		furnaceItemStacks[id] = (is == null || is.getAmount() == 0 || is.getTypeId() == 0) ? null : is;
	}
	
	@Override
	public ItemStack[] getContents() {
		return furnaceItemStacks;
	}

	@Override
	public void setContents(ItemStack[] contents) {
        if (furnaceItemStacks.length < contents.length) throw new IllegalArgumentException("Invalid inventory size; expected " + furnaceItemStacks.length + " or less");
        
		for (int i = 0; i < furnaceItemStacks.length; i++) {
			if (i >= contents.length) {
	            furnaceItemStacks[i] = null;
	        } else {
	            furnaceItemStacks[i] = contents[i];
	        }
	    }
	}
	
	public ItemStack getFuel() {
		return furnaceItemStacks[getFuelIndex()];
	}
	
	public ItemStack getResult() {
		return furnaceItemStacks[getResultIndex()];
	}
	
	public ItemStack getSmelting() {
		return furnaceItemStacks[getBurnIndex()];
	}
	
	public void setFuel(ItemStack fuel) {
		setItem(getFuelIndex(), fuel);
	}
	
	public void setResult(ItemStack result) {
		setItem(getResultIndex(), result);
	}
	
	public void setSmelting(ItemStack smelting) {
		setItem(getBurnIndex(), smelting);
	}
}
