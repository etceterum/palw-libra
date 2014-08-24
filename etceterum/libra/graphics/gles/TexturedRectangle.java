package etceterum.libra.graphics.gles;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageFactory;
import etceterum.libra.graphics.image.SafeImageFactory;
import etceterum.libra.graphics.image.op.Paste;

public final class TexturedRectangle implements TexturedMesh {
    private static final ImageFactory imageFactory = SafeImageFactory.getInstance();
    
    public static final int DEFAULT_SPAN_COUNT_X = 7;
    public static final int DEFAULT_SPAN_COUNT_Y = 7;
    
    private final Image image;
    private final Texture texture;
    private final RectangularMesh mesh;
    private final int x, y;
    
    public TexturedRectangle(Image image) {
        this(image, DEFAULT_SPAN_COUNT_X, DEFAULT_SPAN_COUNT_Y);
    }
    
    public TexturedRectangle(int x, int y, Image image) {
        this(x, y, image, 0, 0);
    }
    
    public TexturedRectangle(Image image, int nSpansX, int nSpansY) {
        this(0, 0, image, nSpansX, nSpansY);
    }
    
    public TexturedRectangle(int x, int y, Image image, int nSpansX, int nSpansY) {
        this.image = image;
        texture = new Texture(imageFactory, image);
        mesh = new RectangularMesh(nSpansX, nSpansY)
            .setRectangle(x, y, texture.getImageWidth(), texture.getImageHeight())
            .setTextureRectangle(0, 0, texture.getRelativeUsedWidth(), texture.getRelativeUsedHeight())
            ;
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return texture.getImageWidth();
    }
    
    
    public int getHeight() {
        return texture.getImageHeight();
    }
    
    public void recycle(GLES10 gl) {
        texture.close(gl);
    }
    
    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public short getIndexCount() {
        return mesh.getIndexCount();
    }

    @Override
    public ShortBuffer getIndices() {
        return mesh.getIndices();
    }

    @Override
    public FloatBuffer getNormals() {
        return mesh.getNormals();
    }

    @Override
    public FloatBuffer getTexCoords() {
        return mesh.getTexCoords();
    }

    @Override
    public FloatBuffer getVertices() {
        return mesh.getVertices();
    }
    
    @Override
    public void invalidateTexture() {
        if (!image.isNormal()) {
            new Paste(image).execute(texture.getImage());
        }
        texture.invalidate();
    }
}
