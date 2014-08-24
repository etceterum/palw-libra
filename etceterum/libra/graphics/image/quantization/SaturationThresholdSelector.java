package etceterum.libra.graphics.image.quantization;

public final class SaturationThresholdSelector implements Selector {
    public static final int DEFAULT_THRESHOLD = 0;
    private int threshold = DEFAULT_THRESHOLD;
    
    public SaturationThresholdSelector() {
        // no-op
    }
    
    public SaturationThresholdSelector(int threshold) {
        setThreshold(threshold);
    }
    
    public SaturationThresholdSelector setThreshold(int value) {
        assert value >= 0 && value <= 0xFF;
        threshold = value;
        return this;
    }
    
    public int getThreshold() {
        return threshold;
    }

    @Override
    public boolean select(byte[] rgb, int offset) {
        final int r = 0xFF & rgb[0 + offset];
        final int g = 0xFF & rgb[1 + offset];
        final int b = 0xFF & rgb[2 + offset];
        
        final int max = Math.max(r, Math.max(g, b));
        
        int saturation;
        if (0 == max) {
            saturation = 0;
        }
        else {
            final int min = Math.min(r, Math.min(g, b));
            saturation = 0xFF - min*0xFF/max;
        }
        
        return saturation >= threshold;
    }

}
