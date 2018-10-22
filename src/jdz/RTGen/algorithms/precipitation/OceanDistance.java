
package jdz.RTGen.algorithms.precipitation;

import java.util.Random;

import jdz.RTGen.algorithms.tectonics.CellDepthCalculator;
import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class OceanDistance extends PrecipitationModel {
	private static float MAX_PRECIPITATION = 450;

	@Override
	public void apply(Map map, Random random) {
		Biome[] biomes = map.cellBiome;
		float[] precipitation = map.cellPrecipitation;

		boolean[] landMask = new boolean[map.size];
		TectonicPlate landPlate = new TectonicPlate(map, landMask, null, null, null);

		for (int i = 0; i < map.size; i++)
			if (biomes[i] != Biome.OCEAN)
				landMask[i] = true;
			else
				precipitation[i] = MAX_PRECIPITATION;

		int[] landDepth = CellDepthCalculator.getDistanceFromEdge(map, landMask,
				new CellDepthCalculator.IsOnEdge(landPlate));

		for (int i = 0; i < map.size; i++)
			if (landMask[i])
				precipitation[i] = MAX_PRECIPITATION / (float) (Math.pow(landDepth[i], 0.4) + 1);
	}

}
