
package jdz.RTGen.algorithms.plateGeneration;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public abstract class TectonicPlateGenerator {
	public static TectonicPlateGenerator getRandom() {
		return new RandomPlateGenerator();
	}

	private final Logger logger = Logger.getGlobal();

	protected Map map;
	protected int numPlates;
	protected Random random;

	public List<TectonicPlate> generatePlates(Map map, double averagePlateArea) {
		return generatePlates(map, (int) Math.ceil(map.size / averagePlateArea));
	}

	public List<TectonicPlate> generatePlates(Map map, int numPlates) {
		this.map = map;
		this.numPlates = numPlates;
		this.random = new Random(map.getSeed());

		long startTime = System.currentTimeMillis();

		logger.log(Level.INFO, "Plate generation started");
		logger.log(Level.INFO, "Map size: " + map.size + " (" + map.width + " x " + map.height + ")");

		List<TectonicPlate> plates = generate();

		logger.log(Level.INFO, "Plate generation completed");
		logger.log(Level.INFO, "Time: " + ((System.currentTimeMillis() - startTime) / 1000) + "s");
		
		return plates;
	}

	protected abstract List<TectonicPlate> generate();
}
