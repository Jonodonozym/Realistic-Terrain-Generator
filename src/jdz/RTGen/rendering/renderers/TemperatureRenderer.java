
package jdz.RTGen.rendering.renderers;

import java.awt.Color;
import java.awt.image.BufferedImage;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.rendering.Renderer;

public class TemperatureRenderer extends Renderer {

	@Override
	public String getName() {
		return "Precipitation";
	}

	@Override
	public void render(BufferedImage image, Map map) {
		float maxTemp = -Float.MAX_VALUE;
		float minTemp = Float.MAX_VALUE;

		for (int i = 0; i < map.size; i++) {
			float t = map.cellTemperature[i];
			if (t < minTemp)
				minTemp = t;
			if (t > maxTemp)
				maxTemp = t;
		}

		final float tMax = maxTemp;
		final float tMin = minTemp;
		final float tRange = tMax - tMin;
		map.forAllCells((x, y, i) -> {
			float ratio = (tMax - map.cellTemperature[i]) / tRange;
			image.setRGB(x, y, getColor(ratio, tRange));
		});
	}

	private int getColor(float ratio, float range) {
		ratio = (float) Math.sqrt(ratio);
		float saturation = Math.abs(0.5f - ratio) * 2f;
		float hue = 0;
		if (ratio > 0.5) {
			hue = 0.6f;
		}
		return Color.HSBtoRGB(hue, saturation, 1);
	}

}
