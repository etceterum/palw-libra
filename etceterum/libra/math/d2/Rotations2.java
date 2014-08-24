package etceterum.libra.math.d2;

import etceterum.libra.math.Const;

public final class Rotations2 {
    private Rotations2() {
        // do not instantiate me
    }
    
    public static DoubleVector2 getRotatedRectangleDimensions(double cos, double sin, double width, double height) {
        final double absCos = Math.abs(cos);
        final double absSin = Math.abs(sin);
        
        // http://objectmix.com/graphics/133490-bounding-box-rotated-rectangle.html
        return new DoubleVector2(width*absCos + height*absSin, width*absSin + height*absCos);
    }
    
    public static DoubleVector2 getRotatedRectangleDimensions(double angle, double width, double height) {
        assert width >= 0 && height >= 0;
        
        if (Math.abs(angle) < Const.EPSILON) {
            return new DoubleVector2(width, height);
        }
        
        return getRotatedRectangleDimensions(Math.cos(angle), Math.sin(angle), width, height);
    }
}
