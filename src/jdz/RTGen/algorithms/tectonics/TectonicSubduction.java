
package jdz.RTGen.algorithms.tectonics;

import java.util.List;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class TectonicSubduction {

	public static List<TectonicPlate> performSubduction(List<TectonicPlate> beforeMoved,
			List<TectonicPlate> afterMoved) {
		Map map = beforeMoved.get(0).getMap();
		boolean[] combinedSubduction = new boolean[map.size];

		for (int i = 0; i < beforeMoved.size(); i++) {
			TectonicPlate before = beforeMoved.get(i);
			TectonicPlate after = afterMoved.get(i);

			boolean[] subductionMask = before.clone().chopOverlap(after.mask).mask;

			TectonicPlate maskPlate = new TectonicPlate(map, subductionMask, null, null, null);

			int[] dists = CellDepthCalculator.getDistanceFromEdge(map, subductionMask,
					new CellDepthCalculator.IsNextToPlate(maskPlate, after));

			for (int j = 0; j < dists.length; j++) {
				if (dists[j] == 0 || combinedSubduction[j])
					continue;

				combinedSubduction[j] = true;
				after.heights[j] -= Math.log(dists[j]);
			}
		}

		return afterMoved;
	}
}
