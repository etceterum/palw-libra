package etceterum.libra.graphics.gles;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import etceterum.libra.Buffers;

public class TransformedMesh implements Mesh {
    private final Mesh base;
    private MeshTransformer transformer;
    
    private FloatBuffer normals;
    private FloatBuffer vertices;
    private boolean dirtyNormals;
    private boolean dirtyVertices;
    
    public TransformedMesh(Mesh base, MeshTransformer transformer) {
        assert null != base;
        
        this.base = base;
        setTransformer(transformer);
    }
    
    public void setTransformer(MeshTransformer transformer) {
        assert null != transformer;
        this.transformer = transformer;
        invalidate();
    }
    
    public void invalidate() {
        dirtyNormals = true;
        dirtyVertices = true;
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
        validateNormals();
        return normals;
    }

    @Override
    public FloatBuffer getTexCoords() {
        return base.getTexCoords();
    }

    @Override
    public FloatBuffer getVertices() {
        validateVertices();
        return vertices;
    }
    
    private void validateNormals() {
        if (!dirtyNormals) {
            return;
        }
        
        // normals
        final FloatBuffer baseNormals = base.getNormals();
        if (null != baseNormals) {
            if (null == normals) {
                normals = Buffers.newFloatBuffer(baseNormals.limit());
            }
            assert normals.limit() == baseNormals.limit();
            while (baseNormals.hasRemaining()) {
                transformer.transformNormal(baseNormals, normals);
            }
            baseNormals.rewind();
            normals.rewind();
        }
        
        dirtyNormals = false;
    }
    
    private void validateVertices() {
        if (!dirtyVertices) {
            return;
        }
        
        final FloatBuffer baseVertices = base.getVertices();
        assert null != baseVertices;
        if (null == vertices) {
            vertices = Buffers.newFloatBuffer(baseVertices.limit());
        }
        assert vertices.limit() == baseVertices.limit();
        while (baseVertices.hasRemaining()) {
            transformer.transformVertex(baseVertices, vertices);
        }
        baseVertices.rewind();
        vertices.rewind();
        
        //System.exit(0);
        
        dirtyVertices = false;
    }
}
