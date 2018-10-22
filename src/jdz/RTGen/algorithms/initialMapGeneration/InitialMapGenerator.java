
package jdz.RTGen.algorithms.initialMapGeneration;

import java.util.Random;
import java.util.logging.Level;

import jdz.RTGen.dataType.Config;
import jdz.RTGen.dataType.Configurable;
import jdz.RTGen.dataType.Map;
import lombok.Getter;

public abstract class InitialMapGenerator extends Configurable {
	public static InitialMapGenerator getContinent() {
		return new ContinentGenerator();
	}

	@Getter private final Config config = new InitialMapGenConfig();

	protected Map map;
	protected Random random;

	public Map generateInitialMap(Map map) {
		this.map = map;
		random = new Random(map.getSeed());

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
