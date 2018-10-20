
package jdz.RTGen.dataType;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class PlateList {
	@Getter private final List<TectonicPlate> plates;
	private final Map map;
	private final int[] indexMask;

	public PlateList(List<TectonicPlate> plates) {
		this.plates = new ArrayList<>(plates);
		map = plates.get(0).getMap();
		indexMask = new int[map.size];
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
		int plateIndex = 0;
		for (TectonicPlate plate: plates) {
			final int finalIndex = plateIndex++;
			plate.forEachCell((x, y, i)->{
				indexMask[i] = finalIndex;
			});
		}
	}

	public void forEachCell(CellItterator iterator) {
		int index = 0;
		for (int y = 0; y < map.height; y++)
			for (int x = 0; x < map.width; x++)
				iterator.execute(x, y, index, plates.get(indexMask[index++]));
	}

	public static interface CellItterator {
		public void execute(int x, int y, int cellIndex, TectonicPlate plate);
	}



}
