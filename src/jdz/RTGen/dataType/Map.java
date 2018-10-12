
package jdz.RTGen.dataType;

import java.util.Random;

import lombok.Getter;
import lombok.ToString;

@ToString(of = { "width", "height", "size" })
public class Map {
	@Getter private final int width, height, size;

	private final double[] cellHeight;
	private final double[] cellTemperature;
	private final double[] cellPrecipitation;
	private final Biome[] cellBiome;

	@Getter private final long seed;
	@Getter private final Random random;

	public Map(int width, int height) {
		this(width, height, new Random().nextLong());
	}

	public Map(int width, int height, long seed) {
		this.width = width;
		this.height = height;
		this.size = width * height;

		cellHeight = new double[size];
		cellTemperature = new double[size];
		cellPrecipitation = new double[size];
		cellBiome = new Biome[size];

		this.seed = seed;
		this.random = new Random(seed);
	}

	public void resetRandom() {
		random.setSeed(seed);
	}

	public final void setHeight(int x, int y, int height) {
		cellHeight[cellIndex(x, y)] = height;
	}

	public final double getHeight(int x, int y) {
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

	private final int cellIndex(int x, int y) {
		return x * height + y;
	}

}