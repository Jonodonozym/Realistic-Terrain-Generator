
package jdz.RTGen.algorithms.biomeClassifier;

import java.util.Random;

import jdz.RTGen.algorithms.tectonics.CellDepthCalculator;
import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class PrecipitationModel {
	private static float MAX_PRECIPITATION = 400;
	
	public static void assignPrecipitation(Map map, Random random) {
		Biome[] biomes = map.cellBiome;
		float[] precipitation = map.cellPrecipitation;
		
		boolean[] oceanMask = new boolean[map.size];
		TectonicPlate mockOceanPlate = new TectonicPlate(map, oceanMask, null, null, null);
		
		for (int i=0; i<map.size; i++)
			oceanMask[i] = biomes[i] == Biome.OCEAN;
		
		int[] landDepth = CellDepthCalculator.getDistanceFromEdge(map, oceanMask, new CellDepthCalculator.IsOnEdge(mockOceanPlate));
		
		for (int i=0; i<map.size; i++)
			precipitation[i] = Math.min(MAX_PRECIPITATION, 1/landDepth[i]);
	}

}
