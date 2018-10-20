
package jdz.RTGen.algorithms.biomeClassifier;

import java.util.Random;

import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;

public class TemperatureModel {
	public static void assignTemperatures(Map map, Random random) {
		// longitude based temperature
		float eqTemp = getEquatorTemperature(random);
		float plTemp = getPolarTemperature(random);
		float longitudeTempRange = eqTemp - plTemp;

		// altigude based temperature
		float heightRange = map.getMaxHeight() - map.getSeaLevel();
		float seaTemp = getSeaLevelTemperature(random);
		float mountTemp = getHighestMountainTemperature(random);
		float heightToTempDiffConst = heightRange * (mountTemp - seaTemp);

		// de-referencing optimisation
		float[] temps = map.cellTemperature;
		float[] heights = map.cellHeight;
		Biome[] biomes = map.cellBiome;

		int i = 0;
		for (int y = 0; y < map.height; y++) {
			float distPercent = Math.abs(y - map.height / 2) / ((float) map.height / 2.f);

			float longitudeTemp = eqTemp - longitudeTempRange * distPercent;
			float oceanTemp = longitudeTemp * 0.8f;

			for (int x = 0; x < map.width; x++) {
				if (biomes[i] == Biome.OCEAN)
					temps[i] = oceanTemp;
				else
					temps[i] = longitudeTemp - (heights[i] - heightRange) * heightToTempDiffConst;
				i++;
			}
		}
	}

	private static float getEquatorTemperature(Random random) {
		return 20 + 20 * random.nextFloat();
	}

	private static float getPolarTemperature(Random random) {
		return -20 + 10 * random.nextFloat();
	}

	private static float getSeaLevelTemperature(Random random) {
		return 0;
	}

	private static float getHighestMountainTemperature(Random random) {
		return -10 - random.nextFloat() * 10.f;
	}

}
