package etceterum.libra.math.d3;

import etceterum.libra.math.Const;

public final class DoubleVector3 implements MutableDoubleVector3 {
    public static final int     SIZE    = 3;
    public static final int     INDEX_X = 0;
    public static final int     INDEX_Y = 1;
    public static final int     INDEX_Z = 2;
    
    public static final ImmutableDoubleVector3 ZERO       = new DoubleVector3(0, 0, 0);
    public static final ImmutableDoubleVector3 UNIT_X     = new DoubleVector3(1, 0, 0);
    public static final ImmutableDoubleVector3 UNIT_Y     = new DoubleVector3(0, 1, 0);
    public static final ImmutableDoubleVector3 UNIT_Z     = new DoubleVector3(0, 0, 1);
    
    private double[] values = new double[SIZE];
    
    public DoubleVector3() {
        // no-op
    }
    
    public DoubleVector3(double x, double y, double z) {
        assign(x, y, z);
    }
    
    public DoubleVector3(ImmutableDoubleVector3 other) {
        assign(other);
    }
    
    public double getX() {
        return values[INDEX_X];
    }
    
    public void setX(double value) {
        values[INDEX_X] = value;
    }
    
    public double getY() {
        return values[INDEX_Y];
    }
    
    public void setY(double value) {
        values[INDEX_Y] = value;
    }
    
    public double getZ() {
        return values[INDEX_Z];
    }
    
    public void setZ(double value) {
        values[INDEX_Z] = value;
    }
    
    public double get(int index) {
        return values[index];
    }
    
    public void set(int index, double value) {
        values[index] = value;
    }
    
    public void reset() {
        assign(0, 0, 0);
    }
    
    public int getSize() {
        return SIZE;
    }
    
    public void assign(double x, double y, double z) {
        this.values[INDEX_X] = x;
        this.values[INDEX_Y] = y;
        this.values[INDEX_Z] = z;
    }
    
    public void assign(ImmutableDoubleVector3 other) {
        assert null != other;
        assign(other.getX(), other.getY(), other.getZ());
    }
    
    public void add(ImmutableDoubleVector3 other) {
        assert null != other;
        values[INDEX_X] += other.getX();
        values[INDEX_Y] += other.getY();
        values[INDEX_Z] += other.getZ();
    }
    
    public DoubleVector3 plus(ImmutableDoubleVector3 other) {
        assert null != other;
        final DoubleVector3 result = new DoubleVector3(this);
        result.add(other);
        return result;
    }
    
    public void multiply(double value) {
        values[INDEX_X] *= value;
        values[INDEX_Y] *= value;
        values[INDEX_Z] *= value;
    }
    
    public DoubleVector3 normalize() {
        final double sqMag = getSquaredMagnitude();
        
        if (sqMag < Const.SQUARED_EPSILON) {
            assign(0, 0, 0);
            return this;
        }
        
        final double mag = Math.sqrt(sqMag);
        assign(getX()/mag, getY()/mag, getZ()/mag);
        return this;
    }
    
    public double getSquaredMagnitude() {
        return DoubleVectorUtil3.getSquaredMagnitude(values[INDEX_X], values[INDEX_Y], values[INDEX_Z]);
    }
}
