
package jdz.RTGen.algorithms.precipitation;

import java.util.Random;

import jdz.RTGen.algorithms.cellDepthCalculator.CellDepthCalculator;
import jdz.RTGen.algorithms.cellDepthCalculator.DepthFunction;
import jdz.RTGen.algorithms.cellDepthCalculator.EdgeListPopulator;
import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class OceanDistanceAndLattitudePrecipitation extends PrecipitationModel {

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

		float r1 = MAX_PRECIPITATION * (1 - PrecipitationConfig.LATTITUDE_EFFECT_PERCENT / 100.f);
		if (r1 < 0)
			r1 = 0;
		if (r1 > MAX_PRECIPITATION)
			r1 = MAX_PRECIPITATION;

		float r2 = r1 / PrecipitationConfig.MOISTURE_LOSS_RATE;

		int i = 0;
		for (int y = 0; y < map.height; y++) {
			float equatorRatio = 1 - Math.abs(y - map.height / 2) / (map.height / 2.f);
			float lattitudeOffset = equatorRatio * equatorRatio * (MAX_PRECIPITATION - r1) - 50.f;

			for (int x = 0; x < map.width; x++) {
				if (landMask[i])
					precipitation[i] = lattitudeOffset + r2 / (float) (stretch * landDepth[i] + 1);
				i++;
			}
		}
	}

}
