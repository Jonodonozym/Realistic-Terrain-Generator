
package jdz.RTGen.algorithms.biomeClassifier;

import static jdz.RTGen.dataType.Biome.*;

import java.util.Random;

import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;

public class BiomeClassifier {

	public void assignBiomes(Map map) {
		Random random = map.getNewRandom();

		map.setSeaLevel(getRandomSeaLevel(map, random));

		assignOcean(map);

		PrecipitationModel.assignPrecipitation(map, random);
		TemperatureModel.assignTemperatures(map, random);

		float[] temps = map.cellTemperature;
		float[] prec = map.cellPrecipitation;
		Biome[] biomes = map.cellBiome;

		for (int i = 0; i < map.size; i++)
			biomes[i] = getLandBiome(temps[i], prec[i]);
	}

	private float getRandomSeaLevel(Map map, Random random) {
		float minHeight = map.getMinHeight();
		float maxHeight = map.getMaxHeight();

		float seaLevelPercent = 0.25f + 0.5f * random.nextFloat();
		return minHeight + (maxHeight - minHeight) * seaLevelPercent;
	}

	public void assignOcean(Map map) {
		float seaLevel = map.getSeaLevel();
		float[] heights = map.cellHeight;
		Biome[] biomes = map.cellBiome;

		for (int i = 0; i < map.size; i++)
			if (heights[i] < seaLevel)
				biomes[i] = OCEAN;
			else
				biomes[i] = NONE;
	}

	public Biome getLandBiome(float temperature, float precipitation) {
		if (temperature < -5)
			return TUNDRA;

		if (precipitation < 75) {
			if (temperature < 20 && precipitation > 50)
				return SHRUBLAND;
			return DESERT;
		}

		if (temperature < 5)
			return TAIGA;

		if (temperature < 20) {
			if (precipitation < 100)
				return GRASSLAND;
			if (precipitation < 125)
				return WOODLAND;
			if (precipitation < 225)
				return TEMPERATE_FOREST;
			return TEMPERATE_RAINFOREST;
		}

		if (precipitation < 100)
			return THORN_SCRUB;
		if (precipitation < 125)
			return SAVANNAH;
		if (precipitation < 150)
			return THORN_FOREST;
		if (precipitation < 250)
			return TROPICAL_SEASONAL_FOREST;
		return TROPICAL_RAINFOREST;
	}

}
