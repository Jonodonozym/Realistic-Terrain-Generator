
package jdz.RTGen.rendering;

import java.util.ArrayList;
import java.util.List;

import jdz.RTGen.configuration.Config;
import jdz.RTGen.configuration.Configurable;
import jdz.RTGen.rendering.renderers.BiomeRenderer;
import jdz.RTGen.rendering.renderers.HeightMapRenderer;
import jdz.RTGen.rendering.renderers.PlateListRenderer;
import jdz.RTGen.rendering.renderers.PrecipitationRenderer;
import jdz.RTGen.rendering.renderers.TemperatureRenderer;
import lombok.Getter;

public class ActiveRenderers extends Configurable {
	@Getter private final Config config = new RenderConfig();
	@Getter private final String name = "Rendering";

	public static List<Renderer> getRenderers() {
		List<Renderer> renderers = new ArrayList<>();
		if (RenderConfig.BIOME)
			renderers.add(new BiomeRenderer());
		if (RenderConfig.HEIGHT)
			renderers.add(new HeightMapRenderer());
		if (RenderConfig.PLATES)
			renderers.add(new PlateListRenderer());
		if (RenderConfig.PRECIPITATION)
			renderers.add(new PrecipitationRenderer());
		if (RenderConfig.TEMPERATURE)
			renderers.add(new TemperatureRenderer());
		return renderers;
	}

	private static class RenderConfig extends Config {
		public static boolean PLATES = false;
		public static boolean BIOME = true;
		public static boolean HEIGHT = true;
		public static boolean PRECIPITATION = false;
		public static boolean TEMPERATURE = false;
	}
}
