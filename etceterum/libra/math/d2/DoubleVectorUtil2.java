package etceterum.libra.math.d2;

public class DoubleVectorUtil2 {
    private DoubleVectorUtil2() {
        // prevent instantiation
    }
    
    public static double getSquaredMagnitude(double x, double y) {
        return x*x + y*y;
    }
    
    public static double getDotProduct(double x1, double y1, double x2, double y2) {
        return x1*x2 + y1*y2;
    }
    
    public static double getDotProduct(ImmutableDoubleVector2 v1, ImmutableDoubleVector2 v2) {
        return getDotProduct(v1.getX(), v1.getY(), v2.getX(), v2.getY());
    }
    
    public static double getDotProduct(ImmutableDoubleVector2 v1, double x2, double y2) {
        return getDotProduct(v1.getX(), v1.getY(), x2, y2);
    }
    
    // euclidean distance: http://en.wikipedia.org/wiki/Euclidean_distance 
    public static double distance(double x1, double y1, double x2, double y2) {
        final double dx = x1 - x2, dy = y1 - y2;
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    public static double distance(ImmutableDoubleVector2 v1, double x2, double y2) {
        return distance(v1.getX(), v1.getY(), x2, y2);
    }
    
    public static void calculatePerpendicular(ImmutableDoubleVector2 vector, DoubleVector2 p) {
        final double x = vector.getX();
        if (0 == x) {
            p.assign(1, 0);
        }
        else {
            p.assign(vector.getY(), -x);
            p.normalize();
        }
    }
}
