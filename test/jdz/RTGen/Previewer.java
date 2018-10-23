
package jdz.RTGen;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.rendering.renderers.HeightMapRenderer;

public abstract class Previewer {
	private static final int PREVIEW_WIDTH = 1024, PREVIEW_HEIGHT = 512;

	protected Map map;

	private JFrame frame;
	private ImageIcon icon;

	private BufferedImage lastUnscaledImage;

	public Previewer(int mapSize) {
		this(mapSize, -1);
	}

	public Previewer(int mapSize, int updateMS) {
		map = new Map(mapSize * 2, mapSize);

		LoggerConfig.init();
		init();

		frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);

		lastUnscaledImage = new HeightMapRenderer().render(map);
		icon = new ImageIcon();
		redrawImage();

		frame.getContentPane().add(new JLabel(icon));
		frame.pack();
		frame.setSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
		frame.setVisible(true);

		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				redrawImage();
			}
		});

		frame.setResizable(false);

		if (updateMS > 0)
			new Timer(updateMS, (e) -> {
				lastUnscaledImage = createPreview();
				redrawImage();
			}).start();
		else {
			lastUnscaledImage = createPreview();
			redrawImage();
		}

	}

	protected void init() {}

	public abstract BufferedImage createPreview();

	public void redrawImage() {
		SwingUtilities.invokeLater(() -> {
			Image scaledImage = lastUnscaledImage.getScaledInstance(frame.getWidth(), frame.getHeight(),
					Image.SCALE_SMOOTH);
			icon.setImage(scaledImage);
			frame.repaint();
		});
	}
}
