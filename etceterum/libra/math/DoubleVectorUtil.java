package etceterum.libra.math;

public final class DoubleVectorUtil {
    private DoubleVectorUtil() {
        // prevent instantiation
    }
    
    public static boolean isUnitSquaredMagnitude(double squaredMagnitude) {
        assert squaredMagnitude >= 0;
        return Math.abs(squaredMagnitude - 1) < Const.SQUARED_EPSILON;
    }
}
