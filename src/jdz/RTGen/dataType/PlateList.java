
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
			plate.forEachCell((x, y, i) -> {
				indexMask[i] = finalIndex;
			});
		}
	}

	public void forEachCell(CellPlateIterator iterator) {
		map.forAllCells((x, y, index) -> {
			int pIndex = indexMask[index++];
			iterator.execute(x, y, index, pIndex == -1 ? null : plates.get(pIndex));
		});
	}

	public static interface CellPlateIterator {
		public void execute(int x, int y, int index, TectonicPlate plate);
	}

	public TectonicPlate toMergedPlate() {
		return toMergedPlate(0);
	}

	public TectonicPlate toMergedPlate(int startIndex) {
		TectonicPlate combined = new TectonicPlate(map);

		for (int i = startIndex; i < plates.size(); i++) {
			TectonicPlate p = plates.get(i);
			for (int j = 0; j < map.size; j++)
				if (p.mask[j]) {
					combined.mask[j] = true;
					combined.heights[j] = p.heights[j];
				}
		}

		return combined;
	}
}
