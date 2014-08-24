package etceterum.libra.graphics.image.op;

import java.util.ArrayList;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;

public final class Pipe implements ImageOp {
    private final ArrayList<ImageOp> ops;
    
    public Pipe(ImageOp... ops) {
        this.ops = new ArrayList<ImageOp>(ops.length);
        for (ImageOp op: ops) {
            if (null != op) {
                this.ops.add(op);
            }
        }
    }

    @Override
    public Image execute(Image input) {
        for (ImageOp op: ops) {
            input = op.execute(input);
        }
        return input;
    }
}
