
package jdz.RTGen.algorithms.tectonics;

import java.util.List;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class TectonicCompression {
	public static List<TectonicPlate> performCollision(List<TectonicPlate> plates) {
		Map map = plates.get(0).getMap();

		boolean[] isProcessed = new boolean[map.size];

		for (int p = 0; p < plates.size(); p++) {
			TectonicPlate plate = plates.get(p);
			TectonicPlate combined = combineOtherPlates(plates, p + 1);

			boolean[] isOverlap = plate.getMasksOverlap(combined);

			TectonicPlate maskPlate = new TectonicPlate(map, isOverlap, null, null, null);

			int[] distances = CellDepthCalculator.getDistanceFromEdge(map, isOverlap,
					new CellDepthCalculator.IsOnEdge(maskPlate));

			for (int i = 0; i < isOverlap.length; i++) {
				if (!isOverlap[i] || isProcessed[i])
					continue;

				isProcessed[i] = true;
				plate.heights[i] = Math.max(plate.heights[i], combined.heights[i]);
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

	public static TectonicPlate combineOtherPlates(List<TectonicPlate> plates, int startIndex) {
		Map map = plates.get(0).getMap();
		TectonicPlate combined = new TectonicPlate(map);

		for (int i = startIndex; i < plates.size(); i++) {
			TectonicPlate p = plates.get(i);
			for (int j = 0; j < map.size; j++)
				if (p.mask[j]) {
					combined.mask[j] = true;
					combined.heights[j] = p.heights[j];
				}
		}

		return combined;
	}
}
