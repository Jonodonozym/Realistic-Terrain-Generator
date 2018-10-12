
package jdz.RTGen.algorithms.plateGenerators;

import java.util.List;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class TectonicPlateGenerator {
	public static TectonicPlateGenerator getRandom(Map map) {
		return new RandomPlateGenerator(map);
	}
	
	@Getter protected final Map map;
	@Getter @Setter protected double averagePlateArea;

	public abstract List<TectonicPlate> generate();
}
