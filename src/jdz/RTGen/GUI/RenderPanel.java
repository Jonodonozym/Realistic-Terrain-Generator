
package jdz.RTGen.GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.rendering.Renderer;
import lombok.Getter;
import lombok.Setter;

public class RenderPanel extends JPanel {
	private static final long serialVersionUID = -1623002607424939250L;

	@Getter @Setter private List<Renderer> renderers;
	@Setter private Map map;

	@Getter private BufferedImage lastUnscaledImage;

	public RenderPanel(Map map, int displayWidth, int displayHeight, Renderer... renderers) {
		this.map = map;
		this.renderers = Arrays.asList(renderers);

		setPreferredSize(new Dimension(displayWidth, displayHeight));
		setSize(displayWidth, displayHeight);
	}

	public void updateRender() {
		lastUnscaledImage = null;

		for (Renderer r : renderers)
			if (lastUnscaledImage == null)
				lastUnscaledImage = r.render(map);
			else
				r.render(lastUnscaledImage, map);

		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension d = getPreferredSize();
		if (lastUnscaledImage != null) {
			Image scaledImage = lastUnscaledImage.getScaledInstance((int) d.getWidth(), (int) d.getHeight(),
					Image.SCALE_SMOOTH);

			int y = Math.max(0, (getHeight() - scaledImage.getHeight(null)) / 2);
			g.drawImage(scaledImage, 0, y, null);
		}
	}

}
