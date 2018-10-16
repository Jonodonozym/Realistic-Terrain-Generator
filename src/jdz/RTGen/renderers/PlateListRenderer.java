
package jdz.RTGen.renderers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import jdz.RTGen.dataType.TectonicPlate;


public class PlateListRenderer extends Renderer<List<TectonicPlate>> {

	@Override
	public String getName() {
		return "Tectonic Plates";
	}

	@Override
	public void render(BufferedImage image, List<TectonicPlate> plates) {
		for (TectonicPlate plate : plates)
			renderPlate(image, plate);
	}

	private void renderPlate(BufferedImage image, TectonicPlate plate) {
		for (int x = 0; x < plate.getMap().width; x++)
			for (int y = 0; y < plate.getMap().height; y++)
				if (plate.isInPlate(x, y) && isOnEdge(plate, x, y))
					invertColor(image, x, y);
	}

	private boolean isOnEdge(TectonicPlate p, int x, int y) {
		return !p.isInPlate(x - 1, y) || !p.isInPlate(x + 1, y) || !p.isInPlate(x, y - 1) || !p.isInPlate(x, y + 1);
	}

	private void invertColor(BufferedImage image, int x, int y) {
		image.setRGB(x, y, invert(image.getRGB(x, y)));
	}

	private int invert(int rgb) {
		Color c = new Color(rgb);
		float[] hsv = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
		hsv[2] = (hsv[2] + 0.5f) % 1.f;
		return Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
	}

}
