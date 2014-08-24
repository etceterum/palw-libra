package etceterum.libra.math.easing;

import etceterum.libra.math.Easing;

// Based on Robert Penner's easing functions, see copyright notice and license inside Easing.java
public final class Linear implements Easing {
    private static final Linear INSTANCE = new Linear();

    private Linear() {
        // to prevent instantiation
    }

    public static Linear getInstance() {
        return INSTANCE;
    }

    @Override
    public double ease(double t, double b, double c, double d) {
        return c * t / d + b;
    }

}
