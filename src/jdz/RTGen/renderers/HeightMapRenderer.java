
package jdz.RTGen.renderers;

import java.awt.Color;
import java.awt.image.BufferedImage;

import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;

public class HeightMapRenderer extends Renderer {

	@Override
	public String getName() {
		return "Height Map";
	}

	@Override
	public void render(BufferedImage image, Map map) {
		float minLandHeight = map.getSeaLevel();
		float maxLandHeight = map.getMaxHeight();

		float minOceanHeight = map.getMinHeight();
		float maxOceanHeight = map.getSeaLevel();

		for (int x = 0; x < map.width; x++)
			for (int y = 0; y < map.height; y++) {
				float height = map.getHeight(x, y);
				float ratio = map.getBiome(x, y) == Biome.OCEAN
						? (height - minOceanHeight) / (maxOceanHeight - minOceanHeight)
						: (height - minLandHeight) / (maxLandHeight - minLandHeight);
				image.setRGB(x, y, multiplyValue(image.getRGB(x, y), ratio));
			}
	}

	private int multiplyValue(int rgb, float ratio) {
		Color c = new Color(rgb);
		ratio = ratio * 0.5f + 0.5f;
		float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		return Color.HSBtoRGB(hsv[0], hsv[1], hsv[2] * ratio);
	}

}
