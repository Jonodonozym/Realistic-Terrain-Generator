
package jdz.RTGen.algorithms.precipitation;

import jdz.RTGen.configuration.Config;
import jdz.RTGen.configuration.Step;

public class PrecipitationConfig extends Config {
	public static float LATTITUDE_EFFECT_PERCENT = 50;
	@Step(0.1f) public static float MOISTURE_LOSS_RATE = 0.5f;
	public static boolean USE_NOISE = false;
	public static float NOISE_VARIENCE = 5;
}
