
package jdz.RTGen;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import jdz.RTGen.GUI.ConfigToolbar;
import jdz.RTGen.GUI.Console;
import jdz.RTGen.GUI.RenderPanel;
import jdz.RTGen.GUI.UIStyleManager;
import jdz.RTGen.algorithms.biomeClassifier.BiomeClassifier;
import jdz.RTGen.algorithms.initialMapGeneration.ContinentGenConfig;
import jdz.RTGen.algorithms.initialMapGeneration.ContinentGenerator;
import jdz.RTGen.algorithms.plateGeneration.TectonicPlateGenerator;
import jdz.RTGen.algorithms.precipitation.PrecipitationModel;
import jdz.RTGen.algorithms.tectonics.TectonicPlateDeformer;
import jdz.RTGen.algorithms.temperature.TemperatureModel;
import jdz.RTGen.configuration.Config;
import jdz.RTGen.configuration.Configurable;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.rendering.ActiveRenderers;
import jdz.RTGen.rendering.ImageExporter;
import jdz.RTGen.rendering.renderers.BiomeRenderer;
import jdz.RTGen.rendering.renderers.HeightMapRenderer;
import jdz.RTGen.rendering.renderers.PlateListRenderer;
import lombok.Getter;

public class Application extends Configurable {
	private static final int PREVIEW_WIDTH = 1024, PREVIEW_HEIGHT = 512;

	@Getter private Config config = new MapConfig();
	@Getter private String name = "Map Config";

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
		UIStyleManager.useCleanStyle();

		toolbar = new ConfigToolbar(this, getConfigurables());
		renderPanel = new RenderPanel(deformedMap, 1024, 512, new BiomeRenderer(), new HeightMapRenderer(),
				new PlateListRenderer());

		frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);

		frame.add(toolbar, BorderLayout.WEST);

		JPanel eastPanel = new JPanel(new BorderLayout());
		eastPanel.add(renderPanel, BorderLayout.PAGE_START);

		eastPanel.add(new Console(), BorderLayout.PAGE_END);

		frame.add(eastPanel, BorderLayout.EAST);

		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);

		frame.repaint();

		updateMap();
	}

	private List<Configurable> getConfigurables() {
		return Arrays.asList(this, new ActiveRenderers(), ContinentGenerator.getContinent(),
				TectonicPlateGenerator.getGenerator(), TectonicPlateDeformer.getBasic(), PrecipitationModel.getModel(),
				TemperatureModel.equatorAndHeight());
	}

	public void updateMap() {
		if (currentUpdateTask == null || currentUpdateTask.isDone() || currentUpdateTask.isCancelled()) {
			currentUpdateTask = new UpdateMapTask();
			currentUpdateTask.execute();
		}
	}

	public void redraw() {
		renderPanel.setRenderers(ActiveRenderers.getRenderers());
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
			Map initialMap = new Map(MapConfig.MAP_HEIGHT * 2, MapConfig.MAP_HEIGHT, MapConfig.MAP_SEED);

			if (ContinentGenConfig.SIMPLEX_NOISE)
				ContinentGenerator.getNoise().generateInitialMap(initialMap);
			else
				ContinentGenerator.getContinent().generateInitialMap(initialMap);
			initialMap.setPlates(TectonicPlateGenerator.getGenerator().generatePlates(initialMap));

			Map deformedMap = initialMap.clone();
			deformedMap.setPlates(TectonicPlateDeformer.getBasic().deform(deformedMap, deformedMap.getPlates()));
			deformedMap.updateHeightFromPlates();
			BiomeClassifier.assignBiomes(deformedMap);
			renderPanel.setMap(deformedMap);
			redraw();

			Application.this.initialMap = initialMap;
			Application.this.deformedMap = deformedMap;

			return null;
		}
	}

	public void exportToFile() {
		if (!ImageExporter.ROOT_FOLDER.exists())
			ImageExporter.ROOT_FOLDER.mkdirs();
		JFileChooser fileChooser = new JFileChooser(ImageExporter.ROOT_FOLDER);
		fileChooser.showSaveDialog(frame);

		File file = fileChooser.getSelectedFile();
		ImageExporter.exportPNG(renderPanel.getLastUnscaledImage(), file);
	}

	public void interrupt() {
		if (currentUpdateTask != null)
			currentUpdateTask.cancel(true);
	}

}
