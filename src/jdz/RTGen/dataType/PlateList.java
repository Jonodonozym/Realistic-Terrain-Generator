
package jdz.RTGen.dataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class PlateList {
	@Getter private final List<TectonicPlate> plates;
	private final Map map;
	@Getter private final int[] indexMask;

	public PlateList(List<TectonicPlate> plates) {
		this.plates = new ArrayList<>(plates);
		map = plates.get(0).getMap();
		indexMask = new int[map.size];
		updateIndexMask();
	}

	public void add(TectonicPlate plate) {
		plates.add(plate);
	}

	public TectonicPlate get(int cellIndex) {
		return plates.get(indexMask[cellIndex]);
	}

	public TectonicPlate get(int x, int y) {
		return get(plates.get(0).cellIndex(x, y));
	}

	public void updateIndexMask() {
		Arrays.fill(indexMask, -1);
		int plateIndex = 0;
		for (TectonicPlate plate : plates) {
			final int finalIndex = plateIndex++;
			plate.forEachCell((x, y) -> {
				indexMask[map.cellIndex(x, y)] = finalIndex;
			});
		}
	}

	public void forEachCell(CellPlateIterator iterator) {
		map.forAllCells((x, y) -> {
			int pIndex = indexMask[map.cellIndex(x, y)];
			iterator.execute(x, y, pIndex == -1 ? null : plates.get(pIndex));
		});
	}

	public static interface CellPlateIterator {
		public void execute(int x, int y, TectonicPlate plate);
	}

	public TectonicPlate toMergedPlate() {
		return toMergedPlate(0);
	}

	public TectonicPlate toMergedPlate(int startIndex) {
		TectonicPlate combined = new TectonicPlate(map);

		for (TectonicPlate plate : plates) {
			plate.forEachCell((x, y) -> {
				combined.addToPlate(x, y, plate.getHeight(x, y));
			});
		}

		return combined;
	}
}
