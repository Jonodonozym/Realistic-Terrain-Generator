
package jdz.RTGen.algorithms.precipitation;

import java.util.Random;

import jdz.RTGen.algorithms.cellDepthCalculator.CellDepthCalculator;
import jdz.RTGen.algorithms.cellDepthCalculator.DepthFunction;
import jdz.RTGen.algorithms.cellDepthCalculator.EdgeListPopulator;
import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class OceanDistance extends PrecipitationModel {

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

		int landCells = landPlate.numCells();

		if (landCells == map.size)
			return;

		if (landCells == 0)
			map.forAllCells((i) -> {
				map.cellPrecipitation[i] = MAX_PRECIPITATION;
			});

		float stretch = 32.f / map.height;

		float[] landDepth = CellDepthCalculator.getDistanceFromEdge(map, landMask,
				new EdgeListPopulator.IsOnEdge(landPlate), DepthFunction::hypot);

		for (int i = 0; i < map.size; i++)
			if (landMask[i])
				precipitation[i] = MAX_PRECIPITATION / (float) (stretch * landDepth[i] + 1);
	}

}
