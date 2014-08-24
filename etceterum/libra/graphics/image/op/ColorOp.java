package etceterum.libra.graphics.image.op;


import etceterum.libra.graphics.RGBA;
import etceterum.libra.graphics.image.ImageOp;

public abstract class ColorOp implements ImageOp {
    public static final byte DEFAULT_R = 0;
    public static final byte DEFAULT_G = 0;
    public static final byte DEFAULT_B = 0;
    public static final byte DEFAULT_A = (byte)0xFF;
    
    protected byte r, g, b, a;
    
    protected ColorOp() {
        resetColor();
    }
    
    protected ColorOp(byte r, byte g, byte b, byte a) {
        setColor(r, g, b, a);
    }
    
    protected ColorOp(byte r, byte g, byte b) {
        setColor(r, g, b);
    }
    
    protected ColorOp(int rgba) {
        setColor(rgba);
    }
    
    public ColorOp resetColor() {
        return setColor(DEFAULT_R, DEFAULT_G, DEFAULT_B, DEFAULT_A);
    }
    
    public ColorOp setColor(byte r, byte g, byte b, byte a) {
        return setR(r).setG(g).setB(b).setA(a);
    }
    
    public ColorOp setColor(byte r, byte g, byte b) {
        return setColor(r, g, b, DEFAULT_A);
    }
    
    public ColorOp setColor(int rgba) {
        return setColor((byte)(rgba >> 24), (byte)(rgba >> 16), (byte)(rgba >> 8), (byte)rgba);
    }
    
    public ColorOp setR(byte value) {
        r = value;
        return this;
    }
    
    public byte getR() {
        return r;
    }

    public ColorOp setG(byte value) {
        g = value;
        return this;
    }
    
    public byte getG() {
        return g;
    }

    public ColorOp setB(byte value) {
        b = value;
        return this;
    }
    
    public byte getB() {
        return b;
    }

    public ColorOp setA(byte value) {
        a = value;
        return this;
    }
    
    public byte getA() {
        return a;
    }
    
    public int getColor() {
        return RGBA.toInt(r, g, b, a);
    }
}
