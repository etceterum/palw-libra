package etceterum.libra.graphics.blending;

public class SourceAlpha_OneMinusSourceAlpha implements Blender {
    public static final SourceAlpha_OneMinusSourceAlpha INSTANCE = new SourceAlpha_OneMinusSourceAlpha();
    
    private SourceAlpha_OneMinusSourceAlpha() {
        // prevent instantiation
    }

    @Override
    public void blend(byte[] sourceData, int sourceOffset, byte[] targetData, int targetOffset) {
        final int sourceR = (int)sourceData[R_INDEX + sourceOffset] & 0xFF;
        final int sourceG = (int)sourceData[G_INDEX + sourceOffset] & 0xFF;
        final int sourceB = (int)sourceData[B_INDEX + sourceOffset] & 0xFF;
        final int sourceA = (int)sourceData[A_INDEX + sourceOffset] & 0xFF;
        
        final int targetR = (int)targetData[R_INDEX + targetOffset] & 0xFF;
        final int targetG = (int)targetData[G_INDEX + targetOffset] & 0xFF;
        final int targetB = (int)targetData[B_INDEX + targetOffset] & 0xFF;
        final int targetA = (int)targetData[A_INDEX + targetOffset] & 0xFF;
        
        final int oneMinusSourceA = 0xFF - sourceA;
        final int r = (sourceR*sourceA + targetR*oneMinusSourceA) >> 8;
        final int g = (sourceG*sourceA + targetG*oneMinusSourceA) >> 8;
        final int b = (sourceB*sourceA + targetB*oneMinusSourceA) >> 8;
        final int a = (sourceA*sourceA + targetA*oneMinusSourceA) >> 8;
        
        targetData[0 + targetOffset] = (byte)r;//Math.min(r, 0xFF);
        targetData[1 + targetOffset] = (byte)g;//Math.min(g, 0xFF);
        targetData[2 + targetOffset] = (byte)b;//Math.min(b, 0xFF);
        targetData[3 + targetOffset] = (byte)a;//Math.min(a, 0xFF);
    }

}
