package org.dyndns.pamelloes.SpoutFurnaces.gui;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.imageio.ImageIO;

import org.dyndns.pamelloes.SpoutFurnaces.client.SpoutFurnacesClient;
import org.spoutcraft.spoutcraftapi.gui.PopupScreen;


public class GoldFurnaceGui extends FurnaceGui {

	public GoldFurnaceGui(PopupScreen parent) {
		super(parent, 216, 166, "GoldFurnace");
		//input
		addSlot(new InventorySlot(8,17));
		addSlot(new InventorySlot(26,17));
		addSlot(new InventorySlot(44,17));
		addSlot(new InventorySlot(62,17));
		//fuel
		addSlot(new InventorySlot(8,53));
		addSlot(new InventorySlot(26,53));
		addSlot(new InventorySlot(44,53));
		addSlot(new InventorySlot(62,53));
		//output
		addSlot(new InventorySlot(116,35).setReadOnly(true));
		addSlot(new InventorySlot(138,39).setReadOnly(true));
		addSlot(new InventorySlot(156,39).setReadOnly(true));
		addSlot(new InventorySlot(174,39).setReadOnly(true));
		addSlot(new InventorySlot(192,39).setReadOnly(true));
	}

	@Override
	public Point getInventoryOffset() {
		return new Point(28, 84);
	}
	
	static {
		try {
			// Load the image from the jar? :O

			// Instead of using a hardcoded .jar file name, get whatever
			// .jar contains this addon's code
			File jarFile = new File(URLDecoder.decode(SpoutFurnacesClient.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "UTF-8"));
			JarFile jar = new JarFile(jarFile);
			ZipEntry ze = jar.getEntry("goldfurnace.png");
			InputStream is = jar.getInputStream(ze);
			BufferedImage bmg = ImageIO.read(is);
			// Don't forget cleanup!
			is.close();
			jar.close();
			
			ByteBuffer buff = TextureUtils.convertImageData(bmg, 256);
			TextureUtils.getInstance("GoldFurnace").initialUpload(buff, 256);
			buff.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getFlameX() {
		return 63;
	}

	@Override
	protected int getFlameY() {
		return 36;
	}

	@Override
	protected int getArrowX() {
		return 79;
	}

	@Override
	protected int getArrowY() {
		return 34;
	}
	
}
