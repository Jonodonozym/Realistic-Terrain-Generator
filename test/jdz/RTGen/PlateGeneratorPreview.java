
package jdz.RTGen;

import java.awt.image.BufferedImage;
import java.util.List;

import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.dataType.TectonicPlate;
import jdz.RTGen.renderers.PlateListRenderer;

public class PlateGeneratorPreview extends Previewer {
	private static final int MAP_SIZE = 512;
	private static final int NUM_PLATES = 24;

	public static void main(String[] args) {
		new PlateGeneratorPreview();
	}

	public PlateGeneratorPreview() {
		super(MAP_SIZE);
	}

	@Override
	public BufferedImage createPreview() {
		List<TectonicPlate> plates = TectonicPlateGenerator.getRandom().generatePlates(map, NUM_PLATES);
		return new PlateListRenderer().render(map, plates);
	}

}
