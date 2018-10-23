
package jdz.RTGen;

import java.util.List;

import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.tectonics.TectonicPlateDeformer;
import jdz.RTGen.algorithms.tectonics.TectonicsConfig;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class DeformPerformanceTest {
	private static final int MAP_SIZE = 2048;
	private static final int NUM_PLATES = 10;
	private static final int STEPS = 5;

	public static void main(String[] args) {
		LoggerConfig.init();

		Map map = new Map(MAP_SIZE, MAP_SIZE, 300);

		List<TectonicPlate> plates = TectonicPlateGenerator.getGenerator().generatePlates(map, NUM_PLATES);
		
		TectonicsConfig.STEPS = STEPS;
		plates = TectonicPlateDeformer.getBasic().deform(map, plates);
	}

}
