package etceterum.libra.math.d3;

public final class DoubleVectorUtil3 {
    private DoubleVectorUtil3() {
        // prevent instantiation
    }
    
    public static double getSquaredMagnitude(double x, double y, double z) {
        return x*x + y*y + z*z;
    }
    
    public static double getDotProduct(double x1, double y1, double z1, double x2, double y2, double z2) {
        return x1*x2 + y1*y2 + z1*z2;
    }
    
    public static double getDotProduct(ImmutableDoubleVector3 v1, ImmutableDoubleVector3 v2) {
        return getDotProduct(v1.getX(), v1.getY(), v1.getZ(), v2.getX(), v2.getY(), v2.getZ());
    }
    
    public static double getDotProduct(ImmutableDoubleVector3 v1, double x2, double y2, double z2) {
        return getDotProduct(v1.getX(), v1.getY(), v1.getZ(), x2, y2, z2);
    }
    
    public static DoubleVector3 getCrossProduct(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new DoubleVector3(y1*z2 - z1*y2, z1*x2 - x1*z2, x1*y2 - y1*x2);
    }
    
    // euclidean distance: http://en.wikipedia.org/wiki/Euclidean_distance 
    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        final double dx = x1 - x2, dy = y1 - y2, dz = z1 - z2;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
    
    public static double distance(ImmutableDoubleVector3 v1, double x2, double y2, double z2) {
        return distance(v1.getX(), v1.getY(), v1.getZ(), x2, y2, z2);
    }
    
    public static void calculatePerpendicular(ImmutableDoubleVector3 vector, DoubleVector3 p) {
        final double x = vector.getX();
        if (0 == x) {
            p.assign(1, 0, 0);
        }
        else {
            p.assign(vector.getY(), -x, 0);
            p.normalize();
        }
    }
    
    public static void calculateCrossProduct(ImmutableDoubleVector3 a, ImmutableDoubleVector3 b, DoubleVector3 r) {
        r.setX(a.getY()*b.getZ() - a.getZ()*b.getY());
        r.setY(a.getZ()*b.getX() - a.getX()*b.getZ());
        r.setZ(a.getX()*b.getY() - a.getY()*b.getX());
    }
    
    public static void calculatePerpendiculars(ImmutableDoubleVector3 vector, DoubleVector3 p1, DoubleVector3 p2) {
        calculatePerpendicular(vector, p1);
        calculateCrossProduct(vector, p1, p2);
        p2.normalize();
    }
}
