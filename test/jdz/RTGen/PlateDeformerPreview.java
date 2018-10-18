
package jdz.RTGen;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.tectonics.TectonicPlateDeformer;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;
import jdz.RTGen.renderers.HeightMapRenderer;
import jdz.RTGen.renderers.PlateListRenderer;

public class PlateDeformerPreview extends Previewer {
	private static final int MAP_SIZE = 256;
	private static final int NUM_PLATES = 24;
	private static final int MS_PER_STEP = 100;

	public static void main(String[] args) {
		new PlateDeformerPreview();
	}

	public PlateDeformerPreview() {
		super(MAP_SIZE, MS_PER_STEP);
	}

	@Override
	public void init() {
		Logger.getGlobal().setLevel(Level.SEVERE);
		map = new Map(map.width, map.height, 100);
		List<TectonicPlate> plates = TectonicPlateGenerator.getRandom().generatePlates(map, NUM_PLATES);
		map.setPlates(plates);
	}

	@Override
	public BufferedImage createPreview() {
		List<TectonicPlate> plates = TectonicPlateDeformer.getBasic().deform(map, map.getPlates(), 1);
		map.setPlates(plates);
		map.updateHeightFromPlates();
		BufferedImage heightmap = new HeightMapRenderer().render(map, map);
		new PlateListRenderer().render(heightmap, plates);
		return heightmap;
	}
}
