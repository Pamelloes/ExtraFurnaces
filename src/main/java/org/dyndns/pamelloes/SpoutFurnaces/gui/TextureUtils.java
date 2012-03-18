package org.dyndns.pamelloes.SpoutFurnaces.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TextureUtils {

	private TextureUtils() {
	}

	private static Map<String, TextureUtils> instances = new HashMap<String, TextureUtils>();
	private int textureID;

	/**
	 * Simply because I only need one instance of this, and I'd like to access
	 * it like this because I'm lazy.
	 * 
	 * Did I mention I'm lazy?
	 * 
	 * @return the class
	 */
	public static TextureUtils getInstance(String key) {
		if (instances.get(key) == null) {
			instances.put(key, new TextureUtils());
		}
		return instances.get(key);
	}

	@SuppressWarnings("rawtypes")
	/**
	 * Moved from MiniMapRender
	 * 
	 * Convert the bufferedImage into a byteBuffer
	 * suitable for textures
	 * @param bufferedImage
	 * @return ByteBuffer (from bufferedImage)
	 */
	public static ByteBuffer convertImageData(BufferedImage bufferedImage, int width) {
		ByteBuffer imageBuffer;
		WritableRaster raster;
		BufferedImage texImage;

		ColorModel glAlphaColorModel = new ComponentColorModel(
				ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8,
						8, 8 }, true, false, Transparency.TRANSLUCENT,
				DataBuffer.TYPE_BYTE);

		raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
				bufferedImage.getWidth(), bufferedImage.getHeight(), 4, null);
		texImage = new BufferedImage(glAlphaColorModel, raster, true,
				new Hashtable());

		// copy the source image into the produced image
		Graphics g = texImage.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, width, width);
		g.drawImage(bufferedImage, 0, 0, null);

		// build a byte buffer from the temporary image
		// that be used by OpenGL to produce a texture.
		byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer())
				.getData();

		imageBuffer = ByteBuffer.allocateDirect(data.length);
		imageBuffer.order(ByteOrder.nativeOrder());
		imageBuffer.put(data, 0, data.length);
		imageBuffer.flip();

		return imageBuffer;
	}

	/**
	 * Setup the initial Texture environment
	 * but with a ByteBuffer
	 */
	public void initialUpload(ByteBuffer buff, int width) {
		System.out.println("Generating texture ID");
		IntBuffer bInt = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(bInt);
		textureID = bInt.get(0);
		System.out.println("ID:" + textureID + " Binding texture");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, width, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				buff);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_NEAREST);
	}

	/**
	 * Gets the texture id for rendering.
	 */
	public int getId() {
		return textureID;
	}

}
