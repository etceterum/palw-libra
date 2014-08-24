package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;

/**
 * Create a new image which is a subimage of the original image with the specified number of pixels 
 * stripped from the sides.
 * Original image will remain unmodified. 
*/
public final class Crop implements ImageOp {
    private int left, top, right, bottom;
    
    public Crop() {
        reset();
    }
    
    public Crop(int size) {
        set(size);
    }
    
    public Crop(int left, int top, int right, int bottom) {
        set(left, top, right, bottom);
    }
    
    public Crop reset() {
        return set(0);
    }
    
    public Crop set(int size) {
        return set(size, size, size, size);
    }
    
    public Crop set(int left, int top, int right, int bottom) {
        return setLeft(left).setTop(top).setRight(right).setBottom(bottom);
    }
    
    public Crop setLeft(int value) {
        assert value >= 0;
        this.left = value;
        return this;
    }
    
    public int getLeft() {
        return left;
    }
    
    public Crop setTop(int value) {
        assert value >= 0;
        this.top = value;
        return this;
    }
    
    public int getTop() {
        return top;
    }
    
    public Crop setRight(int value) {
        assert value >= 0;
        this.right = value;
        return this;
    }
    
    public int getRight() {
        return right;
    }
    
    public Crop setBottom(int value) {
        assert value >= 0;
        this.bottom = value;
        return this;
    }
    
    public int getBottom() {
        return bottom;
    }
    
    @Override
    public Image execute(Image input) {
        return new Slice(left, top, input.getWidth() - left - right, input.getHeight() - top - bottom).execute(input);
    }

}
