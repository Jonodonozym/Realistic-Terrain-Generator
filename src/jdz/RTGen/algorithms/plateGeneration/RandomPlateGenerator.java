
package jdz.RTGen.algorithms.plateGeneration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdz.RTGen.configuration.Config;
import jdz.RTGen.dataType.TectonicPlate;
import lombok.Data;
import lombok.Getter;

/**
 * Inspiration from here:
 * http://experilous.com/1/blog/post/procedural-planet-generation
 *
 * Randomly generates techtonic plates by:
 * - Get the number of plates (n) by map size / averagePlateArea
 * - Pick n random points in the map. These represent one plate each.
 * - Randomly flood fill the map for all points untill all locations on the map
 * belong to a plate.
 *
 * TODO change queue list to balanced binary tree
 * @author Jaiden Baker
 */
class RandomPlateGenerator extends TectonicPlateGenerator {
	@Getter private final Config config = new PlateGenConfig();

	@Override
	protected List<TectonicPlate> generate(int numPlates) {
		List<TectonicPlate> plates = new ArrayList<>();

		if (numPlates < 2) {
			TectonicPlate mapPlate = new TectonicPlate(map);
			Arrays.fill(mapPlate.mask, true);
			map.forAllCells((i) -> {
				mapPlate.heights[i] = map.cellHeight[i];
			});
			plates.add(mapPlate);
			return plates;
		}

		TectonicPlate combined = new TectonicPlate(map);

		List<PlatePoint> floodQueue = getRandomPoints(numPlates, combined);
		floodFill(floodQueue, combined);

		for (int i = 0; i < numPlates; i++)
			plates.add(new TectonicPlate(map));

		map.forAllCells((x, y, i) -> {
			TectonicPlate plate = plates.get((int) combined.heights[i]);
			plate.mask[i] = true;
			plate.heights[i] = map.cellHeight[i];
		});

		return plates;
	}

	private List<PlatePoint> getRandomPoints(int numPlates, TectonicPlate combined) {
		List<PlatePoint> randomPoints = new ArrayList<>(map.size);

		for (int i = 0; i < numPlates; i++) {
			int x = random.nextInt(map.width), y = random.nextInt(map.height);
			while (combined.isInPlate(x, y)) {
				x = random.nextInt(map.width);
				y = random.nextInt(map.height);
			}

			randomPoints.add(new PlatePoint(x, y, i));
			combined.addToPlate(x, y, i);
		}

		return randomPoints;
	}

	private void floodFill(List<PlatePoint> floodQueue, TectonicPlate combined) {
		while (!floodQueue.isEmpty()) {
			PlatePoint point = floodQueue.remove(random.nextInt(floodQueue.size()));
			List<PlatePoint> points = getUnfilledNeighbours(point, combined);

			for (PlatePoint pp : points) {
				combined.addToPlate(pp.getX(), pp.getY(), pp.getIndex());
				floodQueue.add(pp);
			}
		}
	}

	private List<PlatePoint> getUnfilledNeighbours(PlatePoint point, TectonicPlate combined) {
		return point.getAdjacent(combined, PlateGenConfig.RUGGEDNESS);
	}

	@Data
	private class PlatePoint {
		private final int x, y;
		private final int index;

		public List<PlatePoint> getAdjacent(TectonicPlate combined, int ruggedness) {
			List<PlatePoint> points = new ArrayList<>(4);

//			if (ruggedness < 1) {
				addPoint(points, combined, x + 1, y);
				addPoint(points, combined, x - 1, y);
				addPoint(points, combined, x, y + 1);
				addPoint(points, combined, x, y - 1);
//				return points;
//			}

//			int x1 = this.x - ruggedness;
//			int x2 = this.x + ruggedness;
//			for (int d = 0; d < ruggedness; d++) {
//				for (int y = this.y - d; y < this.y + d; y++) {
//					addPoint(points, combined, x1, y);
//					addPoint(points, combined, x2, y);
//				}
//				x1++;
//				x2--;
//			}
			return points;
		}

		private void addPoint(List<PlatePoint> points, TectonicPlate combined, int x, int y) {
			if (!combined.isInPlate(x, y))
				points.add(new PlatePoint(x, y, index));
		}
	}

}
