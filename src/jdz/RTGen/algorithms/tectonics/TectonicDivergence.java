
package jdz.RTGen.algorithms.tectonics;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.PlateList;
import jdz.RTGen.dataType.TectonicPlate;
import lombok.AllArgsConstructor;

/**
 * Uses a flood-fill algorithm
 *
 * @author Jaiden Baker
 */
public class TectonicDivergence {
	public static List<TectonicPlate> performSubduction(List<TectonicPlate> beforeMoved,
			List<TectonicPlate> afterMoved) {
		Map map = beforeMoved.get(0).getMap();

		PlateList plateList = new PlateList(afterMoved);

		TectonicPlate mergedPlate = plateList.toMergedPlate();
		Queue<CellInfo> queue = new ArrayDeque<>();

		float[] heights = mergedPlate.heights;
		boolean[] isSet = mergedPlate.mask;

		CellDepthCalculator.forAllOnEdge(map, new CellDepthCalculator.IsOnEdge(mergedPlate), (x, y, i) -> {
			queue.add(new CellInfo(x, y, plateList.get(i), heights[i]));
		});

		while (!queue.isEmpty()) {
			CellInfo cell = queue.poll();

			enqueueIfNotSet(map, isSet, cell.x + 1, cell.y, cell, queue);
			enqueueIfNotSet(map, isSet, cell.x - 1, cell.y, cell, queue);
			enqueueIfNotSet(map, isSet, cell.x, cell.y + 1, cell, queue);
			enqueueIfNotSet(map, isSet, cell.x, cell.y - 1, cell, queue);
		}

		return afterMoved;
	}

	private static void enqueueIfNotSet(Map map, boolean[] isSet, int x, int y, CellInfo prevCell,
			Queue<CellInfo> queue) {
		int index = map.cellIndex(x, y);
		if (isSet[index])
			return;

		isSet[index] = true;

		float newHeight = (prevCell.height - TectonicsConfig.MIN_HEIGHT) * TectonicsConfig.DIVERGENCE_EXPONENT
				+ TectonicsConfig.MIN_HEIGHT;

		CellInfo newCell = new CellInfo(x, y, prevCell.plate, newHeight);

		newCell.plate.mask[index] = true;
		newCell.plate.heights[index] = newHeight;

		queue.add(newCell);
	}

	@AllArgsConstructor
	private static class CellInfo {
		public final int x, y;
		public final TectonicPlate plate;
		public final float height;
	}
}
