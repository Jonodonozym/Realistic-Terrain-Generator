
package jdz.RTGen.application;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.renderers.Renderer;
import lombok.Getter;
import lombok.Setter;

public class RenderPanel extends JPanel {
	private static final long serialVersionUID = -1623002607424939250L;
	
	@Getter @Setter private List<Renderer> renderStack = new ArrayList<>();
	@Setter private Map map;
	private ImageIcon icon;
	private BufferedImage lastUnscaledImage;

	public RenderPanel(Map map, int displayWidth, int displayHeight) {
		this.map = map;

		lastUnscaledImage = createPreview();
		icon = new ImageIcon();
		redrawImage();

		add(new JLabel(icon));
		setSize(displayWidth, displayHeight);
	}

	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		redrawImage();
	}

	public BufferedImage createPreview() {
		BufferedImage image = new BufferedImage(map.width, map.height, BufferedImage.TYPE_INT_RGB);
		for (Renderer r : renderStack)
			r.render(image, map);
		return image;
	}

	public void redrawImage() {
		SwingUtilities.invokeLater(() -> {
			Image scaledImage = lastUnscaledImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			icon.setImage(scaledImage);
			repaint();
		});
	}

}
