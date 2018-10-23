
package jdz.RTGen.algorithms.tectonics;

import java.util.List;

import jdz.RTGen.algorithms.cellDepthCalculator.CellDepthCalculator;
import jdz.RTGen.algorithms.cellDepthCalculator.DepthFunction;
import jdz.RTGen.algorithms.cellDepthCalculator.EdgeListPopulator;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.PlateList;
import jdz.RTGen.dataType.TectonicPlate;

public class TectonicConvergence {
	public static List<TectonicPlate> performConvergence(List<TectonicPlate> plates) {
		Map map = plates.get(0).getMap();

		boolean[] isProcessed = new boolean[map.size];

		PlateList plateList = new PlateList(plates);

		for (int p = 0; p < plates.size(); p++) {
			TectonicPlate plate = plates.get(p);

			TectonicPlate mergedPlate = plateList.toMergedPlate(p + 1);

			boolean[] isOverlap = plate.getMasksOverlap(mergedPlate);

			TectonicPlate overlapPlate = new TectonicPlate(map, isOverlap, null, null, null);

			float[] distances = CellDepthCalculator.getDistanceFromEdge(map, isOverlap,
					new EdgeListPopulator.IsOnEdge(overlapPlate), DepthFunction::sum);

			for (int i = 0; i < isOverlap.length; i++) {
				if (!isOverlap[i] || isProcessed[i])
					continue;

				isProcessed[i] = true;
				plate.heights[i] = (plate.heights[i] + mergedPlate.heights[i]) / 2.f;
				plate.heights[i] += Math.pow(distances[i], TectonicsConfig.CONVERGENCE_EXPONENT);
			}

			for (int p2 = p + 1; p2 < plates.size(); p2++)
				plates.get(p2).chopOverlap(isOverlap);
		}

		return plates;
	}
}
