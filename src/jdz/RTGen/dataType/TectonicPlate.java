
package jdz.RTGen.dataType;

import lombok.Getter;

public class TectonicPlate {
	@Getter private final Map map;
	private final int mapWidth, mapHeight;

	@Getter private boolean[] mask;
	@Getter private float[] heights;

	public TectonicPlate(Map map) {
		this(map, new boolean[map.getSize()], new float[map.getSize()]);
	}

	public TectonicPlate(Map map, boolean[] mask, float[] heights) {
		this.map = map;
		this.mapWidth = map.getWidth();
		this.mapHeight = map.getHeight();
		this.mask = mask;
		this.heights = heights;
	}

	public final boolean isInPlate(int x, int y) {
		return mask[cellIndex(x, y)];
	}

	public final void setInPlate(int x, int y) {
		mask[cellIndex(x, y)] = true;
	}

	public final void removeFromPlate(int x, int y) {
		mask[cellIndex(x, y)] = false;
	}

	public final float getHeight(int x, int y) {
		return heights[cellIndex(x, y)];
	}

	public final void setHeight(int x, int y, float height) {
		heights[cellIndex(x, y)] = height;
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

	public void add(TectonicPlate other) {
		int size = map.getSize();
		boolean[] otherMask = other.getMask();

		for (int i = 0; i < size; i++)
			mask[i] |= otherMask[i];
	}

	public void subtract(TectonicPlate other) {
		int size = map.getSize();
		boolean[] otherMask = other.getMask();

		for (int i = 0; i < size; i++)
			mask[i] &= !otherMask[i];
	}

	public void overlap(TectonicPlate other) {
		int size = map.getSize();
		boolean[] otherMask = other.getMask();

		for (int i = 0; i < size; i++)
			mask[i] &= otherMask[i];
	}

	public TectonicPlate clone() {
		boolean[] newMask = new boolean[map.getSize()];
		float[] newHeights = new float[map.getSize()];
		System.arraycopy(mask, 0, newMask, 0, map.getSize());
		System.arraycopy(heights, 0, newHeights, 0, map.getSize());
		return new TectonicPlate(map, newMask, newHeights);
	}

	public void move(int dx, int dy) {
		boolean[] newMask = new boolean[mask.length];

		for (int x = 0; x < mapWidth; x++)
			for (int y = 0; y < mapHeight; y++)
				newMask[cellIndex(x + dx, y + dy)] = mask[cellIndex(x, y)];

		this.mask = newMask;
	}
}
