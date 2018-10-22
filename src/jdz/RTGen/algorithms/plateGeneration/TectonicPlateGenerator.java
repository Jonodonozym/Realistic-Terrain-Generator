
package jdz.RTGen.algorithms.plateGeneration;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdz.RTGen.dataType.Configurable;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public abstract class TectonicPlateGenerator extends Configurable {
	public static TectonicPlateGenerator getRandom() {
		return new RandomPlateGenerator();
	}

	private final Logger logger = Logger.getGlobal();

	protected Map map;
	protected Random random;


	public List<TectonicPlate> generatePlates(Map map) {
		return generatePlates(map, PlateGenConfig.PLATES);
	}

	public List<TectonicPlate> generatePlates(Map map, int numPlates) {

		this.map = map;
		this.random = new Random(map.getSeed());

		long startTime = System.currentTimeMillis();

		logger.log(Level.INFO, "Plate generation started");
		logger.log(Level.INFO, "Map size: " + map.size + " (" + map.width + " x " + map.height + ")");

		List<TectonicPlate> plates = generate(numPlates);

		logger.log(Level.INFO, "Plate generation completed");
		logger.log(Level.INFO, "Time: " + (System.currentTimeMillis() - startTime) + "ms");

		return plates;
	}

	protected abstract List<TectonicPlate> generate(int numPlates);
}
