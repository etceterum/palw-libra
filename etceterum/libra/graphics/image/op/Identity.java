package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;
import etceterum.libra.graphics.image.ImageOpFactory;

public final class Identity implements ImageOp {
    public static final class Factory implements ImageOpFactory {
        private static final Factory instance = new Factory();
        
        private Factory() {
            // do not instantiate me
        }
        
        public static Factory getInstance() {
            return instance;
        }

        @Override
        public ImageOp createOp() {
            return Identity.getInstance();
        }
    }
    
    private static final Identity instance = new Identity();
    
    public static Identity getInstance() {
        return instance;
    }
    
    private Identity() {
        // do not instantiate me
    }

    @Override
    public Image execute(Image input) {
        return input;
    }

}
