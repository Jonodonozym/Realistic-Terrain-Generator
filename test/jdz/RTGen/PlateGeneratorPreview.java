
package jdz.RTGen;

import java.awt.FlowLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jdz.RTGen.algorithms.plateGeneration.RandomPlateGenerator;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;
import jdz.RTGen.renderers.PlateListRenderer;

public class PlateGeneratorPreview {
	private static final int MAP_SIZE = 2048;

	private Map map = new Map(MAP_SIZE * 2, MAP_SIZE);
	private RandomPlateGenerator gen = new RandomPlateGenerator();
	private PlateListRenderer renderer = new PlateListRenderer();

	private JFrame frame;

	public static void main(String[] args) {
		new PlateGeneratorPreview();
	}

	public PlateGeneratorPreview() {
		List<TectonicPlate> plates = gen.generatePlates(map, 8);		
		Image image = renderer.render(map, plates);

		image = image.getScaledInstance(1024, 512, Image.SCALE_SMOOTH);

		frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());

		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();

		frame.setVisible(true);
	}

}
