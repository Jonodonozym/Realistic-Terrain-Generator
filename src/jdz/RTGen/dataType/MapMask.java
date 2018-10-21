
package jdz.RTGen.dataType;

public class MapMask {
	private final Map map;
	private final boolean[] mask;

	public MapMask(Map map) {
		this.map = map;
		this.mask = new boolean[map.size];
	}

	public final boolean contains(int x, int y) {
		return mask[map.cellIndex(x, y)];
	}

	public final void set(int x, int y, boolean val) {
		mask[map.cellIndex(x, y)] = val;
	}

	public final void add(int x, int y) {
		mask[map.cellIndex(x, y)] = true;
	}

	public final void remove(int x, int y) {
		mask[map.cellIndex(x, y)] = false;
	}

}
