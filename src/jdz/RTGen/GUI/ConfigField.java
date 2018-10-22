
package jdz.RTGen.GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DecimalFormat;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicSpinnerUI;

import jdz.RTGen.configuration.Config;

public class ConfigField extends JPanel {
	private static final long serialVersionUID = -4802490707820080860L;

	public ConfigField(Config config, String field) {
		super(new FlowLayout(FlowLayout.RIGHT, 0, 0));

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

	private class NumberField extends JSpinner {
		private static final long serialVersionUID = -6278300795636975872L;
		private final SpinnerNumberModel model;

		public NumberField(Config config, String field) {
			if (config.isInteger(field))
				model = new SpinnerNumberModel(config.getInt(field), Integer.MIN_VALUE, Integer.MAX_VALUE,
						config.getStepInt(field));
			else
				model = new SpinnerNumberModel(config.getFloat(field), -Float.MAX_VALUE, Float.MAX_VALUE,
						config.getStepFloat(field));

			setModel(model);

			JSpinner.NumberEditor editor = (JSpinner.NumberEditor) getEditor();
			DecimalFormat format = editor.getFormat();
			format.setMaximumFractionDigits(3);
			format.setGroupingUsed(false);

			addChangeListener((e) -> {
				config.set(field, model.getNumber().floatValue());
			});

			if (!config.hasStep(field))
				hideSpinnerArrow();

			resize();
		}

		public void hideSpinnerArrow() {
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
		}

		public void resize() {
			Dimension d = getPreferredSize();
			d.height = 24;
			d.width = 96;
			setPreferredSize(d);
			setMinimumSize(d);
			setMaximumSize(d);
		}
	}
}
