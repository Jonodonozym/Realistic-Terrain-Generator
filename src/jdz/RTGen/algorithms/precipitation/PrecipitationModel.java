
package jdz.RTGen.algorithms.precipitation;

import java.util.Random;

import jdz.RTGen.dataType.Config;
import jdz.RTGen.dataType.Configurable;
import jdz.RTGen.dataType.Map;
import lombok.Getter;

public abstract class PrecipitationModel extends Configurable {
	public static PrecipitationModel oceanDistance() {
		return new OceanDistance();
	}

	@Getter private Config config = new Config() {};
	
	public abstract void apply(Map map, Random random);
}
