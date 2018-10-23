
package jdz.RTGen.rendering.renderers;

import java.awt.Color;
import java.awt.image.BufferedImage;

import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.rendering.Renderer;

public class HeightMapRenderer extends Renderer {

	@Override
	public String getName() {
		return "Height Map";
	}

	@Override
	public void render(BufferedImage image, Map map) {
		final float minLandHeight = map.getSeaLevel();
		final float maxLandHeight = map.getMaxHeight() == minLandHeight ? minLandHeight + 1 : map.getMaxHeight();

		final float minOceanHeight = map.getMinHeight();
		final float maxOceanHeight = map.getSeaLevel();

		map.forAllCells((x, y, i) -> {
			float height = map.cellHeight[i];
			float ratio = map.cellBiome[i] == Biome.OCEAN
					? (height - minOceanHeight) / (maxOceanHeight - minOceanHeight)
					: (height - minLandHeight) / (maxLandHeight - minLandHeight);
			image.setRGB(x, y, multiplyValue(image.getRGB(x, y), ratio));
		});
	}

	private int multiplyValue(int rgb, float ratio) {
		Color c = new Color(rgb);
		ratio = ratio * 0.5f + 0.5f;
		float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		return Color.HSBtoRGB(hsv[0], hsv[1], hsv[2] * ratio);
	}

}
