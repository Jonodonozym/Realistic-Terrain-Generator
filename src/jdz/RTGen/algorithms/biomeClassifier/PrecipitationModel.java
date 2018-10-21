
package jdz.RTGen.algorithms.biomeClassifier;

import java.util.Random;

import jdz.RTGen.algorithms.tectonics.CellDepthCalculator;
import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class PrecipitationModel {
	private static float MAX_PRECIPITATION = 450;

	public static void assignPrecipitation(Map map, Random random) {
		Biome[] biomes = map.cellBiome;
		float[] precipitation = map.cellPrecipitation;

		boolean[] oceanMask = new boolean[map.size];
		TectonicPlate mockOceanPlate = new TectonicPlate(map, oceanMask, null, null, null);

		for (int i = 0; i < map.size; i++) {
			if (biomes[i] == Biome.OCEAN) {
				oceanMask[i] = true;
				precipitation[i] = MAX_PRECIPITATION;
			}
		}

		int[] landDepth = CellDepthCalculator.getDistanceFromEdge(map, oceanMask,
				new CellDepthCalculator.IsOnEdge(mockOceanPlate));

		for (int i = 0; i < map.size; i++)
			if (!oceanMask[i])
				precipitation[i] = (float) Math.min(MAX_PRECIPITATION,
						MAX_PRECIPITATION / (Math.sqrt(landDepth[i]) + 1));
	}

}
