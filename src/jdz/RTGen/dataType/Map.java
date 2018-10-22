
package jdz.RTGen.dataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(of = { "seed", "width", "height", "size" })
public class Map {
	public final int width, height, size;

	public final float[] cellHeight;
	public final float[] cellTemperature;
	public final float[] cellPrecipitation;
	public final Biome[] cellBiome;

	@Setter @Getter public float seaLevel;

	@Setter @Getter private List<TectonicPlate> plates;

	@Getter private final long seed;

	public Map(int width, int height) {
		this(width, height, new Random().nextLong());
	}

	public Map(int width, int height, long seed) {
		this.width = width;
		this.height = height;
		size = width * height;

		cellHeight = new float[size];
		cellTemperature = new float[size];
		cellPrecipitation = new float[size];
		cellBiome = new Biome[size];

		plates = new ArrayList<>();

		seaLevel = Float.MIN_VALUE;

		this.seed = seed;
	}

	public final void updateHeightFromPlates() {
		for (TectonicPlate plate : plates)
			for (int i = 0; i < size; i++)
				if (plate.mask[i])
					cellHeight[i] = plate.heights[i];
	}

	public final void setHeight(int x, int y, int height) {
		cellHeight[cellIndex(x, y)] = height;
	}

	public final float getHeight(int x, int y) {
		return cellHeight[cellIndex(x, y)];
	}

	public final void setTemperature(int x, int y, int temperature) {
		cellTemperature[cellIndex(x, y)] = temperature;
	}

	public final double getTemperature(int x, int y) {
		return cellTemperature[cellIndex(x, y)];
	}

	public final void setPrecipitation(int x, int y, int precipitation) {
		cellPrecipitation[cellIndex(x, y)] = precipitation;
	}

	public final double getPrecipitation(int x, int y) {
		return cellPrecipitation[cellIndex(x, y)];
	}

	public final void setBiome(int x, int y, Biome biome) {
		cellBiome[cellIndex(x, y)] = biome;
	}

	public final Biome getBiome(int x, int y) {
		return cellBiome[cellIndex(x, y)];
	}

	// Wraps horizontally with no y change
	// Wraps vertically by inverting excess y and mirroring x
	public final int cellIndex(int x, int y) {
		if (y < 0)
			return cellIndex(width - x - 1, -1 - y);
		if (y >= height)
			return cellIndex(width - x - 1, 2 * height - y - 1);
		if (x < 0)
			return cellIndex(width + x, y);
		if (x >= width)
			return cellIndex(x - width, y);
		return y * width + x;
	}

	public final int cellX(int index) {
		return index / height;
	}

	public final int cellY(int index) {
		return index % height;
	}

	public final float getMaxHeight() {
		float maxHeight = Float.MIN_VALUE;

		for (int i = 0; i < size; i++) {
			float height = cellHeight[i];
			if (height > maxHeight)
				maxHeight = height;
		}

		return maxHeight;
	}

	public final Random getNewRandom() {
		return new Random(seed);
	}

	public final float getMinHeight() {
		float minHeight = Float.MAX_VALUE;

		for (int i = 0; i < size; i++) {
			float height = cellHeight[i];
			if (height < minHeight)
				minHeight = height;
		}

		return minHeight;
	}

	public void forAllCells(CellIterator iterator) {
		int index = 0;
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				iterator.execute(x, y, index++);
	}

	@Override
	public Map clone() {
		Map map = new Map(width, height, seed);
		System.arraycopy(cellHeight, 0, map.cellHeight, 0, size);
		System.arraycopy(cellTemperature, 0, map.cellTemperature, 0, size);
		System.arraycopy(cellPrecipitation, 0, map.cellPrecipitation, 0, size);
		System.arraycopy(cellBiome, 0, map.cellBiome, 0, size);
		map.setSeaLevel(seaLevel);
		for (TectonicPlate plate : plates)
			map.getPlates().add(plate.clone());
		return map;
	}

}
