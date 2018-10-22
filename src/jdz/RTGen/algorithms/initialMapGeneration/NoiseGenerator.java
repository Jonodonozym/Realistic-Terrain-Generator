
package jdz.RTGen.algorithms.initialMapGeneration;

import jdz.RTGen.dataType.Map;

/**
 * https://www.redblobgames.com/maps/terrain-from-noise/
 *
 * @author Jaiden Baker
 */
public class NoiseGenerator extends ContinentGenerator {

	@Override
	protected Map generate() {
		float xOffset = random.nextFloat();
		float yOffset = random.nextFloat();
		
		map.forAllCells((x, y, i) -> {
			double nx = x / (float) map.width - 0.5f + xOffset;
			double ny = y / (float) map.height - 0.5f + yOffset;
			map.cellHeight[i] = (float) SimplexNoise.noise(nx, ny)*100;
		});

		return map;
	}

}
