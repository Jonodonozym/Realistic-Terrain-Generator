
package jdz.RTGen.algorithms.plateGeneration;

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
	@Getter @Setter protected int numPlates;

	public void setAveragePlateArea(double area) {
		numPlates = (int) Math.ceil(map.getSize() / area);
	}

	public abstract List<TectonicPlate> generate();
}
