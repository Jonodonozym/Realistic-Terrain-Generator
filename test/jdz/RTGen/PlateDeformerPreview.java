
package jdz.RTGen;

import java.awt.image.BufferedImage;
import java.util.List;

import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.tectonics.TectonicPlateDeformer;
import jdz.RTGen.dataType.TectonicPlate;
import jdz.RTGen.renderers.HeightMapRenderer;

public class PlateDeformerPreview extends Previewer {
	private static final int MAP_SIZE = 512;

	public static void main(String[] args) {
		new PlateDeformerPreview();
	}

	public PlateDeformerPreview() {
		super(MAP_SIZE);
	}

	@Override
	public BufferedImage createPreview() {
		List<TectonicPlate> plates = TectonicPlateGenerator.getRandom().generatePlates(map, 8);	
		plates = TectonicPlateDeformer.getBasic().deform(map, plates, 1);	
		map.setPlates(plates);
		map.updateHeightFromPlates();
		return new HeightMapRenderer().render(map, map);
	}
}
