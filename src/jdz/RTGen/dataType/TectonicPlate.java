
package jdz.RTGen.dataType;

import lombok.Getter;

public class TectonicPlate {
	@Getter private final Map map;
	private final int mapHeight;

	@Getter private boolean[] mask;

	private TectonicPlate(Map map) {
		this(map, new boolean[map.getSize()]);
	}

	private TectonicPlate(Map map, boolean[] mask) {
		this.map = map;
		this.mapHeight = map.getHeight();
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
		return x * mapHeight + y;
	}

	public void merge(TectonicPlate other) {
		int size = map.getSize();
		boolean[] otherMask = other.getMask();

		for (int i = 0; i < size; i++)
			mask[i] |= otherMask[i];
	}

	public void remove(TectonicPlate other) {
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

	public TectonicPlate move(int dx, int dy) {
		boolean[] newMask = new boolean[map.getSize()];

		int minX = dx < 0 ? -dx : 0;
		int maxX = dx > 0 ? map.getWidth() - dx : map.getWidth();
		int minY = dy < 0 ? -dy : 0;
		int maxY = dy > 0 ? map.getHeight() - dy : map.getHeight();

		// shifting bits that are within the final mask, clipping bits that would go
		// outside it
		for (int x = minX; x < maxX; x++)
			for (int y = minY; y < maxY; y++)
				newMask[cellIndex(x + dx, y + dy)] = mask[cellIndex(x, y)];

		// generating new bits along empty borders of final mask by
		// extending old mask to get off-screen bits
		if (dx < 0)
			for (int x = minX; x > 0; x--)
				for (int y = 0; y < map.getHeight(); y++)
					newMask[cellIndex(x, y)] = mask[cellIndex(x + 1, y)];
		if (dx < 0)
			for (int x = maxX; x < map.getWidth(); x++)
				for (int y = 0; y < map.getHeight(); y++)
					newMask[cellIndex(x, y)] = mask[cellIndex(x - 1, y)];

		if (dy < 0)
			for (int x = minX; x < maxX; x++)
				for (int y = minY; y > 0; y--)
					newMask[cellIndex(x, y)] = mask[cellIndex(x, y + 1)];
		if (dy < 0)
			for (int x = minX; x < maxX; x++)
				for (int y = maxY; x < map.getHeight(); y++)
					newMask[cellIndex(x, y)] = mask[cellIndex(x, y - 1)];
		
		return new TectonicPlate(map, newMask);
	}
}
