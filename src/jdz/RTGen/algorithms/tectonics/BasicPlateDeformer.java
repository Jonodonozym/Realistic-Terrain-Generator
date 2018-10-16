
package jdz.RTGen.algorithms.tectonics;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import jdz.RTGen.dataType.TectonicPlate;

public class BasicPlateDeformer extends TectonicPlateDeformer {

	@Override
	protected List<TectonicPlate> initialize(List<TectonicPlate> plates) {
		float initialMagnitude = (float) (Math.sqrt(map.size) / 100f);

		for (TectonicPlate plate : plates) {
			float magnitude = initialMagnitude * (0.5f + random.nextFloat() * 0.5f);
			float direction = (float) (random.nextFloat() * 2.f * Math.PI);
			plate.velocity = new Point2D(Math.cos(direction) * magnitude, 0);
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

		movedPlates = TectonicCompression.performCollision(movedPlates);
		movedPlates = TectonicSubduction.performSubduction(plates, movedPlates);
		movedPlates = TectonicCollisionTrajectory.updateVelocity(movedPlates);

		return movedPlates;
	}
}
