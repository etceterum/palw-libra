package etceterum.libra.graphics.image.op;

import java.util.ArrayList;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;

public final class Random implements ImageOp {
    private final ArrayList<ImageOp> ops;
    
    public Random(ImageOp... ops) {
        this.ops = new ArrayList<ImageOp>(ops.length);
        for (ImageOp op: ops) {
            add(op);
        }
    }
    
    public Random clear() {
        ops.clear();
        return this;
    }
    
    public Random add(ImageOp op) {
        if (null != op) {
            ops.add(op);
        }
        return this;
    }
    
    public int size() {
        return ops.size();
    }

    @Override
    public Image execute(Image input) {
        final int count = ops.size();
        if (0 == count) {
            return input;
        }
        final int index = (int)(Math.random()*count);
        final ImageOp op = ops.get(index);
        assert null != op;
        return op.execute(input);
    }

}
