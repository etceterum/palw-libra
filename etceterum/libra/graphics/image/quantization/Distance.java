package etceterum.libra.graphics.image.quantization;

// assumes that all vectors are [3]
public interface Distance {
    void normalize(float[] v);
    float calculate(float[] v1, float[] v2);
}
