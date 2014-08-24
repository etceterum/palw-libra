package etceterum.libra.graphics.image.quantization;

public final class Point {
    final float[] v;
    float w = 1;
    int k = 0;
    
    public Point(float[] v) {
        this.v = v;
    }
}