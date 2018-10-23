
package jdz.RTGen.algorithms.tectonics;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdz.RTGen.configuration.Configurable;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public abstract class TectonicPlateDeformer extends Configurable {
	public static TectonicPlateDeformer getBasic() {
		return new BasicPlateDeformer();
	}

	private final Logger logger = Logger.getGlobal();

	protected Map map;
	public Random random;

	public List<TectonicPlate> deform(Map map, List<TectonicPlate> plates) {
		return deform(map, plates, (p) -> {});
	}

	public List<TectonicPlate> deform(Map map, List<TectonicPlate> plates, Callback callback) {
		this.map = map;
		random = map.getNewRandom();

		long startTime = System.currentTimeMillis();

		logger.log(Level.INFO, "Plate deformation started");
		logger.log(Level.INFO, "Max itterations: " + TectonicsConfig.STEPS);
		logger.log(Level.INFO, "Map size: " + map.size + " (" + map.width + " x " + map.height + ")");

		List<TectonicPlate> newPlates = initialize(plates);
		if (TectonicsConfig.STEPS < 0)
			TectonicsConfig.STEPS = 0;
		newPlates = deform(newPlates, TectonicsConfig.STEPS + 1, callback);

		logger.log(Level.INFO, "Plate deformation completed");
		logger.log(Level.INFO, "Time: " + (System.currentTimeMillis() - startTime) + "ms");

		return newPlates;
	}

	private List<TectonicPlate> deform(List<TectonicPlate> plates, int maxSteps, Callback c) {
		List<TectonicPlate> newPlates = plates;

		int lastPercent = 0;
		for (int i = 0; i < maxSteps; i++) {
			if (shouldStop(plates))
				break;

			newPlates = deform(newPlates);

			if (maxSteps > 5)
				if (maxSteps < 100)
					logger.log(Level.INFO, "Itteration " + i + " done");
				else {
					int percent = (int) (i / (maxSteps / 10.f)) * 10;
					if (percent > lastPercent) {
						lastPercent = percent;
						logger.log(Level.INFO, percent + "%");
					}
				}

			c.run(newPlates);
		}
		return newPlates;
	}

	public static interface Callback {
		public void run(List<TectonicPlate> plates);
	}

	protected abstract List<TectonicPlate> initialize(List<TectonicPlate> plates);

	protected abstract boolean shouldStop(List<TectonicPlate> plates);

	protected abstract List<TectonicPlate> deform(List<TectonicPlate> plates);
}
