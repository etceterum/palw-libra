package etceterum.libra.graphics.blending;

import etceterum.libra.graphics.RGBA;

public interface Blender {
    public static final int R_INDEX = RGBA.R_INDEX;
    public static final int G_INDEX = RGBA.G_INDEX;
    public static final int B_INDEX = RGBA.B_INDEX;
    public static final int A_INDEX = RGBA.A_INDEX;
    
    void blend(byte[] sourceData, int sourceOffset, byte[] targetData, int targetOffset);
}
