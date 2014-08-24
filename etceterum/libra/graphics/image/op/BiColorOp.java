package etceterum.libra.graphics.image.op;


import etceterum.libra.graphics.RGBA;
import etceterum.libra.graphics.image.ImageOp;

public abstract class BiColorOp implements ImageOp {
    public static final byte DEFAULT_FROM_R = 0;
    public static final byte DEFAULT_FROM_G = 0;
    public static final byte DEFAULT_FROM_B = 0;
    public static final byte DEFAULT_FROM_A = (byte)0xFF;
    
    public static final byte DEFAULT_TO_R   = (byte)0xFF;
    public static final byte DEFAULT_TO_G   = (byte)0xFF;
    public static final byte DEFAULT_TO_B   = (byte)0xFF;
    public static final byte DEFAULT_TO_A   = (byte)0xFF;
    
    protected byte fromR, fromG, fromB, fromA, toR, toG, toB, toA;
    
    protected BiColorOp() {
        resetColors();
    }
    
    protected BiColorOp(int from, int to) {
        setColors(from, to);
    }
    
    protected BiColorOp(byte fromR, byte fromG, byte fromB, byte fromA, byte toR, byte toG, byte toB, byte toA) {
        setColors(fromR, fromG, fromB, fromA, toR, toG, toB, toA);
    }
    
    public BiColorOp resetColors() {
        return resetFromColor().resetToColor();
    }
    
    public BiColorOp setColors(int from, int to) {
        return setFromColor(from).setToColor(to);
    }
    
    public BiColorOp setColors(byte fromR, byte fromG, byte fromB, byte fromA, byte toR, byte toG, byte toB, byte toA) {
        return setFromColor(fromR, fromG, fromB, fromA).setToColor(toR, toG, toB, toA);
    }
    
    public BiColorOp resetFromColor() {
        return setFromColor(DEFAULT_FROM_R, DEFAULT_FROM_G, DEFAULT_FROM_B, DEFAULT_FROM_A);
    }
    
    public BiColorOp setFromColor(byte r, byte g, byte b, byte a) {
        return setFromR(r).setFromG(g).setFromB(b).setFromA(a);
    }
    
    public BiColorOp setFromColor(byte r, byte g, byte b) {
        return setFromColor(r, g, b, DEFAULT_FROM_A);
    }
    
    public BiColorOp setFromColor(int rgba) {
        return setFromColor((byte)(rgba >> 24), (byte)(rgba >> 16), (byte)(rgba >> 8), (byte)rgba);
    }
    
    public BiColorOp setFromR(byte value) {
        fromR = value;
        return this;
    }
    
    public byte getFromR() {
        return fromR;
    }

    public BiColorOp setFromG(byte value) {
        fromG = value;
        return this;
    }
    
    public byte getFromG() {
        return fromG;
    }

    public BiColorOp setFromB(byte value) {
        fromB = value;
        return this;
    }
    
    public byte getFromB() {
        return fromB;
    }

    public BiColorOp setFromA(byte value) {
        fromA = value;
        return this;
    }
    
    public byte getFromA() {
        return fromA;
    }
    
    public int getFromColor() {
        return RGBA.toInt(fromR, fromG, fromB, fromA);
    }
    
    public BiColorOp resetToColor() {
        return setToColor(DEFAULT_FROM_R, DEFAULT_FROM_G, DEFAULT_FROM_B, DEFAULT_FROM_A);
    }
    
    public BiColorOp setToColor(byte r, byte g, byte b, byte a) {
        return setToR(r).setToG(g).setToB(b).setToA(a);
    }
    
    public BiColorOp setToColor(byte r, byte g, byte b) {
        return setToColor(r, g, b, DEFAULT_FROM_A);
    }
    
    public BiColorOp setToColor(int rgba) {
        return setToColor((byte)(rgba >> 24), (byte)(rgba >> 16), (byte)(rgba >> 8), (byte)rgba);
    }
    
    public BiColorOp setToR(byte value) {
        toR = value;
        return this;
    }
    
    public byte getToR() {
        return toR;
    }

    public BiColorOp setToG(byte value) {
        toG = value;
        return this;
    }
    
    public byte getToG() {
        return toG;
    }

    public BiColorOp setToB(byte value) {
        toB = value;
        return this;
    }
    
    public byte getToB() {
        return toB;
    }

    public BiColorOp setToA(byte value) {
        toA = value;
        return this;
    }
    
    public byte getToA() {
        return toA;
    }
    
    public int getToColor() {
        return RGBA.toInt(toR, toG, toB, toA);
    }
}
