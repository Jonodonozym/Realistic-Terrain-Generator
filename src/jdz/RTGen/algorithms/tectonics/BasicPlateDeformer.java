
package jdz.RTGen.algorithms.tectonics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.geometry.Point2D;
import jdz.RTGen.configuration.Config;
import jdz.RTGen.dataType.TectonicPlate;
import lombok.Getter;

public class BasicPlateDeformer extends TectonicPlateDeformer {
	@Getter private final Config config = new TectonicsConfig();

	@Override
	protected List<TectonicPlate> initialize(List<TectonicPlate> plates) {
		return TectonicVelocityCalculator.randomizeVelocity(random, plates,
				map.height * 2.5f);
	}

	@Override
	protected boolean shouldStop(List<TectonicPlate> plates) {
		float totalSpeed = 0;
		for (TectonicPlate plate : plates)
			totalSpeed += plate.velocity.magnitude();
		return totalSpeed <= 1;
	}

	Point2D total = new Point2D(0, 0);

	@Override
	protected List<TectonicPlate> deform(List<TectonicPlate> plates) {
		List<TectonicPlate> movedPlates = new ArrayList<>();
		for (TectonicPlate plate : plates)
			movedPlates.add(plate.step());

		movedPlates = TectonicDivergence.performSubduction(plates, movedPlates);
		movedPlates = TectonicConvergence.performConvergence(movedPlates);
		movedPlates = TectonicVelocityCalculator.updateVelocityFromCollision(random, movedPlates);

		Collections.shuffle(movedPlates, random);

		return movedPlates;
	}
}
