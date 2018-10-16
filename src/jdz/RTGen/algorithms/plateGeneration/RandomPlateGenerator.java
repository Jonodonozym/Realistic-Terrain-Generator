
package jdz.RTGen.algorithms.plateGeneration;

import java.util.ArrayList;
import java.util.List;

import jdz.RTGen.dataType.TectonicPlate;
import lombok.Data;

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
 * @author Jaiden Baker
 */
class RandomPlateGenerator extends TectonicPlateGenerator {

	@Override
	protected List<TectonicPlate> generate() {
		List<TectonicPlate> plates = new ArrayList<TectonicPlate>();

		TectonicPlate combined = new TectonicPlate(map);

		List<PlatePoint> floodQueue = getRandomPoints(numPlates, combined);
		floodFill(floodQueue, combined);

		for (int i = 0; i < numPlates; i++)
			plates.add(new TectonicPlate(map));

		for (int y = 0; y < map.getHeight(); y++)
			for (int x = 0; x < map.getWidth(); x++)
				plates.get((int) combined.getHeight(x, y)).addToPlate(x, y, map.getHeight(x, y));

		return plates;
	}

	private List<PlatePoint> getRandomPoints(int numPlates, TectonicPlate combined) {
		List<PlatePoint> randomPoints = new ArrayList<PlatePoint>();

		for (int i = 0; i < numPlates; i++) {
			int x = random.nextInt(map.getWidth()), y = random.nextInt(map.getHeight());
			while (combined.isInPlate(x, y)) {
				x = random.nextInt(map.getWidth());
				y = random.nextInt(map.getHeight());
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
		List<PlatePoint> points = point.getAdjacent();

		points.removeIf((p) -> {
			return p.isIn(combined);
		});

		return points;
	}

	@Data
	private class PlatePoint {
		private final int x, y;
		private final int index;

		public List<PlatePoint> getAdjacent() {
			List<PlatePoint> points = new ArrayList<>();
			points.add(new PlatePoint(x + 1, y, index));
			points.add(new PlatePoint(x - 1, y, index));
			points.add(new PlatePoint(x, y + 1, index));
			points.add(new PlatePoint(x, y - 1, index));
			return points;
		}

		public boolean isIn(TectonicPlate plate) {
			return plate.isInPlate(x, y);
		}
	}

}
