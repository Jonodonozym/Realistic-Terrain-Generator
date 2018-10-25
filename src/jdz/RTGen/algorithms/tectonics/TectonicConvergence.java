
package jdz.RTGen.algorithms.tectonics;

import java.util.Arrays;
import java.util.List;

import jdz.RTGen.algorithms.cellDepthCalculator.CellDepthCalculator;
import jdz.RTGen.algorithms.cellDepthCalculator.DepthFunction;
import jdz.RTGen.algorithms.cellDepthCalculator.EdgeListPopulator;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class TectonicConvergence {
	public static List<TectonicPlate> performConvergence(List<TectonicPlate> plates) {
		Map map = plates.get(0).getMap();

		int[] numPlates = new int[map.size];
		int[] highestIndex = new int[map.size];
		float[] highestPoint = new float[map.size];
		Arrays.fill(highestIndex, -1);
		Arrays.fill(highestPoint, -Float.MAX_VALUE);

		for (int p = 0; p < plates.size(); p++) {
			final int index = p;
			float[] heights = plates.get(p).heights;
			plates.get(p).forEachCell((i) -> {
				numPlates[i] += 1;
				highestIndex[i] = index;
				if (heights[i] > highestPoint[i])
					highestPoint[i] = heights[i];
			});
		}

		boolean[] overlapMask = new boolean[map.size];
		for (int i = 0; i < map.size; i++)
			if (numPlates[i] > 1)
				overlapMask[i] = true;

		for (TectonicPlate plate : plates)
			plate.chopOverlap(overlapMask);

		float[] depths = CellDepthCalculator.getDistanceFromEdge(map, overlapMask,
				new EdgeListPopulator.IsOnEdge(map, overlapMask), DepthFunction::hypot);

		for (int i = 0; i < map.size; i++) {
			int index = highestIndex[i];
			if (index == -1)
				continue;
			TectonicPlate plate = plates.get(index);
			plate.mask[i] = true;
			plate.heights[i] = highestPoint[i] + depths[i] * (numPlates[i] - 1) * TectonicsConfig.CONVERGENCE_EXPONENT;
		}

		return plates;
	}
}
