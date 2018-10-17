
package jdz.RTGen.algorithms.biomeClassifier;

import static jdz.RTGen.dataType.Biome.*;

import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;
import lombok.Getter;
import lombok.Setter;

public class BiomeClassifier {
	@Getter @Setter private float seaLevel = 0;
	
	public Biome classify(Map map, int x, int y) {
		if (map.getHeight(x, y) <= seaLevel)
			return OCEAN;
		return GRASSLAND;
	}

}
