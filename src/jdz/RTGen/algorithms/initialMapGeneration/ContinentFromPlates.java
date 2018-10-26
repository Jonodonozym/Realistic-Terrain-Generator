
package jdz.RTGen.algorithms.initialMapGeneration;

import java.util.ArrayList;
import java.util.List;

import jdz.RTGen.algorithms.cellDepthCalculator.CellDepthCalculator;
import jdz.RTGen.algorithms.cellDepthCalculator.DepthFunction;
import jdz.RTGen.algorithms.cellDepthCalculator.EdgeListPopulator;
import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.PlateList;
import jdz.RTGen.dataType.TectonicPlate;

public class ContinentFromPlates extends ContinentGenerator {
	@Override
	protected Map generate() {
		TectonicPlateGenerator plateGen = TectonicPlateGenerator.getGenerator();

		if (ContinentGenConfig.RANDOMNESS < 2 || ContinentGenConfig.PERCENT_OCEAN <= 0
				|| ContinentGenConfig.PERCENT_OCEAN >= 100)
			return map;

		List<TectonicPlate> plates = plateGen.generatePlates(map, ContinentGenConfig.RANDOMNESS);

		List<TectonicPlate> pickedPlates = new ArrayList<>(ContinentGenConfig.RANDOMNESS);
		int requiredOceanCells = (int) (map.size * ContinentGenConfig.PERCENT_OCEAN / 100.f);
		int numCells = 0;

		while (numCells < requiredOceanCells) {
			TectonicPlate plate = plates.remove(random.nextInt(plates.size()));
			pickedPlates.add(plate);
			numCells += plate.numCells();
		}

		TectonicPlate continentalCells = new PlateList(pickedPlates).toMergedPlate();
		TectonicPlate oceanCells = continentalCells.clone().invertMask();

		float[] distToOcean = CellDepthCalculator.getDistanceFromEdge(map, continentalCells.mask,
				new EdgeListPopulator.IsNextToPlate(continentalCells, oceanCells), DepthFunction::sum);
		float[] distToLand = CellDepthCalculator.getDistanceFromEdge(map, oceanCells.mask,
				new EdgeListPopulator.IsNextToPlate(oceanCells, continentalCells), DepthFunction::sum);

		for (int i = 0; i < map.size; i++)
			if (continentalCells.mask[i])
				map.cellHeight[i] = (float) Math.pow(distToOcean[i] + 1f, 0.2);
			else
				map.cellHeight[i] = (float) -Math.sqrt(distToLand[i] + 1f);

		return map;
	}

}
