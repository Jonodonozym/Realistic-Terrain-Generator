
package jdz.RTGen;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

import jdz.RTGen.dataType.Map;
import jdz.RTGen.dataType.TectonicPlate;

public class TectonicPlateTests {
	
	private Map map;
	private TectonicPlate plateA;
	private TectonicPlate plateB;
	
	@Before
	public void setup() {
		map = new Map(4, 1);
		plateA = makePlate(map, 
				1, 0, 0, 1
		);
		plateB = makePlate(map, 
				0, 1, 1, 0
		);
	}

	@Test
	public void testAdd() {
		int[] expected = new int[] {
				1, 1, 1, 1
		};
		plateA.add(plateB);
		assertEquals(expected, plateA);
	}

	@Test
	public void testSubtract() {
		plateA = makePlate(map, 
				1, 1, 1, 1
		);
		int[] expected = new int[] {
				1, 0, 0, 1
		};
		plateA.subtract(plateB);
		
		assertEquals(expected, plateA);
	}

	@Test
	public void testOverlap() {
		plateA = makePlate(map, new int[] {
				1, 1, 1, 1
		});
		int[] expected = new int[] {
				0, 1, 1, 0
		};
		plateA.overlap(plateB);
		
		assertEquals(expected, plateA);
	}

	@Test
	public void testMove() {
		int[] expected = new int[] {
				0, 0, 1, 1
		};
		plateB.move(1, 0);
		
		assertEquals(expected, plateB);
	}

	@Test
	public void testMoveOffscreen() {
		map = new Map(3, 3);
		
		plateA = makePlate(map, 
				1, 0, 0,
				0, 0, 0,
				0, 0, 0
		);
		
		int[] expected = new int[] {
				1, 1, 0,
				1, 1, 0,
				0, 0, 0,
		};
		
		plateA.move(1, 1);
		assertEquals(expected, plateA);
	}
	
	private TectonicPlate makePlate(Map map, int... mask) {
		return new TectonicPlate(map, toBoolean(mask));
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
