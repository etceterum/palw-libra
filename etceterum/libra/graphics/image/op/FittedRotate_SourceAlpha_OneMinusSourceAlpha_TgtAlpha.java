package etceterum.libra.graphics.image.op;


import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;
import etceterum.libra.math.d2.DoubleVector2;
import etceterum.libra.math.d2.Rotations2;

// places the source rotated by the specified angle into the target so the rotated source fits the target
// for performance considerations, currently only supporting SourceAlpha_OneMinusSourceAlpha_TgtAlpha blend mode and
// bilinear interpolation method

// deprecated - use AlphaBlendRotatedAndOpaque instead
@Deprecated
public final class FittedRotate_SourceAlpha_OneMinusSourceAlpha_TgtAlpha implements ImageOp {
    public static final boolean DEBUG = true;

    private double angle = 0;
    private Image source = null;
    
    public FittedRotate_SourceAlpha_OneMinusSourceAlpha_TgtAlpha() {
        // no-op
    }
    
    public FittedRotate_SourceAlpha_OneMinusSourceAlpha_TgtAlpha(double angle) {
        setAngle(angle);
    }
    
    public FittedRotate_SourceAlpha_OneMinusSourceAlpha_TgtAlpha(Image source) {
        setSource(source);
    }
    
    public FittedRotate_SourceAlpha_OneMinusSourceAlpha_TgtAlpha(Image source, double angle) {
        setSource(source).setAngle(angle);
    }
    
    public FittedRotate_SourceAlpha_OneMinusSourceAlpha_TgtAlpha resetSource() {
        return setSource(null);
    }
    
    public FittedRotate_SourceAlpha_OneMinusSourceAlpha_TgtAlpha setSource(Image value) {
        source = value;
        return this;
    }
    
    public Image getSource() {
        return source;
    }
    
    public FittedRotate_SourceAlpha_OneMinusSourceAlpha_TgtAlpha resetAngle() {
        return setAngle(0);
    }
    
    public FittedRotate_SourceAlpha_OneMinusSourceAlpha_TgtAlpha setAngle(double value) {
        angle = value;
        return this;
    }
    
    public double getAngle() {
        return angle;
    }
    
    @Override
    public Image execute(Image input) {
        final Image target = input;
        if (null == source || null == target) {
            return target;
        }
        final int sourceWidth = source.getWidth(), sourceHeight = source.getHeight();
        if (0 == sourceWidth || 0 == sourceHeight) {
            return target;
        }
        final int targetWidth = target.getWidth(), targetHeight = target.getHeight();
        if (0 == targetWidth || 0 == targetHeight) {
            return target;
        }
        
        final float cos = (float)Math.cos(angle), sin = (float)Math.sin(angle);
        
        final DoubleVector2 rotatedSourceDims = Rotations2.getRotatedRectangleDimensions(cos, sin, sourceWidth, sourceHeight);
        
        final float rotatedSourceWidth = (float)Math.ceil(rotatedSourceDims.getX());
        final float rotatedSourceHeight = (float)Math.ceil(rotatedSourceDims.getY());
        
        final float scaleX = rotatedSourceWidth/targetWidth;
        final float scaleY = rotatedSourceHeight/targetHeight;
        final float scale = Math.max(scaleX, scaleY);
        
        final float sourceCenterX = 0.5f*sourceWidth, sourceCenterY = 0.5f*sourceHeight;
        final int sourceOffset = source.getOffset(), sourceStride = source.getStride();
        final byte[] sourceData = source.getData();
        
        final float targetCenterX = 0.5f*targetWidth, targetCenterY = 0.5f*targetHeight;
        final int targetOffset = target.getOffset(), targetStride = target.getStride();
        final byte[] targetData = target.getData();
        
        int targetIdx = targetOffset << 2;
        final int targetSkipInBytes = (targetStride - targetWidth) << 2;

        float sourceX, sourceY, sourceWeightX, sourceWeightY;
        int sourceBaseX, sourceBaseY, sourceBaseXPlus1, sourceBaseYPlus1;
        int lowerLeftIdx, lowerRightIdx, upperLeftIdx, upperRightIdx;
        int rLowerLeft, gLowerLeft, bLowerLeft, aLowerLeft;
        int rLowerRight, gLowerRight, bLowerRight, aLowerRight;
        int rUpperLeft, gUpperLeft, bUpperLeft, aUpperLeft;
        int rUpperRight, gUpperRight, bUpperRight, aUpperRight;
        float rLowerAverage, rUpperAverage;
        float gLowerAverage, gUpperAverage;
        float bLowerAverage, bUpperAverage;
        float aLowerAverage, aUpperAverage;
        int sourceR, sourceG, sourceB, sourceA, targetR, targetG, targetB, targetA, edgeR, edgeG, edgeB, edgeA, oneMinusSourceA;
        int r, g, b;
        float a;
        float targetDeltaX, targetDeltaY;
        
        for (int targetY = 0; targetY < targetHeight; ++targetY, targetIdx += targetSkipInBytes) {
            targetDeltaY = targetY - targetCenterY;
            for (int targetX = 0; targetX < targetWidth; ++targetX, targetIdx += 4) {
                targetDeltaX = targetX - targetCenterX;
                sourceX = scale*(targetDeltaX*cos - targetDeltaY*sin) + sourceCenterX;
                sourceY = scale*(targetDeltaY*cos + targetDeltaX*sin) + sourceCenterY;
                
                sourceBaseX = (int)Math.floor(sourceX);
                //sourceBaseX = (int)(sourceX >= 0 ? sourceX : sourceX - 1); 
                sourceBaseXPlus1 = sourceBaseX + 1;
                sourceWeightX = sourceX - sourceBaseX;

                sourceBaseY = (int)Math.floor(sourceY);
                //sourceBaseY = (int)(sourceY >= 0 ? sourceY : sourceY - 1);
                sourceBaseYPlus1 = sourceBaseY + 1;
                sourceWeightY = sourceY - sourceBaseY;

                targetR = (int)targetData[0 + targetIdx] & 0xFF;
                targetG = (int)targetData[1 + targetIdx] & 0xFF;
                targetB = (int)targetData[2 + targetIdx] & 0xFF;
                targetA = (int)targetData[3 + targetIdx] & 0xFF;

                if (DEBUG) {
                    edgeR = (byte)0xFF; edgeG = (byte)0xFF; edgeB = (byte)0xFF; edgeA = 0;
                }
                else {
                    edgeR = targetR;
                    edgeG = targetG;
                    edgeB = targetB;
                    edgeA = targetA;
                }
                //boolean hasLowerLeft = true, hasLowerRight = true, hasUpperLeft = true, hasUpperRight = true;
                
                // lower left
                if (sourceBaseX >= 0 && sourceBaseY >= 0 && sourceBaseX < sourceWidth && sourceBaseY < sourceHeight) {
                    lowerLeftIdx = (sourceOffset + sourceBaseY*sourceStride + sourceBaseX) << 2;
                    rLowerLeft = (int)sourceData[0 + lowerLeftIdx] & 0xFF;
                    gLowerLeft = (int)sourceData[1 + lowerLeftIdx] & 0xFF;
                    bLowerLeft = (int)sourceData[2 + lowerLeftIdx] & 0xFF;
                    aLowerLeft = (int)sourceData[3 + lowerLeftIdx] & 0xFF;
                }
                else {
                    rLowerLeft = edgeR;
                    gLowerLeft = edgeG;
                    bLowerLeft = edgeB;
                    aLowerLeft = edgeA;
                    //hasLowerLeft = false;
                }
                
                // lower right
                if (sourceBaseXPlus1 >= 0 && sourceBaseY >= 0 && sourceBaseXPlus1 < sourceWidth && sourceBaseY < sourceHeight) {
                    lowerRightIdx = (sourceOffset + sourceBaseY*sourceStride + sourceBaseXPlus1) << 2;
                    rLowerRight = (int)sourceData[0 + lowerRightIdx] & 0xFF;
                    gLowerRight = (int)sourceData[1 + lowerRightIdx] & 0xFF;
                    bLowerRight = (int)sourceData[2 + lowerRightIdx] & 0xFF;
                    aLowerRight = (int)sourceData[3 + lowerRightIdx] & 0xFF;
                }
                else {
                    rLowerRight = edgeR;
                    gLowerRight = edgeG;
                    bLowerRight = edgeB;
                    aLowerRight = edgeA;
                    //hasLowerRight = false;
                }
                
                // upper left
                if (sourceBaseX >= 0 && sourceBaseYPlus1 >= 0 && sourceBaseX < sourceWidth && sourceBaseYPlus1 < sourceHeight) {
                    upperLeftIdx = (sourceOffset + sourceBaseYPlus1*sourceStride + sourceBaseX) << 2;
                    rUpperLeft = (int)sourceData[0 + upperLeftIdx] & 0xFF;
                    gUpperLeft = (int)sourceData[1 + upperLeftIdx] & 0xFF;
                    bUpperLeft = (int)sourceData[2 + upperLeftIdx] & 0xFF;
                    aUpperLeft = (int)sourceData[3 + upperLeftIdx] & 0xFF;
                }
                else {
                    rUpperLeft = edgeR;
                    gUpperLeft = edgeG;
                    bUpperLeft = edgeB;
                    aUpperLeft = edgeA;
                    //hasUpperLeft = false;
                }
                
                // upper right
                if (sourceBaseXPlus1 >= 0 && sourceBaseYPlus1 >= 0 && sourceBaseXPlus1 < sourceWidth && sourceBaseYPlus1 < sourceHeight) {
                    upperRightIdx = (sourceOffset + sourceBaseYPlus1*sourceStride + sourceBaseXPlus1) << 2;
                    rUpperRight = (int)sourceData[0 + upperRightIdx] & 0xFF;
                    gUpperRight = (int)sourceData[1 + upperRightIdx] & 0xFF;
                    bUpperRight = (int)sourceData[2 + upperRightIdx] & 0xFF;
                    aUpperRight = (int)sourceData[3 + upperRightIdx] & 0xFF;
                }
                else {
                    rUpperRight = edgeR;
                    gUpperRight = edgeG;
                    bUpperRight = edgeB;
                    aUpperRight = edgeA;
                    //hasUpperRight = false;
                }
                
                
                /*
                if (hasLowerLeft) {
                    if (!hasLowerRight) {
                        rLowerRight = rLowerLeft; gLowerRight = gLowerLeft; bLowerRight = gLowerLeft; aLowerRight = aLowerLeft;
                        hasLowerRight = true;
                    }
                    if (!hasUpperLeft) {
                        rUpperLeft = rLowerLeft; gUpperLeft = gLowerLeft; bUpperLeft = gLowerLeft; aUpperLeft = aLowerLeft;
                        hasUpperLeft = true;
                    }
                }
                if (hasLowerRight) {
                    if (!hasLowerLeft) {
                        rLowerLeft = rLowerRight; gLowerLeft = gLowerRight; bLowerLeft = gLowerRight; aLowerLeft = aLowerRight;
                        hasLowerLeft = true;
                    }
                    if (!hasUpperRight) {
                        rUpperRight = rLowerRight; gUpperRight = gLowerRight; bUpperRight = gLowerRight; aUpperRight = aLowerRight;
                        hasUpperRight = true;
                    }
                }
                if (hasUpperLeft) {
                    if (!hasUpperRight) {
                        rUpperRight = rUpperLeft; gUpperRight = gUpperLeft; bUpperRight = gUpperLeft; aUpperRight = aUpperLeft;
                        hasUpperRight = true;
                    }
                    if (!hasLowerLeft) {
                        rLowerLeft = rUpperLeft; gLowerLeft = gUpperLeft; bLowerLeft = gUpperLeft; aLowerLeft = aUpperLeft;
                        hasLowerLeft = true;
                    }
                }
                if (hasUpperRight) {
                    if (!hasUpperLeft) {
                        rUpperLeft = rUpperRight; gUpperLeft = gUpperRight; bUpperLeft = gUpperRight; aUpperLeft = aUpperRight;
                        hasUpperLeft = true;
                    }
                    if (!hasLowerRight) {
                        rLowerRight = rUpperRight; gLowerRight = gUpperRight; bLowerRight = gUpperRight; aLowerRight = aUpperRight;
                        hasLowerRight = true;
                    }
                }
                */
                
                // alpha
                aLowerAverage = lerp(aLowerLeft, aLowerRight, sourceWeightX);
                aUpperAverage = lerp(aUpperLeft, aUpperRight, sourceWeightY);
                sourceA = (int)lerp(aLowerAverage, aUpperAverage, sourceWeightY);
                oneMinusSourceA = 0xFF - sourceA;
                if (DEBUG) {
                    a = ((sourceA*sourceA + targetA*oneMinusSourceA) >> 8)/255f;
                    targetData[3 + targetIdx] = (byte)0xFF;
                }
                
                // red
                rLowerAverage = lerp(rLowerLeft, rLowerRight, sourceWeightX);
                rUpperAverage = lerp(rUpperLeft, rUpperRight, sourceWeightX);
                sourceR = (int)lerp(rLowerAverage, rUpperAverage, sourceWeightY);
                r = (sourceR*sourceA + targetR*oneMinusSourceA) >> 8;
                if (DEBUG) {
                    r = Math.round(r*a);
                }
                targetData[0 + targetIdx] = (byte)r;
                
                // green
                gLowerAverage = lerp(gLowerLeft, gLowerRight, sourceWeightX);
                gUpperAverage = lerp(gUpperLeft, gUpperRight, sourceWeightX);
                sourceG = (int)lerp(gLowerAverage, gUpperAverage, sourceWeightY);
                g = (sourceG*sourceA + targetG*oneMinusSourceA) >> 8;
                if (DEBUG) {
                    g = Math.round(g*a);
                }
                targetData[1 + targetIdx] = (byte)g;
                
                // blue
                bLowerAverage = lerp(bLowerLeft, bLowerRight, sourceWeightX);
                bUpperAverage = lerp(bUpperLeft, bUpperRight, sourceWeightX);
                sourceB = (int)lerp(bLowerAverage, bUpperAverage, sourceWeightY);
                b = (sourceB*sourceA + targetB*oneMinusSourceA) >> 8;
                if (DEBUG) {
                    b = Math.round(b*a);
                }
                targetData[2 + targetIdx] = (byte)b;
            }
        }
        
        return target;
    }
    
    private static float lerp(float a, float b, float w) {
        return a + w*(b - a);
    }

}
