package etceterum.libra.graphics;

public final class Colors {
    private Colors() {
        // instantiate me not
    }
    
    public static void rgb2hsl(byte[] rgb, int rgbOffset, float[] hsl, int hslOffset) {
        final float r = ((int)rgb[0 + rgbOffset] & 0xFF)/255f;
        final float g = ((int)rgb[1 + rgbOffset] & 0xFF)/255f;
        final float b = ((int)rgb[2 + rgbOffset] & 0xFF)/255f;
        rgb2hsl(r, g, b, hsl, hslOffset);
    }
    
    public static void rgb2hsl(float[] rgb, int rgbOffset, float[] hsl, int hslOffset) {
        final float r = rgb[0 + rgbOffset];
        final float g = rgb[1 + rgbOffset];
        final float b = rgb[2 + rgbOffset];
        rgb2hsl(r, g, b, hsl, hslOffset);
    }
    
    // http://mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript
    // ttp://en.wikipedia.org/wiki/HSL_color_space
    public static void rgb2hsl(float r, float g, float b, float[] hsl, int hslOffset) {
        final float max = Math.max(r, Math.max(g, b));
        final float min = Math.min(r, Math.min(g, b));
        final float avg = (max + min)/2;
        
        float h = avg, s = avg, l = avg;
        if (min == max) {
            h = s = 0; // achromatic
        }
        else {
            final float d = max - min;
            s = l > 0.5f ? d/(2 - max - min) : d/(max + min);
            if (max == r) {
                h = (g - b)/d + (g < b ? 6 : 0);
            }
            else if (max == g) {
                h = (b - r)/d + 2;
            }
            else if (max == b) {
                h = (r - g)/d + 4;
            }
            h /= 6;
        }
        
        hsl[0 + hslOffset] = h;
        hsl[1 + hslOffset] = s;
        hsl[2 + hslOffset] = l;
    }
    
    public static void hsl2rgb(float[] hsl, int hslOffset, byte[] rgb, int rgbOffset) {
        final float h = hsl[0 + hslOffset], s = hsl[1 + hslOffset], l = hsl[2 + hslOffset];
        float r, g, b;
        if (s == 0) {
            r = g = b = l; // achromatic
        }
        else {
            final float q = l < 0.5f ? l*(1 + s) : l + s - l*s;
            final float p = 2*l - q;
            r = hue2rgb(p, q, h + 1/3f);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1/3f);
        }
        
        rgb[0 + rgbOffset] = (byte)(int)(r*255);
        rgb[1 + rgbOffset] = (byte)(int)(g*255);
        rgb[2 + rgbOffset] = (byte)(int)(b*255);
    }
    
    private static float hue2rgb(float p, float q, float t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1/6f) return p + (q - p) * 6 * t;
        if (t < 1/2f) return q;
        if (t < 2/3f) return p + (q - p) * (2/3f - t) * 6;
        return p;        
    }
}
