package org.dyndns.pamelloes.ExtraFurnaces.gui;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.imageio.ImageIO;

import org.dyndns.pamelloes.ExtraFurnaces.client.SpoutFurnacesClient;
import org.spoutcraft.spoutcraftapi.gui.PopupScreen;


public class DiamondFurnaceGui extends FurnaceGui {

	public DiamondFurnaceGui(PopupScreen parent) {
		super(parent, 216, 202, "DiamondFurnace");
		//input
		addSlot(new InventorySlot(8,17));
		addSlot(new InventorySlot(26,17));
		addSlot(new InventorySlot(44,17));
		addSlot(new InventorySlot(8,35));
		addSlot(new InventorySlot(26,35));
		addSlot(new InventorySlot(44,35));
		addSlot(new InventorySlot(62,35));
		//fuel
		addSlot(new InventorySlot(8,89));
		addSlot(new InventorySlot(26,89));
		addSlot(new InventorySlot(44,89));
		addSlot(new InventorySlot(8,71));
		addSlot(new InventorySlot(26,71));
		addSlot(new InventorySlot(44,71));
		addSlot(new InventorySlot(62,71));
		//output
		addSlot(new InventorySlot(116,53).setReadOnly(true));
		addSlot(new InventorySlot(138,39).setReadOnly(true));
		addSlot(new InventorySlot(156,39).setReadOnly(true));
		addSlot(new InventorySlot(174,39).setReadOnly(true));
		addSlot(new InventorySlot(192,39).setReadOnly(true));
		addSlot(new InventorySlot(138,57).setReadOnly(true));
		addSlot(new InventorySlot(156,57).setReadOnly(true));
		addSlot(new InventorySlot(174,57).setReadOnly(true));
		addSlot(new InventorySlot(192,57).setReadOnly(true));
	}

	@Override
	public Point getInventoryOffset() {
		return new Point(28, 120);
	}
	
	static {
		try {
			// Load the image from the jar? :O

			// Instead of using a hardcoded .jar file name, get whatever
			// .jar contains this addon's code
			File jarFile = new File(URLDecoder.decode(SpoutFurnacesClient.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "UTF-8"));
			JarFile jar = new JarFile(jarFile);
			ZipEntry ze = jar.getEntry("diamondfurnace.png");
			InputStream is = jar.getInputStream(ze);
			BufferedImage bmg = ImageIO.read(is);
			// Don't forget cleanup!
			is.close();
			jar.close();
			
			ByteBuffer buff = TextureUtils.convertImageData(bmg, 256);
			TextureUtils.getInstance("DiamondFurnace").initialUpload(buff, 256);
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
		return 54;
	}

	@Override
	protected int getArrowX() {
		return 79;
	}

	@Override
	protected int getArrowY() {
		return 52;
	}
	
}
