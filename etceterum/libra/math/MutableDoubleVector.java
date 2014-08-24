package etceterum.libra.math;

public interface MutableDoubleVector extends ImmutableDoubleVector {
    void set(int index, double value);
    void reset();
}
