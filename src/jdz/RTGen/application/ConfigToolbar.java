
package jdz.RTGen.application;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import jdz.RTGen.dataType.Config;
import jdz.RTGen.dataType.Configurable;

public class ConfigToolbar extends JPanel {
	private static final long serialVersionUID = 1282619261646310143L;
	
	public static final int TOOLBAR_WIDTH = 256;

	public ConfigToolbar(List<Configurable> configurables) {
		VerticalFlowLayout l = new VerticalFlowLayout();
		setLayout(l);
		
		l.setAlignment(VerticalFlowLayout.RIGHT, VerticalFlowLayout.TOP);

		for (Configurable configurable : configurables) {
			
			JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel label = new JLabel(configurable.getName());
			label.setFont(label.getFont().deriveFont(Font.BOLD, label.getFont().getSize()+6));
			labelPanel.add(label);
			labelPanel.setPreferredSize(new Dimension(TOOLBAR_WIDTH, 28));
			add(labelPanel);
			add(new ConfigPanel(configurable));
			add(new JSeparator(JSeparator.HORIZONTAL));
		}
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

}
