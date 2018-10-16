
package jdz.RTGen;

import java.awt.image.BufferedImage;

import jdz.RTGen.algorithms.initialMapGeneration.InitialMapGenerator;
import jdz.RTGen.renderers.HeightMapRenderer;

public class InitialMapPreview extends Previewer {
	private static final int MAP_SIZE = 512;

	public static void main(String[] args) {
		new InitialMapPreview();
	}

	public InitialMapPreview() {
		super(MAP_SIZE);
	}

	@Override
	public BufferedImage createPreview() {
		map = InitialMapGenerator.getContinent().generateInitialMap(map);
		return new HeightMapRenderer().render(map, map);
	}

}
