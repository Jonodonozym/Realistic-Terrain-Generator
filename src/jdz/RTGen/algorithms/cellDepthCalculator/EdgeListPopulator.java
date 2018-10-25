
package jdz.RTGen.algorithms.cellDepthCalculator;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;
import lombok.AllArgsConstructor;

public interface EdgeListPopulator {
	public boolean shouldPopulate(int x, int y);

	@AllArgsConstructor
	public static class IsOnEdge implements EdgeListPopulator {
		private final TectonicPlate maskPlate;
		
		public IsOnEdge(Map map, boolean[] mask) {
			maskPlate = new TectonicPlate(map, mask, null, null, null);
		}

		@Override
		public boolean shouldPopulate(int x, int y) {
			return maskPlate.isInPlate(x, y) && !(maskPlate.isInPlate(x + 1, y) && maskPlate.isInPlate(x - 1, y)
					&& maskPlate.isInPlate(x, y + 1) && maskPlate.isInPlate(x, y - 1));
		}
	}

	@AllArgsConstructor
	public static class IsNextToPlate implements EdgeListPopulator {
		private final TectonicPlate maskPlate, secondPlate;
		
		public IsNextToPlate(Map map, boolean[] maskA, boolean[] maskB) {
			maskPlate = new TectonicPlate(map, maskA, null, null, null);
			secondPlate = new TectonicPlate(map, maskB, null, null, null);
		}

		@Override
		public boolean shouldPopulate(int x, int y) {
			return maskPlate.isInPlate(x, y) && (secondPlate.isInPlate(x + 1, y) || secondPlate.isInPlate(x - 1, y)
					|| secondPlate.isInPlate(x, y + 1) || secondPlate.isInPlate(x, y - 1));
		}
	}
}
