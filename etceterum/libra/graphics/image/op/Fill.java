package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;

public final class Fill extends ColorOp {
    public Fill() {
        // no-op
    }
    
    public Fill(byte r, byte g, byte b) {
        super(r, g, b);
    }
    
    public Fill(byte r, byte g, byte b, byte a) {
        super(r, g, b, a);
    }
    
    public Fill(int rgba) {
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
        final int targetOffset = target.getOffset() << 2;
        int targetIdx = targetOffset;
        for (int xIdx = 0; xIdx < targetWidth; ++xIdx, targetIdx += 4) {
            targetData[0 + targetIdx] = r;
            targetData[1 + targetIdx] = g;
            targetData[2 + targetIdx] = b;
            targetData[3 + targetIdx] = a;
        }
        
        final int targetRowLength = targetWidth << 2;
        final int targetStride = target.getStride() << 2;
        targetIdx = targetOffset + targetStride;
        for (int yIdx = 1; yIdx < targetHeight; ++yIdx, targetIdx += targetStride) {
            System.arraycopy(targetData, targetOffset, targetData, targetIdx, targetRowLength);
        }
        
        return target;
    }
}
