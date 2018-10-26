
package jdz.RTGen.algorithms.precipitation;

import java.util.Random;

import jdz.RTGen.algorithms.initialMapGeneration.SimplexNoise;
import jdz.RTGen.dataType.Map;

public class NoiseAndLattitudePrecipitation extends PrecipitationModel {

	@Override
	public void apply(Map map, Random random) {
		float xOffset = random.nextFloat();
		float yOffset = random.nextFloat();

		float r1 = PrecipitationConfig.NOISE_VARIENCE;
		float r2 = MAX_PRECIPITATION * (1 - PrecipitationConfig.LATTITUDE_EFFECT_PERCENT / 100.f);
		if (r2 < 0)
			r2 = 0;
		if (r2 > MAX_PRECIPITATION)
			r2 = MAX_PRECIPITATION;

		int i = 0;
		for (int y = 0; y < map.height; y++) {
			double ny = y / (float) map.height - 0.5f + yOffset;
			float equatorRatio = 1 - Math.abs(y - map.height / 2) / (map.height / 2.f);
			float lattitudeOffset = equatorRatio * equatorRatio * (MAX_PRECIPITATION - r2) - 50.f;

			for (int x = 0; x < map.width; x++) {
				double nx = x / (float) map.width - 0.5f + xOffset;
				map.cellPrecipitation[i] = lattitudeOffset + (float) (SimplexNoise.noise(nx * r1, ny * r1) + 1) * r2;
				i++;
			}
		}
	}

}
