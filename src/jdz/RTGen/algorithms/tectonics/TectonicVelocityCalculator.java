
package jdz.RTGen.algorithms.tectonics;

import java.util.List;
import java.util.Random;

import javafx.geometry.Point2D;
import jdz.RTGen.dataType.TectonicPlate;

public class TectonicVelocityCalculator {
	private static final float FRICTION_PER_TILE = 0.001f;

	public static List<TectonicPlate> updateVelocityFromCollision(List<TectonicPlate> plates) {
		return randomizeVelocity(new Random(System.currentTimeMillis()), plates, (float) (Math.sqrt(plates.get(0).getMap().size) / 100f)); 
	}

	public static List<TectonicPlate> randomizeVelocity(Random random, List<TectonicPlate> plates, float magnitude) {
		for (TectonicPlate plate : plates) {
			float plateMag = magnitude * (0.5f + random.nextFloat() * 0.5f);
			float direction = (float) (random.nextFloat() * 2.f * Math.PI);
			plate.setVelocity(Math.cos(direction) * plateMag, Math.sin(direction) * plateMag);
		}

		return plates;
	}

}
