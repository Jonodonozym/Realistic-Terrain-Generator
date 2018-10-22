
package jdz.RTGen.algorithms.initialMapGeneration;

import java.util.Random;
import java.util.logging.Level;

import jdz.RTGen.configuration.Config;
import jdz.RTGen.configuration.Configurable;
import jdz.RTGen.dataType.Map;
import lombok.Getter;

public abstract class ContinentGenerator extends Configurable {
	public static ContinentGenerator getContinent() {
		return new ContinentFromPlates();
	}
	
	public static ContinentGenerator getNoise() {
		return new NoiseGenerator();
	}

	@Getter private final Config config = new ContinentGenConfig();

	protected Map map;
	protected Random random;

	public Map generateInitialMap(Map map) {
		this.map = map;
		random = map.getNewRandom();

		long startTime = System.currentTimeMillis();

		logger.log(Level.INFO, "Initial map generation started");
		logger.log(Level.INFO, "Map size: " + map.size + " (" + map.width + " x " + map.height + ")");

		this.map = generate();

		logger.log(Level.INFO, "Initial map completed");
		logger.log(Level.INFO, "Time: " + (System.currentTimeMillis() - startTime) + "ms");

		return map;
	}

	protected abstract Map generate();
}
