package etceterum.libra.graphics.npatch;


import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.op.Clone;
import etceterum.libra.graphics.image.op.Crop;

public final class NPatchUtil {
    private NPatchUtil() {
        // prevent instantiation
    }
    
    public static NPatch createNPatchFromSource(Image sourceImage) throws NPatchException {
        final NPatchInfo info = new NPatchCompiler().compileSource(sourceImage);
        final Image image = new Clone().execute(new Crop(1).execute(sourceImage));
        return new NPatch(image, info);
    }
}
