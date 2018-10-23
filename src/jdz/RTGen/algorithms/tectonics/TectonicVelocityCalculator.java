
package jdz.RTGen.algorithms.tectonics;

import java.util.List;
import java.util.Random;

import javafx.geometry.Point2D;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class TectonicVelocityCalculator {
	public static List<TectonicPlate> updateVelocityFromCollision(Random random, List<TectonicPlate> plates) {
		Map map = plates.get(0).getMap();
		return randomizeVelocity(random, plates, map.height * TectonicsConfig.AVERAGE_SPEED / 1000f);
	}

	public static List<TectonicPlate> randomizeVelocity(Random random, List<TectonicPlate> plates, float magnitude) {
		for (TectonicPlate plate : plates) {
			float plateMag = magnitude * (0.5f + random.nextFloat() * 0.5f);
			float direction = (float) (random.nextFloat() * 2.f * Math.PI);
			plate.velocity = new Point2D(Math.cos(direction) * plateMag, Math.sin(direction) * plateMag);
		}

		return plates;
	}

}
