package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;
import etceterum.libra.graphics.image.ImageOpFactory;

public final class Grayscale implements ImageOp {
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
            return new Grayscale();
        }
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
        
        for (int y = 0; y < targetHeight; ++y, rowIdx += targetStride) {
            int colIdx = rowIdx;
            for (int x = 0; x < targetWidth; ++x, colIdx += 4) {
                final int r = (int)targetData[0 + colIdx] & 0xFF;
                final int g = (int)targetData[1 + colIdx] & 0xFF;
                final int b = (int)targetData[2 + colIdx] & 0xFF;
                
                // NTSC luma
                final int rgb = (r*77 + g*151 + b*28) >> 8;
            
                targetData[0 + colIdx] = (byte)rgb;
                targetData[1 + colIdx] = (byte)rgb;
                targetData[2 + colIdx] = (byte)rgb;
            }
        }
        
        return target;
    }

}
