package etceterum.libra.graphics.blending;

public class One_SourceAlpha implements Blender {
    public static final One_SourceAlpha INSTANCE = new One_SourceAlpha();
    
    private One_SourceAlpha() {
        // prevent instantiation
    }

    @Override
    public void blend(byte[] sourceData, int sourceOffset, byte[] targetData, int targetOffset) {
        int a = (int)sourceData[A_INDEX + sourceOffset] & 0xFF;
        int r = Math.min(0xFF, ((int)sourceData[R_INDEX + sourceOffset] & 0xFF) + a*((int)targetData[R_INDEX + sourceOffset] & 0xFF)/0xFF);
        int g = Math.min(0xFF, ((int)sourceData[G_INDEX + sourceOffset] & 0xFF) + a*((int)targetData[G_INDEX + sourceOffset] & 0xFF)/0xFF);
        int b = Math.min(0xFF, ((int)sourceData[B_INDEX + sourceOffset] & 0xFF) + a*((int)targetData[B_INDEX + sourceOffset] & 0xFF)/0xFF);
        a = Math.min(0xFF, a + a*((int)targetData[A_INDEX + sourceOffset] & 0xFF)/0xFF);
        targetData[R_INDEX + targetOffset] = (byte)r;
        targetData[G_INDEX + targetOffset] = (byte)g;
        targetData[B_INDEX + targetOffset] = (byte)b;
        targetData[A_INDEX + targetOffset] = (byte)a;
    }

}
