package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;

/**
 * Create a new image with dimensions (width x height) which is a subimage of the original image
 * starting at (x, y).
 * Original image will remain unmodified. 
 */
public final class Slice implements ImageOp {
    private int x, y, width, height;
    
    public Slice() {
        reset();
    }
    
    public Slice(int x, int y, int width, int height) {
        set(x, y, width, height);
    }
    
    public Slice reset() {
        return set(0, 0, -1, -1);
    }
    
    public Slice set(int x, int y, int width, int height) {
        return setX(x).setY(y).setWidth(width).setHeight(height);
    }
    
    public Slice setX(int value) {
        this.x = value;
        return this;
    }
    
    public int getX() {
        return x;
    }

    public Slice setY(int value) {
        this.y = value;
        return this;
    }
    
    public int getY() {
        return y;
    }
    
    public Slice setWidth(int value) {
        this.width = value;
        return this;
    }
    
    public int getWidth() {
        return width;
    }
    
    public Slice setHeight(int value) {
        this.height = value;
        return this;
    }
    
    public int getHeight() {
        return height;
    }
    
    @Override
    public Image execute(Image input) {
        return input.getSlice(x, y, width, height);
    }
    
    
}
