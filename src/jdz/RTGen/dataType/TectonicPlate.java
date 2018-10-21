
package jdz.RTGen.dataType;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import javafx.geometry.Point2D;
import lombok.Getter;

public class TectonicPlate {
	@Getter private final Map map;

	@Getter private Object2FloatMap<MapCell> heights = new Object2FloatOpenHashMap<>();

	private Point2D offset;
	public Point2D velocity;

	public TectonicPlate(Map map) {
		this(map, new Object2FloatOpenHashMap<>(), new Point2D(0, 0), new Point2D(0, 0));
	}

	public TectonicPlate(Map map, Object2FloatMap<MapCell> cells, Point2D velocity, Point2D fractionOffset) {
		this.map = map;
		this.heights = cells;
		this.velocity = velocity;
		this.offset = fractionOffset;
	}

	public final boolean isInPlate(int x, int y) {
		return heights.containsKey(getCell(x, y));
	}

	public final void addToPlate(int x, int y) {
		addToPlate(x, y, 0);
	}

	public final void addToPlate(int x, int y, float height) {
		heights.put(getCell(x, y), height);
	}

	public final void removeFromPlate(int x, int y) {
		heights.removeFloat(getCell(x, y));
	}

	public final float getHeight(int x, int y) {
		return heights.getFloat(getCell(x, y));
	}

	public final void setHeight(int x, int y, float height) {
		heights.put(getCell(x, y), height);
	}

	public void addHeight(int x, int y, float height) {
		heights.put(getCell(x, y), getHeight(x, y));
	}

	public void forEachCell(CellIterator iterator) {
		for (MapCell c : heights.keySet())
			iterator.execute(c.x, c.y);
	}

	public final int cellIndex(int x, int y) {
		return map.cellIndex(x, y);
	}

	public int numCells() {
		return heights.size();
	}

	// Transformations

	public TectonicPlate chopOverlap(TectonicPlate other) {
		for (MapCell c : other.getHeights().keySet())
			heights.removeFloat(c);

		return this;
	}

	public TectonicPlate getMasksOverlap(TectonicPlate other) {
		TectonicPlate newMask = new TectonicPlate(map);

		for (MapCell c : other.heights.keySet())
			if (heights.containsKey(c))
				newMask.heights.put(c, 0);

		return newMask;
	}

	public TectonicPlate getInvertedMask() {
		TectonicPlate newMask = new TectonicPlate(map);

		map.forAllCells((x, y) -> {
			if (!isInPlate(x, y))
				newMask.addToPlate(x, y);
		});

		return newMask;
	}

	public TectonicPlate step() {
		offset.add(velocity);
		return this;
	}

	@Override
	public TectonicPlate clone() {
		return new TectonicPlate(map, new Object2FloatOpenHashMap<MapCell>(heights), velocity, offset);
	}

	private MapCell getCell(int x, int y) {
		int dx = x + (int) Math.round(offset.getX());
		int dy = y + (int) Math.round(offset.getY());
		return map.getCell(dx, dy);
	}

}
