package etceterum.libra.graphics.npatch;


import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;
import etceterum.libra.graphics.image.op.ColorOp;
import etceterum.libra.graphics.image.op.Fill;
import etceterum.libra.graphics.image.op.Identity;
import etceterum.libra.graphics.image.op.Paste;
import etceterum.libra.graphics.image.op.SourceOp;

public class ApplyNPatch implements NPatchOp {
    private NPatch source = null;
    private SourceOp pasteOp = null;
    private ColorOp fillOp = null;
    private ImageOp transparentFillOp = null;
    
    public ApplyNPatch() {
        // no-op
    }
    
    public ApplyNPatch(NPatch source) {
        this(source, null, null, null);
    }
    
    public ApplyNPatch(NPatch source, SourceOp paste, ColorOp fill) {
        this(source, paste, fill, null);
    }
    
    public ApplyNPatch(NPatch source, SourceOp paste, ColorOp fill, ImageOp transparentFill) {
        set(source, paste, fill, transparentFill);
    }
    
    public ApplyNPatch reset() {
        return set(null, null, null, null);
    }
    
    public ApplyNPatch set(NPatch source, SourceOp paste, ColorOp fill, ImageOp transparentFill) {
        return setSource(source).setPasteOp(paste).setFillOp(fill).setTransparentFillOp(transparentFill);
    }
    
    public ApplyNPatch resetSource() {
        return setSource(null);
    }
    
    public ApplyNPatch setSource(NPatch value) {
        source = value;
        return this;
    }
    
    public NPatch getSource() {
        return source;
    }
    
    public ApplyNPatch resetPasteOp() {
        return setPasteOp(null);
    }
    
    public ApplyNPatch setPasteOp(SourceOp value) {
        pasteOp = value;
        return this;
    }
    
    public SourceOp getPasteOp() {
        return pasteOp;
    }
    
    public ApplyNPatch resetFillOp() {
        return setFillOp(null);
    }
    
    public ApplyNPatch setFillOp(ColorOp value) {
        fillOp = value;
        return this;
    }
    
    public ColorOp getFillOp() {
        return fillOp;
    }
    
    public ApplyNPatch resetTransparentFillOp() {
        return setTransparentFillOp(null);
    }
    
    public ApplyNPatch setTransparentFillOp(ImageOp value) {
        transparentFillOp = value;
        return this;
    }
    
    public ImageOp getTransparentFillOp() {
        return transparentFillOp;
    }
    
    @Override
    public Image execute(Image input) {
        final Image target = input;
        
        if (null == source || null == target) {
            return target;
        }
        
        final SourceOp paste = null == pasteOp ? new Paste() : pasteOp;
        final ColorOp fill = null == fillOp ? new Fill() : fillOp;
        final ImageOp transparentFill = null == transparentFillOp ? Identity.getInstance() : transparentFillOp;
        
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

                final Image targetSlice = target.getSlice(targetSpanX, targetSpanY, targetSpanWidth, targetSpanHeight);
                final int color = colors[colorIdx];
                switch (color) {
                case NPatchInfo.TRANSPARENT_COLOR:
                    transparentFill.execute(targetSlice);
                    break;
                    
                case NPatchInfo.NO_COLOR:
                    // the source is not single-color: draw normally
                    paste.setSource(sourceImage.getSlice(sourceSpanX, sourceSpanY, sourceSpanWidth, sourceSpanHeight)).execute(targetSlice);
                    break;
                    
                default:
                    // source is single-color: fill target
                    // (note: 9-patch color is ARGB, we convert it to RGBA for our purposes)
                    final byte a = (byte)(color >> 24);
                    final byte r = (byte)(color >> 16);
                    final byte g = (byte)(color >> 8);
                    final byte b = (byte)color;
                    
                    fill.setColor(r, g, b, a).execute(targetSlice);
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
