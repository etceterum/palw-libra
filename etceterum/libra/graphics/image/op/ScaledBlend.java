package etceterum.libra.graphics.image.op;


import etceterum.libra.graphics.blending.Blender;
import etceterum.libra.graphics.blending.SourceColor_Zero;
import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;

// made obsolete by new implementation of Blend that supports scaling 
@Deprecated
public class ScaledBlend implements ImageOp {
    public static final Blender DEFAULT_BLENDER = SourceColor_Zero.INSTANCE;
    private static final int bpp = Image.BYTES_PER_PIXEL;
    
    private Image source;
    private Blender blender;
    
    public ScaledBlend() {
        this(null, null);
    }
    
    public ScaledBlend(Image source) {
        this(source, null);
    }
    
    public ScaledBlend(Blender blender) {
        this(null, blender);
    }
    
    public ScaledBlend(Image source, Blender blender) {
        set(source, blender);
    }
    
    public ScaledBlend reset() {
        return set(null, null);
    }
    
    public ScaledBlend set(Image source, Blender blender) {
        return setSource(source).setBlender(blender);
    }
    
    public ScaledBlend resetSource() {
        return setSource(null);
    }
    
    public ScaledBlend setSource(Image value) {
        source = value;
        return this;
    }
    
    public Image getSource() {
        return source;
    }
    
    public ScaledBlend resetBlender() {
        return setBlender(null);
    }
    
    public ScaledBlend setBlender(Blender value) {
        this.blender = value;
        return this;
    }
    
    public Blender getBlender() {
        return blender;
    }

    @Override
    public Image execute(Image input) {
        final Image target = input;
        if (null == source) {
            return target;
        }
        
        final int sourceWidth = source.getWidth(), sourceHeight = source.getHeight();
        final int targetWidth = target.getWidth(), targetHeight = target.getHeight();
        
        if (0 == sourceWidth || 0 == sourceHeight || 0 == targetWidth || 0 == targetHeight) {
            // nothing to do
            return target;
        }
        
        if (sourceWidth == targetWidth && sourceHeight == targetHeight) {
            // special case: no scaling, delegate to regular blend
            return new Blend(source, blender).execute(target);
        }
        else {
            // scale using nearest-neighbor interpolation
            // TODO: have an option to use other interpolation algorithms: bilinear, bicubic...
            // TODO: optimize
            
            int sourceOffset = source.getOffset();
            int targetOffset = target.getOffset();
            final byte[] sourceData = source.getData();
            final byte[] targetData = target.getData();
            final int sourceStride = source.getStride();
            final int targetStride = target.getStride();
            final Blender b = null == blender ? DEFAULT_BLENDER : blender;
            
            double xRatio = sourceWidth/(double)targetWidth;
            double yRatio = sourceHeight/(double)targetHeight;
            
            for (int i = 0; i < targetHeight; ++i) {
                final double py = Math.floor(i*yRatio);
                for (int j = 0; j < targetWidth; ++j) {
                    final double px = Math.floor(j*xRatio);
                    
                    final int sourceIdx = (sourceOffset + (int)(py*sourceStride + px))*bpp;
                    final int targetIdx = (targetOffset + i*targetStride + j)*bpp;
                    
                    b.blend(sourceData, sourceIdx, targetData, targetIdx);
                }
            }
        }
        
        return target;
    }
}
