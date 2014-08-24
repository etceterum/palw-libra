package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;

public abstract class SourceOp implements ImageOp {
    protected Image source;
    
    protected SourceOp() {
        resetSource();
    }
    
    protected SourceOp(Image source) {
        setSource(source);
    }
    
    public SourceOp resetSource() {
        return setSource(null);
    }
    
    public SourceOp setSource(Image source) {
        this.source = source;
        return this;
    }
    
    public Image getSource() {
        return source;
    }
}
