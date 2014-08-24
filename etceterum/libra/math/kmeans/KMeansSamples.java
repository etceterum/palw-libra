package etceterum.libra.math.kmeans;

public final class KMeansSamples {
    final float[] data;
    final int offset;
    final int size;
    final int count;
    final int[] clusterIndices;
    final float[] distanceRef;
    
    public KMeansSamples(float[] data, int offset, int count, int size) {
        assert null != data;
        assert offset >= 0;
        assert count > 0;
        assert size > 0;
        assert offset + count*size < data.length;
        
        this.data = data;
        this.offset = offset;
        this.size = size;
        this.count = count;
        
        clusterIndices = new int[count];
        distanceRef = new float[1];
    }
    
    void resetClusterIndices() {
        for (int i = 0; i < count; ++i) {
            clusterIndices[i] = -1;
        }
    }
    
    public int getCount() {
        return count;
    }
}
