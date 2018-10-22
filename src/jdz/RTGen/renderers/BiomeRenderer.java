
package jdz.RTGen.renderers;

import static jdz.RTGen.dataType.Biome.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;

public class BiomeRenderer extends Renderer {
	Reference2IntMap<Biome> colorMap = new Reference2IntOpenHashMap<>();

	public BiomeRenderer() {
		colorMap.put(OCEAN, 0x0077be); // Blue

		colorMap.put(TROPICAL_RAINFOREST, 0x008439);
		colorMap.put(TEMPERATE_RAINFOREST, 0x005123);

		colorMap.put(TROPICAL_SEASONAL_FOREST, 0x0d7500);
		colorMap.put(TEMPERATE_FOREST, 0x095000);

		colorMap.put(WOODLAND, 0x446325);
		colorMap.put(SHRUBLAND, 0x76b735);
		colorMap.put(GRASSLAND, 0x5bc63d);

		colorMap.put(THORN_FOREST, 0x9b7d25);
		colorMap.put(THORN_SCRUB, 0xcead4c);

		colorMap.put(SAVANNAH, 0x4e7500);

		colorMap.put(DESERT, 0xdbb600);

		colorMap.put(TAIGA, 0x00cebd);
		colorMap.put(TUNDRA, 0xbafffd);

		for (Biome biome : Biome.values())
			if (!colorMap.containsKey(biome))
				colorMap.put(biome, new Color(125, 125, 125).getRGB());
	}

	@Override
	public String getName() {
		return "Biomes";
	}

	@Override
	public void render(BufferedImage image, Map map) {
		for (int x = 0; x < map.width; x++)
			for (int y = 0; y < map.height; y++)
				image.setRGB(x, y, getColor(map, x, y));
	}

	private int getColor(Map map, int x, int y) {
		Biome biome = map.getBiome(x, y);
		return colorMap.getInt(biome);
	}

}
