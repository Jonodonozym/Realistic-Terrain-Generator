
package jdz.RTGen.algorithms.tectonics;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import jdz.RTGen.dataType.CellIterator;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;
import lombok.AllArgsConstructor;

public class CellDepthCalculator {
	public static interface EdgeListPopulator {
		public boolean shouldPopulate(int x, int y);
	}

	@AllArgsConstructor
	public static class IsOnEdge implements EdgeListPopulator {
		private final TectonicPlate maskPlate;

		@Override
		public boolean shouldPopulate(int x, int y) {
			return (maskPlate.contains(x, y) && (!maskPlate.contains(x + 1, y) || !maskPlate.contains(x - 1, y)
					|| !maskPlate.contains(x, y + 1) || !maskPlate.contains(x, y - 1)));
		}
	}

	@AllArgsConstructor
	public static class IsNextToPlate implements EdgeListPopulator {
		private final TectonicPlate maskPlate, secondPlate;

		@Override
		public boolean shouldPopulate(int x, int y) {
			return (maskPlate.contains(x, y) && (secondPlate.contains(x + 1, y) || secondPlate.contains(x - 1, y)
					|| secondPlate.contains(x, y + 1) || secondPlate.contains(x, y - 1)));
		}
	}

	public static void forAllOnEdge(Map map, EdgeListPopulator populator, CellIterator iterator) {
		map.forAllCells((x, y) -> {
			if (populator.shouldPopulate(x, y))
				iterator.execute(x, y);
		});
	}

	public static TectonicPlate getEdgeMask(Map map, EdgeListPopulator populator) {
		TectonicPlate maskPlate = new TectonicPlate(map);

		map.forAllCells((x, y) -> {
			if (populator.shouldPopulate(x, y))
				maskPlate.addToPlate(x, y);
		});

		return maskPlate;
	}

	public static TectonicPlate getDistanceFromEdge(Map map, TectonicPlate maskPlate, EdgeListPopulator populator) {
		IntArrayFIFOQueue cellX = new IntArrayFIFOQueue();
		IntArrayFIFOQueue cellY = new IntArrayFIFOQueue();

		TectonicPlate dists = populateArrays(map, populator, cellX, cellY);

		while (!cellX.isEmpty()) {
			int x = cellX.dequeueInt();
			int y = cellY.dequeueInt();
			int depth = (int) dists.getHeight(x, y) + 1;

			enqueueIfNotSet(maskPlate, dists, x + 1, y, depth, cellX, cellY);
			enqueueIfNotSet(maskPlate, dists, x - 1, y, depth, cellX, cellY);
			enqueueIfNotSet(maskPlate, dists, x, y + 1, depth, cellX, cellY);
			enqueueIfNotSet(maskPlate, dists, x, y - 1, depth, cellX, cellY);
		}

		return dists;
	}

	public static TectonicPlate populateArrays(Map map, EdgeListPopulator populator, IntArrayFIFOQueue cellX,
			IntArrayFIFOQueue cellY) {
		TectonicPlate plate = new TectonicPlate(map);

		forAllOnEdge(map, populator, (x, y) -> {
			cellX.enqueue(x);
			cellY.enqueue(y);

			plate.setHeight(x, y, 1);
		});

		return plate;
	}

	private static void enqueueIfNotSet(TectonicPlate maskPlate, TectonicPlate distsPlate, int x, int y, int depth,
			IntArrayFIFOQueue xQ, IntArrayFIFOQueue yQ) {
		if (!maskPlate.contains(x, y))
			return;
		if (!distsPlate.contains(x, y)) {
			distsPlate.setHeight(x, y, depth);
			xQ.enqueue(x);
			yQ.enqueue(y);
		}
	}
}
