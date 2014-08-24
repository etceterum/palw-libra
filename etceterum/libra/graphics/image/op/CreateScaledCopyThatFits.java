package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageFactory;
import etceterum.libra.graphics.image.ImageOp;

public final class CreateScaledCopyThatFits implements ImageOp {
    private int width, height;
    private ImageFactory imageFactory;
    
    public CreateScaledCopyThatFits(int width, int height, ImageFactory imageFactory) {
        set(width, height, imageFactory);
    }
    
    public CreateScaledCopyThatFits(int width, int height) {
        set(width, height, null);
    }
    
    public CreateScaledCopyThatFits(ImageFactory imageFactory) {
        set(0, 0, imageFactory);
    }
    
    public CreateScaledCopyThatFits() {
        set(0, 0, null);
    }
    
    public CreateScaledCopyThatFits set(int width, int height, ImageFactory imageFactory) {
        return setWidth(width).setHeight(height).setImageFactory(imageFactory);
    }
    
    public CreateScaledCopyThatFits setWidth(int value) {
        assert value >= 0;
        width = value;
        return this;
    }
    
    public int getWidth() {
        return width;
    }
    
    public CreateScaledCopyThatFits setHeight(int value) {
        assert value >= 0;
        height = value;
        return this;
    }
    
    public int getHeight() {
        return height;
    }
    
    public CreateScaledCopyThatFits setImageFactory(ImageFactory value) {
        this.imageFactory = value;
        return this;
    }
    
    public ImageFactory getImageFactory() {
        return imageFactory;
    }

    @Override
    public Image execute(Image source) {
        if (null == source) {
            return source;
        }
        final int sourceWidth = source.getWidth(), sourceHeight = source.getHeight();
        if (0 == sourceWidth || 0 == sourceHeight) {
            return source;
        }
        if (width <= 0 || height <= 0) {
            return null;
        }
        
        float scale = Math.min(width/(float)sourceWidth, height/(float)sourceHeight);
        final int scaledWidth = (int)(sourceWidth*scale), scaledHeight = (int)(sourceHeight*scale);
        final ImageFactory factory = null == imageFactory ? Image.DEFAULT_FACTORY : imageFactory;
        final Image target = factory.createImage(scaledWidth, scaledHeight);
        if (null == target) {
            return source;
        }
        return new Paste(source).execute(target);
    }
}
