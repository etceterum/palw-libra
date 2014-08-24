package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageFactory;
import etceterum.libra.graphics.image.ImageOp;

/**
 * Create a scaled version of the image using the nearest neighbor interpolation algorithm.
 * Original image will remain unmodified.
 * If the original image is already of the scaled size, no new image is created and the 
 * original image is simply returned. 
 */
@Deprecated
public final class Scale implements ImageOp {
    public static final ImageFactory DEFAULT_IMAGE_FACTORY = Image.DEFAULT_FACTORY;
    
    private ImageFactory imageFactory;
    private int width, height;
    
    public Scale() {
        this(0, 0);
    }
    
    public Scale(int width, int height) {
        set(width, height);
    }
    
    public Scale resetImageFactory() {
        return setImageFactory(null);
    }
    
    public Scale setImageFactory(ImageFactory imageFactory) {
        this.imageFactory = imageFactory;
        return this;
    }
    
    public ImageFactory getImageFactory() {
        return imageFactory;
    }
    
    public Scale set(int width, int height) {
        return setWidth(width).setHeight(height);
    }
    
    public Scale setWidth(int value) {
        assert value >= 0;
        this.width = value;
        return this;
    }
    
    public int getWidth() {
        return width;
    }

    public Scale setHeight(int value) {
        assert value >= 0;
        this.width = value;
        return this;
    }
    
    int getHeight() {
        return height;
    }

    @Override
    public Image execute(Image input) {
        final Image source = input;
        final int sourceWidth = source.getWidth(), sourceHeight = source.getHeight();
        final int targetWidth = width, targetHeight = height;
        
        if (sourceWidth == targetWidth && sourceHeight == targetHeight) {
            return source;
        }
        
        final ImageFactory outputFactory = null == imageFactory ? DEFAULT_IMAGE_FACTORY : imageFactory;
        return new Paste(input).execute(outputFactory.createImage(targetWidth, targetHeight));
    }

}
