
package jdz.RTGen.algorithms.tectonics;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
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
			return (maskPlate.isInPlate(x, y) && (!maskPlate.isInPlate(x + 1, y) || !maskPlate.isInPlate(x - 1, y)
					|| !maskPlate.isInPlate(x, y + 1) || !maskPlate.isInPlate(x, y - 1)));
		}
	}

	@AllArgsConstructor
	public static class IsNextToPlate implements EdgeListPopulator {
		private final TectonicPlate maskPlate, secondPlate;
		
		@Override
		public boolean shouldPopulate(int x, int y) {
			return (maskPlate.isInPlate(x, y) && (secondPlate.isInPlate(x + 1, y) || secondPlate.isInPlate(x - 1, y)
					|| secondPlate.isInPlate(x, y + 1) || secondPlate.isInPlate(x, y - 1)));
		}
	}

	public static int[] getDistanceFromEdge(Map map, boolean[] mask, EdgeListPopulator populator) {
		TectonicPlate maskPlate = new TectonicPlate(map, mask, null, null, null);

		int[] dists = new int[mask.length];

		IntArrayFIFOQueue cellX = new IntArrayFIFOQueue();
		IntArrayFIFOQueue cellY = new IntArrayFIFOQueue();
		IntArrayFIFOQueue cellI = new IntArrayFIFOQueue();

		for (int x = 0; x < map.width; x++)
			for (int y = 0; y < map.height; y++)
				if (populator.shouldPopulate(x, y)) {
					int index = maskPlate.cellIndex(x, y);

					cellX.enqueue(x);
					cellY.enqueue(y);
					cellI.enqueue(index);

					dists[index] = 1;
				}

		while (!cellX.isEmpty()) {
			int i = cellI.dequeueInt();
			int x = cellX.dequeueInt();
			int y = cellY.dequeueInt();
			int z = dists[i] + 1;

			enqueueIfNotSet(maskPlate, dists, x + 1, y, z, cellX, cellY, cellI);
			enqueueIfNotSet(maskPlate, dists, x - 1, y, z, cellX, cellY, cellI);
			enqueueIfNotSet(maskPlate, dists, x, y + 1, z, cellX, cellY, cellI);
			enqueueIfNotSet(maskPlate, dists, x, y - 1, z, cellX, cellY, cellI);
		}

		return dists;
	}

	private static void enqueueIfNotSet(TectonicPlate p, int[] isSet, int x, int y, int z, IntArrayFIFOQueue xQ,
			IntArrayFIFOQueue yQ, IntArrayFIFOQueue iQ) {
		int index = p.cellIndex(x, y);
		if (!p.mask[index])
			return;
		if (isSet[index] == 0) {
			isSet[index] = z;
			xQ.enqueue(x);
			yQ.enqueue(y);
			iQ.enqueue(index);
		}
	}
}
