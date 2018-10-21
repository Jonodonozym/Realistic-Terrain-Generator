
package jdz.RTGen.algorithms.tectonics;

import java.util.List;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.PlateList;
import jdz.RTGen.dataType.TectonicPlate;

public class TectonicCompression {
	public static List<TectonicPlate> performCollision(List<TectonicPlate> plates) {
		Map map = plates.get(0).getMap();

		boolean[] isProcessed = new boolean[map.size];

		PlateList plateList = new PlateList(plates);

		for (int p = 0; p < plates.size(); p++) {
			TectonicPlate plate = plates.get(p);
			TectonicPlate otherPlates = plateList.toMergedPlate(p + 1);

			boolean[] isOverlap = plate.getMasksOverlap(otherPlates);

			TectonicPlate overlapPlate = new TectonicPlate(map, isOverlap, null, null, null);

			int[] distances = CellDepthCalculator.getDistanceFromEdge(map, isOverlap,
					new CellDepthCalculator.IsOnEdge(overlapPlate));

			for (int i = 0; i < isOverlap.length; i++) {
				if (!isOverlap[i] || isProcessed[i])
					continue;

				isProcessed[i] = true;
				plate.heights[i] = (plate.heights[i] + otherPlates.heights[i]) / 2.f;
				plate.heights[i] += Math.pow(distances[i], 2) / 100f;
			}

			for (TectonicPlate other : plates) {
				if (other.equals(plate))
					continue;

				other.chopOverlap(isOverlap);
			}
		}

		return plates;
	}
}
