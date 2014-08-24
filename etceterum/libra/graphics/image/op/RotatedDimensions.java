package etceterum.libra.graphics.image.op;


import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageFactory;
import etceterum.libra.graphics.image.ImageOp;
import etceterum.libra.math.d2.ImmutableDoubleVector2;
import etceterum.libra.math.d2.Rotations2;

// creates a new image with the dimensions of the original image rotated by the given angle
public final class RotatedDimensions implements ImageOp {
    public static final ImageFactory DEFAULT_IMAGE_FACTORY = Image.DEFAULT_FACTORY;
    
    private ImageFactory imageFactory = null;
    private double angle;
    
    public RotatedDimensions() {
        resetAngle();
    }
    
    public RotatedDimensions(double angle) {
        setAngle(angle);
    }
    
    public RotatedDimensions resetImageFactory() {
        return setImageFactory(null);
    }
    
    public RotatedDimensions setImageFactory(ImageFactory imageFactory) {
        this.imageFactory = imageFactory;
        return this;
    }
    
    public ImageFactory getImageFactory() {
        return imageFactory;
    }
    
    public RotatedDimensions resetAngle() {
        return setAngle(0);
    }
    
    public RotatedDimensions setAngle(double value) {
        angle = value;
        return this;
    }
    
    public double getAngle() {
        return angle;
    }

    @Override
    public Image execute(Image input) {
        if (null == input) {
            return null;
        }
        
        final ImmutableDoubleVector2 dimensions = Rotations2.getRotatedRectangleDimensions(angle, input.getWidth(), input.getHeight());
        final ImageFactory outputFactory = null == imageFactory ? DEFAULT_IMAGE_FACTORY : imageFactory;
        return outputFactory.createImage((int)Math.ceil(dimensions.getX()), (int)Math.ceil(dimensions.getY()));
    }

}
