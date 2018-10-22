
package jdz.RTGen.renderers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jdz.RTGen.dataType.Map;

public abstract class Renderer {
	private static final File ROOT_FOLDER = new File("Renders");

	public void exportPNG(Map map) {
		if (!ROOT_FOLDER.exists())
			ROOT_FOLDER.mkdirs();

		File file = new File(ROOT_FOLDER, "Render - " + getName() + " - " + map.getSeed());
		BufferedImage image = render(map);

		try {
			ImageIO.write(image, "png", file);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public BufferedImage render(Map map) {
		BufferedImage image = new BufferedImage(map.width, map.height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.dispose();
		
		render(image, map);
		return image;
	}

	public abstract String getName();
	public abstract void render(BufferedImage image, Map object);
}
