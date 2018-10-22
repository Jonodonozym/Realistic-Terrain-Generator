
package jdz.RTGen.algorithms.biomeClassifier;

import java.util.Arrays;
import java.util.List;

import jdz.RTGen.algorithms.initialMapGeneration.ContinentGenConfig;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class SeaLevelCalculator {

	public static float updateSeaLevel(Map map) {
		float[] sortedHeights = new float[map.size];
		System.arraycopy(map.cellHeight, 0, sortedHeights, 0, map.size);
		Arrays.sort(sortedHeights);

		float percentOcean = getPercentOcean();
		float seaLevel = sortedHeights[(int) (map.size * percentOcean)];
		map.setSeaLevel(seaLevel);
		return seaLevel;
	}

	public static float updateSeaLevel(Map map, List<TectonicPlate> plates) {
		map.setPlates(plates);
		map.updateHeightFromPlates();
		return updateSeaLevel(map);
	}

	public static float getPercentOcean() {
		return ContinentGenConfig.PERCENT_OCEAN / 100.f;
	}
}
