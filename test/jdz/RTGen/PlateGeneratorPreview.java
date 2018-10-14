
package jdz.RTGen;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jdz.RTGen.algorithms.plateGeneration.RandomPlateGenerator;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;
import jdz.RTGen.renderers.PlateListRenderer;

public class PlateGeneratorPreview {
	private Map map = new Map(512, 512);
	private RandomPlateGenerator p = new RandomPlateGenerator(map);
	private PlateListRenderer r = new PlateListRenderer();
	
	private JFrame frame;
	
	public static void main(String[] args) {
		new PlateGeneratorPreview();
	}
	
	public PlateGeneratorPreview() {
		p.setAveragePlateArea(200 * 200);
		List<TectonicPlate> plates = p.generate();
		BufferedImage image = r.render(map, plates);
		
		frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		
		frame.setVisible(true);
	}

}
