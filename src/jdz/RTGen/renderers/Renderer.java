
package jdz.RTGen.renderers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jdz.RTGen.dataType.Map;

public abstract class Renderer<E> {
	private static final File ROOT_FOLDER = new File("Renders");

	public void exportPNG(Map map, E object) {
		if (!ROOT_FOLDER.exists())
			ROOT_FOLDER.mkdirs();

		File file = new File(ROOT_FOLDER, "Render - " + getName() + " - " + map.getSeed());
		BufferedImage image = render(map, object);

		try {
			ImageIO.write(image, "png", file);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public BufferedImage render(Map map, E object) {
		BufferedImage image = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_RGB);
		render(image, object);
		return image;
	}

	public abstract String getName();
	public abstract void render(BufferedImage image, E object);
}
