
package jdz.RTGen.algorithms.biomeClassifier;

import java.util.Random;

import jdz.RTGen.algorithms.tectonics.CellDepthCalculator;
import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class PrecipitationModel {
	private static float MAX_PRECIPITATION = 450;

	public static void assignPrecipitation(Map map, Random random) {
		TectonicPlate oceanMask = new TectonicPlate(map);

		map.forAllCells((x, y) -> {
			if (map.getBiome(x, y) == Biome.OCEAN) {
				oceanMask.addToPlate(x, y);
				map.setPrecipitation(x, y, MAX_PRECIPITATION);
			}
		});

		TectonicPlate landDepth = CellDepthCalculator.getDistanceFromEdge(map, oceanMask,
				new CellDepthCalculator.IsOnEdge(oceanMask));

		map.forAllCells((x, y) -> {
			if (landDepth.isInPlate(x, y)) {
				float p = (float) Math.min(MAX_PRECIPITATION,
						MAX_PRECIPITATION / (Math.sqrt(landDepth.getHeight(x, y)) + 1));
				map.setPrecipitation(x, y, p);
			}
		});
	}

}
