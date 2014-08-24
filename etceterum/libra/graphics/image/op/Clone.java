package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageFactory;
import etceterum.libra.graphics.image.ImageOp;

public class Clone implements ImageOp {
    public static final ImageFactory DEFAULT_IMAGE_FACTORY = Image.DEFAULT_FACTORY;
    private ImageFactory imageFactory;

    public Clone resetImageFactory() {
        return setImageFactory(null);
    }
    
    public Clone setImageFactory(ImageFactory imageFactory) {
        this.imageFactory = imageFactory;
        return this;
    }
    
    public ImageFactory getImageFactory() {
        return imageFactory;
    }

    @Override
    public Image execute(Image input) {
        return input.getClone(null == imageFactory ? DEFAULT_IMAGE_FACTORY : imageFactory);
    }

}
