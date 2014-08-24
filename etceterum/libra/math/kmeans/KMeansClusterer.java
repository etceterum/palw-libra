package etceterum.libra.math.kmeans;


import etceterum.libra.math.Randoms;
import etceterum.libra.math.Space;
import etceterum.libra.math.space.EuclideanSpace;

public final class KMeansClusterer {
    public static final boolean NOISY                   = true;
    
    public static final Space   DEFAULT_SPACE           = EuclideanSpace.getInstance();
    public static final int     DEFAULT_MAX_ITER_COUNT  = 100;
    
    private Space space;
    private int maxIterCount;
    
    public KMeansClusterer() {
        this(DEFAULT_SPACE, DEFAULT_MAX_ITER_COUNT);
    }
    
    public KMeansClusterer(Space space) {
        this(space, DEFAULT_MAX_ITER_COUNT);
    }
    
    public KMeansClusterer(int maxIterCount) {
        this(DEFAULT_SPACE, maxIterCount);
    }
    
    public KMeansClusterer(Space space, int maxIterCount) {
        setSpace(space).setMaxIterCount(maxIterCount);
    }
    
    public KMeansClusterer resetSpace() {
        return setSpace(DEFAULT_SPACE);
    }
    
    public KMeansClusterer setSpace(Space value) {
        space = null == value ? DEFAULT_SPACE : value;
        return this;
    }
    
    public KMeansClusterer resetMaxIterCount() {
        return setMaxIterCount(DEFAULT_MAX_ITER_COUNT);
    }
    
    public KMeansClusterer setMaxIterCount(int value) {
        maxIterCount = value < 1 ? DEFAULT_MAX_ITER_COUNT : value;
        return this;
    }
    
    public int getMaxIterCount() {
        return maxIterCount;
    }
    
    public Space getSpace() {
        return space;
    }
    
    public KMeansClusters cluster(KMeansSamples samples, int k) {
        assert null != samples;
        assert k > 0;
        
        final float[] sampleData = samples.data;
        final int sampleCount = samples.count;
        final int samplesOffset = samples.offset;
        final int sampleSize = samples.size;
        final int[] sampleClusterIndices = samples.clusterIndices;
        final float[] distanceRef = samples.distanceRef;
        samples.resetClusterIndices();
        
        final KMeansClusters clusters = new KMeansClusters(space, k, sampleSize);
        
        // seed centroids with random samples
        for (KMeansCluster cluster: clusters) {
            final float[] centroid = cluster.centroid;
            final int n = generateRandomInt(sampleCount);
            for (int j = 0, o = samplesOffset + n*sampleSize; j < sampleSize; ++j, ++o) {
                centroid[j] = sampleData[o];
            }
        }
        
        // main loop
        boolean again = true;
        int iter = 0;
        for (; again && iter < maxIterCount; ++iter) {
            again = false;
            clusters.error = 0;
            
            // reset counts
            clusters.resetSampleCounts();
            
            // iterate over all data samples and classify each sample into a cluster
            for (int i = 0, o = samplesOffset; i < sampleCount; ++i, o += sampleSize) {
                // cluster the sample was previously assigned to:
                final int oldClusterIdx = sampleClusterIndices[i];
                final int newClusterIdx = clusters.classify(sampleData, o, distanceRef);
                if (oldClusterIdx != newClusterIdx) {
                    sampleClusterIndices[i] = newClusterIdx;
                    again = true;
                }
                clusters.error += Math.abs(distanceRef[0]);
                final KMeansCluster cluster = clusters.get(newClusterIdx);
                ++cluster.sampleCount;
            }
            
            // recalculate clusters
            recalculateClusters(clusters, samples);
        }
        
        clusters.convergence = iter < maxIterCount;
        
        return clusters;
    }
    
    private void recalculateClusters(KMeansClusters clusters, KMeansSamples samples) {
        
        // reset centroids
        clusters.resetCentroids();
        
        // generate clusters from samples
        for (int i = 0, o = samples.offset; i < samples.count; ++i) {
            final KMeansCluster cluster = clusters.get(samples.clusterIndices[i]);
            final float[] centroid = cluster.centroid;
            for (int j = 0; j < samples.size; ++j, ++o) {
                centroid[j] += samples.data[o];
            }
        }
        
        // normalize centroids
        clusters.normalizeCentroids();
    }
    
    private int generateRandomInt(int upTo) {
        return Randoms.randomInt(upTo);
    }
}
