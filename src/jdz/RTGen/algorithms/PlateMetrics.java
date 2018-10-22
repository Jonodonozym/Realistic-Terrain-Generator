
package jdz.RTGen.algorithms;

import javafx.geometry.Point2D;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class PlateMetrics {
	public static float getMass(TectonicPlate plate) {
		return 500f * plate.numCells() + getCumulativeHeight(plate);
	}

	public static float getCumulativeHeight(TectonicPlate plate) {
		float h = 0;
		for (int i = 0; i < plate.heights.length; i++)
			if (plate.mask[i])
				h += plate.heights[i];
		return h;
	}

	// TODO include height calculations
	public static Point2D getCenterOfMass(TectonicPlate plate) {
		Map m = plate.getMap();

		// separate left and right for cases where plates wrap horizontally
		Point2D leftCenter = new Point2D(0, 0);
		Point2D rightCenter = new Point2D(0, 0);

		int midX = m.width / 2;
		int i = 0, leftSize = 0, rightSize = 0;
		for (int y = 0; y < m.height; y++) {
			int x = 0;
			for (; x < midX; x++)
				if (plate.mask[i++]) {
					leftSize++;
					leftCenter = leftCenter.add(x, y);
				}
			for (; x < m.width; x++)
				if (plate.mask[i++]) {
					rightSize++;
					rightCenter = rightCenter.add(x, y);
				}
		}

		if (leftSize > 0)
			leftCenter = leftCenter.multiply(1D / leftSize);

		if (rightSize > 0)
			rightCenter = rightCenter.multiply(1D / rightSize);

		if (leftSize > 0 && rightSize > 0 && rightCenter.getX() - leftCenter.getX() > m.width / 4)
			if (leftSize > rightSize)
				return leftCenter;
			else
				return rightCenter;

		return leftCenter.multiply(leftSize).add(rightCenter.multiply(rightSize))
				.multiply(1 / (double) (leftSize + rightSize));
	}
}
