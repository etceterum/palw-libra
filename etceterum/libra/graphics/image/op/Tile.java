package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;

// tile the source image inside target image as many times as it fits
public class Tile implements ImageOp {
    private Image source;
    
    public Tile() {
        resetSource();
    }
    
    public Tile(Image source) {
        setSource(source);
    }
    
    public Tile resetSource() {
        return setSource(null);
    }
    
    public Tile setSource(Image value) {
        source = value;
        return this;
    }

    @Override
    public Image execute(Image input) {
        final Image target = input;
        if (null == target || null == source) {
            return target;
        }
        
        final int targetWidth = target.getWidth(), targetHeight = target.getHeight();
        final int sourceWidth = source.getWidth(), sourceHeight = source.getHeight();
        if (0 == targetWidth || 0 == targetHeight || 0 == sourceWidth || 0 == sourceHeight) {
            return target;
        }
        final int targetWidthInBytes = targetWidth << 2;
        
        final byte[] sourceData = source.getData(), targetData = target.getData();
        int sourceIdx = source.getOffset() << 2, targetIdx = target.getOffset() << 2;
        final int sourceStrideInBytes = source.getStride() << 2, targetStrideInBytes = target.getStride() << 2;
        final int targetStrideRemainderInBytes = targetStrideInBytes - targetWidthInBytes;
        
        int fullSpanLengthX, fullSpanCountX, lastSpanLengthX;
        if (sourceWidth < targetWidth) {
            fullSpanLengthX = sourceWidth;
            fullSpanCountX = targetWidth/sourceWidth;
            lastSpanLengthX = targetWidth - fullSpanCountX*fullSpanLengthX;
        }
        else {
            fullSpanLengthX = targetWidth;
            fullSpanCountX = 1;
            lastSpanLengthX = 0;
        }
        
        int fullSpanLengthY, fullSpanCountY, lastSpanLengthY;
        if (sourceHeight < targetHeight) {
            fullSpanLengthY = sourceHeight;
            fullSpanCountY = targetHeight/sourceHeight;
            lastSpanLengthY = targetHeight - fullSpanCountY*fullSpanLengthY;
        }
        else {
            fullSpanLengthY = targetHeight;
            fullSpanCountY = 1;
            lastSpanLengthY = 0;
        }
        
        final int fullSpanLengthInBytesX = fullSpanLengthX << 2;
        final int lastSpanLengthInBytesX = lastSpanLengthX << 2;
        
        for (int y = 0; y < fullSpanLengthY; ++y, sourceIdx += sourceStrideInBytes, targetIdx += targetStrideRemainderInBytes) {
            
            for (int i = 0; i < fullSpanCountX; ++i, targetIdx += fullSpanLengthInBytesX) {
                System.arraycopy(sourceData, sourceIdx, targetData, targetIdx, fullSpanLengthInBytesX);
            }
            if (lastSpanLengthX > 0) {
                System.arraycopy(sourceData, sourceIdx, targetData, targetIdx, lastSpanLengthInBytesX);
                targetIdx += lastSpanLengthInBytesX;
            }
        }
        
        final int targetSourceOffset = targetStrideInBytes*fullSpanLengthY;
        for (int i = 1; i < fullSpanCountY; ++i) {
            for (int y = 0; y < fullSpanLengthY; ++y, targetIdx += targetStrideInBytes) {
                System.arraycopy(targetData, targetIdx - targetSourceOffset, targetData, targetIdx, targetWidthInBytes);
            }
        }
        
        for (int y = 0; y < lastSpanLengthY; ++y, targetIdx += targetStrideInBytes) {
            System.arraycopy(targetData, targetIdx - targetSourceOffset, targetData, targetIdx, targetWidthInBytes);
        }
        
        return target;
    }
}
