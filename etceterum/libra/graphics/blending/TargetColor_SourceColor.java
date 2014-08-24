package etceterum.libra.graphics.blending;

public class TargetColor_SourceColor implements Blender {
    public static final TargetColor_SourceColor INSTANCE = new TargetColor_SourceColor();
    
    private TargetColor_SourceColor() {
        // prevent instantiation
    }

    @Override
    public void blend(byte[] sourceData, int sourceOffset, byte[] targetData, int targetOffset) {
        int sr = (int)sourceData[R_INDEX + sourceOffset] & 0xFF;
        int sg = (int)sourceData[G_INDEX + sourceOffset] & 0xFF;
        int sb = (int)sourceData[B_INDEX + sourceOffset] & 0xFF;
        int sa = (int)sourceData[A_INDEX + sourceOffset] & 0xFF;
        
        int tr = (int)targetData[R_INDEX + targetOffset] & 0xFF;
        int tg = (int)targetData[G_INDEX + targetOffset] & 0xFF;
        int tb = (int)targetData[B_INDEX + targetOffset] & 0xFF;
        int ta = (int)targetData[A_INDEX + targetOffset] & 0xFF;
        
        targetData[R_INDEX + targetOffset] = (byte)Math.min(0xFF, sr*tr/0xFF + tr*sr/0xFF);
        targetData[G_INDEX + targetOffset] = (byte)Math.min(0xFF, sg*tg/0xFF + tg*sg/0xFF);;
        targetData[B_INDEX + targetOffset] = (byte)Math.min(0xFF, sb*tb/0xFF + tb*sb/0xFF);;
        targetData[A_INDEX + targetOffset] = (byte)Math.min(0xFF, sa*ta/0xFF + ta*sa/0xFF);;
    }

}
