
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
			return maskPlate.isInPlate(x, y) && !(maskPlate.isInPlate(x + 1, y) && maskPlate.isInPlate(x - 1, y)
					&& maskPlate.isInPlate(x, y + 1) && maskPlate.isInPlate(x, y - 1));
		}
	}

	@AllArgsConstructor
	public static class IsNextToPlate implements EdgeListPopulator {
		private final TectonicPlate maskPlate, secondPlate;

		@Override
		public boolean shouldPopulate(int x, int y) {
			return maskPlate.isInPlate(x, y) && (secondPlate.isInPlate(x + 1, y) || secondPlate.isInPlate(x - 1, y)
					|| secondPlate.isInPlate(x, y + 1) || secondPlate.isInPlate(x, y - 1));
		}
	}

	public static void forAllOnEdge(Map map, EdgeListPopulator populator, CellIterator iterator) {
		map.forAllCells((x, y, index) -> {
			if (populator.shouldPopulate(x, y))
				iterator.execute(x, y, index);
		});
	}

	public static boolean[] getEdgeMask(Map map, EdgeListPopulator populator) {
		boolean[] mask = new boolean[map.size];

		map.forAllCells((x, y, index) -> {
			if (populator.shouldPopulate(x, y))
				mask[index] = true;
		});

		return mask;
	}

	public static int[] getDistanceFromEdge(Map map, boolean[] mask, EdgeListPopulator populator) {
		TectonicPlate maskPlate = new TectonicPlate(map, mask, null, null, null);

		IntArrayFIFOQueue cellX = new IntArrayFIFOQueue();
		IntArrayFIFOQueue cellY = new IntArrayFIFOQueue();
		IntArrayFIFOQueue cellI = new IntArrayFIFOQueue();

		int[] dists = populateArrays(map, populator, cellX, cellY, cellI);

		while (!cellX.isEmpty()) {
			int i = cellI.dequeueInt();
			int x = cellX.dequeueInt();
			int y = cellY.dequeueInt();
			int depth = dists[i] + 1;

			enqueueIfNotSet(maskPlate, dists, x + 1, y, depth, cellX, cellY, cellI);
			enqueueIfNotSet(maskPlate, dists, x - 1, y, depth, cellX, cellY, cellI);
			enqueueIfNotSet(maskPlate, dists, x, y + 1, depth, cellX, cellY, cellI);
			enqueueIfNotSet(maskPlate, dists, x, y - 1, depth, cellX, cellY, cellI);
		}

		return dists;
	}

	public static int[] populateArrays(Map map, EdgeListPopulator populator, IntArrayFIFOQueue cellX,
			IntArrayFIFOQueue cellY, IntArrayFIFOQueue cellI) {
		int[] dists = new int[map.size];

		forAllOnEdge(map, populator, (x, y, index) -> {
			cellX.enqueue(x);
			cellY.enqueue(y);
			cellI.enqueue(index);

			dists[index] = 1;
		});

		return dists;
	}

	private static void enqueueIfNotSet(TectonicPlate p, int[] isSet, int x, int y, int depth, IntArrayFIFOQueue xQ,
			IntArrayFIFOQueue yQ, IntArrayFIFOQueue iQ) {
		int index = p.cellIndex(x, y);
		if (!p.mask[index])
			return;
		if (isSet[index] == 0) {
			isSet[index] = depth;
			xQ.enqueue(x);
			yQ.enqueue(y);
			iQ.enqueue(index);
		}
	}
}
