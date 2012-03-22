package org.dyndns.pamelloes.ExtraFurnaces.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.dyndns.pamelloes.ExtraFurnaces.gui.FurnaceGui;
import org.spoutcraft.spoutcraftapi.addon.java.JavaAddon;
import org.spoutcraft.spoutcraftapi.io.AddonPacket;

public class ExtraFurnacesClient extends JavaAddon {
	public boolean isEnabled = false;
	public static FurnaceGui current = null;

	@Override
	@SuppressWarnings("unchecked")
	public void onEnable() {
		try {
			ClassLoader cl = createClassLoader();
			Class<? extends AddonPacket> clazz = (Class<? extends AddonPacket>) cl.loadClass("org.dyndns.pamelloes.ExtraFurnaces.packet.OpenGUIClient");
			AddonPacket.register(clazz, "OGUI");
			clazz = (Class<? extends AddonPacket>) cl.loadClass("org.dyndns.pamelloes.ExtraFurnaces.packet.ChangeInventoryClient");
			AddonPacket.register(clazz, "CInv");
			clazz = (Class<? extends AddonPacket>) cl.loadClass("org.dyndns.pamelloes.ExtraFurnaces.packet.ChangeDataClient");
			AddonPacket.register(clazz, "CData");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		extractFile("ironfurnace.png");
		extractFile("goldfurnace.png");
		extractFile("diamondfurnace.png");
		
		isEnabled=true;
		
		
	}
	
	@Override
	public void onDisable() {
		isEnabled=false;
	}
	
	/**
	 * Extract files from the plugin jar and optionally cache them on the client.
	 * @param regex a pattern of files to extract
	 * @param cache if any files found should be added to the Spout cache
	 * @return if any files were extracted
	 */
	public boolean extractFile(String regex) {
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
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
		}
		return found;
	}
	
	private ClassLoader createClassLoader() {
		ClassLoader  /*AddonClassLoader*/ cl1 = getClass().getClassLoader();
		ClassLoader /*SC Class Loader*/ cl2  = cl1.getClass().getClassLoader();
		
		return DualClassLoader.getDualClassLoader(cl1,cl2, "org.dyndns.pamelloes.ExtraFurnaces.packet", "org.dyndns.pamelloes.ExtraFurnaces.gui");
	}
	
	public static double xscale,yscale;

}
