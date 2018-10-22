
package jdz.RTGen.application;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;

import jdz.RTGen.dataType.Config;

public class ConfigField extends JPanel implements ChangeListener {
	private static final long serialVersionUID = -4802490707820080860L;

	private final Config config;

	private final JSpinner spinner;
	private final JLabel label;
	private final SpinnerNumberModel model;
	private final String field;

	public ConfigField(Config config, String field) {
		super(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		this.config = config;
		this.field = field;

		label = new JLabel(field + ":    ");

		if (config.isInteger(field))
			model = new SpinnerNumberModel((int) config.get(field), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
		else
			model = new SpinnerNumberModel(config.get(field), -Double.MAX_VALUE, Double.MAX_VALUE, 1);
		spinner = new JSpinner(model);
		spinner.addChangeListener(this);
		hideSpinnerArrow(spinner);

		add(label);
		add(spinner);
		validate();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		config.set(field, model.getNumber().floatValue());
	}

	public void hideSpinnerArrow(JSpinner spinner) {
		Dimension d = spinner.getPreferredSize();
		d.height = 24;
		spinner.setUI(new BasicSpinnerUI() {
			@Override
			protected Component createNextButton() {
				return null;
			}

			@Override
			protected Component createPreviousButton() {
				return null;
			}
		});
		spinner.setPreferredSize(d);
		d.width = 96;
		spinner.setMaximumSize(d);
	}
}
