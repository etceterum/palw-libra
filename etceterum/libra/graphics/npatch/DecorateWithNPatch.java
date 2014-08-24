package etceterum.libra.graphics.npatch;


import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageFactory;
import etceterum.libra.graphics.image.op.AlphaBlend;
import etceterum.libra.graphics.image.op.AlphaFill;
import etceterum.libra.graphics.image.op.Paste;

public final class DecorateWithNPatch implements NPatchOp {
    public static final ImageFactory DEFAULT_IMAGE_FACTORY = Image.DEFAULT_FACTORY;
    
    public static final class Factory implements NPatchOpFactory {
        private final ImageFactory imageFactory; 
        private final NPatch patch;
        private final NPatchOp op;
        
        public Factory(ImageFactory imageFactory, NPatch patch, NPatchOp op) {
            this.imageFactory = imageFactory;
            this.patch = patch;
            this.op = op;
        }
        
        public Factory(ImageFactory imageFactory, NPatch patch) {
            this(imageFactory, patch, null);
        }
        
        public Factory(NPatch patch, NPatchOp op) {
            this(null, patch, op);
        }
        
        public Factory(NPatch patch) {
            this(null, patch, null);
        }
        
        public Factory() {
            this(null, null, null);
        }

        @Override
        public DecorateWithNPatch createOp() {
            return new DecorateWithNPatch(imageFactory, patch, op);
        }
    }
    
    private ImageFactory imageFactory;
    private NPatch patch;
    private NPatchOp op;
    
    public DecorateWithNPatch(ImageFactory imageFactory, NPatch patch, NPatchOp op) {
        setImageFactory(imageFactory).setPatch(patch).setOp(op);
    }
    
    public DecorateWithNPatch(NPatch patch, NPatchOp op) {
        this(null, patch, op);
    }
    
    public DecorateWithNPatch(NPatch patch) {
        this(null, patch, null);
    }
    
    public DecorateWithNPatch() {
        this(null, null, null);
    }
    
    public DecorateWithNPatch resetImageFactory() {
        return setImageFactory(null);
    }
    
    public DecorateWithNPatch setImageFactory(ImageFactory imageFactory) {
        this.imageFactory = imageFactory;
        return this;
    }
    
    public ImageFactory getImageFactory() {
        return imageFactory;
    }
    
    public DecorateWithNPatch resetPatch() {
        return setPatch(patch);
    }
    
    public DecorateWithNPatch setPatch(NPatch patch) {
        this.patch = patch;
        return this;
    }
    
    public NPatch getPatch() {
        return patch;
    }
    
    public DecorateWithNPatch resetOp() {
        return setOp(null);
    }
    
    public DecorateWithNPatch setOp(NPatchOp op) {
        this.op = op;
        return this;
    }
    
    public NPatchOp getOp() {
        return op;
    }
    
    @Override
    public Image execute(Image input) {
        if (null == patch || null == input) {
            return input;
        }
        
        final NPatchOp action = null == op ? new ApplyNPatch(patch, new AlphaBlend(), new AlphaFill()) : op;
        
        final int inputWidth = input.getWidth(), inputHeight = input.getHeight();
        final int paddingX = patch.getHorizontalPadding(), paddingY = patch.getVerticalPadding();
        Image target = input;
        if (0 != paddingX || 0 != paddingY) {
            final ImageFactory targetFactory = null == imageFactory ? DEFAULT_IMAGE_FACTORY : imageFactory;
            target = targetFactory.createImage(inputWidth + paddingX, inputHeight + paddingY);
            if (null == target) {
                return input;
            }
        }
        final Image content = target.getSlice(patch.getPaddingLeft(), patch.getPaddingTop(), inputWidth, inputHeight);
        new Paste(input).execute(content);
        action.execute(target);
        return target;
    }
}
