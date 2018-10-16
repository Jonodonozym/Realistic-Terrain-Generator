
package jdz.RTGen.algorithms.initialMapGeneration;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdz.RTGen.dataType.Map;

public abstract class InitialMapGenerator {
	public static InitialMapGenerator getContinent() {
		return new ContinentGenerator();
	}
	
	private final Logger logger = Logger.getGlobal();

	protected Map map;
	protected Random random;

	public Map generateInitialMap(Map map) {
		this.map = map;
		this.random = new Random(map.getSeed());

		long startTime = System.currentTimeMillis();

		logger.log(Level.INFO, "Initial map generation started");
		logger.log(Level.INFO, "Map size: " + map.size + " (" + map.width + " x " + map.height + ")");

		this.map = generate();

		logger.log(Level.INFO, "Initial map completed");
		logger.log(Level.INFO, "Time: " + ((System.currentTimeMillis() - startTime) / 1000) + "s");
		
		return map;
	}
	
	protected abstract Map generate();
}
