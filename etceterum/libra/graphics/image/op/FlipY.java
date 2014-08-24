package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageFactory;
import etceterum.libra.graphics.image.ImageOp;

public final class FlipY implements ImageOp {
    public static final ImageFactory DEFAULT_IMAGE_FACTORY = Image.DEFAULT_FACTORY;
    private ImageFactory imageFactory;
    
    public FlipY resetImageFactory() {
        return setImageFactory(null);
    }
    
    public FlipY setImageFactory(ImageFactory imageFactory) {
        this.imageFactory = imageFactory;
        return this;
    }
    
    public ImageFactory getImageFactory() {
        return imageFactory;
    }
    
    @Override
    public Image execute(Image input) {
        final Image source = input;
        final int width = source.getWidth(), height = source.getHeight();
        final byte[] sourceData = source.getData();
        final ImageFactory targetFactory = null == imageFactory ? DEFAULT_IMAGE_FACTORY : imageFactory;
        final Image target = targetFactory.createImage(width, height);
        if (null == target) {
            return input;
        }
        final byte[] targetData = target.getData();
        final int sourceStride = source.getStride() << 2;
        final int targetStride = -(width << 2);
        final int rowLength = width << 2;
        int sourceIdx = source.getOffset() << 2;
        int targetIdx = targetData.length + targetStride;
        
        for (int y = 0; y < height; ++y, sourceIdx += sourceStride, targetIdx += targetStride) {
            System.arraycopy(sourceData, sourceIdx, targetData, targetIdx, rowLength);
        }
        return target;
    }

}
