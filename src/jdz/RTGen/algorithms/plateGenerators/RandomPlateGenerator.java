
package jdz.RTGen.algorithms.plateGenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jdz.RTGen.dataType.Map;
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
public class RandomPlateGenerator extends TectonicPlateGenerator {
	public RandomPlateGenerator(Map map) {
		super(map, 100 * 100);
	}

	@Override
	public List<TectonicPlate> generate() {
		int numPlates = (int) Math.ceil(map.getSize() / averagePlateArea);
		List<TectonicPlate> plates = new ArrayList<TectonicPlate>();

		TectonicPlate combined = new TectonicPlate(map);

		List<PlatePoint> floodQueue = getRandomPoints(numPlates, combined);
		floodFill(floodQueue, combined);

		for (int i = 0; i < numPlates; i++)
			plates.add(new TectonicPlate(map));

		for (int y = 0; y < map.getHeight(); y++)
			for (int x = 0; x < map.getWidth(); x++)
				plates.get((int) combined.getHeight(x, y)).setInPlate(x, y);

		map.resetRandom();
		return plates;
	}

	private List<PlatePoint> getRandomPoints(int numPlates, TectonicPlate combined) {
		List<PlatePoint> randomPoints = new ArrayList<PlatePoint>();

		Random random = map.getRandom();
		for (int i = 0; i < numPlates; i++) {
			int x = random.nextInt(map.getWidth()), y = random.nextInt(map.getHeight());
			while (combined.isInPlate(x, y)) {
				x = random.nextInt(map.getWidth());
				y = random.nextInt(map.getHeight());
			}

			randomPoints.add(new PlatePoint(x, y, i));
			combined.setInPlate(x, y);
			combined.setHeight(x, y, i);
		}

		return randomPoints;
	}

	private void floodFill(List<PlatePoint> floodQueue, TectonicPlate combined) {
		Random random = map.getRandom();

		while (!floodQueue.isEmpty()) {
			PlatePoint point = floodQueue.remove(random.nextInt(floodQueue.size()));
			List<PlatePoint> points = getUnfilledNeighbours(point, combined);

			for (PlatePoint pp : points) {
				combined.setInPlate(pp.getX(), pp.getY());
				combined.setHeight(pp.getX(), pp.getY(), pp.getIndex());
				floodQueue.add(pp);
			}
		}
	}

	private List<PlatePoint> getUnfilledNeighbours(PlatePoint point, TectonicPlate combined) {
		List<PlatePoint> points = new ArrayList<>();

		points.add(new PlatePoint(point.getX() + 1, point.getY(), point.getIndex()));
		points.add(new PlatePoint(point.getX() - 1, point.getY(), point.getIndex()));
		points.add(new PlatePoint(point.getX(), point.getY() + 1, point.getIndex()));
		points.add(new PlatePoint(point.getX(), point.getY() - 1, point.getIndex()));

		points.removeIf((p) -> {
			return combined.isInPlate(p.getX(), p.getY());
		});

		return points;
	}

	@Data
	private class PlatePoint {
		private final int x, y;
		private final int index;
	}

}
