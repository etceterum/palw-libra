package etceterum.libra.graphics.image.op;


import etceterum.libra.graphics.blending.Blender;
import etceterum.libra.graphics.blending.SourceColor_Zero;
import etceterum.libra.graphics.image.Image;

public final class Blend extends SourceOp {
    public static final Blender DEFAULT_BLENDER = SourceColor_Zero.INSTANCE;
    
    private Blender blender = null;
    private Interpolation interpolationMethod = null;
    
    public Blend() {
        // no-op
    }
    
    public Blend(Image source) {
        super(source);
    }
    
    public Blend(Blender blender) {
        setBlender(blender);
    }
    
    public Blend(Interpolation interpolationMethod) {
        setInterpolation(interpolationMethod);
    }
    
    public Blend(Image source, Blender blender) {
        setSource(source).setBlender(blender);
    }
    
    public Blend(Blender blender, Interpolation interpolationMethod) {
        setBlender(blender).setInterpolation(interpolationMethod);
    }
    
    public Blend(Image source, Blender blender, Interpolation interpolationMethod) {
        set(source, blender, interpolationMethod);
    }
    
    public Blend reset() {
        return set(null, null, null);
    }
    
    public Blend set(Image source, Blender blender, Interpolation interpolationMethod) {
        return setSource(source).setBlender(blender).setInterpolation(interpolationMethod);
    }
    
    @Override
    public Blend resetSource() {
        return setSource(null);
    }
    
    @Override
    public Blend setSource(Image value) {
        super.setSource(value);
        return this;
    }
    
    public Blend resetBlender() {
        return setBlender(null);
    }
    
    public Blend setBlender(Blender value) {
        this.blender = value;
        return this;
    }
    
    public Blender getBlender() {
        return blender;
    }
    
    public Blend resetInterpolation() {
        return setInterpolation(null);
    }
    
    public Blend setInterpolation(Interpolation value) {
        this.interpolationMethod = value;
        return this;
    }
    
    public Interpolation getInterpolation() {
        return interpolationMethod;
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
        
        final Blender blendOp = null == blender ? DEFAULT_BLENDER : blender;
        
        if (sourceWidth == targetWidth && sourceHeight == targetHeight) {
            // special case: no scaling
            
            int sourceIdx = source.getOffset() << 2;
            int targetIdx = target.getOffset() << 2;
            final byte[] sourceData = source.getData();
            final byte[] targetData = target.getData();
            final int sourceStrideInBytes = source.getStride() << 2;
            final int targetStrideInBytes = target.getStride() << 2;
            
            for (int row = 0; row < sourceHeight; ++row, sourceIdx += sourceStrideInBytes, targetIdx += targetStrideInBytes) {
                int sourcePixelIdx = sourceIdx, targetPixelIdx = targetIdx;
                for (int col = 0; col < sourceWidth; ++col, sourcePixelIdx += 4, targetPixelIdx += 4) {
                    blendOp.blend(sourceData, sourcePixelIdx, targetData, targetPixelIdx);
                }
            }
            
            return target;
        }
        
        final byte[] interpolatedData = new byte[4];
        
        if (Interpolation.BILINEAR == interpolationMethod) {
            // TODO optimize
            int sourceOffset = source.getOffset();
            int targetOffset = target.getOffset();
            final byte[] sourceData = source.getData();
            final byte[] targetData = target.getData();
            final int sourceStride = source.getStride();
            final int targetStride = target.getStride();
            
            final float scaleX = sourceWidth/(float)targetWidth;
            final float scaleY = sourceHeight/(float)targetHeight;

            final int sourceWidthMinus1 = sourceWidth - 1, sourceHeightMinus1 = sourceHeight - 1;
            final int targetSkipInBytes = (targetStride - targetWidth) << 2;
            final int sourceStrideInBytes = sourceStride << 2;
            
            int targetIdx = targetOffset << 2;
            for (int targetY = 0; targetY < targetHeight; ++targetY, targetIdx += targetSkipInBytes) {
                final float sourceY = targetY*scaleY;
                final int sourceBaseY = (int)Math.floor(sourceY);
                final float sourceWeightY = sourceY - sourceBaseY;
                for (int targetX = 0; targetX < targetWidth; ++targetX, targetIdx += 4) {
                    final float sourceX = targetX*scaleX;
                    final int sourceBaseX = (int)Math.floor(sourceX);
                    final float sourceWeightX = sourceX - sourceBaseX;
                    
                    final int lowerLeftIdx = (sourceOffset + sourceBaseY*sourceStride + sourceBaseX) << 2;
                    final int lowerRightIdx = sourceBaseX >= sourceWidthMinus1 ? lowerLeftIdx : lowerLeftIdx + 4;
                    final int upperLeftIdx = sourceBaseY >= sourceHeightMinus1 ? lowerLeftIdx : lowerLeftIdx + sourceStrideInBytes;
                    final int upperRightIdx = sourceBaseX >= sourceWidthMinus1 ? upperLeftIdx : upperLeftIdx + 4;
                    
                    // red
                    final int rLowerLeft = (int)sourceData[0 + lowerLeftIdx] & 0xFF;
                    final int rLowerRight = (int)sourceData[0 + lowerRightIdx] & 0xFF;
                    final float rLowerAverage = rLowerLeft + sourceWeightX*(rLowerRight - rLowerLeft);
                    final int rUpperLeft = (int)sourceData[0 + upperLeftIdx] & 0xFF;
                    final int rUpperRight = (int)sourceData[0 + upperRightIdx] & 0xFF;
                    final float rUpperAverage = rUpperLeft + sourceWeightX*(rUpperRight - rUpperLeft);
                    final float r = rLowerAverage + sourceWeightY*(rUpperAverage - rLowerAverage);
                    interpolatedData[0] = (byte)(int)r;
                    
                    // green
                    final int gLowerLeft = (int)sourceData[1 + lowerLeftIdx] & 0xFF;
                    final int gLowerRight = (int)sourceData[1 + lowerRightIdx] & 0xFF;
                    final float gLowerAverage = gLowerLeft + sourceWeightX*(gLowerRight - gLowerLeft);
                    final int gUpperLeft = (int)sourceData[1 + upperLeftIdx] & 0xFF;
                    final int gUpperRight = (int)sourceData[1 + upperRightIdx] & 0xFF;
                    final float gUpperAverage = gUpperLeft + sourceWeightX*(gUpperRight - gUpperLeft);
                    final float g = gLowerAverage + sourceWeightY*(gUpperAverage - gLowerAverage);
                    interpolatedData[1] = (byte)(int)g;
                    
                    // blue
                    final int bLowerLeft = (int)sourceData[2 + lowerLeftIdx] & 0xFF;
                    final int bLowerRight = (int)sourceData[2 + lowerRightIdx] & 0xFF;
                    final float bLowerAverage = bLowerLeft + sourceWeightX*(bLowerRight - bLowerLeft);
                    final int bUpperLeft = (int)sourceData[2 + upperLeftIdx] & 0xFF;
                    final int bUpperRight = (int)sourceData[2 + upperRightIdx] & 0xFF;
                    final float bUpperAverage = bUpperLeft + sourceWeightX*(bUpperRight - bUpperLeft);
                    final float b = bLowerAverage + sourceWeightY*(bUpperAverage - bLowerAverage);
                    interpolatedData[2] = (byte)(int)b;
                    
                    // alpha
                    final int aLowerLeft = (int)sourceData[3 + lowerLeftIdx] & 0xFF;
                    final int aLowerRight = (int)sourceData[3 + lowerRightIdx] & 0xFF;
                    final float aLowerAverage = aLowerLeft + sourceWeightX*(aLowerRight - aLowerLeft);
                    final int aUpperLeft = (int)sourceData[3 + upperLeftIdx] & 0xFF;
                    final int aUpperRight = (int)sourceData[3 + upperRightIdx] & 0xFF;
                    final float aUpperAverage = aUpperLeft + sourceWeightX*(aUpperRight - aUpperLeft);
                    final float a = aLowerAverage + sourceWeightY*(aUpperAverage - aLowerAverage);
                    interpolatedData[3] = (byte)(int)a;
                    
                    blendOp.blend(interpolatedData, 0, targetData, targetIdx);
                }
            }
            
            return target;
        }
        
        assert null == interpolationMethod || Interpolation.NEAREST_NEIGHBOR == interpolationMethod;
        // scale using nearest-neighbor interpolation
        // TODO: move common initializations out of if/else block
        // TODO: optimize
        
        int sourceOffset = source.getOffset();
        int targetOffset = target.getOffset();
        final byte[] sourceData = source.getData();
        final byte[] targetData = target.getData();
        final int sourceStride = source.getStride();
        final int targetStride = target.getStride();
        
        float xRatio = sourceWidth/(float)targetWidth;
        float yRatio = sourceHeight/(float)targetHeight;
        
        for (int i = 0; i < targetHeight; ++i) {
            final float py = (float)Math.floor(i*yRatio);
            for (int j = 0; j < targetWidth; ++j) {
                final float px = (float)Math.floor(j*xRatio);
                
                final int sourceIdx = (sourceOffset + (int)(py*sourceStride + px)) << 2;
                final int targetIdx = (targetOffset + i*targetStride + j) << 2;
                
                blendOp.blend(sourceData, sourceIdx, targetData, targetIdx);
            }
        }
        
        return target;
    }
}
