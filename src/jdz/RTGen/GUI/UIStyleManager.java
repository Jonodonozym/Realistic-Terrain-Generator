
package jdz.RTGen.GUI;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

public class UIStyleManager {
	public static void useCleanStyle() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("Panel.background", Color.WHITE);
			UIManager.put("OptionPane.background", Color.WHITE);
			UIManager.put("CheckBox.background", Color.WHITE);
			UIManager.put("Button.color", Color.WHITE);

			UIManager.put("Button.focus", new ColorUIResource(Color.BLACK));
			UIManager.put("CheckBox.focus", new ColorUIResource(Color.BLACK));
			UIManager.put("TabbedPane.focus", new ColorUIResource(Color.BLACK));
			UIManager.put("ComboBox.focus", new ColorUIResource(Color.BLACK));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
