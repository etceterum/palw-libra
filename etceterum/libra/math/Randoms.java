package etceterum.libra.math;

import java.util.Random;

public final class Randoms {
    private static final Random random = new Random();

    private Randoms() {
        // don't
    }

    public static int randomInt(int upTo) {
        return (int)(randomDouble()*upTo);
    }

    public static int randomInt(int from, int upTo) {
        return from + randomInt(upTo - from);
    }

    public static int randomInt() {
        return randomInt(Integer.MAX_VALUE);
    }

    public static double randomDouble() {
        return Math.random();
    }

    public static double randomDouble(double upTo) {
        return randomDouble()*upTo;
    }

    public static double randomDouble(double from, double upTo) {
        return from + randomDouble(upTo - from);
    }
    
    public static float randomFloat() {
        return (float)Math.random();
    }

    // normally distributed value with mean of 0.0 and std deviation of 1.0
    public static double randomGaussian() {
        return random.nextGaussian();
    }
}
