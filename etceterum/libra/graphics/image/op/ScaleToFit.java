package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageFactory;
import etceterum.libra.graphics.image.ImageOp;

public final class ScaleToFit implements ImageOp {
    private ImageFactory imageFactory = Image.DEFAULT_FACTORY;
    private int width = 0, height = 0;
    
    public ScaleToFit() {
        // no-op
    }
    
    public ScaleToFit(int width, int height) {
        setDimensions(width, height);
    }
    
    public ScaleToFit setDimensions(int width, int height) {
        return setWidth(width).setHeight(height);
    }
    
    public ScaleToFit setWidth(int value) {
        width = value;
        return this;
    }
    
    public int getWidth() {
        return width;
    }
    
    public ScaleToFit setHeight(int value) {
        height = value;
        return this;
    }
    
    public int getHeight() {
        return height;
    }

    @Override
    public Image execute(Image input) {
        final Image source = input;
        if (null == source) {
            return source;
        }
        final int sourceWidth = source.getWidth(), sourceHeight = source.getHeight();
        if (0 == sourceWidth || 0 == sourceHeight || width <= 0 || height <= 0) {
            return null;
        }
        final double scale = Math.min(width/(double)sourceWidth, height/(double)sourceHeight);
        final int targetWidth = (int)(sourceWidth*scale), targetHeight = (int)(sourceHeight*scale);
        if (0 == targetWidth || 0 == targetHeight) {
            return null;
        }
        final Image target = imageFactory.createImage(targetWidth, targetHeight);
        if (null == target) {
            return null;
        }
        return new Paste(source, Interpolation.BILINEAR).execute(target);
    }
}
