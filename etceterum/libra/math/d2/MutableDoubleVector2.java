package etceterum.libra.math.d2;

import etceterum.libra.math.MutableDoubleVector;

public interface MutableDoubleVector2 extends MutableDoubleVector, ImmutableDoubleVector2 {
    void setX(double value);
    void setY(double value);
}
