package etceterum.libra.graphics.npatch;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;

public final class SliceNPatchContent implements ImageOp {
    private NPatch npatch;
    
    public SliceNPatchContent() {
        resetNPatch();
    }
    
    public SliceNPatchContent(NPatch npatch) {
        setNPatch(npatch);
    }
    
    public SliceNPatchContent resetNPatch() {
        return setNPatch(null);
    }
    
    public SliceNPatchContent setNPatch(NPatch value) {
        npatch = value;
        return this;
    }
    
    public NPatch getNPatch() {
        return npatch;
    }

    @Override
    public Image execute(Image input) {
        final Image target = input;
        if (null == npatch) {
            return target;
        }
        return input.getSlice(
            npatch.getPaddingLeft(), 
            npatch.getPaddingTop(), 
            npatch.getScaledContentWidth(target.getWidth()), 
            npatch.getScaledContentHeight(target.getHeight())
            );
    }

}
