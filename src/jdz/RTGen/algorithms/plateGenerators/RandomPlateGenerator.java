
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
public class RandomPlateGenerator extends TectonicPlateGenerator{
	private int[] indicies;

	public RandomPlateGenerator(Map map) {
		super(map, 100 * 100);
	}

	@Override
	public List<TectonicPlate> generate() {
		int numPlates = (int) Math.ceil(map.getSize() / averagePlateArea);
		List<TectonicPlate> plates = new ArrayList<TectonicPlate>();

		indicies = new int[map.getWidth() * map.getHeight()];
		for (int i = 0; i < indicies.length; i++)
			indicies[i] = -1;

		List<PlatePoint> floodQueue = getRandomPoints(numPlates);
		floodFill(floodQueue);

		for (int i = 0; i < numPlates; i++)
			plates.add(new TectonicPlate(map));
		
		int i = 0;
		for (int y = 0; y < map.getHeight(); y++)
			for (int x = 0; x < map.getWidth(); x++)
				plates.get(indicies[i++]).setInPlate(x, y);

		map.resetRandom();
		return plates;
	}
	
	private List<PlatePoint> getRandomPoints(int numPlates) {
		List<PlatePoint> randomPoints = new ArrayList<PlatePoint>();

		Random random = map.getRandom();
		for (int i = 0; i < numPlates; i++) {
			int x = random.nextInt(map.getWidth()), y = random.nextInt(map.getHeight());
			while (isSet(x, y)) {
				x = random.nextInt(map.getWidth());
				y = random.nextInt(map.getHeight());
			}

			randomPoints.add(new PlatePoint(x, y, i));
			set(x, y, i);
		}
		
		return randomPoints;
	}
	
	private void floodFill(List<PlatePoint> floodQueue) {
		Random random = map.getRandom();

		while (!floodQueue.isEmpty()) {
			PlatePoint point = floodQueue.remove(random.nextInt(floodQueue.size()));
			List<PlatePoint> points = getUnfilledNeighbours(point);

			for (PlatePoint pp : points) {
				set(pp.getX(), pp.getY(), pp.getIndex());
				floodQueue.add(pp);
			}
		}
	}

	private boolean isSet(int x, int y) {
		int index = y * map.getWidth() + x;
		return indicies[index] != -1;
	}

	private void set(int x, int y, int plate) {
		int index = y * map.getWidth() + x;
		indicies[index] = plate;
	}

	private List<PlatePoint> getUnfilledNeighbours(PlatePoint point) {
		List<PlatePoint> points = new ArrayList<>();

		points.add(new PlatePoint(point.getX() + 1, point.getY(), point.getIndex()));
		points.add(new PlatePoint(point.getX() - 1, point.getY(), point.getIndex()));
		points.add(new PlatePoint(point.getX(), point.getY() + 1, point.getIndex()));
		points.add(new PlatePoint(point.getX(), point.getY() - 1, point.getIndex()));

		
		points.removeIf((p) -> {
			return !p.isInMap() || isSet(p.getX(), p.getY());
		});

		return points;
	}

	@Data
	private class PlatePoint {
		private final int x, y;
		private final int index;

		public boolean isInMap() {
			return x >= 0 && y >= 0 && x < map.getWidth() && y < map.getHeight();
		}
	}

}
