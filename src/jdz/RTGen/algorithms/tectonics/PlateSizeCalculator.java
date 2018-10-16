
package jdz.RTGen.algorithms.tectonics;

import javafx.geometry.Point2D;
import jdz.RTGen.dataType.TectonicPlate;

public class PlateSizeCalculator {
	public static float getMass(TectonicPlate plate) {
		return 500f * getNumCells(plate) + getCumulativeHeight(plate);
	}

	public static int getNumCells(TectonicPlate plate) {
		int size = 0;
		for (int i = 0; i < plate.heights.length; i++)
			if (plate.mask[i])
				size++;
		return size;
	}

	public static float getCumulativeHeight(TectonicPlate plate) {
		float h = 0;
		for (int i = 0; i < plate.heights.length; i++)
			if (plate.mask[i])
				h += plate.heights[i];
		return h;
	}
	
	public static Point2D getCenterOfMass(TectonicPlate plate) {
		return getCenterOfMass(plate.mask, plate.heights);
	}
	
	public static Point2D getCenterOfMass(boolean[] mask, float[] heights) {
		return null;
	}
}
