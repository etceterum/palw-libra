package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;

// (Rtgt, Gtgt, Btgt, Atgt) = (Rsrc, Gsrc, Bsrc, Asrc)*Asrc + (Rtgt, Gtgt, Btgt, Atgt)*(1 - Asrc)
public final class AlphaBlend extends SourceOp {
    
    public AlphaBlend() {
        // no-op
    }
    
    public AlphaBlend(Image source) {
        super(source);
    }
    
    //////////
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
            int sourceR, sourceG, sourceB, sourceA, targetR, targetG, targetB, targetA, oneMinusSourceA, r, g, b, a;
            // special case: no scaling
            
            int sourceIdx = source.getOffset() << 2;
            int targetIdx = target.getOffset() << 2;
            final byte[] sourceData = source.getData();
            final byte[] targetData = target.getData();
            final int sourceStrideInBytes = source.getStride() << 2;
            final int targetStrideInBytes = target.getStride() << 2;
            
            for (int y = 0; y < sourceHeight; ++y, sourceIdx += sourceStrideInBytes, targetIdx += targetStrideInBytes) {
                int sourcePixelIdx = sourceIdx, targetPixelIdx = targetIdx;
                for (int x = 0; x < sourceWidth; ++x, sourcePixelIdx += 4, targetPixelIdx += 4) {

                    sourceA = (int)sourceData[3 + sourcePixelIdx] & 0xFF;
                    oneMinusSourceA = 0xFF - sourceA;
                    targetA = (int)targetData[3 + targetPixelIdx] & 0xFF;
                    a = (sourceA*sourceA + targetA*oneMinusSourceA) >> 8;
                    targetData[3 + targetPixelIdx] = (byte)a;
                    
                    sourceR = (int)sourceData[0 + sourcePixelIdx] & 0xFF;
                    targetR = (int)targetData[0 + targetPixelIdx] & 0xFF;
                    r = (sourceR*sourceA + targetR*oneMinusSourceA) >> 8;
                    targetData[0 + targetPixelIdx] = (byte)r;
                    
                    sourceG = (int)sourceData[1 + sourcePixelIdx] & 0xFF;
                    targetG = (int)targetData[1 + targetPixelIdx] & 0xFF;
                    g = (sourceG*sourceA + targetG*oneMinusSourceA) >> 8;
                    targetData[1 + targetPixelIdx] = (byte)g;
                    
                    sourceB = (int)sourceData[2 + sourcePixelIdx] & 0xFF;
                    targetB = (int)targetData[2 + targetPixelIdx] & 0xFF;
                    b = (sourceB*sourceA + targetB*oneMinusSourceA) >> 8;
                    targetData[2 + targetPixelIdx] = (byte)b;
                }
            }
            
            return target;
        }
        
        final byte[] sourceData = source.getData();
        final int sourceOffset = source.getOffset(), sourceStride = source.getStride(), sourceStrideInBytes = sourceStride << 2;
        final int sourceWidthMinus1 = sourceWidth - 1, sourceHeightMinus1 = sourceHeight - 1;
        
        final byte[] targetData = target.getData();
        final int targetOffset = target.getOffset(), targetStride = target.getStride();
        final int targetSkip = (targetStride - targetWidth) << 2;
        
        final float scaleX = sourceWidth/(float)targetWidth;
        final float scaleY = sourceHeight/(float)targetHeight;
        
        int targetIdx = targetOffset << 2;
        for (int targetY = 0; targetY < targetHeight; ++targetY, targetIdx += targetSkip) {
            float sourceY = targetY*scaleY;
            int sourceBaseY = (int)sourceY;
            final int sourceBaseOffsetY = (sourceOffset + sourceBaseY*sourceStride) << 2;
            float sourceWeightY = sourceY - sourceBaseY;
            for (int targetX = 0; targetX < targetWidth; ++targetX, targetIdx += 4) {
                
                final int targetR = 0xFF & targetData[0 + targetIdx];
                final int targetG = 0xFF & targetData[1 + targetIdx];
                final int targetB = 0xFF & targetData[2 + targetIdx];
                final int targetA = 0xFF & targetData[3 + targetIdx];
                
                final float sourceX = targetX*scaleX;
                final int sourceBaseX = (int)sourceX;
                final int sourceBaseOffsetX = sourceBaseX << 2; 
                final float sourceWeightX = sourceX - sourceBaseX;
                
                // north-west
                final int nwSourceIdx = sourceBaseOffsetY + sourceBaseOffsetX;
                final int nwSourceR = 0xFF & sourceData[0 + nwSourceIdx];
                final int nwSourceG = 0xFF & sourceData[1 + nwSourceIdx];
                final int nwSourceB = 0xFF & sourceData[2 + nwSourceIdx];
                final int nwSourceA = 0xFF & sourceData[3 + nwSourceIdx];
                final int nwOneMinusSourceA = 0xFF - nwSourceA;
                final int nwR = nwSourceR*nwSourceA + targetR*nwOneMinusSourceA;
                final int nwG = nwSourceG*nwSourceA + targetG*nwOneMinusSourceA;
                final int nwB = nwSourceB*nwSourceA + targetB*nwOneMinusSourceA;
                final int nwA = nwSourceA*nwSourceA + targetA*nwOneMinusSourceA;
                
                // north-east
                final int neSourceIdx = sourceBaseX >= sourceWidthMinus1 ? nwSourceIdx : nwSourceIdx + 4;
                final int neSourceR = 0xFF & sourceData[0 + neSourceIdx];
                final int neSourceG = 0xFF & sourceData[1 + neSourceIdx];
                final int neSourceB = 0xFF & sourceData[2 + neSourceIdx];
                final int neSourceA = 0xFF & sourceData[3 + neSourceIdx];
                final int neOneMinusSourceA = 0xFF - neSourceA;
                final int neR = neSourceR*neSourceA + targetR*neOneMinusSourceA;
                final int neG = neSourceG*neSourceA + targetG*neOneMinusSourceA;
                final int neB = neSourceB*neSourceA + targetB*neOneMinusSourceA;
                final int neA = neSourceA*neSourceA + targetA*neOneMinusSourceA;
                
                // south-west
                final int swSourceIdx = sourceBaseY >= sourceHeightMinus1 ? nwSourceIdx : nwSourceIdx + sourceStrideInBytes;
                final int swSourceR = 0xFF & sourceData[0 + swSourceIdx];
                final int swSourceG = 0xFF & sourceData[1 + swSourceIdx];
                final int swSourceB = 0xFF & sourceData[2 + swSourceIdx];
                final int swSourceA = 0xFF & sourceData[3 + swSourceIdx];
                final int swOneMinusSourceA = 0xFF - swSourceA;
                final int swR = swSourceR*swSourceA + targetR*swOneMinusSourceA;
                final int swG = swSourceG*swSourceA + targetG*swOneMinusSourceA;
                final int swB = swSourceB*swSourceA + targetB*swOneMinusSourceA;
                final int swA = swSourceA*swSourceA + targetA*swOneMinusSourceA;
                
                // south-east
                final int seSourceIdx = sourceBaseX >= sourceWidthMinus1 ? swSourceIdx : swSourceIdx + 4;
                final int seSourceR = 0xFF & sourceData[0 + seSourceIdx];
                final int seSourceG = 0xFF & sourceData[1 + seSourceIdx];
                final int seSourceB = 0xFF & sourceData[2 + seSourceIdx];
                final int seSourceA = 0xFF & sourceData[3 + seSourceIdx];
                final int seOneMinusSourceA = 0xFF - seSourceA;
                final int seR = seSourceR*seSourceA + targetR*seOneMinusSourceA;
                final int seG = seSourceG*seSourceA + targetG*seOneMinusSourceA;
                final int seB = seSourceB*seSourceA + targetB*seOneMinusSourceA;
                final int seA = seSourceA*seSourceA + targetA*seOneMinusSourceA;
                
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
                
                final int nA = (int)(nwA + sourceWeightX*(neA - nwA));
                final int sA = (int)(swA + sourceWeightX*(seA - swA));
                final int a = (int)(nA + sourceWeightY*(sA - nA));
                targetData[3 + targetIdx] = (byte)(a >> 8);
            }
        }
        
        return target;
    }
}
