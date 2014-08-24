package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;

// fit the source image in the target so it is centered; shrink if needed - but do not expand
public final class CenterFit implements ImageOp {
    private SourceOp op;
    
    public CenterFit() {
        resetOp();
    }
    
    public CenterFit(SourceOp op) {
        setOp(op);
    }
    
    public CenterFit resetOp() {
        return setOp(null);
    }
    
    public CenterFit setOp(SourceOp op) {
        this.op = op;
        return this;
    }
    
    public SourceOp getOp() {
        return op;
    }

    @Override
    public Image execute(Image input) {
        final Image target = input;
        if (null == target || null == op) {
            return target;
        }
        final int targetWidth = target.getWidth(), targetHeight = target.getHeight();
        if (0 == targetWidth || 0 == targetHeight) {
            return target;
        }
        final Image source = op.getSource();
        if (null == source) {
            return target;
        }
        final int sourceWidth = source.getWidth(), sourceHeight = source.getHeight();
        if (0 == sourceWidth || 0 == sourceHeight) {
            return target;
        }
        
        int x = 0, y = 0, width = sourceWidth, height = sourceHeight;
        if (sourceWidth <= targetWidth && sourceHeight <= targetHeight) {
            // source fully fits in target
            x = (targetWidth - sourceWidth) >> 1;
            y = (targetHeight - sourceHeight) >> 1;
        }
        else {
            // need to scale the slice
            final double scaleX = targetWidth/(double)sourceWidth;
            final double scaleY = targetHeight/(double)sourceHeight;
            final double scale = Math.min(scaleX, scaleY);
            final double scaledWidth = sourceWidth*scale;
            final double scaledHeight = sourceHeight*scale;
            assert scaledWidth <= targetWidth && scaledHeight <= targetHeight;
            width = (int)scaledWidth;
            height = (int)scaledHeight;
            if (0 == width || 0 == height) {
                return target;
            }
            x = (targetWidth - width) >> 1;
            y = (targetHeight - height) >> 1;
        }
        
        return op.execute(target.getSlice(x, y, width, height));
    }

}
