package etceterum.libra.math.space;

import etceterum.libra.math.Space;

public final class EuclideanSpace implements Space {
    private static final EuclideanSpace instance = new EuclideanSpace();
    
    public static EuclideanSpace getInstance() {
        return instance;
    }
    
    private EuclideanSpace() {
        // instantiate me not
    }

    @Override
    public float calculateDistance(int datumLength, float[] data1, int offset1, float[] data2, int offset2) {
        final float end1 = offset1 + datumLength;
        float distance = 0;
        for (int o1 = offset1, o2 = offset2; o1 < end1; ++o1, ++o2) {
            final float delta = data1[o1] - data2[o2];
            distance += delta*delta;
        }
        return (float)Math.sqrt(distance);
    }

    @Override
    public void normalize(float[] data, int length, int offset) {
        // no-op
    }
}
