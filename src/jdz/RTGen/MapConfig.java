
package jdz.RTGen;

import java.util.Random;

import jdz.RTGen.configuration.Config;

public class MapConfig extends Config {
	public static int MAP_SEED = new Random().nextInt();

	public static int MAP_HEIGHT = 256;
}
