
package jdz.RTGen;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import javafx.geometry.Point2D;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class TectonicPlateTests {

	private Map map;
	private TectonicPlate plate, plate2;

	@Before
	public void setup() {
		map = new Map(4, 1);
	}

	@Test
	public void testIndex() {
		map = new Map(2, 2);
		plate = makePlate(map, 0, 0, 0, 0);

		// general
		assertEquals(0, plate.cellIndex(0, 0));
		assertEquals(1, plate.cellIndex(1, 0));
		assertEquals(2, plate.cellIndex(0, 1));
		assertEquals(3, plate.cellIndex(1, 1));

		// wrap left
		assertEquals(1, plate.cellIndex(-1, 0));
		assertEquals(3, plate.cellIndex(-1, 1));

		// warp right
		assertEquals(0, plate.cellIndex(2, 0));
		assertEquals(2, plate.cellIndex(2, 1));

		// wrap top
		assertEquals(0, plate.cellIndex(1, -1));
		assertEquals(1, plate.cellIndex(0, -1));

		// wrap bottom
		assertEquals(3, plate.cellIndex(0, 2));
		assertEquals(2, plate.cellIndex(1, 2));
	}

	@Test
	public void testOverlap() {
		plate = makePlate(map, 0, 1, 1, 0);
		plate2 = makePlate(map, 1, 1, 0, 0);
		assertArrayEquals(new int[]{0, 1, 0, 0}, toInt(plate.getMasksOverlap(plate2)));
	}
	
	@Test
	public void testInvert() {
		plate = makePlate(map, 0, 1, 1, 0);
		assertArrayEquals(new int[]{1, 0, 0, 1}, toInt(plate.getInvertedMask()));
	}

	@Test
	public void testMoveHorizontalWrap() {
		plate = makePlate(map, 1, 0, 0, 1);
		int[] expected = new int[] { 1, 1, 0, 0 };
		plate.velocity = new Point2D(1, 0);
		assertPlateEquals(expected, plate.step());
	}

	@Test
	public void testMoveVerticalWrap() {
		map = new Map(4, 2);
		plate = makePlate(map, 0, 0, 0, 0, 0, 1, 0, 1);
		int[] expected = new int[] { 0, 1, 0, 1, 1, 0, 1, 0 };
		plate.velocity = new Point2D(0, -1);
		assertPlateEquals(expected, plate.step());
	}

	private TectonicPlate makePlate(Map map, int... mask) {
		return new TectonicPlate(map, toBoolean(mask), new float[map.size], new Point2D(0, 0), new Point2D(0, 0));
	}

	private boolean[] toBoolean(int[] ints) {
		boolean[] booleans = new boolean[ints.length];
		for (int i = 0; i < ints.length; i++)
			booleans[i] = ints[i] != 0;
		return booleans;
	}

	private void assertPlateEquals(int[] expected, TectonicPlate actual) {
		int[] ints = toInt(actual.mask);
		assertArrayEquals(Arrays.toString(ints) + " != " + Arrays.toString(expected), expected, ints);
	}

	private int[] toInt(boolean[] bools) {
		int[] ints = new int[bools.length];
		for (int i = 0; i < bools.length; i++)
			ints[i] = bools[i] ? 1 : 0;
		return ints;
	}
}
