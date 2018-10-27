
package jdz.RTGen.algorithms.tectonics;

import jdz.RTGen.configuration.Config;
import jdz.RTGen.configuration.Ignore;
import jdz.RTGen.configuration.Step;

public class TectonicsConfig extends Config {
	@Step(1) public static int STEPS = 25;
	
	@Ignore public static float CONVERGENCE_EXPONENT = 1.1f;
	@Ignore public static float DIVERGENCE_EXPONENT = 0.9f;
	@Ignore public static float MIN_HEIGHT = -10;

	@Step(1) public static float AVERAGE_SPEED = 10;
	public static float FRICTION_PER_CELL = 0.001f;
}
