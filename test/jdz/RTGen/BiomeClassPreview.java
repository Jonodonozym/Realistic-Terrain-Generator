
package jdz.RTGen;

import java.awt.image.BufferedImage;
import java.util.List;

import jdz.RTGen.algorithms.biomeClassifier.BiomeClassifier;
import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.tectonics.TectonicPlateDeformer;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;
import jdz.RTGen.renderers.BiomeRenderer;
import jdz.RTGen.renderers.HeightMapRenderer;

public class BiomeClassPreview extends Previewer {
	private static final int MAP_SIZE = 256;
	private static final int NUM_PLATES = 16;
	private static final int STEPS = 100;

	public static void main(String[] args) {
		new BiomeClassPreview();
	}

	public BiomeClassPreview() {
		super(MAP_SIZE);
	}

	@Override
	public void init() {
		map = new Map(map.width, map.height, 300);
		
		//InitialMapGenerator.getContinent().generateInitialMap(map);
		
		List<TectonicPlate> plates = TectonicPlateGenerator.getRandom().generatePlates(map, NUM_PLATES);
		plates = TectonicPlateDeformer.getBasic().deform(map, plates, STEPS);
		map.setPlates(plates);
		map.updateHeightFromPlates();
		
		BiomeClassifier.assignBiomes(map);
	}

	@Override
	public BufferedImage createPreview() {
		BufferedImage image = new BiomeRenderer().render(map, map);
		new HeightMapRenderer().render(image, map);
		return image;
	}

}
