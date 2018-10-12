
package jdz.RTGen.renderers;

import static java.awt.Color.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import jdz.RTGen.dataType.TectonicPlate;


public class PlateListRenderer extends Renderer<List<TectonicPlate>> {
	private List<Color> colors = Arrays.asList(RED, ORANGE, YELLOW, GREEN, MAGENTA, CYAN, BLUE, PINK, WHITE, LIGHT_GRAY,
			GRAY, DARK_GRAY, BLACK);

	@Override
	public String getName() {
		return "Tectonic Plates";
	}

	@Override
	public void render(BufferedImage image, List<TectonicPlate> plates) {
		int i = 0;

		Graphics2D g = (Graphics2D) image.getGraphics();

		for (TectonicPlate plate : plates) {
			Color plateColor = colors.get(i++ % colors.size());
			renderPlate(image, plate, plateColor);
		}

		g.dispose();
	}

	private void renderPlate(BufferedImage image, TectonicPlate plate, Color color) {
		int rgb = color.getRGB();
		for (int x = 0; x < plate.getMap().getWidth(); x++)
			for (int y = 0; y < plate.getMap().getHeight(); y++)
				if (plate.isInPlate(x, y))
					image.setRGB(x, y, rgb);
	}

}
