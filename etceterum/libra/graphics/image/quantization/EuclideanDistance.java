package etceterum.libra.graphics.image.quantization;

public final class EuclideanDistance implements Distance {
    private static final EuclideanDistance instance = new EuclideanDistance();
    
    public static EuclideanDistance getInstance() {
        return instance;
    }
    
    private EuclideanDistance() {
        // instantiate me not
    }

    @Override
    public void normalize(float[] v) {
        // no-op
    }

    @Override
    public float calculate(float[] v1, float[] v2) {
        final float dx = v1[0] - v2[0];
        final float dy = v1[1] - v2[1];
        final float dz = v1[2] - v2[2];
        return (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

}
