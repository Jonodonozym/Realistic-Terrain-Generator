
package jdz.RTGen.rendering.renderers;

import java.awt.Color;
import java.awt.image.BufferedImage;

import jdz.RTGen.algorithms.precipitation.PrecipitationModel;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.rendering.Renderer;

public class PrecipitationRenderer extends Renderer {

	@Override
	public String getName() {
		return "Precipitation";
	}

	@Override
	public void render(BufferedImage image, Map map) {
		map.forAllCells((x, y, i)->{
			image.setRGB(x, y, getColor(map, i));
		});
	}

	private int getColor(Map map, int index) {
		float saturation = map.cellPrecipitation[index] / PrecipitationModel.MAX_PRECIPITATION * 0.5f;
		return Color.HSBtoRGB(0.6f, saturation, 1);
	}

}
