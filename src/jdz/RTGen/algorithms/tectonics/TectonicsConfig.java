
package jdz.RTGen.algorithms.tectonics;

import jdz.RTGen.configuration.Config;
import jdz.RTGen.configuration.Step;

public class TectonicsConfig extends Config {
	@Step(1) public static int STEPS = 25;
	
	public static float CONVERGENCE_EXPONENT = 1.1f;
	public static float DIVERGENCE_EXPONENT = 0.9f;
	public static float MIN_HEIGHT = -10;

	@Step(0.01f) public static float INITIAL_VELOCITY = 0.01f;
	public static float FRICTION_PER_CELL = 0.001f;
}
