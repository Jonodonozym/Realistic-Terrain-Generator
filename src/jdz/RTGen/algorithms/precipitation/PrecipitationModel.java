
package jdz.RTGen.algorithms.precipitation;

import java.util.Random;

import jdz.RTGen.configuration.Config;
import jdz.RTGen.configuration.Configurable;
import jdz.RTGen.dataType.Map;
import lombok.Getter;

public abstract class PrecipitationModel extends Configurable {
	public static final float MAX_PRECIPITATION = 450;
	
	public static PrecipitationModel getModel() {
		if (PrecipitationConfig.USE_NOISE)
			return new NoisePrecipitation();
		return new OceanDistance();
	}
	

	@Getter private Config config = new PrecipitationConfig();

	public abstract void apply(Map map, Random random);
}
