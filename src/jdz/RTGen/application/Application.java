
package jdz.RTGen.application;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import jdz.RTGen.LoggerConfig;
import jdz.RTGen.algorithms.biomeClassifier.BiomeClassifier;
import jdz.RTGen.algorithms.initialMapGeneration.InitialMapGenerator;
import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.precipitation.PrecipitationModel;
import jdz.RTGen.algorithms.tectonics.TectonicPlateDeformer;
import jdz.RTGen.algorithms.temperature.TemperatureModel;
import jdz.RTGen.dataType.Config;
import jdz.RTGen.dataType.Configurable;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.renderers.BiomeRenderer;
import jdz.RTGen.renderers.HeightMapRenderer;
import jdz.RTGen.renderers.PlateListRenderer;
import jdz.RTGen.renderers.Renderer;
import lombok.Getter;

public class Application extends Configurable {
	private static final int PREVIEW_WIDTH = 1024, PREVIEW_HEIGHT = 512;

	@Getter private Config config = new AppConfig();
	@Getter private String name = "General config";

	protected Map initialMap;
	protected Map deformedMap;

	private ConfigToolbar toolbar;
	private RenderPanel renderPanel;

	private UpdateMapTask currentUpdateTask = null;

	private JFrame frame;

	public static void main(String[] args) {
		new Application();
	}

	public Application() {
		LoggerConfig.init();

		toolbar = new ConfigToolbar(this, getConfigurables());
		renderPanel = new RenderPanel(deformedMap, 1024, 512, new BiomeRenderer(), new HeightMapRenderer(),
				new PlateListRenderer());

		frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);

		frame.add(toolbar, BorderLayout.WEST);
		frame.add(renderPanel, BorderLayout.EAST);

		frame.add(new Console(), BorderLayout.PAGE_END);

		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);

		frame.repaint();

		updateMap();
	}

	private List<Configurable> getConfigurables() {
		return Arrays.asList(this, InitialMapGenerator.getContinent(), TectonicPlateGenerator.getRandom(),
				TectonicPlateDeformer.getBasic(), PrecipitationModel.oceanDistance(),
				TemperatureModel.equatorAndHeight());
	}
	
	private List<Renderer> getRenderers() {
		List<Renderer> renderers = new ArrayList<>();
		if (AppConfig.RENDER_BIOME)
			renderers.add(new BiomeRenderer());
		if (AppConfig.RENDER_HEIGHT)
			renderers.add(new HeightMapRenderer());
		if (AppConfig.RENDER_PLATES)
			renderers.add(new PlateListRenderer());
		return renderers;
	}
	
	public void updateMap() {
		if (currentUpdateTask == null || currentUpdateTask.isDone() || currentUpdateTask.isCancelled()) {
			currentUpdateTask = new UpdateMapTask();
			currentUpdateTask.execute();
		}
	}

	public void redraw() {
		renderPanel.setRenderers(getRenderers());
		renderPanel.updateRender();
	}

	/**
	 * Updates the map asynchronously, not freezing the console or UI
	 *
	 * @author Jaiden Baker
	 */
	private class UpdateMapTask extends SwingWorker<Void, Void> {
		@Override
		protected Void doInBackground() throws Exception {
			initialMap = new Map(512, 256, AppConfig.MAP_SEED);
			InitialMapGenerator.getContinent().generateInitialMap(initialMap);
			initialMap.setPlates(TectonicPlateGenerator.getRandom().generatePlates(initialMap));

			deformedMap = initialMap.clone();

			deformedMap.setPlates(TectonicPlateDeformer.getBasic().deform(deformedMap, deformedMap.getPlates()));
			deformedMap.updateHeightFromPlates();

			BiomeClassifier.assignBiomes(deformedMap);

			renderPanel.setMap(deformedMap);
			redraw();
			return null;
		}
	}

}
