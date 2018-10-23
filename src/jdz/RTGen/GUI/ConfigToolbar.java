
package jdz.RTGen.GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import jdz.RTGen.Application;
import jdz.RTGen.configuration.Config;
import jdz.RTGen.configuration.Configurable;

public class ConfigToolbar extends JPanel {
	private static final long serialVersionUID = 1282619261646310143L;

	public static final int TOOLBAR_WIDTH = 256;

	public ConfigToolbar(Application app, List<Configurable> configurables) {
		VerticalFlowLayout l = new VerticalFlowLayout();
		setLayout(l);

		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
		l.setAlignment(VerticalFlowLayout.RIGHT, VerticalFlowLayout.TOP);

		for (Configurable configurable : configurables) {
			if (configurable.getConfig().numFields() == 0)
				continue;
			JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel label = new JLabel(configurable.getName());
			label.setFont(label.getFont().deriveFont(Font.BOLD, label.getFont().getSize() + 6));
			labelPanel.add(label);
			labelPanel.setPreferredSize(new Dimension(TOOLBAR_WIDTH, 28));
			add(labelPanel);

			add(new ConfigPanel(configurable));
			add(new JSeparator(SwingConstants.HORIZONTAL));
		}

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 32));
		panel.add(new RedrawButton(app));
		panel.add(new ExportButton(app));
		add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 16));
		panel.add(new GenerateButton(app));
		panel.add(new InterruptButton(app));
		add(panel);
	}

	private class ConfigPanel extends JPanel {
		private static final long serialVersionUID = -2722875562073841596L;

		public ConfigPanel(Configurable configurable) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setAlignmentX(RIGHT_ALIGNMENT);

			Config config = configurable.getConfig();
			for (String field : config.getFieldNames())
				add(new ConfigField(config, field));
		}
	}

	private class GenerateButton extends JButton {
		private static final long serialVersionUID = 5838577886884493106L;

		public GenerateButton(Application app) {
			setText("Generate Map");
			addActionListener((event) -> {
				app.updateMap();
			});
		}

	}

	private class RedrawButton extends JButton {
		private static final long serialVersionUID = 8471482167329652168L;

		public RedrawButton(Application app) {
			setText("Redraw Map");
			addActionListener((event) -> {
				app.redraw();
			});
		}

	}
	
	private class ExportButton extends JButton {
		private static final long serialVersionUID = -1988737878122065456L;

		public ExportButton(Application app) {
			setText("Export PNG");
			addActionListener((event)->{
				app.exportToFile();
			});
		}
	}
	
	private class InterruptButton extends JButton {
		private static final long serialVersionUID = -8434443391519132056L;

		public InterruptButton(Application app) {
			setText("Halt Generation");
			addActionListener((event)->{
				app.interrupt();
			});
		}
	}

}
