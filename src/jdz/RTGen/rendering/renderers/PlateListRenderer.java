
package jdz.RTGen.rendering.renderers;

import java.awt.Color;
import java.awt.image.BufferedImage;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;
import jdz.RTGen.rendering.Renderer;


public class PlateListRenderer extends Renderer {

	@Override
	public String getName() {
		return "Tectonic Plates";
	}

	@Override
	public void render(BufferedImage image, Map map) {
		for (TectonicPlate plate : map.getPlates())
			renderPlate(image, plate);
	}

	private void renderPlate(BufferedImage image, TectonicPlate plate) {
		plate.forEachCell((x, y) -> {
			if (isOnEdge(plate, x, y))
				highlightPixel(image, x, y);
		});
	}

	private boolean isOnEdge(TectonicPlate p, int x, int y) {
		return !p.isInPlate(x - 1, y) || !p.isInPlate(x + 1, y) || !p.isInPlate(x, y - 1) || !p.isInPlate(x, y + 1);
	}

	private void highlightPixel(BufferedImage image, int x, int y) {
		image.setRGB(x, y, invert(image.getRGB(x, y)));
	}

	private int invert(int rgb) {
		Color c = new Color(rgb);
		float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		hsv[2] = (hsv[2] + 0.5f) % 1.f;
		return Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
	}

}
