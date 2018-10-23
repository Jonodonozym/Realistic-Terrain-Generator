
package jdz.RTGen.algorithms.cellDepthCalculator;

public interface DepthFunction {
	public float getDepth(int x, int y);

	public static float hypot(int x, int y) {
		return (float) Math.sqrt(x * x + y * y);
	}

	public static float sum(int x, int y) {
		return Math.abs(x) + Math.abs(y);
	}
}
