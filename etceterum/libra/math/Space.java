package etceterum.libra.math;

public interface Space {
    public float calculateDistance(int datumLength, float[] data1, int offset1, float[] data2, int offset2);
    public void normalize(float[] data, int length, int offset);
}
