package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;

// like AlphaBlendAndOpaque, but rotate the source by the given angle before blending
public final class AlphaBlendRotatedAndOpaque extends SourceOp {
    
    private double angle = 0;
    
    public AlphaBlendRotatedAndOpaque() {
        // no-op
    }
    
    public AlphaBlendRotatedAndOpaque(double angle) {
        setAngle(angle);
    }
    
    public AlphaBlendRotatedAndOpaque(Image source) {
        super(source);
    }
    
    public AlphaBlendRotatedAndOpaque(Image source, double angle) {
        super(source);
        setAngle(angle);
    }
    
    public AlphaBlendRotatedAndOpaque resetAngle() {
        return setAngle(0);
    }
    
    public AlphaBlendRotatedAndOpaque setAngle(double value) {
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
        
        final float cos = (float)Math.cos(angle), absCos = Math.abs(cos); 
        final float sin = (float)Math.sin(angle), absSin = Math.abs(sin);
        
        // http://objectmix.com/graphics/133490-bounding-box-rotated-rectangle.html
        final float rotatedSourceWidth = (float)Math.ceil(sourceWidth*absCos + sourceHeight*absSin);
        final float rotatedSourceHeight = (float)Math.ceil(sourceWidth*absSin + sourceHeight*absCos);
        
        final float scaleX = rotatedSourceWidth/targetWidth;
        final float scaleY = rotatedSourceHeight/targetHeight;
        final float scale = Math.max(scaleX, scaleY);
        
        final float sourceCenterX = 0.5f*sourceWidth, sourceCenterY = 0.5f*sourceHeight;
        final int sourceOffset = source.getOffset(), sourceStride = source.getStride(), sourceStrideInBytes = sourceStride << 2;
        final byte[] sourceData = source.getData();
        
        final float targetCenterX = 0.5f*targetWidth, targetCenterY = 0.5f*targetHeight;
        final int targetOffset = target.getOffset(), targetStride = target.getStride();
        final byte[] targetData = target.getData();
        
        int targetIdx = targetOffset << 2;
        final int targetSkipInBytes = (targetStride - targetWidth) << 2;

        for (int targetY = 0; targetY < targetHeight; ++targetY, targetIdx += targetSkipInBytes) {
            final float targetDeltaY = targetY - targetCenterY;
            for (int targetX = 0; targetX < targetWidth; ++targetX, targetIdx += 4) {
                
                final int targetR = 0xFF & targetData[0 + targetIdx];
                final int targetG = 0xFF & targetData[1 + targetIdx];
                final int targetB = 0xFF & targetData[2 + targetIdx];
                
                final float targetDeltaX = targetX - targetCenterX;
                
                final float sourceY = scale*(targetDeltaY*cos + targetDeltaX*sin) + sourceCenterY;
                final int sourceBaseY = (int)(sourceY >= 0 ? sourceY : sourceY - 1);
                final float sourceWeightY = sourceY - sourceBaseY;
                final int northSourceY = sourceBaseY, southSourceY = northSourceY + 1;
                final boolean haveNorth = northSourceY >= 0 && northSourceY < sourceHeight;
                final boolean haveSouth = southSourceY >= 0 && southSourceY < sourceHeight;
                final boolean haveNorthAndSouth = haveNorth && haveSouth;
                final int northSourceOffsetY = (sourceOffset + northSourceY*sourceStride) << 2, southSourceOffsetY = northSourceOffsetY + sourceStrideInBytes;
                
                final float sourceX = scale*(targetDeltaX*cos - targetDeltaY*sin) + sourceCenterX;
                final int sourceBaseX = (int)(sourceX >= 0 ? sourceX : sourceX - 1); 
                final float sourceWeightX = sourceX - sourceBaseX;
                final int westSourceX = sourceBaseX, westSourceOffsetX = westSourceX << 2; 
                final int eastSourceX = westSourceX + 1, eastSourceOffsetX = westSourceOffsetX + 4;
                final boolean haveWest = westSourceX >= 0 && westSourceX < sourceWidth;
                final boolean haveEast = eastSourceX >= 0 && eastSourceX < sourceWidth;
                final boolean haveWestAndEast = haveWest && haveEast;
                
                int scaledTargetR = 0, scaledTargetG = 0, scaledTargetB = 0;
                if (!haveNorthAndSouth || !haveWestAndEast) {
                    scaledTargetR = targetR << 8;
                    scaledTargetG = targetG << 8;
                    scaledTargetB = targetB << 8;
                }
                
                // north-west
                int nwR, nwG, nwB;
                if (haveNorth && haveWest) {
                    final int sourceIdx = northSourceOffsetY + westSourceOffsetX;
                    final int sourceR = 0xFF & sourceData[0 + sourceIdx];
                    final int sourceG = 0xFF & sourceData[1 + sourceIdx];
                    final int sourceB = 0xFF & sourceData[2 + sourceIdx];
                    final int sourceA = 0xFF & sourceData[3 + sourceIdx];
                    final int oneMinusSourceA = 0xFF - sourceA;
                    nwR = sourceR*sourceA + targetR*oneMinusSourceA;
                    nwG = sourceG*sourceA + targetG*oneMinusSourceA;
                    nwB = sourceB*sourceA + targetB*oneMinusSourceA;
                }
                else {
                    nwR = scaledTargetR;
                    nwG = scaledTargetG;
                    nwB = scaledTargetB;
                }
                
                // north-east
                int neR, neG, neB;
                if (haveNorth && haveEast) {
                    final int sourceIdx = northSourceOffsetY + eastSourceOffsetX;
                    final int sourceR = 0xFF & sourceData[0 + sourceIdx];
                    final int sourceG = 0xFF & sourceData[1 + sourceIdx];
                    final int sourceB = 0xFF & sourceData[2 + sourceIdx];
                    final int sourceA = 0xFF & sourceData[3 + sourceIdx];
                    final int oneMinusSourceA = 0xFF - sourceA;
                    neR = sourceR*sourceA + targetR*oneMinusSourceA;
                    neG = sourceG*sourceA + targetG*oneMinusSourceA;
                    neB = sourceB*sourceA + targetB*oneMinusSourceA;
                }
                else {
                    neR = scaledTargetR;
                    neG = scaledTargetG;
                    neB = scaledTargetB;
                }
                
                // south-west
                int swR, swG, swB;
                if (haveSouth && haveWest) {
                    final int sourceIdx = southSourceOffsetY + westSourceOffsetX;
                    final int sourceR = 0xFF & sourceData[0 + sourceIdx];
                    final int sourceG = 0xFF & sourceData[1 + sourceIdx];
                    final int sourceB = 0xFF & sourceData[2 + sourceIdx];
                    final int sourceA = 0xFF & sourceData[3 + sourceIdx];
                    final int oneMinusSourceA = 0xFF - sourceA;
                    swR = sourceR*sourceA + targetR*oneMinusSourceA;
                    swG = sourceG*sourceA + targetG*oneMinusSourceA;
                    swB = sourceB*sourceA + targetB*oneMinusSourceA;
                }
                else {
                    swR = scaledTargetR;
                    swG = scaledTargetG;
                    swB = scaledTargetB;
                }
                
                // south-east
                int seR, seG, seB;
                if (haveSouth && haveEast) {
                    int sourceIdx = (sourceOffset + southSourceY*sourceStride + eastSourceX) << 2;
                    final int sourceR = 0xFF & sourceData[0 + sourceIdx];
                    final int sourceG = 0xFF & sourceData[1 + sourceIdx];
                    final int sourceB = 0xFF & sourceData[2 + sourceIdx];
                    final int sourceA = 0xFF & sourceData[3 + sourceIdx];
                    final int oneMinusSourceA = 0xFF - sourceA;
                    seR = sourceR*sourceA + targetR*oneMinusSourceA;
                    seG = sourceG*sourceA + targetG*oneMinusSourceA;
                    seB = sourceB*sourceA + targetB*oneMinusSourceA;
                }
                else {
                    seR = scaledTargetR;
                    seG = scaledTargetG;
                    seB = scaledTargetB;
                }
                
                // averages
                final int nR = (int)(nwR + sourceWeightX*(neR - nwR));
                final int sR = (int)(swR + sourceWeightX*(seR - swR));
                final int r = (int)(nR + sourceWeightY*(sR - nR));
                targetData[0 + targetIdx] = (byte)(r >> 8);
                
                final int nG = (int)(nwG + sourceWeightX*(neG - nwG));
                final int sG = (int)(swG + sourceWeightX*(seG - swG));
                final int g = (int)(nG + sourceWeightY*(sG - nG));
                targetData[1 + targetIdx] = (byte)(g >> 8);
                
                final int nB = (int)(nwB + sourceWeightX*(neB - nwB));
                final int sB = (int)(swB + sourceWeightX*(seB - swB));
                final int b = (int)(nB + sourceWeightY*(sB - nB));
                targetData[2 + targetIdx] = (byte)(b >> 8);
                
                targetData[3 + targetIdx] = (byte)0xFF;
            }
        }
        
        return target;
    }
}
