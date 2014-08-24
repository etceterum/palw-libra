package etceterum.libra.math.kmeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import etceterum.libra.math.Space;

public class KMeansClusters implements Iterable<KMeansCluster> {
    private final Space space;
    private final int sampleSize;
    private final ArrayList<KMeansCluster> clusters;
    float error;
    boolean convergence;
    
    KMeansClusters(Space space, int clusterCount, int sampleSize) {
        assert null != space;
        assert clusterCount > 0;
        assert sampleSize > 0;
        
        this.space = space;
        this.sampleSize = sampleSize;
        clusters = new ArrayList<KMeansCluster>(clusterCount);
        for (int i = 0; i < clusterCount; ++i) {
            clusters.add(new KMeansCluster(sampleSize));
        }
    }
    
    public int classify(float[] data, int offset, float[] distanceRef) {
        final int clusterCount = clusters.size();
        final KMeansCluster firstCluster = clusters.get(0);
        final float[] firstCentroid = firstCluster.centroid;
        final int sampleSize = firstCentroid.length;
        float minDistance = space.calculateDistance(sampleSize, firstCentroid, 0, data, offset);
        int minIdx = 0;
        for (int i = 1; i < clusterCount; ++i) {
            final float distance = space.calculateDistance(sampleSize, clusters.get(i).centroid, 0, data, offset);
            if (distance < minDistance) {
                minDistance = distance;
                minIdx = i;
            }
        }
        if (null != distanceRef) {
            distanceRef[0] = minDistance;
        }
        return minIdx;
    }
    
    public int classify(float[] data, int offset) {
        return classify(data, offset, null);
    }
    
    public KMeansCluster get(int index) {
        return clusters.get(index);
    }
    
    public int size() {
        return clusters.size();
    }

    @Override
    public Iterator<KMeansCluster> iterator() {
        return clusters.iterator();
    }
    
    public float getError() {
        return error;
    }
    
    public boolean getConvergence() {
        return convergence;
    }
    
    public void sort(Comparator<KMeansCluster> comparator) {
        Collections.sort(clusters, comparator);
    }
    
    void resetSampleCounts() {
        for (KMeansCluster cluster: clusters) {
            cluster.sampleCount = 0;
        }
    }
    
    void resetCentroids() {
        for (KMeansCluster cluster: clusters) {
            cluster.resetCentroid();
        }
    }
    
    void normalizeCentroids() {
        for (KMeansCluster cluster: clusters) {
            final float[] centroid = cluster.centroid;
            final int sampleCount = cluster.sampleCount;
            if (0 != sampleCount) {
                for (int j = 0; j < sampleSize; ++j) {
                    centroid[j] /= sampleCount;
                }
            }
            space.normalize(centroid, sampleSize, 0);
        }
    }
}
