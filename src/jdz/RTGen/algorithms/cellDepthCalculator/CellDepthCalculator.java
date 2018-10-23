
package jdz.RTGen.algorithms.cellDepthCalculator;

import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import jdz.RTGen.dataType.CellIterator;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;
import lombok.Data;

public class CellDepthCalculator {
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

	public static float[] getDistanceFromEdge(Map map, boolean[] mask, EdgeListPopulator populator, DepthFunction f) {
		TectonicPlate maskPlate = new TectonicPlate(map, mask, null, null, null);

		ObjectArrayFIFOQueue<PlatePoint> queue = new ObjectArrayFIFOQueue<>(mask.length);

		float[] dists = new float[map.size];
		for (int i = 0; i < map.size; i++)
			if (mask[i])
				dists[i] = Float.MAX_VALUE;

		forAllOnEdge(map, populator, (x, y, index) -> {
			queue.enqueue(new PlatePoint(x, y, index, x, y, 0));
		});

		while (!queue.isEmpty()) {
			PlatePoint p = queue.dequeue();

			if (dists[p.i] > p.depth) {
				dists[p.i] = p.depth;

				enqueue(maskPlate, p.x + 1, p.y, p, queue, f);
				enqueue(maskPlate, p.x - 1, p.y, p, queue, f);
				enqueue(maskPlate, p.x, p.y + 1, p, queue, f);
				enqueue(maskPlate, p.x, p.y - 1, p, queue, f);
			}
		}

		return dists;
	}

	private static void enqueue(TectonicPlate plate, int x, int y, PlatePoint p, ObjectArrayFIFOQueue<PlatePoint> queue,
			DepthFunction f) {
		int index = plate.cellIndex(x, y);
		if (!plate.mask[index])
			return;
		float depth = f.getDepth(p.x - p.edgeX, p.y - p.edgeY);
		queue.enqueue(new PlatePoint(x, y, index, p.edgeX, p.edgeY, depth));
	}

	@Data
	private static class PlatePoint {
		public final int x, y, i, edgeX, edgeY;
		public final float depth;
	}
}
