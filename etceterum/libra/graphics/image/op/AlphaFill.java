package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;

public class AlphaFill extends ColorOp {
    public AlphaFill() {
        // no-op
    }
    
    public AlphaFill(byte r, byte g, byte b, byte a) {
        super(r, g, b, a);
    }
    
    public AlphaFill(byte r, byte g, byte b) {
        super(r, g, b);
    }
    
    public AlphaFill(int rgba) {
        super(rgba);
    }
    
    @Override
    public Image execute(Image input) {
        final Image target = input;
        final int targetWidth = target.getWidth(), targetHeight = target.getHeight();
        if (0 == targetWidth || 0 == targetHeight) {
            // nothing to do
            return target;
        }
        
        final byte[] targetData = target.getData();
        final int targetOffset = target.getOffset(), targetStride = target.getStride();
        final int targetSkipInBytes = (targetStride - targetWidth) << 2;
        
        final int sourceR = (int)r & 0xFF, sourceG = (int)g & 0xFF, sourceB = (int)b & 0xFF, sourceA = (int)a & 0xFF;
        
        int targetR, targetG, targetB, targetA, oneMinusSourceA, r, g, b, a;
        
        int targetIdx = targetOffset << 2;
        for (int targetY = 0; targetY < targetHeight; ++targetY, targetIdx += targetSkipInBytes) {
            for (int targetX = 0; targetX < targetWidth; ++targetX, targetIdx += 4) {
                
                oneMinusSourceA = 0xFF - sourceA;
                targetA = (int)targetData[3 + targetIdx] & 0xFF;
                a = (sourceA*sourceA + targetA*oneMinusSourceA) >> 8;
                targetData[3 + targetIdx] = (byte)a;
                
                targetR = (int)targetData[0 + targetIdx] & 0xFF;
                r = (sourceR*sourceA + targetR*oneMinusSourceA) >> 8;
                targetData[0 + targetIdx] = (byte)r;
                
                targetG = (int)targetData[1 + targetIdx] & 0xFF;
                g = (sourceG*sourceA + targetG*oneMinusSourceA) >> 8;
                targetData[1 + targetIdx] = (byte)g;
                
                targetB = (int)targetData[2 + targetIdx] & 0xFF;
                b = (sourceB*sourceA + targetB*oneMinusSourceA) >> 8;
                targetData[2 + targetIdx] = (byte)b;
            }
        }
        
        return target;
    }
}
