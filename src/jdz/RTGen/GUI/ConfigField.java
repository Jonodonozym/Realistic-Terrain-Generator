
package jdz.RTGen.GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;

import jdz.RTGen.dataType.Config;

public class ConfigField extends JPanel {
	private static final long serialVersionUID = -4802490707820080860L;

	private final Config config;
	private final String field;

	public ConfigField(Config config, String field) {
		super(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		this.config = config;
		this.field = field;

		JLabel label = new JLabel(field + ":    ");
		add(label);
		if (config.isBoolean(field))
			add(new BooleanField(config, field));
		else
			add(new NumberField(config, field));
		validate();
	}

	private class BooleanField extends JCheckBox {
		private static final long serialVersionUID = -3842682277190511585L;

		public BooleanField(Config config, String field) {
			setSelected(config.getBoolean(field));
			addActionListener((e) -> {
				config.set(field, isSelected());
			});
			Dimension d = getPreferredSize();
			d.height = 24;
			setPreferredSize(d);
			d.width = 96;
			setMaximumSize(d);
		}
	}

	private class NumberField extends JSpinner implements ChangeListener {
		private static final long serialVersionUID = -6278300795636975872L;
		private final SpinnerNumberModel model;

		public NumberField(Config config, String field) {
			if (config.isInteger(field))
				model = new SpinnerNumberModel(config.getInt(field), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
			else
				model = new SpinnerNumberModel(config.getFloat(field), -Float.MAX_VALUE, Float.MAX_VALUE, 1);
			
			setModel(model);
			addChangeListener(this);
			hideSpinnerArrow();
		}

		public void hideSpinnerArrow() {
			Dimension d = getPreferredSize();
			d.height = 24;
			setUI(new BasicSpinnerUI() {
				@Override
				protected Component createNextButton() {
					return null;
				}

				@Override
				protected Component createPreviousButton() {
					return null;
				}
			});
			setPreferredSize(d);
			d.width = 96;
			setMaximumSize(d);
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			config.set(field, model.getNumber().floatValue());
		}
	}
}
