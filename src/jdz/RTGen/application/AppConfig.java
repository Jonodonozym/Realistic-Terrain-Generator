
package jdz.RTGen.application;

import java.util.Random;

import jdz.RTGen.dataType.Config;

public class AppConfig extends Config {
	public static long MAP_SEED = new Random().nextInt();
	
	public static boolean RENDER_PLATES = true;
	public static boolean RENDER_BIOME = true;
	public static boolean RENDER_HEIGHT = true;
}
