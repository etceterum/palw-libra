package etceterum.libra.math.d2;

import etceterum.libra.math.Const;

public class DoubleVector2 implements MutableDoubleVector2 {
    public static final int     SIZE    = 2;
    public static final int     INDEX_X = 0;
    public static final int     INDEX_Y = 1;
    
    public static final ImmutableDoubleVector2 ZERO       = new DoubleVector2(0, 0);
    public static final ImmutableDoubleVector2 UNIT_X     = new DoubleVector2(1, 0);
    public static final ImmutableDoubleVector2 UNIT_Y     = new DoubleVector2(0, 1);
    
    private double[] values = new double[SIZE];
    
    public DoubleVector2() {
        // no-op
    }
    
    public DoubleVector2(double x, double y) {
        assign(x, y);
    }
    
    public DoubleVector2(ImmutableDoubleVector2 other) {
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
    
    public double get(int index) {
        return values[index];
    }
    
    public void set(int index, double value) {
        values[index] = value;
    }
    
    public void reset() {
        assign(0, 0);
    }
    
    public int getSize() {
        return SIZE;
    }
    
    public void assign(double x, double y) {
        this.values[INDEX_X] = x;
        this.values[INDEX_Y] = y;
    }
    
    public void assign(ImmutableDoubleVector2 other) {
        assert null != other;
        assign(other.getX(), other.getY());
    }
    
    public void add(ImmutableDoubleVector2 other) {
        assert null != other;
        values[INDEX_X] += other.getX();
        values[INDEX_Y] += other.getY();
    }
    
    public DoubleVector2 plus(ImmutableDoubleVector2 other) {
        assert null != other;
        final DoubleVector2 result = new DoubleVector2(this);
        result.add(other);
        return result;
    }
    
    public void multiply(double value) {
        values[INDEX_X] *= value;
        values[INDEX_Y] *= value;
    }
    
    public DoubleVector2 normalize() {
        final double sqMag = getSquaredMagnitude();
        
        if (sqMag < Const.SQUARED_EPSILON) {
            assign(0, 0);
            return this;
        }
        
        final double mag = Math.sqrt(sqMag);
        assign(getX()/mag, getY()/mag);
        return this;
    }
    
    public double getSquaredMagnitude() {
        return DoubleVectorUtil2.getSquaredMagnitude(values[INDEX_X], values[INDEX_Y]);
    }
}
