
package jdz.RTGen;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import jdz.RTGen.dataType.Map;

public abstract class Previewer {
	protected Map map;

	public Previewer(int mapSize) {
		map = new Map(mapSize * 2, mapSize);
		
		LoggerConfig.init();
		
		Image image = createPreview().getScaledInstance(1024, 512, Image.SCALE_SMOOTH);

		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());

		frame.getContentPane().add(new JLabel(new ImageIcon(image)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();

		frame.setVisible(true);
	}

	public abstract BufferedImage createPreview();
}
