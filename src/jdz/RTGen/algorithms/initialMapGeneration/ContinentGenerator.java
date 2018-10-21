
package jdz.RTGen.algorithms.initialMapGeneration;

import java.util.ArrayList;
import java.util.List;

import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.tectonics.CellDepthCalculator;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.PlateList;
import jdz.RTGen.dataType.TectonicPlate;

public class ContinentGenerator extends InitialMapGenerator {
	private static int DUMMY_PLATES = 250;
	private static int PERCENT_LAND = 30;

	@Override
	protected Map generate() {
		TectonicPlateGenerator plateGen = TectonicPlateGenerator.getRandom();
		List<TectonicPlate> plates = plateGen.generatePlates(map, DUMMY_PLATES);

		List<TectonicPlate> pickedPlates = new ArrayList<TectonicPlate>(DUMMY_PLATES);
		for (TectonicPlate plate : plates)
			if (random.nextInt(100) < PERCENT_LAND)
				pickedPlates.add(plate);

		TectonicPlate continentalCells = new PlateList(pickedPlates).toMergedPlate();
		TectonicPlate oceanCells = continentalCells.clone().getInvertedMask();

		TectonicPlate distToOcean = CellDepthCalculator.getDistanceFromEdge(map, continentalCells,
				new CellDepthCalculator.IsNextToPlate(continentalCells, oceanCells));
		TectonicPlate distToLand = CellDepthCalculator.getDistanceFromEdge(map, oceanCells,
				new CellDepthCalculator.IsNextToPlate(oceanCells, continentalCells));

		continentalCells.forEachCell((x, y) -> {
			map.setHeight(x, y, (float) Math.log10(distToOcean.getHeight(x, y)));
		});

		oceanCells.forEachCell((x, y) -> {
			map.setHeight(x, y, (float) -Math.log10(distToLand.getHeight(x, y) * 2 - 0.9));
		});

		return map;
	}

}
