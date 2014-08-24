package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;
import etceterum.libra.graphics.image.ImageOpFactory;

// http://www.rhinocerus.net/forum/lang-java-programmer/574119-sepia-tone-image-filter-java.html
// http://stackoverflow.com/questions/5132015/how-to-convert-image-to-sepia-in-java
public final class Sepia implements ImageOp {
    public static final class Factory implements ImageOpFactory {
        private static final Factory instance = new Factory();
        
        private Factory() {
            // do not instantiate me
        }
        
        public static Factory getInstance() {
            return instance;
        }

        @Override
        public ImageOp createOp() {
            return new Sepia();
        }
    }
    
    public static final int DEFAULT_DEPTH       = 20;
    public static final int MIN_DEPTH           = 0;
    
    public static final int DEFAULT_INTENSITY   = 30;
    public static final int MIN_INTENSITY       = 0;
    public static final int MAX_INTENSITY       = 255;
    
    private int depth, intensity;
    
    public Sepia() {
        reset();
    }
    
    public Sepia(int depth, int intensity) {
        set(depth, intensity);
    }
    
    public Sepia reset() {
        return set(DEFAULT_DEPTH, DEFAULT_INTENSITY);
    }
    
    public Sepia set(int depth, int intensity) {
        return setDepth(depth).setIntensity(intensity);
    }
    
    public Sepia setDepth(int value) {
        depth = value;
        return this;
    }
    
    public int getDepth() {
        return depth;
    }
    
    public Sepia setIntensity(int value) {
        intensity = value;
        return this;
    }
    
    public int getIntensity() {
        return intensity;
    }

    @Override
    public Image execute(Image input) {
        final Image target = input;
        if (null == target) {
            return target;
        }
        
        final int targetWidth = target.getWidth();
        final int targetHeight = target.getHeight();
        final int targetStride = target.getStride() << 2;
        final byte[] targetData = target.getData();
        int rowIdx = target.getOffset();
        
        final int i = intensity < MIN_INTENSITY ? MIN_INTENSITY : intensity > MAX_INTENSITY ? MAX_INTENSITY : intensity;
        final int d = depth < MIN_DEPTH ? MIN_DEPTH : depth;
        final int d2 = d << 1;
        
        int colIdx;
        int r, g, b, rgb;
        
        for (int y = 0; y < targetHeight; ++y, rowIdx += targetStride) {
            colIdx = rowIdx;
            for (int x = 0; x < targetWidth; ++x, colIdx += 4) {
                r = (int)targetData[0 + colIdx] & 0xFF;
                g = (int)targetData[1 + colIdx] & 0xFF;
                b = (int)targetData[2 + colIdx] & 0xFF;

                // NTSC luma:
                rgb = (r*77 + g*151 + b*28) >> 8;
                // simple average:
                //rgb = (r + g + b)/3;
                
                r = g = b = rgb;
                r += d2;
                if (r > 0xFF) {
                    r = 0xFF;
                }
                
                g += d;
                if (g > 0xFF) {
                    g = 0xFF;
                }

                // Darken blue color to increase sepia effect
                b -= i;

                // normalize if out of bounds
                if (b < 0) {
                    b = 0;
                }
                else if (b > 0xFF) {
                    b = 0xFF;
                }

                targetData[0 + colIdx] = (byte)r;
                targetData[1 + colIdx] = (byte)g;
                targetData[2 + colIdx] = (byte)b;
            }
        }
        
        return target;
    }
    
}
