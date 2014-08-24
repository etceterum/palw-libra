package etceterum.libra.graphics.blending;

public final class SourceColor_Zero implements Blender {
    public static final SourceColor_Zero INSTANCE = new SourceColor_Zero();
    
    private SourceColor_Zero() {
        // prevent instantiation
    }
    
    @Override
    public void blend(byte[] sourceData, int sourceOffset, byte[] targetData, int targetOffset) {
        targetData[0 + targetOffset] = sourceData[0 + sourceOffset];
        targetData[1 + targetOffset] = sourceData[1 + sourceOffset];
        targetData[2 + targetOffset] = sourceData[2 + sourceOffset];
        targetData[3 + targetOffset] = sourceData[3 + sourceOffset];
    }
}
