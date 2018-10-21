
package jdz.RTGen.algorithms.initialMapGeneration;

import jdz.RTGen.dataType.Map;

/**
 * https://www.redblobgames.com/maps/terrain-from-noise/
 *
 * @author Jaiden Baker
 */
public class PerlinNoiseGenerator extends InitialMapGenerator {

	@Override
	protected Map generate() {
		map.forAllCells((x, y, i)->{
			float nx = x / (float) map.width - 0.5f;
			float ny = y / (float) map.height - 0.5f;
			map.cellHeight[i++] = noise(nx, ny);
		});

		return map;
	}

	private float noise(float nx, float ny) {
		return 0;
	}

}
