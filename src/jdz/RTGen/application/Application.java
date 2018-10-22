
package jdz.RTGen.application;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import jdz.RTGen.LoggerConfig;
import jdz.RTGen.algorithms.initialMapGeneration.InitialMapGenerator;
import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.precipitation.PrecipitationModel;
import jdz.RTGen.algorithms.tectonics.TectonicPlateDeformer;
import jdz.RTGen.algorithms.temperature.TemperatureModel;
import jdz.RTGen.dataType.Configurable;
import jdz.RTGen.dataType.Map;

public class Application {
	private static final int PREVIEW_WIDTH = 1024, PREVIEW_HEIGHT = 512;

	protected Map initialMap;

	private JFrame frame;

	public static void main(String[] args) {
		new Application();
	}

	public Application() {
		initialMap = new Map(512, 256);

		LoggerConfig.init();

		InitialMapGenerator.getContinent().generateInitialMap(initialMap);

		frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);

		frame.add(new ConfigToolbar(getConfigurables()), BorderLayout.WEST);
		frame.add(new RenderPanel(initialMap, 512, 512), BorderLayout.EAST);


		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

	private List<Configurable> getConfigurables() {
		return Arrays.asList(InitialMapGenerator.getContinent(), TectonicPlateGenerator.getRandom(),
				TectonicPlateDeformer.getBasic(), PrecipitationModel.oceanDistance(),
				TemperatureModel.equatorAndHeight());
	}

}
