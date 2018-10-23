
package jdz.RTGen;

import java.awt.image.BufferedImage;

import jdz.RTGen.algorithms.initialMapGeneration.ContinentGenerator;
import jdz.RTGen.rendering.renderers.HeightMapRenderer;

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
		map = ContinentGenerator.getContinent().generateInitialMap(map);
		return new HeightMapRenderer().render(map);
	}

}
