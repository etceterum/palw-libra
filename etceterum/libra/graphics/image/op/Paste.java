package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;

public final class Paste extends SourceOp {
    private Interpolation interpolation = null;
    
    public Paste() {
        // no-op
    }
    
    public Paste(Image source) {
        super(source);
    }
    
    public Paste(Interpolation interpolation) {
        setInterpolationMethod(interpolation);
    }
    
    public Paste(Image source, Interpolation interpolation) {
        set(source, interpolation);
    }
    
    public Paste reset() {
        return resetSource().resetInterpolationMethod();
    }
    
    public Paste set(Image source, Interpolation interpolation) {
        return setSource(source).setInterpolationMethod(interpolation);
    }
    
    @Override
    public Paste resetSource() {
        return setSource(null);
    }

    @Override
    public Paste setSource(Image source) {
        super.setSource(source);
        return this;
    }
    
    public Paste resetInterpolationMethod() {
        return setInterpolationMethod(null);
    }
    
    public Paste setInterpolationMethod(Interpolation interpolation) {
        this.interpolation = interpolation;
        return this;
    }
    
    public Interpolation getInterpolationMethod() {
        return interpolation;
    }
    
    @Override
    public Image execute(Image input) {
        final Image target = input;
        
        if (null == source || source == target) {
            // nothing to do
            return target;
        }
        
        final int sourceWidth = source.getWidth(), sourceHeight = source.getHeight();
        final int targetWidth = target.getWidth(), targetHeight = target.getHeight();
        
        if (0 == sourceWidth || 0 == sourceHeight || 0 == targetWidth || 0 == targetHeight) {
            // nothing to do
            return target;
        }
        
        if (sourceWidth == targetWidth && sourceHeight == targetHeight) {
            // special case: no scaling
            // TODO: this code duplicates Image.getClone()
            
            int sourceIdx = source.getOffset() << 2;
            int targetIdx = target.getOffset() << 2;
            final byte[] sourceData = source.getData();
            final byte[] targetData = target.getData();
            final int rowLength = sourceWidth << 2;
            final int sourceStride = source.getStride() << 2;
            final int targetStride = target.getStride() << 2;
            
            for (int row = 0; row < sourceHeight; ++row, sourceIdx += sourceStride, targetIdx += targetStride) {
                System.arraycopy(sourceData, sourceIdx, targetData, targetIdx, rowLength);
            }
            
            return target;
        }
        
        if (Interpolation.BILINEAR == interpolation) {
            // TODO optimize
            int sourceOffset = source.getOffset();
            int targetOffset = target.getOffset();
            final byte[] sourceData = source.getData();
            final byte[] targetData = target.getData();
            final int sourceStride = source.getStride(), sourceStrideInBytes = sourceStride << 2;
            final int targetStride = target.getStride();
            
            final float xRatio = sourceWidth/(float)targetWidth;
            final float yRatio = sourceHeight/(float)targetHeight;

            final int targetSkipInBytes = (targetStride - targetWidth) << 2;
            
            int targetIdx = targetOffset << 2;
            for (int targetY = 0; targetY < targetHeight; ++targetY, targetIdx += targetSkipInBytes) {
                final float sourceY = targetY*yRatio;
                final int sourceYBase = (int)sourceY;
                final float sourceYFraction = sourceY - sourceYBase;
                for (int targetX = 0; targetX < targetWidth; ++targetX, targetIdx += 4) {
                    final float sourceX = targetX*xRatio;
                    final int sourceXBase = (int)sourceX;
                    final float sourceXFraction = sourceX - sourceXBase;
                    
                    final int lowerLeftIdx = (sourceOffset + sourceYBase*sourceStride + sourceXBase) << 2;
                    final int lowerRightIdx = sourceXBase >= sourceWidth - 1 ? lowerLeftIdx : lowerLeftIdx + 4;
                    final int upperLeftIdx = sourceYBase >= sourceHeight - 1 ? lowerLeftIdx : lowerLeftIdx + sourceStrideInBytes;
                    final int upperRightIdx = sourceXBase >= sourceWidth - 1 ? upperLeftIdx : upperLeftIdx + 4;
                    
                    //final int targetIdx = (targetOffset + y*targetStride + j) << 2;
                    
                    // red
                    final int rLowerLeft = (int)sourceData[0 + lowerLeftIdx] & 0xFF;
                    final int rLowerRight = (int)sourceData[0 + lowerRightIdx] & 0xFF;
                    final float rLowerAverage = rLowerLeft + sourceXFraction*(rLowerRight - rLowerLeft);
                    final int rUpperLeft = (int)sourceData[0 + upperLeftIdx] & 0xFF;
                    final int rUpperRight = (int)sourceData[0 + upperRightIdx] & 0xFF;
                    final float rUpperAverage = rUpperLeft + sourceXFraction*(rUpperRight - rUpperLeft);
                    final float r = rLowerAverage + sourceYFraction*(rUpperAverage - rLowerAverage);
                    targetData[0 + targetIdx] = (byte)(int)r;
                    
                    // green
                    final int gLowerLeft = (int)sourceData[1 + lowerLeftIdx] & 0xFF;
                    final int gLowerRight = (int)sourceData[1 + lowerRightIdx] & 0xFF;
                    final float gLowerAverage = gLowerLeft + sourceXFraction*(gLowerRight - gLowerLeft);
                    final int gUpperLeft = (int)sourceData[1 + upperLeftIdx] & 0xFF;
                    final int gUpperRight = (int)sourceData[1 + upperRightIdx] & 0xFF;
                    final float gUpperAverage = gUpperLeft + sourceXFraction*(gUpperRight - gUpperLeft);
                    final float g = gLowerAverage + sourceYFraction*(gUpperAverage - gLowerAverage);
                    targetData[1 + targetIdx] = (byte)(int)g;
                    
                    // blue
                    final int bLowerLeft = (int)sourceData[2 + lowerLeftIdx] & 0xFF;
                    final int bLowerRight = (int)sourceData[2 + lowerRightIdx] & 0xFF;
                    final float bLowerAverage = bLowerLeft + sourceXFraction*(bLowerRight - bLowerLeft);
                    final int bUpperLeft = (int)sourceData[2 + upperLeftIdx] & 0xFF;
                    final int bUpperRight = (int)sourceData[2 + upperRightIdx] & 0xFF;
                    final float bUpperAverage = bUpperLeft + sourceXFraction*(bUpperRight - bUpperLeft);
                    final float b = bLowerAverage + sourceYFraction*(bUpperAverage - bLowerAverage);
                    targetData[2 + targetIdx] = (byte)(int)b;
                    
                    // alpha
                    final int aLowerLeft = (int)sourceData[3 + lowerLeftIdx] & 0xFF;
                    final int aLowerRight = (int)sourceData[3 + lowerRightIdx] & 0xFF;
                    final float aLowerAverage = aLowerLeft + sourceXFraction*(aLowerRight - aLowerLeft);
                    final int aUpperLeft = (int)sourceData[3 + upperLeftIdx] & 0xFF;
                    final int aUpperRight = (int)sourceData[3 + upperRightIdx] & 0xFF;
                    final float aUpperAverage = aUpperLeft + sourceXFraction*(aUpperRight - aUpperLeft);
                    final float a = aLowerAverage + sourceYFraction*(aUpperAverage - aLowerAverage);
                    targetData[3 + targetIdx] = (byte)(int)a;
                }
            }
            
            return target;
        }
        
        assert null == interpolation || Interpolation.NEAREST_NEIGHBOR == interpolation;
        // scale using nearest-neighbor interpolation
        // TODO: move common initializations out of if/else block
        // TODO: optimize
        
        int sourceOffset = source.getOffset();
        int targetOffset = target.getOffset();
        final byte[] sourceData = source.getData();
        final byte[] targetData = target.getData();
        final int sourceStride = source.getStride();
        final int targetStride = target.getStride();
        
        final float xRatio = sourceWidth/(float)targetWidth;
        final float yRatio = sourceHeight/(float)targetHeight;
        
        final int targetSkipInBytes = (targetStride - targetWidth) << 2;
        int targetIdx = targetOffset << 2;
        for (int targetY = 0; targetY < targetHeight; ++targetY, targetIdx += targetSkipInBytes) {
            final float py = (float)Math.floor(targetY*yRatio);
            for (int targetX = 0; targetX < targetWidth; ++targetX, targetIdx += 4) {
                final float px = (float)Math.floor(targetX*xRatio);
                
                final int sourceIdx = (sourceOffset + (int)(py*sourceStride + px)) << 2;
                //final int targetIdx = (targetOffset + y*targetStride + x) << 2;
                
                targetData[0 + targetIdx] = sourceData[0 + sourceIdx];
                targetData[1 + targetIdx] = sourceData[1 + sourceIdx];
                targetData[2 + targetIdx] = sourceData[2 + sourceIdx];
                targetData[3 + targetIdx] = sourceData[3 + sourceIdx];
            }
        }
        
        return target;
    }
}
