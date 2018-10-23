
package jdz.RTGen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import jdz.RTGen.algorithms.PlateMetrics;
import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.dataType.Biome;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;
import jdz.RTGen.rendering.renderers.BiomeRenderer;

public class BiomeColorPreview extends Previewer {
	private static final int MAP_SIZE = 512;

	private List<Point2D> plateCenters;

	public static void main(String[] args) {
		new BiomeColorPreview();
	}

	public BiomeColorPreview() {
		super(MAP_SIZE);
	}

	@Override
	public void init() {
		map = new Map(map.width, map.height, 300);
		plateCenters = new ArrayList<Point2D>();

		List<TectonicPlate> plates = TectonicPlateGenerator.getGenerator().generatePlates(map, Biome.values().length);

		map.setPlates(plates);

		for (int i = 0; i < plates.size(); i++) {
			Biome biome = Biome.values()[i];
			plates.get(i).forEachCell((j) -> {
				map.cellBiome[j] = biome;
			});
			plateCenters.add(PlateMetrics.getCenterOfMass(plates.get(i)));
		}
	}

	@Override
	public BufferedImage createPreview() {
		BufferedImage image = new BiomeRenderer().render(map);

		Graphics2D g = (Graphics2D) image.getGraphics();

		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 16));

		FontMetrics metrics = g.getFontMetrics();

		for (int i = 0; i < plateCenters.size(); i++) {
			String[] words = Biome.values()[i].name().toLowerCase().split("_");

			Point2D offset = plateCenters.get(i);
			int line = 0;
			for (String s : words) {
				Point2D wordOffset = offset.add(-metrics.stringWidth(s) / 2,
						(line++ - (words.length - 1) / 2D) * metrics.getHeight());
				g.drawString(s, (int) wordOffset.getX(), (int) wordOffset.getY());
			}
		}

		g.dispose();
		return image;
	}

}
