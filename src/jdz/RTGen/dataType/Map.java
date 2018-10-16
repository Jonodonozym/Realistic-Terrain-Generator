
package jdz.RTGen.dataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(of = { "width", "height", "size" })
public class Map {
	public final int width, height, size;

	public final float[] cellHeight;
	public final float[] cellTemperature;
	public final float[] cellPrecipitation;
	public final Biome[] cellBiome;

	@Setter @Getter private List<TectonicPlate> plates;

	@Getter private final long seed;

	public Map(int width, int height) {
		this(width, height, new Random().nextLong());
	}

	public Map(int width, int height, long seed) {
		this.width = width;
		this.height = height;
		this.size = width * height;

		cellHeight = new float[size];
		cellTemperature = new float[size];
		cellPrecipitation = new float[size];
		cellBiome = new Biome[size];

		this.plates = new ArrayList<>();

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

	public final int cellIndex(int x, int y) {
		return y * width + x;
	}

	public final int cellX(int index) {
		return index / height;
	}

	public final int cellY(int index) {
		return index % height;
	}

}
