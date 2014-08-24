package etceterum.libra;

public final class Bits {
    private Bits() {
        // don't
    }
    
    // (Solution found on stackoverflow. http://stackoverflow.com/a/365068)
    public static int getSmallestPowerOfTwoGreaterThanOrEqualTo(int x) {
        --x;
        
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        
        return x + 1;
    }
}
