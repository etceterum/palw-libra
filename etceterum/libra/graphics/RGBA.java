package etceterum.libra.graphics;

public final class RGBA {
    public static final int R_INDEX = 0;
    public static final int G_INDEX = 1;
    public static final int B_INDEX = 2;
    public static final int A_INDEX = 3;
    
    public static final int BYTES_PER_PIXEL = 4;
    
    private RGBA() {
        // prevent instantiation
    }
    
    public static int toInt(byte r, byte g, byte b, byte a) {
        return (((int)r & 0xFF) << 24) | (((int)g & 0xFF) << 16) | (((int)b & 0xFF) << 8) | ((int)a & 0xFF);
    }
    
    public static int toInt(byte[] bytes, int offset) {
        return toInt(bytes[R_INDEX + offset], bytes[G_INDEX + offset], bytes[B_INDEX + offset], bytes[A_INDEX + offset]);
    }
    
    public static int toInt(byte[] bytes) {
        return toInt(bytes, 0);
    }
    
    public static byte[] toBytes(int rgba, byte[] bytes) {
        bytes[R_INDEX] = (byte)(rgba >> 24);
        bytes[G_INDEX] = (byte)(rgba >> 16);
        bytes[B_INDEX] = (byte)(rgba >> 8);
        bytes[A_INDEX] = (byte)rgba;
        return bytes;
    }
    
    /**
     * Converts an int array where each value represents an ARGB quad, i.e.:
     * Alpha = (Value >> 24) & 0xFF
     * Red = (Value >> 16) & 0xFF
     * Green = (Value >> 8) & 0xFF
     * Blue = Value & 0xFF
     * into a byte array where each quad of bytes represents an RGBA quad, i.e.
     * for each given pixel index I,
     * pixels[I + 0] = Red
     * pixels[I + 1] = Green
     * pixels[I + 2] = Blue
     * pixels[I + 3] = Alpha
     * Also note that the image is flipped along the Y-axis if flipY is set to true
     */
    public static byte[] argbToRGBA(int[] argb, int w, int h, boolean flipY) {
        if (null == argb) {
            assert 0 == w && 0 == h;
            return null;
        }
        assert w > 0 && h > 0 && w*h == argb.length;
        final int bpp = BYTES_PER_PIXEL;
        final byte[] rgba = new byte[argb.length*bpp];
        
        if (flipY) {
            final int wb = w*bpp;
            int i = 0, j = rgba.length - wb;
            for (int y = h - 1; y >= 0; --y, j -= wb) {
                for (int x = 0, k = j; x < w; ++x, ++i, k += bpp) {
                    final int v = argb[i];
                    rgba[0 + k] = (byte)((v >> 16) & 0xFF);
                    rgba[1 + k] = (byte)((v >> 8) & 0xFF);
                    rgba[2 + k] = (byte)(v & 0xFF);
                    rgba[3 + k] = (byte)((v >> 24) & 0xFF);
                }
            }
        }
        else {
            for (int i = 0, j = 0; i != argb.length; ++i, j += bpp) {
                final int v = argb[i];
                rgba[0 + j] = (byte)((v >> 16) & 0xFF);
                rgba[1 + j] = (byte)((v >> 8) & 0xFF);
                rgba[2 + j] = (byte)(v & 0xFF);
                rgba[3 + j] = (byte)((v >> 24) & 0xFF);
            }
        }
        return rgba;
    }
    
    // performs conversion that is opposite to argbToRGBA
    // offset and stride, unlike in Image class, are in bytes, not pixels
    public static int[] rgbaToARGB(byte[] rgba, int w, int h, int offset, int stride) {
        if (null == rgba) {
            assert 0 == w && 0 == h;
            return null;
        }
        assert w > 0 && h > 0 && w*h*4 == rgba.length;
        final int[] argb = new int[w*h];
        
        final int sourceSkip = stride - (w << 2);
        int sourceIdx = offset;
        int targetIdx = 0;
        for (int y = 0; y < h; ++y, sourceIdx += sourceSkip) {
            for (int x = 0; x < w; ++x, ++targetIdx, sourceIdx += 4) {
                final byte r = rgba[0 + sourceIdx];
                final byte g = rgba[1 + sourceIdx];
                final byte b = rgba[2 + sourceIdx];
                final byte a = rgba[3 + sourceIdx];
                argb[targetIdx] = toInt(a, r, g, b);
            }
        }
        
        return argb;
    }
    
}
