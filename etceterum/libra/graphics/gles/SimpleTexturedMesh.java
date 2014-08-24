package etceterum.libra.graphics.gles;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


public class SimpleTexturedMesh<M extends Mesh> implements TexturedMesh {
    private final Texture texture;
    private final M base;
    
    public SimpleTexturedMesh(Texture texture, M base) {
        assert null != texture;
        assert null != base;
        
        this.texture = texture;
        this.base = base;
    }
    
    public M getBaseMesh() {
        return base;
    }
    
    @Override
    public void recycle(GLES10 gl) {
        texture.close(gl);
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public short getIndexCount() {
        return base.getIndexCount();
    }

    @Override
    public ShortBuffer getIndices() {
        return base.getIndices();
    }

    @Override
    public FloatBuffer getNormals() {
        return base.getNormals();
    }

    @Override
    public FloatBuffer getTexCoords() {
        return base.getTexCoords();
    }

    @Override
    public FloatBuffer getVertices() {
        return base.getVertices();
    }

    @Override
    public void invalidateTexture() {
        texture.invalidate();
    }
}
