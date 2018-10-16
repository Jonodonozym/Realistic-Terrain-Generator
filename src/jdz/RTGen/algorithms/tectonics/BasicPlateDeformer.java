
package jdz.RTGen.algorithms.tectonics;

import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import javafx.geometry.Point2D;
import jdz.RTGen.dataType.TectonicPlate;

public class BasicPlateDeformer extends TectonicPlateDeformer {
	private static final float INITIAL_MAGNITUDE = 10;
	private static final float FRICTION_PER_TILE = 0.001f;

	@Override
	protected List<TectonicPlate> initialize(List<TectonicPlate> plates) {
		for (TectonicPlate plate : plates) {
			float direction = (float) (random.nextFloat() * 2.f * Math.PI);
			plate.velocity = new Point2D(Math.cos(direction) * INITIAL_MAGNITUDE,
					Math.sin(direction) * INITIAL_MAGNITUDE);
		}

		return plates;
	}

	@Override
	protected boolean shouldStop(List<TectonicPlate> plates) {
		float totalSpeed = 0;
		for (TectonicPlate plate : plates)
			totalSpeed += plate.velocity.magnitude();
		return totalSpeed <= 1;
	}

	@Override
	protected List<TectonicPlate> deform(List<TectonicPlate> plates) {

		List<TectonicPlate> movedPlates = new ArrayList<>();
		for (TectonicPlate plate : plates)
			movedPlates.add(plate.step());

		movedPlates = performCompression(movedPlates);
		movedPlates = performSubduction(plates, movedPlates);

		return movedPlates;
	}

	private List<TectonicPlate> performCompression(List<TectonicPlate> plates) {
		boolean[] isProcessed = new boolean[map.getSize()];

		for (int p = 0; p < plates.size(); p++) {
			TectonicPlate plate = plates.get(p);
			TectonicPlate combined = combineOtherPlates(plates, p);

			boolean[] isOverlap = plate.getMasksOverlap(combined);
			TectonicPlate maskPlate = new TectonicPlate(map, isOverlap, null, null, null);
			int[] distances = getDistanceFromEdge(isOverlap, (x, y) -> {
				return (maskPlate.isInPlate(x, y) && (!maskPlate.isInPlate(x + 1, y) || !maskPlate.isInPlate(x - 1, y)
						|| !maskPlate.isInPlate(x, y + 1) || !maskPlate.isInPlate(x, y - 1)));
			});

			for (int i = 0; i < isOverlap.length; i++) {
				if (!isOverlap[i] || isProcessed[i])
					continue;

				isProcessed[i] = true;
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

	private TectonicPlate combineOtherPlates(List<TectonicPlate> plates, int startIndex) {
		TectonicPlate combined = new TectonicPlate(plates.get(0).getMap());

		for (int i = startIndex; i < plates.size(); i++)
			setRegionHeight(combined, plates.get(i).mask, i);

		return combined;
	}

	private void setRegionHeight(TectonicPlate plate, boolean[] mask, int value) {
		for (int i = 0; i < mask.length; i++)
			if (mask[i]) {
				plate.mask[i] = true;
				plate.heights[i] = value;
			}
	}

	private int[] getDistanceFromEdge(boolean[] mask, EdgeListPopulator populator) {
		TectonicPlate maskPlate = new TectonicPlate(map, mask, null, null, null);

		int[] dists = new int[mask.length];

		IntArrayFIFOQueue cellX = new IntArrayFIFOQueue();
		IntArrayFIFOQueue cellY = new IntArrayFIFOQueue();
		IntArrayFIFOQueue cellI = new IntArrayFIFOQueue();

		for (int x = 0; x < map.getWidth(); x++)
			for (int y = 0; y < map.getHeight(); y++)
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

	private void enqueueIfNotSet(TectonicPlate p, int[] isSet, int x, int y, int z, IntArrayFIFOQueue xQ,
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

	private interface EdgeListPopulator {
		public boolean shouldPopulate(int x, int y);
	}

	private List<TectonicPlate> performSubduction(List<TectonicPlate> beforeMoved, List<TectonicPlate> afterMoved) {
		boolean[] combinedSubduction = new boolean[beforeMoved.get(0).getMap().getSize()];

		for (int i = 0; i < beforeMoved.size(); i++) {
			TectonicPlate before = beforeMoved.get(i);
			TectonicPlate after = afterMoved.get(i);

			boolean[] subductionMask = before.clone().chopOverlap(after.mask).mask;

			TectonicPlate maskPlate = new TectonicPlate(map, subductionMask, null, null, null);

			int[] dists = getDistanceFromEdge(subductionMask, (x, y) -> {
				return (maskPlate.isInPlate(x, y) && (after.isInPlate(x + 1, y) || after.isInPlate(x - 1, y)
						|| after.isInPlate(x, y + 1) || after.isInPlate(x, y - 1)));
			});

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
