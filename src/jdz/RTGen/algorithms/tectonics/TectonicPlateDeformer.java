
package jdz.RTGen.algorithms.tectonics;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public abstract class TectonicPlateDeformer {
	public static TectonicPlateDeformer getBasic() {
		return new BasicPlateDeformer();
	}
	
	private final Logger logger = Logger.getGlobal();

	protected Map map;
	protected Random random;

	public List<TectonicPlate> deform(Map map, List<TectonicPlate> plates, int maxSteps) {
		this.map = map;
		random = new Random(map.getSeed());

		long startTime = System.currentTimeMillis();

		logger.log(Level.INFO, "Plate deformation started");
		logger.log(Level.INFO, "Max itterations: " + maxSteps);
		logger.log(Level.INFO, "Map size: " + map.size + " (" + map.width + " x " + map.height + ")");

		List<TectonicPlate> newPlates = initialize(plates);
		newPlates = deform(newPlates, maxSteps);

		logger.log(Level.INFO, "Plate deformation completed");
		logger.log(Level.INFO, "Time: " + ((System.currentTimeMillis() - startTime) / 1000) + "s");

		return newPlates;
	}

	private List<TectonicPlate> deform(List<TectonicPlate> plates, int maxSteps) {
		List<TectonicPlate> newPlates = plates;
		for (int i = 0; i < maxSteps; i++) {
			if (shouldStop(plates))
				break;

			newPlates = deform(plates);

			if (maxSteps < 100)
				logger.log(Level.INFO, "Itteration " + i + " done");
		}
		return newPlates;
	}

	protected abstract List<TectonicPlate> initialize(List<TectonicPlate> plates);

	protected abstract boolean shouldStop(List<TectonicPlate> plates);

	protected abstract List<TectonicPlate> deform(List<TectonicPlate> plates);
}
