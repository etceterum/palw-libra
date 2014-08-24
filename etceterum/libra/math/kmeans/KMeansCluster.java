package etceterum.libra.math.kmeans;

public class KMeansCluster {
    final float[] centroid;
    int sampleCount;
    
    KMeansCluster(int centroidSize) {
        assert centroidSize > 0;
        centroid = new float[centroidSize];
    }
    
    void resetCentroid() {
        for (int i = 0; i < centroid.length; ++i) {
            centroid[i] = 0;
        }
    }
    
    public float[] getCentroid() {
        return centroid;
    }
    
    public int getSampleCount() {
        return sampleCount;
    }
}
