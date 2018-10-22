
package jdz.RTGen.algorithms.tectonics;

import jdz.RTGen.dataType.Config;

public class TectonicsConfig extends Config {
	public static int STEPS = 25;

	public static float COMPRESSION_EXPONENT = 1.1f;
	public static float SUBDUCTION_EXPONENT = 0.9f;
	public static float MIN_HEIGHT = -10;
	public static float FRICTION_PER_CELL = 0.001f;
}
