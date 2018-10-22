
package jdz.RTGen.algorithms.temperature;

import java.util.Random;

import jdz.RTGen.dataType.Config;
import jdz.RTGen.dataType.Configurable;
import jdz.RTGen.dataType.Map;
import lombok.Getter;

public abstract class TemperatureModel extends Configurable {
	public static TemperatureModel equatorAndHeight() {
		return new EquatorAndHeight();
	}

	@Getter private Config config = new Config() {};

	public abstract void apply(Map map, Random random);
}
