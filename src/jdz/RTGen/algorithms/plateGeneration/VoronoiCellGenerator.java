
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
 * @author Jaiden Baker
 */
class VoronoiCellGenerator extends TectonicPlateGenerator {
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

		List<PlatePoint> centers = getRandomPoints(numPlates, combined);

		for (int i = 0; i < numPlates; i++)
			plates.add(new TectonicPlate(map));

		map.forAllCells((x, y, i) -> {
			int plateIndex = getNearestPlate(centers, x, y);
			TectonicPlate plate = plates.get(plateIndex);
			plate.mask[i] = true;
			plate.heights[i] = map.cellHeight[i];
		});

		return plates;
	}

	private List<PlatePoint> getRandomPoints(int numPlates, TectonicPlate combined) {
		List<PlatePoint> randomPoints = new ArrayList<>(numPlates);

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

	private int getNearestPlate(List<PlatePoint> centers, int x, int y) {
		int neaerestPlate = -1;
		double nearestDistSquared = Double.MAX_VALUE;

		for (PlatePoint p : centers) {
			int dx = Math.abs(p.x - x);
			int dy = Math.abs(p.y - y);
			if (dx > map.width / 2)
				dx = map.width - dx;
			if (dy > map.width / 2)
				dy = map.width - dy;
			double dist = dx * dx + dy * dy;
			if (dist < nearestDistSquared) {
				neaerestPlate = p.index;
				nearestDistSquared = dist;
			}
		}

		return neaerestPlate;
	}

	@Data
	private class PlatePoint {
		private final int x, y;
		private final int index;
	}

}
