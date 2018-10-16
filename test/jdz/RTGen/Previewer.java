
package jdz.RTGen;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import jdz.RTGen.dataType.Map;

public abstract class Previewer implements ActionListener {
	private static final int PREVIEW_WIDTH = 512, PREVIEW_HEIGHT = 256;

	protected Map map;

	private JFrame frame;
	private ImageIcon icon;

	public Previewer(int mapSize) {
		this(mapSize, -1);
	}

	public Previewer(int mapSize, int updateMS) {
		map = new Map(mapSize * 2, mapSize);

		LoggerConfig.init();
		init();

		icon = new ImageIcon(getScaledImage());

		if (updateMS > 0)
			new Timer(updateMS, this).start();

		frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());

		frame.getContentPane().add(new JLabel(icon));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();

		frame.setVisible(true);
	}

	protected void init() {}

	private Image getScaledImage() {
		return createPreview().getScaledInstance(PREVIEW_WIDTH, PREVIEW_HEIGHT, Image.SCALE_SMOOTH);
	}

	public abstract BufferedImage createPreview();

	@Override
	public void actionPerformed(ActionEvent e) {
		icon.setImage(getScaledImage());
		frame.repaint();
	}
}
