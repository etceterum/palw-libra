package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageFactory;
import etceterum.libra.graphics.image.ImageOp;

public final class FlipX implements ImageOp {
    public static final ImageFactory DEFAULT_IMAGE_FACTORY = Image.DEFAULT_FACTORY;
    private ImageFactory imageFactory;
    
    public FlipX resetImageFactory() {
        return setImageFactory(null);
    }
    
    public FlipX setImageFactory(ImageFactory imageFactory) {
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
        final ImageFactory targetFactory = null == imageFactory ? DEFAULT_IMAGE_FACTORY : imageFactory;
        final Image target = targetFactory.createImage(width, height);
        if (null == target) {
            return input;
        }
        
        final byte[] sourceData = source.getData();
        final int sourceOffset = source.getOffset(), sourceStride = source.getStride();
        final int sourceSkip = (sourceStride - width) << 2;
        int sourceIdx = sourceOffset << 2;
        
        final byte[] targetData = target.getData();
        final int targetOffset = target.getOffset(), targetStride = target.getStride();
        final int targetBaseSkip = targetStride << 2;
        int targetBaseIdx = (targetOffset + width - 1) << 2;
        
        for (int y = 0; y < height; ++y, sourceIdx += sourceSkip, targetBaseIdx += targetBaseSkip) {
            int targetIdx = targetBaseIdx;
            for (int x = 0; x < width; ++x, sourceIdx += 4, targetIdx -= 4) {
                targetData[0 + targetIdx] = sourceData[0 + sourceIdx];
                targetData[1 + targetIdx] = sourceData[1 + sourceIdx];
                targetData[2 + targetIdx] = sourceData[2 + sourceIdx];
                targetData[3 + targetIdx] = sourceData[3 + sourceIdx];
            }
        }
        return target;
    }

}
