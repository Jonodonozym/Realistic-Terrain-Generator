
package jdz.RTGen.algorithms.tectonics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.geometry.Point2D;
import jdz.RTGen.dataType.TectonicPlate;

public class BasicPlateDeformer extends TectonicPlateDeformer {

	@Override
	protected List<TectonicPlate> initialize(List<TectonicPlate> plates) {
		return TectonicVelocityCalculator.randomizeVelocity(new Random(), plates, (float) (Math.sqrt(map.size) / 100f));
	}

	@Override
	protected boolean shouldStop(List<TectonicPlate> plates) {
		float totalSpeed = 0;
		for (TectonicPlate plate : plates)
			totalSpeed += plate.velocity.magnitude();
		return totalSpeed <= 1;
	}

	Point2D total = new Point2D(0,0);
	
	@Override
	protected List<TectonicPlate> deform(List<TectonicPlate> plates) {
		List<TectonicPlate> movedPlates = new ArrayList<>();
		for (TectonicPlate plate : plates)
			movedPlates.add(plate.step());

		movedPlates = TectonicSubduction.performSubduction(plates, movedPlates);
		movedPlates = TectonicCompression.performCollision(movedPlates);
		movedPlates = TectonicVelocityCalculator.updateVelocityFromCollision(movedPlates);
		
		Collections.shuffle(movedPlates);

		return movedPlates;
	}
}
