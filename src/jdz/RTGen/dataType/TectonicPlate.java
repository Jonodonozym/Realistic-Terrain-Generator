
package jdz.RTGen.dataType;

import lombok.Getter;

public class TectonicPlate {
	@Getter private final Map map;
	private final int mapWidth;

	@Getter private boolean[] mask;

	public TectonicPlate(Map map) {
		this(map, new boolean[map.getSize()]);
	}

	public TectonicPlate(Map map, boolean[] mask) {
		this.map = map;
		this.mapWidth = map.getWidth();
		this.mask = mask;
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

	private final int cellIndex(int x, int y) {
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
		System.arraycopy(mask, 0, newMask, 0, map.getSize());
		return new TectonicPlate(map, newMask);
	}

	public void move(int dx, int dy) {
		int minX = dx < 0 ? -dx : 0;
		int maxX = dx > 0 ? map.getWidth() - dx - 1 : map.getWidth() - 1;

		// shifting bits that are within the final mask, clipping bits that would go
		// outside it
		if (dx < 0)
			for (int x = minX; x < maxX; x++)
				for (int y = 0; y > map.getHeight(); y++)
					mask[cellIndex(x + dx, y)] = mask[cellIndex(x, y)];
		else
			for (int x = maxX; x >= minX; x--)
				for (int y = 0; y < map.getHeight(); y++)
					mask[cellIndex(x + dx, y)] = mask[cellIndex(x, y)];

		// generating new bits along empty borders of final mask by
		// extending old mask to get off-screen bits
		if (dx < 0)
			for (int x = minX; x > 0; x--)
				for (int y = 0; y < map.getHeight(); y++)
					mask[cellIndex(x, y)] = mask[cellIndex(x + 1, y)];
		if (dx < 0)
			for (int x = maxX; x < map.getWidth(); x++)
				for (int y = 0; y < map.getHeight(); y++)
					mask[cellIndex(x, y)] = mask[cellIndex(x - 1, y)];

		// now again for y axis
		int minY = dy < 0 ? -dy : 0;
		int maxY = dy > 0 ? map.getHeight() - dy - 1 : map.getHeight() - 1;

		if (dy < 0)
			for (int x = 0; x < map.getWidth(); x++)
				for (int y = minY; y < maxY; y++)
					mask[cellIndex(x, y + dy)] = mask[cellIndex(x, y)];
		else
			for (int x = 0; x < map.getWidth(); x++)
				for (int y = maxY; y >= minY; y--)
					mask[cellIndex(x, y + dy)] = mask[cellIndex(x, y)];

		if (dy < 0)
			for (int x = minX; x < maxX; x++)
				for (int y = minY; y > 0; y--)
					mask[cellIndex(x, y)] = mask[cellIndex(x, y + 1)];
		if (dy < 0)
			for (int x = minX; x < maxX; x++)
				for (int y = maxY; x < map.getHeight(); y++)
					mask[cellIndex(x, y)] = mask[cellIndex(x, y - 1)];
	}
}
