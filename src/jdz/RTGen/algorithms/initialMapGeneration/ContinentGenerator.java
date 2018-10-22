
package jdz.RTGen.algorithms.initialMapGeneration;

import java.util.ArrayList;
import java.util.List;

import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.tectonics.CellDepthCalculator;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.PlateList;
import jdz.RTGen.dataType.TectonicPlate;

public class ContinentGenerator extends InitialMapGenerator {
	@Override
	protected Map generate() {
		TectonicPlateGenerator plateGen = TectonicPlateGenerator.getRandom();

		if (InitialMapGenConfig.RANDOMNESS < 2 || InitialMapGenConfig.PERCENT_OCEAN <= 0
				|| InitialMapGenConfig.PERCENT_OCEAN >= 100)
			return map;

		List<TectonicPlate> plates = plateGen.generatePlates(map, InitialMapGenConfig.RANDOMNESS);

		List<TectonicPlate> pickedPlates = new ArrayList<>(InitialMapGenConfig.RANDOMNESS);
		int requiredOceanCells = (int)(map.size * InitialMapGenConfig.PERCENT_OCEAN/100.f);
		int numCells = 0;
		
		while (numCells < requiredOceanCells) {
			TectonicPlate plate = plates.remove(random.nextInt(plates.size()));
			pickedPlates.add(plate);
			numCells += plate.numCells();
		}

		TectonicPlate continentalCells = new PlateList(pickedPlates).toMergedPlate();
		TectonicPlate oceanCells = continentalCells.clone().invertMask();

		int[] distToOcean = CellDepthCalculator.getDistanceFromEdge(map, continentalCells.mask,
				new CellDepthCalculator.IsNextToPlate(continentalCells, oceanCells));
		int[] distToLand = CellDepthCalculator.getDistanceFromEdge(map, oceanCells.mask,
				new CellDepthCalculator.IsNextToPlate(oceanCells, continentalCells));

		for (int i = 0; i < map.size; i++)
			if (continentalCells.mask[i])
				map.cellHeight[i] = (float) Math.log10(distToOcean[i]);
			else
				map.cellHeight[i] = (float) -Math.log10(distToLand[i] / 20.f + 0.9) * 10.f;
		
		return map;
	}

}
