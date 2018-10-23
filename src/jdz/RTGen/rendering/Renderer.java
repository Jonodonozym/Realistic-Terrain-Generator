
package jdz.RTGen.rendering;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import jdz.RTGen.dataType.Map;

public abstract class Renderer {

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
