
package jdz.RTGen.algorithms.precipitation;

import java.util.Random;
import jdz.RTGen.dataType.Map;

public interface PrecipitationModel {

	public static PrecipitationModel oceanDistance() {
		return new OceanDistance();
	}
	
	public void apply(Map map, Random random);
}
