
package jdz.RTGen.renderers;

import java.awt.Color;
import java.awt.image.BufferedImage;

import jdz.RTGen.dataType.Map;

public class HeightMapRenderer extends Renderer {

	@Override
	public String getName() {
		return "Height Map";
	}

	@Override
	public void render(BufferedImage image, Map map) {
		float minHeight = map.getMinHeight();
		float maxHeight = map.getMaxHeight();

		for (int x = 0; x < map.width; x++)
			for (int y = 0; y < map.height; y++) {
				float height = map.getHeight(x, y);
				float ratio = (height - minHeight) / (maxHeight - minHeight);
				image.setRGB(x, y, multiplyValue(image.getRGB(x, y), ratio));
			}
	}

	private int multiplyValue(int rgb, float ratio) {
		Color c = new Color(rgb);
		float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		return Color.HSBtoRGB(hsv[0], hsv[1], hsv[2] * ratio);
	}

}
