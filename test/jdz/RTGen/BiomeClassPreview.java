
package jdz.RTGen;

import java.awt.image.BufferedImage;
import java.util.List;

import jdz.RTGen.algorithms.biomeClassifier.BiomeClassifier;
import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.tectonics.TectonicPlateDeformer;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;
import jdz.RTGen.rendering.renderers.BiomeRenderer;
import jdz.RTGen.rendering.renderers.HeightMapRenderer;

public class BiomeClassPreview extends Previewer {
	private static final int MAP_SIZE = 256;
	private static final int NUM_PLATES = 20;

	public static void main(String[] args) {
		new BiomeClassPreview();
	}

	public BiomeClassPreview() {
		super(MAP_SIZE);
	}

	@Override
	public void init() {
		map = new Map(map.width, map.height, 300);
		
		List<TectonicPlate> plates = TectonicPlateGenerator.getGenerator().generatePlates(map, NUM_PLATES);
		
		plates = TectonicPlateDeformer.getBasic().deform(map, plates);
		map.setPlates(plates);
		map.updateHeightFromPlates();
		
		BiomeClassifier.assignBiomes(map);
	}

	@Override
	public BufferedImage createPreview() {
		BufferedImage image = new BiomeRenderer().render(map);
		new HeightMapRenderer().render(image, map);
		return image;
	}

}
