
package jdz.RTGen.algorithms.initialMapGeneration;

import java.util.ArrayList;
import java.util.List;

import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.tectonics.CellDepthCalculator;
import jdz.RTGen.algorithms.tectonics.TectonicCompression;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class ContinentGenerator extends InitialMapGenerator {

	@Override
	protected Map generate() {
		TectonicPlateGenerator plateGen = TectonicPlateGenerator.getRandom();
		List<TectonicPlate> plates = plateGen.generatePlates(map, 100);

		List<TectonicPlate> pickedPlates = new ArrayList<TectonicPlate>(100);
		for (TectonicPlate plate : plates)
			if (random.nextInt(10) < 3)
				pickedPlates.add(plate);

		TectonicPlate continentalCells = TectonicCompression.combineOtherPlates(pickedPlates, 0);
		TectonicPlate oceanCells = continentalCells.clone().invertMask();

		int[] distToOcean = CellDepthCalculator.getDistanceFromEdge(map, continentalCells.mask,
				new CellDepthCalculator.IsNextToPlate(continentalCells, oceanCells));
		int[] distToLand = CellDepthCalculator.getDistanceFromEdge(map, oceanCells.mask,
				new CellDepthCalculator.IsNextToPlate(oceanCells, continentalCells));

		for (int i = 0; i < map.size; i++) {
			if (continentalCells.mask[i])
				map.cellHeight[i] = (float) Math.log10(distToOcean[i]);
			else
				map.cellHeight[i] = (float) -Math.log10(distToLand[i] * 2 - 0.9);
		}

		return map;
	}

}
