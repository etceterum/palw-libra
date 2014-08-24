package etceterum.libra.graphics.image.quantization;

import etceterum.libra.graphics.image.Image;

public final class TestQuantizer1 {
    public static final boolean NOISY = true;
    public static final Distance DISTANCE = EuclideanDistance.getInstance();
    
    public float[][] quantize(Selector selector, Image image, int k, int iterCount) {
        return doQuantize(selector, image, k, iterCount);
    }
    
    private float[][] doQuantize(Selector selector, Image image, int k, int iterCount) {
        if (null == selector || null == image || k <= 0) {
            return null;
        }
        final int imageWidth = image.getWidth(), imageHeight = image.getHeight();
        if (0 == imageWidth || 0 == imageHeight) {
            return null;
        }
        
        if (NOISY) System.out.println(getClass().getName() + ": quantizing image " + imageWidth + "x" + imageHeight + " (" + (imageWidth*imageHeight) + " pixels) into " + k + " clusters with max of " + iterCount + " iterations");
        
        final int imageOffset = image.getOffset(), imageStride = image.getStride();
        final int imageSkip = (imageStride - imageWidth) << 2;
        final byte[] imageData = image.getData();
        
        final Data data = new Data((imageWidth*imageHeight) >> 2);
        
        int imageIdx = imageOffset << 2;
        for (int y = 0; y < imageHeight; ++y, imageIdx += imageSkip) {
            for (int x = 0; x < imageWidth; ++x, imageIdx += 4) {
                if (selector.select(imageData, imageIdx)) {
                    final int r = (int)imageData[0 + imageIdx] & 0xFF;
                    final int g = (int)imageData[1 + imageIdx] & 0xFF;
                    final int b = (int)imageData[2 + imageIdx] & 0xFF;
                    final float[] v = new float[] { r/255f, g/255f, b/255f };
                    DISTANCE.normalize(v);
                    data.addPoint(new Point(v));
                }
            }
        }
        
        if (NOISY) System.out.println(getClass().getName() + ": selected " + data.getSize() + " data points");
        
        final KMeansClusterer clusterer = new KMeansClusterer();
        float[][] centroids = clusterer.cluster(DISTANCE, data, k, iterCount);
        
        if (NOISY) {
            System.out.println(getClass().getName() + ": clustering done (E=" + clusterer.error + "):");
            for (int i = 0; i < centroids.length; ++i) {
                final float[] centroid = centroids[i];
                System.out.println("  centroid: " + centroid[0] + "," + centroid[1] + "," + centroid[2]);
            }
        }
        
        return centroids;
    }
}
