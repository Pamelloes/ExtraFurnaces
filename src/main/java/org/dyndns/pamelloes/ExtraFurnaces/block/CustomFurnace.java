package org.dyndns.pamelloes.ExtraFurnaces.block;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.dyndns.pamelloes.ExtraFurnaces.ExtraFurnaces;
import org.dyndns.pamelloes.ExtraFurnaces.data.CustomFurnaceData;
import org.dyndns.pamelloes.ExtraFurnaces.packet.OpenGUIServer;
import org.dyndns.pamelloes.ExtraFurnaces.packet.OpenGUI.GUIType;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.PopupScreen;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.io.AddonPacket;
import org.getspout.spoutapi.material.block.GenericCubeCustomBlock;
import org.getspout.spoutapi.player.SpoutPlayer;

public abstract class CustomFurnace extends GenericCubeCustomBlock {
	protected ExtraFurnaces plugin;
	
	public CustomFurnace(ExtraFurnaces plugin, String name, Texture tex, int[] idson,int[] idsoff) {
		super(plugin, name, Material.FURNACE.getId(), 0, new GenericCubeBlockDesign(plugin, tex, idsoff));
		this.plugin=plugin;
	}
	
	public abstract GUIType getGUIType();

	@Override
    public boolean onBlockInteract(World world, int x, int y, int z, SpoutPlayer player) {
		if(!player.isSpoutCraftEnabled()) {
			player.sendMessage(ChatColor.RED + "");
			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + " This is a special furnace and requires Spoutcraft");
			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + " for you to use it. You can get Spoutcraft at ");
			player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + " http://get.spout.org/index.php");
			player.sendMessage(ChatColor.RED + "");
			player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + " If you are using Spoutcraft, please inform your server admin");
			player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + " that they need to have " + ChatColor.GRAY + "\"AuthenticateSpoutcraft: true\"" + ChatColor.RED + "" + ChatColor.ITALIC + " in the");
			player.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + " server's Spout configuration.");
			player.sendMessage(ChatColor.RED + "");
			return true;
		}
		Map<String,String> addons = player.getAddons();
		if(addons == null) {
			// Window Title
			GenericLabel wintitle = new GenericLabel("Spoutcraft outdated!");
			wintitle.setX(158).setY(25);
			wintitle.setPriority(RenderPriority.Lowest);
			wintitle.setWidth(-1).setHeight(-1);
			
			// Content Title
			GenericLabel contitle = new GenericLabel("You need to have Spoutcraft 1248 or newer!");
			contitle.setX(105).setY(55);
			contitle.setPriority(RenderPriority.Lowest);
			contitle.setWidth(-1).setHeight(-1);
			contitle.setTextColor(new Color(1.0f,0.3f,0.3f));
			
			// Content
			GenericLabel content1 = new GenericLabel("The ExtraFurnaces addon (which allows you to use");
			content1.setX(91).setY(80);
			content1.setPriority(RenderPriority.Lowest);
			content1.setWidth(-1).setHeight(-1);
			GenericLabel content2 = new GenericLabel("the furnace) requires that you are using Spout-");
			content2.setX(91).setY(93);
			content2.setPriority(RenderPriority.Lowest);
			content2.setWidth(-1).setHeight(-1);
			GenericLabel content3 = new GenericLabel("craft 1248 or newer. If you are using an older");
			content3.setX(91).setY(106);
			content3.setPriority(RenderPriority.Lowest);
			content3.setWidth(-1).setHeight(-1);
			GenericLabel content4 = new GenericLabel("build then the server can't verify what addons");
			content4.setX(91).setY(119);
			content4.setPriority(RenderPriority.Lowest);
			content4.setWidth(-1).setHeight(-1);
			GenericLabel content5 = new GenericLabel("you are using and might send you invalid data");
			content5.setX(91).setY(132);
			content5.setPriority(RenderPriority.Lowest);
			content5.setWidth(-1).setHeight(-1);
			GenericLabel content6 = new GenericLabel("that will cause Spoutcraft to crash!");
			content6.setX(91).setY(145);
			content6.setPriority(RenderPriority.Lowest);
			content6.setWidth(-1).setHeight(-1);
			GenericLabel content7 = new GenericLabel("You can update Spoutcraft in the \"Options\" section");
			content7.setX(91).setY(158);
			content7.setPriority(RenderPriority.Lowest);
			content7.setWidth(-1).setHeight(-1);
			content7.setTextColor(new Color(1.0f,0.3f,0.3f));
			GenericLabel content8 = new GenericLabel("of the launcher!");
			content8.setX(91).setY(171);
			content8.setPriority(RenderPriority.Lowest);
			content8.setWidth(-1).setHeight(-1);
			content8.setTextColor(new Color(1.0f,0.3f,0.3f));

			// Border
			GenericTexture border = new GenericTexture("http://dl.dropbox.com/u/27507830/GuildCraft/Images/HUD/blue.png");
			border.setX(65).setY(20);
			border.setPriority(RenderPriority.High);
			border.setWidth(300).setHeight(200);
			border.setDrawAlphaChannel(true);

			// Background gradient
			GenericGradient gradient = new GenericGradient();
			gradient.setTopColor(new Color(0.25F, 0.25F, 0.25F, 1.0F));
			gradient.setBottomColor(new Color(0.35F, 0.35F, 0.35F, 1.0F));
			gradient.setWidth(300).setHeight(200);
			gradient.setX(65).setY(20);
			gradient.setPriority(RenderPriority.Highest);

			// Close button
			GenericButton close = new GenericButton("Close") {
				@Override
				public void onButtonClick(ButtonClickEvent event) {
					event.getPlayer().getMainScreen().getActivePopup().close();
				}
			};
			close.setX(188).setY(195);
			close.setWidth(60).setHeight(20);
			close.setPriority(RenderPriority.Lowest);
			
			player.getMainScreen().attachPopupScreen((PopupScreen) new GenericPopup().attachWidgets(plugin,wintitle,contitle,content1,content2,content3,content4,content5,content6,content7,content8,border,gradient,close));
			return true;
		}
		if(!addons.containsKey("ExtraFurnaces")) {
			// Window Title
			GenericLabel wintitle = new GenericLabel("You need ExtraFurnaces!");
			wintitle.setX(158).setY(25);
			wintitle.setPriority(RenderPriority.Lowest);
			wintitle.setWidth(-1).setHeight(-1);
			
			// Content Title
			GenericLabel contitle = new GenericLabel("This furnace requires the ExtraFurnaces addon!");
			contitle.setX(93).setY(55);
			contitle.setPriority(RenderPriority.Lowest);
			contitle.setWidth(-1).setHeight(-1);
			contitle.setTextColor(new Color(1.0f,0.3f,0.3f));
			
			// Content
			GenericLabel content1 = new GenericLabel("You can download the addon from:");
			content1.setX(91).setY(80);
			content1.setPriority(RenderPriority.Lowest);
			content1.setWidth(-1).setHeight(-1);
			GenericLabel content2 = new GenericLabel("http://goo.gl/7LalD");
			content2.setX(101).setY(93);
			content2.setPriority(RenderPriority.Lowest);
			content2.setWidth(-1).setHeight(-1);
			content2.setTextColor(new Color(0.4f,0.5f,1.0f));
			GenericLabel content3 = new GenericLabel("For more information on ExtraFurnaces, and for");
			content3.setX(91).setY(106);
			content3.setPriority(RenderPriority.Lowest);
			content3.setWidth(-1).setHeight(-1);
			GenericLabel content4 = new GenericLabel("help installing it, go to:");
			content4.setX(91).setY(119);
			content4.setPriority(RenderPriority.Lowest);
			content4.setWidth(-1).setHeight(-1);
			GenericLabel content5 = new GenericLabel("http://forums.spout.org/threads/287");
			content5.setX(101).setY(132);
			content5.setPriority(RenderPriority.Lowest);
			content5.setWidth(-1).setHeight(-1);
			content5.setTextColor(new Color(0.4f,0.5f,1.0f));

			// Border
			GenericTexture border = new GenericTexture("http://dl.dropbox.com/u/27507830/GuildCraft/Images/HUD/blue.png");
			border.setX(65).setY(20);
			border.setPriority(RenderPriority.High);
			border.setWidth(300).setHeight(200);
			border.setDrawAlphaChannel(true);

			// Background gradient
			GenericGradient gradient = new GenericGradient();
			gradient.setTopColor(new Color(0.25F, 0.25F, 0.25F, 1.0F));
			gradient.setBottomColor(new Color(0.35F, 0.35F, 0.35F, 1.0F));
			gradient.setWidth(300).setHeight(200);
			gradient.setX(65).setY(20);
			gradient.setPriority(RenderPriority.Highest);

			// Close button
			GenericButton close = new GenericButton("Close") {
				@Override
				public void onButtonClick(ButtonClickEvent event) {
					event.getPlayer().getMainScreen().getActivePopup().close();
				}
			};
			close.setX(188).setY(195);
			close.setWidth(60).setHeight(20);
			close.setPriority(RenderPriority.Lowest);
			
			player.getMainScreen().attachPopupScreen((PopupScreen) new GenericPopup().attachWidgets(plugin,wintitle,contitle,content1,content2,content3,content4,content5,border,gradient,close));
			return true;
		}
		AddonPacket packet = new OpenGUIServer(getGUIType());
		packet.send(player);
		CustomFurnaceData dat = (CustomFurnaceData) SpoutManager.getChunkDataManager().getBlockData("ExtraFurnaces", world, x, y, z);
		dat.onPlayerOpenFurnace(player);
		ExtraFurnaces.map.put(player, dat);
		return true;
    }

	@Override
    public void onBlockDestroyed(World world, int x, int y, int z) {
		CustomFurnaceData dat = (CustomFurnaceData) SpoutManager.getChunkDataManager().getBlockData("ExtraFurnaces", world, x, y, z);
		if(dat==null)  return;
		OpenGUIServer close = new OpenGUIServer();
		close.setType(GUIType.CloseGui.ordinal());
		Iterator<Entry<SpoutPlayer,CustomFurnaceData>> iter = ExtraFurnaces.map.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<SpoutPlayer,CustomFurnaceData> entry = iter.next();
			if(entry.getValue().equals(dat)) {
				close.send(entry.getKey());
				iter.remove();
			}
		}
		SpoutManager.getChunkDataManager().removeBlockData("ExtraFurnaces", world, x, y, z);
		Location loc = new Location(world,x,y,z);
		for(int i = 0; i < dat.getSizeInventory(); i++) if(dat.getStackInSlot(i)!=null) world.dropItem(loc, dat.getStackInSlot(i));
        HandlerList.unregisterAll(dat);
	}
}
