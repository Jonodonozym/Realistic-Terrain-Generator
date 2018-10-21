
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
public class TectonicSubduction {
	private static final float SUBDUCTION_DECAY_RATE = 0.9f;
	private static final float MIN_HEIGHT = -1000;

	public static List<TectonicPlate> performSubduction(List<TectonicPlate> beforeMoved,
			List<TectonicPlate> afterMoved) {
		Map map = beforeMoved.get(0).getMap();

		PlateList plateList = new PlateList(afterMoved);

		TectonicPlate mergedPlate = plateList.toMergedPlate();
		Queue<CellInfo> queue = new ArrayDeque<CellInfo>();

		CellDepthCalculator.forAllOnEdge(map, new CellDepthCalculator.IsOnEdge(mergedPlate), (x, y) -> {
			queue.add(new CellInfo(x, y, plateList.get(x, y), mergedPlate.getHeight(x, y)));
		});

		while (!queue.isEmpty()) {
			CellInfo cell = queue.poll();

			enqueueIfNotSet(map, mergedPlate, cell.x + 1, cell.y, cell, queue);
			enqueueIfNotSet(map, mergedPlate, cell.x - 1, cell.y, cell, queue);
			enqueueIfNotSet(map, mergedPlate, cell.x, cell.y + 1, cell, queue);
			enqueueIfNotSet(map, mergedPlate, cell.x, cell.y - 1, cell, queue);
		}

		return afterMoved;
	}

	private static void enqueueIfNotSet(Map map, TectonicPlate isSet, int x, int y, CellInfo prevCell,
			Queue<CellInfo> queue) {
		if (isSet.contains(x, y))
			return;

		isSet.addToPlate(x, y);

		float newHeight = (prevCell.height - MIN_HEIGHT) * SUBDUCTION_DECAY_RATE + MIN_HEIGHT;

		CellInfo newCell = new CellInfo(x, y, prevCell.plate, newHeight);

		newCell.plate.addToPlate(x, y, newHeight);

		queue.add(newCell);
	}

	@AllArgsConstructor
	private static class CellInfo {
		public final int x, y;
		public final TectonicPlate plate;
		public final float height;
	}
}
