
package jdz.RTGen.algorithms.precipitation;

import java.util.Random;

import jdz.RTGen.algorithms.initialMapGeneration.SimplexNoise;
import jdz.RTGen.dataType.Map;

public class NoisePrecipitation extends PrecipitationModel {

	@Override
	public void apply(Map map, Random random) {
		float xOffset = random.nextFloat();
		float yOffset = random.nextFloat();

		float r = PrecipitationConfig.RANDOMNESS;

		map.forAllCells((x, y, i) -> {
			double nx = x / (float) map.width - 0.5f + xOffset;
			double ny = y / (float) map.height - 0.5f + yOffset;
			map.cellPrecipitation[i] = (float) (SimplexNoise.noise(nx * r, ny * r) + 1) * MAX_PRECIPITATION/2f;
		});
	}

}
