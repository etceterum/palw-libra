package etceterum.libra.graphics.image.quantization;

public final class PearsonDistance implements Distance {
    private static final PearsonDistance instance = new PearsonDistance();
    
    public static PearsonDistance getInstance() {
        return instance;
    }
    
    private PearsonDistance() {
        // instantiate me not
    }
    
    @Override
    public void normalize(float[] v) {
        final float mu = (v[0] + v[1] + v[2])/3;
        v[0] -= mu;
        v[1] -= mu;
        v[2] -= mu;
        final float norm = (float)Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
        if (norm > 0) {
            v[0] /= norm;
            v[1] /= norm;
            v[2] /= norm;
        }
    }
    
    // v1, v2 must be normalized; result is not checked but assumed to be in the [0..2] range
    @Override
    public float calculate(float[] v1, float[] v2) {
        return v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2];
    }
}
