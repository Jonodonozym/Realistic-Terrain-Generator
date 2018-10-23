
package jdz.RTGen.algorithms.cellDepthCalculator;

import jdz.RTGen.dataType.TectonicPlate;
import lombok.AllArgsConstructor;

public interface EdgeListPopulator {
	public boolean shouldPopulate(int x, int y);

	@AllArgsConstructor
	public static class IsOnEdge implements EdgeListPopulator {
		private final TectonicPlate maskPlate;

		@Override
		public boolean shouldPopulate(int x, int y) {
			return maskPlate.isInPlate(x, y) && !(maskPlate.isInPlate(x + 1, y) && maskPlate.isInPlate(x - 1, y)
					&& maskPlate.isInPlate(x, y + 1) && maskPlate.isInPlate(x, y - 1));
		}
	}

	@AllArgsConstructor
	public static class IsNextToPlate implements EdgeListPopulator {
		private final TectonicPlate maskPlate, secondPlate;

		@Override
		public boolean shouldPopulate(int x, int y) {
			return maskPlate.isInPlate(x, y) && (secondPlate.isInPlate(x + 1, y) || secondPlate.isInPlate(x - 1, y)
					|| secondPlate.isInPlate(x, y + 1) || secondPlate.isInPlate(x, y - 1));
		}
	}
}
