package etceterum.libra.graphics.image.quantization;

import etceterum.libra.math.Randoms;

public final class KMeansClusterer {
    //public static final int DEFAULT_ITER_COUNT = 10;
    
    float error = 0;
    
    public float[][] cluster(Distance distance, Data data, int k, int iterCount) {
        
        // seed clusters
        for (Point point: data) {
            point.k = -1;
        }
        
        // create/seed K centroids
        final float[][] centroids = new float[k][];
        for (int i = 0; i < k; ++i) {
            final float[] centroid = centroids[i] = new float[3];
            final int r = Randoms.randomInt(data.getSize());
            final float[] v = data.getPoint(r).v;
            centroid[0] = v[0];
            centroid[1] = v[1];
            centroid[2] = v[2];
            
            //System.out.println(" -> " + centroid[0] + "," + centroid[1] + "," + centroid[2]);
            //distance.normalize(centroid);
            //System.out.println(" <- " + centroid[0] + "," + centroid[1] + "," + centroid[2]);
        }
        
        // create weights vector to avoid re-creation in each call to recalculate()
        final float[] weights = new float[k];
        final float[] currentError = new float[1];
        
        // main iteration loop
        boolean again = true;
        for (int nIter = 0; again && nIter < iterCount; ++nIter) {
            again = false;
            error = 0;
            for (Point point: data) {
                final int oldK = point.k;
                point.k = classify(distance, centroids, point.v, currentError);
                error += currentError[0];
                if (oldK != point.k) {
                    again = true;
                }
            }
            
            recalculate(distance, data, centroids, weights);
        }
        
        return centroids;
    }
    
    public int classify(Distance distance, float[][] centroids, float[] v) {
        return classify(distance, centroids, v, null);
    }

    public int classify(Distance distance, float[][] centroids, float[] v, float[] e) {
        assert centroids.length > 0;
        
        int minK = 0;
        float minD = distance.calculate(centroids[minK], v);
        for (int k = minK + 1; k < centroids.length; ++k) {
            final float d = distance.calculate(centroids[k], v);
            if (d < minD) {
                minK = k;
                minD = d;
            }
        }
        
        if (null != e) {
            e[0] = minD*minD;
        }
        return minK;
    }
    
    private void recalculate(Distance distance, Data data, float[][] centroids, float[] weights) {
        final int k = centroids.length;
        
        // reset centroids and weights
        for (int i = 0; i < k; ++i) {
            centroids[i][0] = centroids[i][1] = centroids[i][2] = weights[i] = 0;
        }
        
        // collect new centroid data and weights from data
        for (Point point: data) {
            final float[] v = point.v;
            final int cluster = point.k;
            final float w = point.w;
            final float[] centroid = centroids[cluster];
            centroid[0] += v[0]*w;
            centroid[1] += v[1]*w;
            centroid[2] += v[2]*w;
            weights[cluster] += point.w;
        }
        
        // normalize/calculate medians
        for (int i = 0; i < k; ++i) {
            final float[] centroid = centroids[i];
            final float w = weights[i];
            if (w > 0) {
                centroid[0] /= w;
                centroid[1] /= w;
                centroid[2] /= w;
            }
            distance.normalize(centroid);
        }
    }
}
