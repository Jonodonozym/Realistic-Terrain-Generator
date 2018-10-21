
package jdz.RTGen.algorithms.temperature;

import java.util.Random;

import jdz.RTGen.dataType.Map;

public interface TemperatureModel {
	public static TemperatureModel equatorAndHeight() {
		return new EquatorAndHeight();
	}
	
	public void apply(Map map, Random random);
}
