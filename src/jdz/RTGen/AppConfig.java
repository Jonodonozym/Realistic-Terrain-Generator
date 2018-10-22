
package jdz.RTGen;

import java.util.Random;

import jdz.RTGen.configuration.Config;

public class AppConfig extends Config {
	public static int MAP_SEED = new Random().nextInt();

	public static int MAP_HEIGHT = 256;

	public static boolean RENDER_PLATES = false;
	public static boolean RENDER_BIOME = true;
	public static boolean RENDER_HEIGHT = true;
}
