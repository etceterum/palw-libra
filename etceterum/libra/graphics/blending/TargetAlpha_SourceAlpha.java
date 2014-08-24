package etceterum.libra.graphics.blending;

public final class TargetAlpha_SourceAlpha implements Blender {
    public static final TargetAlpha_SourceAlpha INSTANCE = new TargetAlpha_SourceAlpha();
    
    private TargetAlpha_SourceAlpha() {
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
        
        targetData[R_INDEX + targetOffset] = (byte)Math.min(0xFF, sr*ta/0xFF + tr*sa/0xFF);
        targetData[G_INDEX + targetOffset] = (byte)Math.min(0xFF, sg*ta/0xFF + tg*sa/0xFF);;
        targetData[B_INDEX + targetOffset] = (byte)Math.min(0xFF, sb*ta/0xFF + tb*sa/0xFF);;
        targetData[A_INDEX + targetOffset] = (byte)Math.min(0xFF, sa*ta/0xFF + ta*sa/0xFF);;
    }

}
