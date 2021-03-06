package etceterum.libra.graphics.blending;

public final class SourceColor_TargetColor implements Blender {
    public static final SourceColor_TargetColor INSTANCE = new SourceColor_TargetColor();
    
    private SourceColor_TargetColor() {
        // prevent instantiation
    }

    @Override
    public void blend(byte[] sourceData, int sourceOffset, byte[] targetData, int targetOffset) {
        targetData[0 + targetOffset] = (byte)Math.min(0xFF, ((int)sourceData[0 + sourceOffset] & 0xFF) + ((int)targetData[0 + targetOffset] & 0xFF));
        targetData[1 + targetOffset] = (byte)Math.min(0xFF, ((int)sourceData[1 + sourceOffset] & 0xFF) + ((int)targetData[1 + targetOffset] & 0xFF));
        targetData[2 + targetOffset] = (byte)Math.min(0xFF, ((int)sourceData[2 + sourceOffset] & 0xFF) + ((int)targetData[2 + targetOffset] & 0xFF));
        targetData[3 + targetOffset] = (byte)Math.min(0xFF, ((int)sourceData[3 + sourceOffset] & 0xFF) + ((int)targetData[3 + targetOffset] & 0xFF));
    }
}
