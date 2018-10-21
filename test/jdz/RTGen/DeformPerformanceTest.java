
package jdz.RTGen;

import java.util.List;

import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.tectonics.TectonicPlateDeformer;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class DeformPerformanceTest {
	private static final int MAP_SIZE = 512;
	private static final int NUM_PLATES = 10;
	private static final int STEPS = 100;

	public static void main(String[] args) {
		LoggerConfig.init();

		Map map = new Map(MAP_SIZE, MAP_SIZE, 300);

		List<TectonicPlate> plates = TectonicPlateGenerator.getRandom().generatePlates(map, NUM_PLATES);

		plates = TectonicPlateDeformer.getBasic().deform(map, plates, STEPS);
	}

}
