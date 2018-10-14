
package jdz.RTGen;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

import javafx.geometry.Point2D;
import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class TectonicPlateTests {

	private Map map;
	private TectonicPlate plateA;
	private TectonicPlate plateB;

	@Before
	public void setup() {
		map = new Map(4, 1);
		plateA = makePlate(map, 1, 0, 0, 1);
		plateB = makePlate(map, 0, 1, 1, 0);
	}

	@Test
	public void testAdd() {
		int[] expected = new int[] { 1, 1, 1, 1 };
		assertEquals(expected, plateA.merge(plateB));
	}

	@Test
	public void testSubtract() {
		plateA = makePlate(map, 1, 1, 1, 1);
		int[] expected = new int[] { 1, 0, 0, 1 };

		assertEquals(expected, plateA.removeOverlap(plateB));
	}

	@Test
	public void testOverlap() {
		plateA = makePlate(map, new int[] { 1, 1, 1, 1 });
		int[] expected = new int[] { 0, 1, 1, 0 };

		assertEquals(expected, plateA.getOverlap(plateB));
	}

	@Test
	public void testMove() {
		int[] expected = new int[] { 0, 0, 1, 1 };
		assertEquals(expected, plateB.move(1, 0));
	}

	@Test
	public void testMoveWrap() {
		int[] expected = new int[] { 1, 1, 0, 0 };
		assertEquals(expected, plateA.move(1, 0));
	}

	private TectonicPlate makePlate(Map map, int... mask) {
		return new TectonicPlate(map, toBoolean(mask), new float[map.getSize()], new Point2D(0, 0));
	}

	private boolean[] toBoolean(int[] ints) {
		boolean[] booleans = new boolean[ints.length];
		for (int i = 0; i < ints.length; i++)
			booleans[i] = ints[i] != 0;
		return booleans;
	}

	private void assertEquals(int[] expected, TectonicPlate actual) {
		assertArrayEquals(expected, toInt(actual.getMask()));
	}

	private int[] toInt(boolean[] bools) {
		int[] ints = new int[bools.length];
		for (int i = 0; i < bools.length; i++)
			ints[i] = bools[i] ? 1 : 0;
		return ints;
	}
}
