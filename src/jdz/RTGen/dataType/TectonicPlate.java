
package jdz.RTGen.dataType;

import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;

public class TectonicPlate {
	@Getter private final Map map;
	private final int mapWidth, mapHeight;

	@Getter private boolean[] mask;
	@Getter private float[] heights;
	
	@Getter @Setter private Point2D velocity;

	public TectonicPlate(Map map) {
		this(map, new boolean[map.getSize()], new float[map.getSize()], new Point2D(0, 0));
	}

	public TectonicPlate(Map map, boolean[] mask, float[] heights, Point2D speed) {
		this.map = map;
		this.mapWidth = map.getWidth();
		this.mapHeight = map.getHeight();
		this.mask = mask;
		this.heights = heights;
		this.velocity = speed;
	}

	public final boolean isInPlate(int x, int y) {
		return mask[cellIndex(x, y)];
	}

	public final void setInPlate(int x, int y) {
		int index = cellIndex(x, y);
		mask[index] = true;
		heights[index] = 0;
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

	public TectonicPlate getOverlap(TectonicPlate other) {
		boolean[] newMask = new boolean[mask.length];
		float[] newHeights = new float[mask.length];

		boolean[] otherMask = other.getMask();
		float[] otherHeights = other.getHeights();

		for (int i = 0; i < newMask.length; i++) {
			newMask[i] = mask[i] && otherMask[i];
			if (newMask[i]) {
				newHeights[i] += heights[i];
				newHeights[i] += otherHeights[i];
			}
		}

		return new TectonicPlate(map, newMask, newHeights, velocity);
	}

	public TectonicPlate merge(TectonicPlate other) {
		boolean[] newMask = new boolean[mask.length];
		float[] newHeights = new float[mask.length];

		boolean[] otherMask = other.getMask();
		float[] otherHeights = other.getHeights();

		System.arraycopy(mask, 0, newMask, 0, mask.length);

		for (int i = 0; i < newMask.length; i++) {
			newMask[i] |= otherMask[i];
			if (mask[i])
				newHeights[i] += heights[i];
			if (otherMask[i])
				newHeights[i] += otherHeights[i];
		}

		return new TectonicPlate(map, newMask, newHeights, velocity);
	}

	public TectonicPlate removeOverlap(TectonicPlate other) {
		boolean[] newMask = new boolean[mask.length];
		float[] newHeights = new float[mask.length];

		System.arraycopy(mask, 0, newMask, 0, mask.length);
		System.arraycopy(heights, 0, newHeights, 0, mask.length);

		TectonicPlate overlap = getOverlap(other);

		boolean[] overlapMask = overlap.getMask();

		for (int i = 0; i < mask.length; i++) {
			if (overlapMask[i]) {
				newMask[i] = false;
				newHeights[i] = Float.MIN_VALUE;
			}
		}

		return new TectonicPlate(map, newMask, newHeights, velocity);
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
	
}
