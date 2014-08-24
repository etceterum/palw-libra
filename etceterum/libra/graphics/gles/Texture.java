package etceterum.libra.graphics.gles;


import etceterum.libra.Bits;
import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageFactory;
import etceterum.libra.graphics.image.op.Paste;

public final class Texture {
    private final Image sourceImage;
    private final int width, height;
    private final Image textureImage;
    private int name = 0;
    private boolean dirty;
    
    public Texture(ImageFactory imageFactory, Image image) {
        assert null != image;
        
        sourceImage = image;
        width = Bits.getSmallestPowerOfTwoGreaterThanOrEqualTo(image.getWidth());
        height = Bits.getSmallestPowerOfTwoGreaterThanOrEqualTo(image.getHeight());
        textureImage = sourceImage.isNormal() ? sourceImage : sourceImage.getClone(imageFactory);
        
        invalidate();
    }
    
    public void open(GLES10 gl) {
        assert null != gl;
        
        close(gl);
        
        final int[] names = new int[1];
        names[0] = name;
        gl.glGenTextures(1, names, 0);
        name = names[0];
        assert 0 != name;
        
        gl.glBindTexture(gl.GL_TEXTURE_2D(), name);
        
        gl.glTexParameterf(gl.GL_TEXTURE_2D(), gl.GL_TEXTURE_MAG_FILTER(), gl.GL_LINEAR());
        gl.glTexParameterf(gl.GL_TEXTURE_2D(), gl.GL_TEXTURE_MIN_FILTER(), gl.GL_LINEAR());
        gl.glTexParameterf(gl.GL_TEXTURE_2D(), gl.GL_TEXTURE_WRAP_S(), gl.GL_CLAMP_TO_EDGE());
        gl.glTexParameterf(gl.GL_TEXTURE_2D(), gl.GL_TEXTURE_WRAP_T(), gl.GL_CLAMP_TO_EDGE());

        gl.glTexImage2D(gl.GL_TEXTURE_2D(), 0, gl.GL_RGBA(), width, height, 0, gl.GL_RGBA(), gl.GL_UNSIGNED_BYTE(), null);
        //doUpdate(gl);
    }
    
    public void close(GLES10 gl) {
        assert null != gl;
        
        if (!isOpen()) {
            return;
        }
        
        final int[] names = new int[] { name };
        gl.glDeleteTextures(1, names, 0);
        name = 0;
    }
    
    public boolean isOpen() {
        return 0 != name;
    }
    
    public void activate(GLES10 gl) {
        assert null != gl;
        
        if (!isOpen()) {
            open(gl);
        }
        gl.glBindTexture(gl.GL_TEXTURE_2D(), name);
        validate(gl);
    }
    
    public void invalidate() {
        dirty = true;
    }
    
    public static void deactivate(GLES10 gl) {
        assert null != gl;
        gl.glBindTexture(gl.GL_TEXTURE_2D(), 0);
    }
    
    public int getTextureWidth() {
        return width;
    }
    
    public int getTextureHeight() {
        return height;
    }
    
    public Image getSourceImage() {
        return sourceImage;
    }
    
    public Image getImage() {
        return textureImage;
    }
    
    public int getImageWidth() {
        return null == textureImage? 0 : textureImage.getWidth();
    }
    
    public int getImageHeight() {
        return null == textureImage ? 0 : textureImage.getHeight();
    }
    
    public float getRelativeUsedWidth() {
        return 0 == width ? 0 : getImageWidth()/(float)width;
    }
    
    public float getRelativeUsedHeight() {
        return 0 == height ? 0 : getImageHeight()/(float)height;
    }
    
    private void validate(GLES10 gl) {
        if (!dirty) {
            return;
        }
        if (null != textureImage) {
            if (sourceImage != textureImage) {
                new Paste(sourceImage).execute(textureImage);
            }
            gl.glTexSubImage2D(gl.GL_TEXTURE_2D(), 0, 0, 0, textureImage.getWidth(), textureImage.getHeight(), gl.GL_RGBA(), gl.GL_UNSIGNED_BYTE(), textureImage.getBuffer());
        }
        dirty = false;
    }
}
