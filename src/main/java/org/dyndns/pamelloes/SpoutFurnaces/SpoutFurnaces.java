package org.dyndns.pamelloes.SpoutFurnaces;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.dyndns.pamelloes.SpoutFurnaces.block.DiamondFurnace;
import org.dyndns.pamelloes.SpoutFurnaces.block.GoldFurnace;
import org.dyndns.pamelloes.SpoutFurnaces.block.IronFurnace;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.block.design.GenericCubeBlockDesign;
import org.getspout.spoutapi.block.design.Texture;
import org.getspout.spoutapi.inventory.SpoutItemStack;
import org.getspout.spoutapi.inventory.SpoutShapedRecipe;
import org.getspout.spoutapi.material.CustomBlock;
import org.getspout.spoutapi.material.MaterialData;

public class SpoutFurnaces extends JavaPlugin {
	int[] furnaceoff = {0,1,1,2,1,0};
	int[] furnaceon = {0,4,4,4,3,0};
	int ironfurnaceincr = 0;
	int goldfurnaceincr = 5;
	int diamondfurnaceincr = 10;
	
	public static CustomBlock ironfurnace, goldfurnace, diamondfurnace;
	
	public void onEnable() {
		extractFile("moreFurnaces.png",true);
		registerItems();
		registerRecipes();
	}

	public void onDisable() {
		// TODO Auto-generated method stub
		
	}
	
	private void registerItems() {
		Texture texture = new Texture(this,"plugins/SpoutFurnaces/moreFurnaces.png",256,256,16);
		
		GenericCubeBlockDesign ironon = new GenericCubeBlockDesign(this,texture,incrementArray(furnaceon,ironfurnaceincr));
		GenericCubeBlockDesign ironoff = new GenericCubeBlockDesign(this,texture,incrementArray(furnaceoff,ironfurnaceincr));
		ironfurnace = new IronFurnace(this,ironon,ironoff);
		
		GenericCubeBlockDesign goldon = new GenericCubeBlockDesign(this,texture,incrementArray(furnaceon,goldfurnaceincr));
		GenericCubeBlockDesign goldoff = new GenericCubeBlockDesign(this,texture,incrementArray(furnaceoff,goldfurnaceincr));
		goldfurnace = new GoldFurnace(this,goldon,goldoff);
		
		GenericCubeBlockDesign diamondon = new GenericCubeBlockDesign(this,texture,incrementArray(furnaceon,diamondfurnaceincr));
		GenericCubeBlockDesign diamondoff = new GenericCubeBlockDesign(this,texture,incrementArray(furnaceoff,diamondfurnaceincr));
		diamondfurnace = new DiamondFurnace(this,diamondon,diamondoff);
	}
	
	private void registerRecipes() {
		ItemStack result = new SpoutItemStack(ironfurnace, 1);
		SpoutShapedRecipe recipe = new SpoutShapedRecipe(result);
		recipe.shape("AAA", "ABA", "AAA");
		recipe.setIngredient('A', MaterialData.ironIngot);
		recipe.setIngredient('B', MaterialData.furnace);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
		
		result = new SpoutItemStack(goldfurnace, 1);
		recipe = new SpoutShapedRecipe(result);
		recipe.shape("AAA", "ABA", "AAA");
		recipe.setIngredient('A', MaterialData.goldIngot);
		recipe.setIngredient('B', ironfurnace);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);

		result = new SpoutItemStack(diamondfurnace, 1);
		recipe = new SpoutShapedRecipe(result);
		recipe.shape("AAA", "ABA", "AAA");
		recipe.setIngredient('A', MaterialData.diamond);
		recipe.setIngredient('B', goldfurnace);
		SpoutManager.getMaterialManager().registerSpoutRecipe(recipe);
	}
	
	public static int[] incrementArray(final int[] array,final int increment) {
		int[] intarray = Arrays.copyOf(array, array.length);
		for(int i=0;i<array.length;i++) intarray[i]=array[i]+increment;
		return intarray;
	}
	
	/**
	 * Extract files from the plugin jar and optionally cache them on the client.
	 * @param regex a pattern of files to extract
	 * @param cache if any files found should be added to the Spout cache
	 * @return if any files were extracted
	 */
	public boolean extractFile(String regex, boolean cache) {
		boolean found = false;
		try {
			JarFile jar = new JarFile(getFile());
			for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
				JarEntry entry = (JarEntry) entries.nextElement();
				String name = entry.getName();
				if (name.matches(regex)) {
					if (!getDataFolder().exists()) {
						getDataFolder().mkdir();
					}
					try {
						File file = new File(getDataFolder(), name);
						if (!file.exists()) {
							InputStream is = jar.getInputStream(entry);
							FileOutputStream fos = new FileOutputStream(file);
							while (is.available() > 0) {
								fos.write(is.read());
							}
							fos.close();
							is.close();
							found = true;
						}
						if (cache && name.matches(".*\\.(txt|yml|xml|png|jpg|ogg|midi|wav|zip)$")) {
							SpoutManager.getFileManager().addToCache(this, file);
						}
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
		}
		return found;
	}
}
