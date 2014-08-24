package etceterum.libra.graphics.image.op;


import etceterum.libra.graphics.RGBA;
import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageFactory;
import etceterum.libra.graphics.image.ImageOp;
import etceterum.libra.math.d2.DoubleVector2;
import etceterum.libra.math.d2.Rotations2;

// TODO: separate classes:
// - Rotate - source is rotated without scaling and mapped onto target
// - ScaledRotate - source is rotated with given scale and mapped onto target
// - FittedRotate - rotated source fits into target (implemented via ScaledRotate)
// - Rotated (what this class does the ad-hoc way) - a new target image is created with 
//   dimensions of the rotated input (using RotatedDimensions) - and then source is 
//   rotated into target using Rotate.

// do not use this class if possible. although working, it is not optimized;
@Deprecated
public final class Rotated implements ImageOp {
    public static final ImageFactory DEFAULT_IMAGE_FACTORY = Image.DEFAULT_FACTORY;
    
    public static final byte DEFAULT_BACKGROUND_R = 0;
    public static final byte DEFAULT_BACKGROUND_G = 0;
    public static final byte DEFAULT_BACKGROUND_B = 0;
    public static final byte DEFAULT_BACKGROUND_A = 0;

    private ImageFactory imageFactory = null;
    private double angle = 0;
    private Interpolation interpolation = null;
    private byte bgR = DEFAULT_BACKGROUND_R, bgG = DEFAULT_BACKGROUND_G, bgB = DEFAULT_BACKGROUND_B, bgA = DEFAULT_BACKGROUND_A;
    
    public Rotated() {
        // no-op
    }
    
    public Rotated(double angle) {
        setAngle(angle);
    }
    
    public Rotated(double angle, Interpolation interpolation) {
        setAngle(angle).setInterpolationMethod(interpolation);
    }
    
    public Rotated resetImageFactory() {
        return setImageFactory(null);
    }
    
    public Rotated setImageFactory(ImageFactory imageFactory) {
        this.imageFactory = imageFactory;
        return this;
    }
    
    public ImageFactory getImageFactory() {
        return imageFactory;
    }
    
    public Rotated resetAngle() {
        return setAngle(0);
    }
    
    public Rotated setAngle(double value) {
        angle = value;
        return this;
    }
    
    public double getAngle() {
        return angle;
    }
    
    public Rotated resetInterpolationMethod() {
        return setInterpolationMethod(null);
    }
    
    public Rotated setInterpolationMethod(Interpolation value) {
        interpolation = value;
        return this;
    }
    
    public Interpolation getInterpolationMethod() {
        return interpolation;
    }

    public Rotated resetBackground() {
        return setBackground(DEFAULT_BACKGROUND_R, DEFAULT_BACKGROUND_G, DEFAULT_BACKGROUND_B, DEFAULT_BACKGROUND_A);
    }
    
    public Rotated setBackground(int rgba) {
        return setBackground((byte)(rgba >> 24), (byte)(rgba >> 16), (byte)(rgba >> 8), (byte)rgba);
    }
    
    public Rotated setBackground(byte r, byte g, byte b, byte a) {
        bgR = r;
        bgG = g;
        bgB = b;
        bgA = a;
        return this;
    }
    
    public int getBackground() {
        return RGBA.toInt(bgR, bgG, bgB, bgA);
    }
    
    public byte getBackgroundR() {
        return bgR;
    }

    public byte getBackgroundG() {
        return bgG;
    }

    public byte getBackgroundB() {
        return bgB;
    }

    public byte getBackgroundA() {
        return bgA;
    }
    
    @Override
    public Image execute(Image input) {
        final Image source = input;
        if (null == source) {
            return source;
        }
        final int sourceWidth = source.getWidth(), sourceHeight = source.getHeight();
        if (0 == sourceWidth || 0 == sourceHeight) {
            return source;
        }
        
        final float cos = (float)Math.cos(angle), sin = (float)Math.sin(angle);
        
        final DoubleVector2 targetDims = Rotations2.getRotatedRectangleDimensions(cos, sin, sourceWidth, sourceHeight);
        final int targetWidth = (int)Math.ceil(targetDims.getX());
        final int targetHeight = (int)Math.ceil(targetDims.getY());
        //System.out.println("source: " + sourceWidth + "x" + sourceHeight + ", target: " + targetWidth + "x" + targetHeight);
        
        final float sourceCenterX = 0.5f*sourceWidth, sourceCenterY = 0.5f*sourceHeight;
        final int sourceOffset = source.getOffset(), sourceStride = source.getStride();
        final byte[] sourceData = source.getData();
        
        final float targetCenterX = 0.5f*targetWidth, targetCenterY = 0.5f*targetHeight;
        final ImageFactory targetFactory = null == imageFactory ? DEFAULT_IMAGE_FACTORY : imageFactory;
        final Image target = targetFactory.createImage(targetWidth, targetHeight);
        if (null == target) {
            return source;
        }
        final int targetOffset = target.getOffset(), targetStride = target.getStride();
        final byte[] targetData = target.getData();
        
        final int br = (int)bgR & 0xFF, bg = (int)bgG & 0xFF, bb = (int)bgB & 0xFF, ba = (int)bgA & 0xFF;
        
        if (Interpolation.BILINEAR == interpolation) {
            final int targetSkipInBytes = (targetStride - targetWidth) << 2;
            int targetIdx = targetOffset << 2;
            
            int rLowerLeft, gLowerLeft, bLowerLeft, aLowerLeft;
            int rLowerRight, gLowerRight, bLowerRight, aLowerRight;
            int rUpperLeft, gUpperLeft, bUpperLeft, aUpperLeft;
            int rUpperRight, gUpperRight, bUpperRight, aUpperRight;
            
            for (int targetY = 0; targetY < targetHeight; ++targetY, targetIdx += targetSkipInBytes) {
                for (int targetX = 0; targetX < targetWidth; ++targetX, targetIdx += 4) {
                    final float targetDeltaX = targetX - targetCenterX, targetDeltaY = targetY - targetCenterY;
                    final float sourceX = targetDeltaX*cos - targetDeltaY*sin + sourceCenterX;
                    final float sourceY = targetDeltaY*cos + targetDeltaX*sin + sourceCenterY;
                    //final int targetIdx = (targetOffset + targetY*targetStride + targetX)*bpp;
                    
                    final int sourceBaseX = (int)Math.floor(sourceX), sourceBaseY = (int)Math.floor(sourceY);
                    final float sourceWeightX = sourceX - sourceBaseX, sourceWeightY = sourceY - sourceBaseY;
                    final int sourceBaseXPlus1 = sourceBaseX + 1, sourceBaseYPlus1 = sourceBaseY + 1;
                    
                    // lower left
                    if (sourceBaseX >= 0 && sourceBaseY >= 0 && sourceBaseX < sourceWidth && sourceBaseY < sourceHeight) {
                        final int lowerLeftIdx = (sourceOffset + sourceBaseY*sourceStride + sourceBaseX) << 2;
                        rLowerLeft = (int)sourceData[0 + lowerLeftIdx] & 0xFF;
                        gLowerLeft = (int)sourceData[1 + lowerLeftIdx] & 0xFF;
                        bLowerLeft = (int)sourceData[2 + lowerLeftIdx] & 0xFF;
                        aLowerLeft = (int)sourceData[3 + lowerLeftIdx] & 0xFF;
                    }
                    else {
                        rLowerLeft = br;
                        gLowerLeft = bg;
                        bLowerLeft = bb;
                        aLowerLeft = ba;
                    }
                    
                    // lower right
                    if (sourceBaseXPlus1 >= 0 && sourceBaseY >= 0 && sourceBaseXPlus1 < sourceWidth && sourceBaseY < sourceHeight) {
                        final int lowerRightIdx = (sourceOffset + sourceBaseY*sourceStride + sourceBaseXPlus1) << 2;
                        rLowerRight = (int)sourceData[0 + lowerRightIdx] & 0xFF;
                        gLowerRight = (int)sourceData[1 + lowerRightIdx] & 0xFF;
                        bLowerRight = (int)sourceData[2 + lowerRightIdx] & 0xFF;
                        aLowerRight = (int)sourceData[3 + lowerRightIdx] & 0xFF;
                    }
                    else {
                        rLowerRight = br;
                        gLowerRight = bg;
                        bLowerRight = bb;
                        aLowerRight = ba;
                    }
                    
                    // upper left
                    if (sourceBaseX >= 0 && sourceBaseYPlus1 >= 0 && sourceBaseX < sourceWidth && sourceBaseYPlus1 < sourceHeight) {
                        final int upperLeftIdx = (sourceOffset + sourceBaseYPlus1*sourceStride + sourceBaseX) << 2;
                        rUpperLeft = (int)sourceData[0 + upperLeftIdx] & 0xFF;
                        gUpperLeft = (int)sourceData[1 + upperLeftIdx] & 0xFF;
                        bUpperLeft = (int)sourceData[2 + upperLeftIdx] & 0xFF;
                        aUpperLeft = (int)sourceData[3 + upperLeftIdx] & 0xFF;
                    }
                    else {
                        rUpperLeft = br;
                        gUpperLeft = bg;
                        bUpperLeft = bb;
                        aUpperLeft = ba;
                    }
                    
                    // upper right
                    if (sourceBaseXPlus1 >= 0 && sourceBaseYPlus1 >= 0 && sourceBaseXPlus1 < sourceWidth && sourceBaseYPlus1 < sourceHeight) {
                        final int upperRightIdx = (sourceOffset + sourceBaseYPlus1*sourceStride + sourceBaseXPlus1) << 2;
                        rUpperRight = (int)sourceData[0 + upperRightIdx] & 0xFF;
                        gUpperRight = (int)sourceData[1 + upperRightIdx] & 0xFF;
                        bUpperRight = (int)sourceData[2 + upperRightIdx] & 0xFF;
                        aUpperRight = (int)sourceData[3 + upperRightIdx] & 0xFF;
                    }
                    else {
                        rUpperRight = br;
                        gUpperRight = bg;
                        bUpperRight = bb;
                        aUpperRight = ba;
                    }
                    
                    // red
                    final float rLowerAverage = rLowerLeft + sourceWeightX*(rLowerRight - rLowerLeft);
                    final float rUpperAverage = rUpperLeft + sourceWeightX*(rUpperRight - rUpperLeft);
                    final float r = rLowerAverage + sourceWeightY*(rUpperAverage - rLowerAverage);
                    targetData[0 + targetIdx] = (byte)(int)r;
                    
                    // green
                    final float gLowerAverage = gLowerLeft + sourceWeightX*(gLowerRight - gLowerLeft);
                    final float gUpperAverage = gUpperLeft + sourceWeightX*(gUpperRight - gUpperLeft);
                    final float g = gLowerAverage + sourceWeightY*(gUpperAverage - gLowerAverage);
                    targetData[1 + targetIdx] = (byte)(int)g;
                    
                    // blue
                    final float bLowerAverage = bLowerLeft + sourceWeightX*(bLowerRight - bLowerLeft);
                    final float bUpperAverage = bUpperLeft + sourceWeightX*(bUpperRight - bUpperLeft);
                    final float b = bLowerAverage + sourceWeightY*(bUpperAverage - bLowerAverage);
                    targetData[2 + targetIdx] = (byte)(int)b;
                    
                    // alpha
                    final float aLowerAverage = aLowerLeft + sourceWeightX*(aLowerRight - aLowerLeft);
                    final float aUpperAverage = aUpperLeft + sourceWeightX*(aUpperRight - aUpperLeft);
                    final float a = aLowerAverage + sourceWeightY*(aUpperAverage - aLowerAverage);
                    targetData[3 + targetIdx] = (byte)(int)a;
                }
            }
            
            return target;
        }
        
        // default: nearest neighbor
        
        for (int targetY = 0; targetY < targetHeight; ++targetY) {
            for (int targetX = 0; targetX < targetWidth; ++targetX) {
                final float targetDeltaX = targetX - targetCenterX, targetDeltaY = targetY - targetCenterY;
                final int sourceX = (int)Math.round((targetDeltaX*cos - targetDeltaY*sin) + sourceCenterX);
                final int sourceY = (int)Math.round((targetDeltaY*cos + targetDeltaX*sin) + sourceCenterY);
                final int targetIdx = (targetOffset + targetY*targetStride + targetX) << 2;
                if (sourceX < 0 || sourceX >= sourceWidth || sourceY < 0 || sourceY >= sourceHeight) {
                    targetData[0 + targetIdx] = bgR;
                    targetData[1 + targetIdx] = bgG;
                    targetData[2 + targetIdx] = bgB;
                    targetData[3 + targetIdx] = bgA;
                    continue;
                }
                final int sourceIdx = (sourceOffset + sourceY*sourceStride + sourceX) << 2;
                targetData[0 + targetIdx] = sourceData[0 + sourceIdx];
                targetData[1 + targetIdx] = sourceData[1 + sourceIdx];
                targetData[2 + targetIdx] = sourceData[2 + sourceIdx];
                targetData[3 + targetIdx] = sourceData[3 + sourceIdx];
            }
        }
        
        return target;
    }
}
