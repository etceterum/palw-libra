package etceterum.libra.graphics.npatch;


import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;
import etceterum.libra.graphics.image.op.Fill;
import etceterum.libra.graphics.image.op.Interpolation;
import etceterum.libra.graphics.image.op.Paste;
import etceterum.libra.graphics.image.op.Slice;

// use ApplyNPatch instead
@Deprecated
public final class PasteNPatch implements ImageOp {
    private NPatch source = null;
    private Interpolation interpolationMethod = null;
    
    public PasteNPatch() {
        // no-op
    }
    
    public PasteNPatch(NPatch source) {
        setSource(source);
    }
    
    public PasteNPatch(NPatch source, Interpolation interpolationMethod) {
        set(source, interpolationMethod);
    }
    
    public PasteNPatch reset() {
        return set(null, null);
    }
    
    public PasteNPatch set(NPatch source, Interpolation interpolationMethod) {
        return setSource(source).setInterpolationMethod(interpolationMethod);
    }
    
    public PasteNPatch resetSource() {
        return setSource(null);
    }
    
    public PasteNPatch setSource(NPatch value) {
        source = value;
        return this;
    }
    
    public NPatch getSource() {
        return source;
    }
    
    public PasteNPatch resetInterpolationMethod() {
        return setInterpolationMethod(null);
    }
    
    public PasteNPatch setInterpolationMethod(Interpolation value) {
        interpolationMethod = value;
        return this;
    }
    
    public Interpolation getInterpolationMethod() {
        return interpolationMethod;
    }
    
    @Override
    public Image execute(Image input) {
        final Image target = input;
        
        if (null == source) {
            return target;
        }
        
        final int targetWidth = target.getWidth(), targetHeight = target.getHeight();
        final int sourceStretchableHeight = source.getStretchableHeight();
        final int sourceNonStretchableHeight = source.getNonStretchableHeight();
        final int targetStretchableHeight = targetHeight - sourceNonStretchableHeight;
        final NPatchInfo sourceInfo = source.getInfo();
        final int[] yDivs = sourceInfo.getYDivs();
        final int[] ySpans = source.getYSpans();
        final int numStretchableSpansY = yDivs.length - 1;
        
        final int sourceStretchableWidth = source.getStretchableWidth();
        final int sourceNonStretchableWidth = source.getNonStretchableWidth();
        final int targetStretchableWidth = targetWidth - sourceNonStretchableWidth;
        final int[] xDivs = sourceInfo.getXDivs();
        final int[] xSpans = source.getXSpans();
        final int numStretchableSpansX = xDivs.length >> 1;
        
        if (sourceNonStretchableWidth > targetWidth || sourceNonStretchableHeight > targetHeight) {
            // target too small
            return target;
        }
        
        final Image sourceImage = source.getImage();
        
        final int[] colors = sourceInfo.getColors();
        int colorIdx = 0;
        
        int numRemainingStretchableSpansY = numStretchableSpansY;
        int sourceRemainingStretchableHeight = sourceStretchableHeight;
        int targetRemainingStretchableHeight = targetStretchableHeight;
        int targetSpanY = 0;
        float yError = 0;
        
        final Fill fill = new Fill();
        final Paste paste = new Paste(interpolationMethod);
        final Slice slice = new Slice();
        
        for (int yIdx = 0; yIdx < ySpans.length - 1; ++yIdx) {
            final boolean yStretchable = 0 != (yIdx & 1);
            final int sourceSpanY = ySpans[yIdx];
            final int sourceSpanHeight = ySpans[yIdx + 1] - sourceSpanY;
            assert sourceSpanHeight >= 0;
            if (0 == sourceSpanHeight) {
                continue;
            }
            
            int targetSpanHeight = sourceSpanHeight;
            if (yStretchable) {
                assert numRemainingStretchableSpansY > 0;
                if (1 == numRemainingStretchableSpansY) {
                    targetSpanHeight = targetRemainingStretchableHeight;
                }
                else {
                    assert sourceRemainingStretchableHeight != 0;
                    final float yRatio = (targetRemainingStretchableHeight*sourceSpanHeight)/(float)sourceRemainingStretchableHeight;
                    targetSpanHeight = (int)yRatio;
                    yError += yRatio - targetSpanHeight;
                    if (yError >= 0.5f) {
                        ++targetSpanHeight;
                        yError -= 1;
                    }
                }
                
                targetRemainingStretchableHeight -= targetSpanHeight;
                sourceRemainingStretchableHeight -= sourceSpanHeight;
                --numRemainingStretchableSpansY;
            }
            
            int numRemainingStretchableSpansX = numStretchableSpansX;
            int sourceRemainingStretchableWidth = sourceStretchableWidth;
            int targetRemainingStretchableWidth = targetStretchableWidth;
            int targetSpanX = 0;
            float xError = 0;
            
            for (int xIdx = 0; xIdx < xSpans.length - 1; ++xIdx) {
                final boolean xStretchable = 0 != (xIdx & 1);
                final int sourceSpanX = xSpans[xIdx];
                final int sourceSpanWidth = xSpans[xIdx + 1] - sourceSpanX;
                assert sourceSpanWidth >= 0;
                if (0 == sourceSpanWidth) {
                    continue;
                }
                
                int targetSpanWidth = sourceSpanWidth;
                if (xStretchable) {
                    assert numRemainingStretchableSpansX > 0;
                    if (1 == numRemainingStretchableSpansX) {
                        targetSpanWidth = targetRemainingStretchableWidth;
                    }
                    else {
                        assert sourceRemainingStretchableWidth != 0;
                        final float xRatio = (targetRemainingStretchableWidth*sourceSpanWidth)/(float)sourceRemainingStretchableWidth;
                        targetSpanWidth = (int)xRatio;
                        xError += xRatio - targetSpanWidth;
                        if (xError >= 0.5f) {
                            ++targetSpanWidth;
                            xError -= 1;
                        }
                    }
                    
                    targetRemainingStretchableWidth -= targetSpanWidth;
                    sourceRemainingStretchableWidth -= sourceSpanWidth;
                    --numRemainingStretchableSpansX;
                }
                
                final int color = colors[colorIdx];
                switch (color) {
                case NPatchInfo.TRANSPARENT_COLOR:
                    // do not draw: the source is fully transparent
                    break;
                    
                case NPatchInfo.NO_COLOR:
                    // the source is not single-color: draw normally
                    
                    paste.setSource(
                        slice.set(sourceSpanX, sourceSpanY, sourceSpanWidth, sourceSpanHeight).execute(sourceImage)).execute(
                            slice.set(targetSpanX, targetSpanY, targetSpanWidth, targetSpanHeight).execute(target)
                        );
                    
                    //sourceImage.draw(sourceSpanX, sourceSpanY, sourceSpanWidth, sourceSpanHeight, target, targetSpanX, targetSpanY, targetSpanWidth, targetSpanHeight);
                    break;
                    
                default:
                    // source is single-color: fill target
                    // (note: 9-patch color is ARGB, we convert it to RGBA for our purposes)
                    final byte a = (byte)(color >> 24);
                    final byte r = (byte)(color >> 16);
                    final byte g = (byte)(color >> 8);
                    final byte b = (byte)color;
                    
                    fill.setColor(r, g, b, a).execute(slice.set(targetSpanX, targetSpanY, targetSpanWidth, targetSpanHeight).execute(target));
                    //target.fill(targetSpanX, targetSpanY, targetSpanWidth, targetSpanHeight, r, g, b, a);
                }
                
                targetSpanX += targetSpanWidth;
                ++colorIdx;
            }
            
            assert 0 == targetRemainingStretchableWidth;
            assert 0 == sourceRemainingStretchableWidth;
            assert 0 == numRemainingStretchableSpansX;
            
            targetSpanY += targetSpanHeight;
        }
        
        assert 0 == targetRemainingStretchableHeight;
        assert 0 == sourceRemainingStretchableHeight;
        assert 0 == numRemainingStretchableSpansY;
        
        return target;
    }

}
