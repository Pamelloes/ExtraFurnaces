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

import org.dyndns.pamelloes.ExtraFurnaces.client.ExtraFurnacesClient;
import org.spoutcraft.spoutcraftapi.gui.PopupScreen;


public class IronFurnaceGui extends FurnaceGui {

	public IronFurnaceGui(PopupScreen parent) {
		super(parent, 176, 166, "IronFurnace");
		InventorySlot in = new InventorySlot(56,17);
		InventorySlot fuel = new InventorySlot(56,53);
		InventorySlot out = new InventorySlot(116,35);
		out.setReadOnly(true);
		InventorySlot in2 = new InventorySlot(38,17);
		InventorySlot fuel2 = new InventorySlot(38,53);
		InventorySlot out2 = new InventorySlot(138,39);
		out2.setReadOnly(true);
		addSlot(in2);
		addSlot(in);
		addSlot(fuel2);
		addSlot(fuel);
		addSlot(out);
		addSlot(out2);
	}

	@Override
	public Point getInventoryOffset() {
		return new Point(8, 84);
	}
	
	static {
		try {
			// Load the image from the jar? :O

			// Instead of using a hardcoded .jar file name, get whatever
			// .jar contains this addon's code
			File jarFile = new File(URLDecoder.decode(ExtraFurnacesClient.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath(), "UTF-8"));
			JarFile jar = new JarFile(jarFile);
			ZipEntry ze = jar.getEntry("ironfurnace.png");
			InputStream is = jar.getInputStream(ze);
			BufferedImage bmg = ImageIO.read(is);
			// Don't forget cleanup!
			is.close();
			jar.close();
			
			ByteBuffer buff = TextureUtils.convertImageData(bmg, 256);
			TextureUtils.getInstance("IronFurnace").initialUpload(buff, 256);
			buff.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getFlameX() {
		return 56;
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

	@Override
	protected String getName() {
		return "Iron Furnace";
	}
	
}
