package etceterum.libra.math.d3;

import etceterum.libra.math.MutableDoubleVector;

public interface MutableDoubleVector3 extends MutableDoubleVector, ImmutableDoubleVector3 {
    void setX(double value);
    void setY(double value);
    void setZ(double value);
}
