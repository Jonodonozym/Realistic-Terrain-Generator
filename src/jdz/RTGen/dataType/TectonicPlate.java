
package jdz.RTGen.dataType;

import javafx.geometry.Point2D;
import lombok.Getter;

public class TectonicPlate {
	@Getter private final Map map;
	private final int mapWidth, mapHeight;

	public boolean[] mask;
	public float[] heights;

	public Point2D velocity;

	public TectonicPlate(Map map) {
		this(map, new boolean[map.getSize()], new float[map.getSize()], new Point2D(0, 0));
	}

	public TectonicPlate(Map map, boolean[] mask, float[] heights, Point2D velocity) {
		this.map = map;
		this.mapWidth = map.getWidth();
		this.mapHeight = map.getHeight();
		this.mask = mask;
		this.heights = heights;
		this.velocity = velocity;
	}

	public final boolean isInPlate(int x, int y) {
		return mask[cellIndex(x, y)];
	}

	public final void addToPlate(int x, int y) {
		addToPlate(x, y, 0);
	}

	public final void addToPlate(int x, int y, float height) {
		int index = cellIndex(x, y);
		mask[index] = true;
		heights[index] = height;
	}

	public final void removeFromPlate(int x, int y) {
		int index = cellIndex(x, y);
		mask[index] = false;
		heights[index] = Float.MIN_VALUE;
	}

	public final float getHeight(int x, int y) {
		return heights[cellIndex(x, y)];
	}

	public final void setHeight(int x, int y, float height) {
		heights[cellIndex(x, y)] = height;
	}
	
	public void addHeight(int x, int y, float height) {
		heights[cellIndex(x, y)] += height;
	}

	public void addHeights(double[] heights) {
		for (int i = 0; i < heights.length; i++)
			this.heights[i] += heights[i];
	}

	// Wraps horizontally with no y change
	// Wraps vertically by inverting excess y and mirroring x
	public final int cellIndex(int x, int y) {
		if (y < 0)
			return cellIndex(mapWidth - x, -1 - y);
		if (y >= mapHeight)
			return cellIndex(mapWidth - x, 2 * map.getHeight() - y - 1);
		if (x < 0)
			return cellIndex(mapWidth + x, y);
		if (x >= mapWidth)
			return cellIndex(x - mapWidth, y);
		return y * mapWidth + x;
	}

	public void chopOverlap(TectonicPlate other) {
		boolean[] overlapMask = getMasksOverlap(other);

		for (int i = 0; i < mask.length; i++)
			if (overlapMask[i]) {
				mask[i] = false;
				heights[i] = Float.MIN_VALUE;
			}
	}

	public boolean[] getMasksOverlap(TectonicPlate other) {
		boolean[] newMask = new boolean[mask.length];
		System.arraycopy(mask, 0, newMask, 0, mask.length);

		boolean[] otherMask = other.mask;

		for (int i = 0; i < newMask.length; i++)
			newMask[i] &= otherMask[i];

		return newMask;
	}

	public boolean[] getInvertedMask() {
		boolean[] newMask = new boolean[mask.length];
		for (int i = 0; i < mask.length; i++)
			newMask[i] = !mask[i];

		return newMask;
	}

	public TectonicPlate move(int dx, int dy) {
		boolean[] newMask = new boolean[mask.length];
		float[] newHeights = new float[mask.length];

		for (int x = 0; x < mapWidth; x++)
			for (int y = 0; y < mapHeight; y++) {
				newMask[cellIndex(x + dx, y + dy)] = mask[cellIndex(x, y)];
				newHeights[cellIndex(x + dx, y + dy)] = heights[cellIndex(x, y)];
			}

		return new TectonicPlate(map, newMask, newHeights, velocity);
	}

	@Override
	public TectonicPlate clone() {
		boolean[] newMask = new boolean[mask.length];
		float[] newHeights = new float[mask.length];

		System.arraycopy(mask, 0, newMask, 0, mask.length);
		System.arraycopy(heights, 0, newHeights, 0, mask.length);

		return new TectonicPlate(map, newMask, newHeights, velocity);
	}

}
