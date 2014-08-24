package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;

// Port of TritoneFilter from JHLabs library
// Original license:
/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

public class TriTone implements ImageOp {
    public static final byte DEFAULT_SHADOW_R   = (byte)0x00;
    public static final byte DEFAULT_SHADOW_G   = (byte)0x00;
    public static final byte DEFAULT_SHADOW_B   = (byte)0x00;
    public static final byte DEFAULT_SHADOW_A   = (byte)0xFF;
    
    public static final byte DEFAULT_MID_R      = (byte)0x22;
    public static final byte DEFAULT_MID_G      = (byte)0x22;
    public static final byte DEFAULT_MID_B      = (byte)0x88;
    public static final byte DEFAULT_MID_A      = (byte)0xFF;
    
    public static final byte DEFAULT_HIGH_R     = (byte)0xFF;
    public static final byte DEFAULT_HIGH_G     = (byte)0x00;
    public static final byte DEFAULT_HIGH_B     = (byte)0x00;
    public static final byte DEFAULT_HIGH_A     = (byte)0xFF;
    
    private byte shadowR, shadowG, shadowB, shadowA;
    private byte midR, midG, midB, midA;
    private byte highR, highG, highB, highA;
    
    private int[] lut;
    
    public TriTone() {
        reset();
    }
    
    public TriTone(int shadowRGBA, int midRGBA, int highRGBA) {
        set(shadowRGBA, midRGBA, highRGBA);
    }
    
    public TriTone(
        byte shadowR, byte shadowG, byte shadowB, byte shadowA,
        byte midR, byte midG, byte midB, byte midA,
        byte highR, byte highG, byte highB, byte highA
        )
    {
        set(shadowR, shadowG, shadowB, shadowA, midR, midG, midB, midA, highR, highG, highB, highA);
    }
    
    public TriTone reset() {
        return set(
            DEFAULT_SHADOW_R, DEFAULT_SHADOW_G, DEFAULT_SHADOW_B, DEFAULT_SHADOW_A, 
            DEFAULT_MID_R, DEFAULT_MID_G, DEFAULT_MID_B, DEFAULT_MID_A, 
            DEFAULT_HIGH_R, DEFAULT_HIGH_G, DEFAULT_HIGH_B, DEFAULT_HIGH_A 
            );
    }
    
    public TriTone set(int shadowRGBA, int midRGBA, int highRGBA) {
        return setShadow(shadowRGBA).setMid(midRGBA).setHigh(highRGBA);
    }
        
    public TriTone set(
        byte shadowR, byte shadowG, byte shadowB, byte shadowA,
        byte midR, byte midG, byte midB, byte midA,
        byte highR, byte highG, byte highB, byte highA
        ) 
    {
        return setShadow(shadowR, shadowG, shadowB, shadowA).setMid(midR, midG, midB, midA).setHigh(highR, highG, highB, highA);
    }
    
    public TriTone setShadow(int rgba) {
        return setShadow((byte)(rgba >> 24), (byte)(rgba >> 16), (byte)(rgba >> 8), (byte)rgba);
    }
    
    public TriTone setShadow(byte r, byte g, byte b, byte a) {
        return setShadowR(r).setShadowG(g).setShadowB(b).setShadowA(a);
    }
    
    public TriTone setShadowR(byte value) {
        shadowR = value;
        return invalidate();
    }
    
    public byte getShadowR() {
        return shadowR;
    }
    
    public TriTone setShadowG(byte value) {
        shadowG = value;
        return invalidate();
    }
    
    public byte getShadowG() {
        return shadowG;
    }
    
    public TriTone setShadowB(byte value) {
        shadowB = value;
        return invalidate();
    }
    
    public byte getShadowB() {
        return shadowB;
    }
    
    public TriTone setShadowA(byte value) {
        shadowA = value;
        return invalidate();
    }
    
    public byte getShadowA() {
        return shadowA;
    }
    
    public TriTone setMid(int rgba) {
        return setMid((byte)(rgba >> 24), (byte)(rgba >> 16), (byte)(rgba >> 8), (byte)rgba);
    }
    
    public TriTone setMid(byte r, byte g, byte b, byte a) {
        return setMidR(r).setMidG(g).setMidB(b).setMidA(a);
    }
    
    public TriTone setMidR(byte value) {
        midR = value;
        return invalidate();
    }
    
    public byte getMidR() {
        return midR;
    }
    
    public TriTone setMidG(byte value) {
        midG = value;
        return invalidate();
    }
    
    public byte getMidG() {
        return midG;
    }
    
    public TriTone setMidB(byte value) {
        midB = value;
        return invalidate();
    }
    
    public byte getMidB() {
        return midB;
    }
    
    public TriTone setMidA(byte value) {
        midA = value;
        return invalidate();
    }
    
    public byte getMidA() {
        return midA;
    }
    
    public TriTone setHigh(int rgba) {
        return setHigh((byte)(rgba >> 24), (byte)(rgba >> 16), (byte)(rgba >> 8), (byte)rgba);
    }
    
    public TriTone setHigh(byte r, byte g, byte b, byte a) {
        return setHighR(r).setHighG(g).setHighB(b).setHighA(a);
    }
    
    public TriTone setHighR(byte value) {
        highR = value;
        return invalidate();
    }
    
    public byte getHighR() {
        return highR;
    }
    
    public TriTone setHighG(byte value) {
        highG = value;
        return invalidate();
    }
    
    public byte getHighG() {
        return highG;
    }
    
    public TriTone setHighB(byte value) {
        highB = value;
        return invalidate();
    }
    
    public byte getHighB() {
        return highB;
    }
    
    public TriTone setHighA(byte value) {
        highA = value;
        return invalidate();
    }
    
    public byte getHighA() {
        return highA;
    }
    
    @Override
    public Image execute(Image input) {
        final Image target = input;
        final int width = target.getWidth(), height = target.getHeight();
        if (0 == width || 0 == height) {
            return target;
        }
        
        final int stride = target.getStride() << 2;
        final byte[] data = target.getData();
        
        validate();
        
        int rowIndex = target.getOffset() << 2;
        for (int y = 0; y < height; ++y, rowIndex += stride) {
            int colIndex = rowIndex;
            for (int x = 0; x < width; ++x, colIndex += 4) {
                final int color = lut[brightness(data[0 + colIndex], data[1 + colIndex], data[2 + colIndex])];
                data[0 + colIndex] = (byte)(color >> 24);
                data[1 + colIndex] = (byte)(color >> 16);
                data[2 + colIndex] = (byte)(color >> 8);
                data[3 + colIndex] = (byte)color;
            }
        }
        
        return target;
    }
    
    private TriTone invalidate() {
        lut = null;
        return this;
    }
    
    private void validate() {
        if (null == lut) {
            lut = new int[256];
            for (int i = 0; i < 128; i++) {
                float t = i / 127.0f;
                lut[i] = mixColors(t, shadowR, shadowG, shadowB, shadowA, midR, midG, midB, midA);
            }
            for ( int i = 128; i < 256; i++ ) {
                float t = (i-127) / 128.0f;
                lut[i] = mixColors(t, midR, midG, midB, midA, highR, highG, highB, highA);
            }
        }
    }
    
    /**
     * Linear interpolation of ARGB values.
     * @param t the interpolation parameter
     * @param rgb1 the lower interpolation range
     * @param rgb2 the upper interpolation range
     * @return the interpolated value
     */
    private static int mixColors(float t, byte r1, byte g1, byte b1, byte a1, byte r2, byte g2, byte b2, byte a2) {
        final int r = lerp(t, r1, r2);
        final int g = lerp(t, g1, g2);
        final int b = lerp(t, b1, b2);
        final int a = lerp(t, a1, a2);
        return (r << 24) | (g << 16) | (b << 8) | a;
    }
    
    /**
     * Linear interpolation.
     * @param t the interpolation parameter
     * @param a the lower interpolation range
     * @param b the upper interpolation range
     * @return the interpolated value
     */
    private static int lerp(float t, byte a, byte b) {
        final int aa = (int)a & 0xFF, bb = (int)b & 0xFF;
        return (int)(aa + t*(bb - aa));
    }
    
    private static int brightness(byte r, byte g, byte b) {
        return (((int)r & 0xFF) + ((int)g & 0xFF) + ((int)b & 0xFF))/3;
    }
}
