
package jdz.RTGen.algorithms.tectonics;

import java.util.List;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.PlateList;
import jdz.RTGen.dataType.TectonicPlate;

public class TectonicCompression {
	public static List<TectonicPlate> performCollision(List<TectonicPlate> plates) {
		Map map = plates.get(0).getMap();

		TectonicPlate isProcessed = new TectonicPlate(map);

		PlateList plateList = new PlateList(plates);

		for (int p = 0; p < plates.size(); p++) {
			TectonicPlate plate = plates.get(p);
			TectonicPlate otherPlates = plateList.toMergedPlate(p + 1);

			TectonicPlate overlapPlate = plate.getMasksOverlap(otherPlates);

			TectonicPlate distances = CellDepthCalculator.getDistanceFromEdge(map, overlapPlate,
					new CellDepthCalculator.IsOnEdge(overlapPlate));

			overlapPlate.forEachCell((x, y) -> {
				if (isProcessed.contains(x, y))
					return;

				isProcessed.addToPlate(x, y);
				
				float averageHeight = (plate.getHeight(x, y) + otherPlates.getHeight(x, y)) / 2.f;
				averageHeight += Math.pow(distances.getHeight(x, y), 2) / 100f;
				plate.setHeight(x, y, averageHeight);
			});

			for (TectonicPlate other : plates) {
				if (other.equals(plate))
					continue;

				other.chopOverlap(overlapPlate);
			}
		}

		return plates;
	}
}
