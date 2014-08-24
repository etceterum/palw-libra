package etceterum.libra.graphics.gles;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import etceterum.libra.Buffers;

public final class RectangularMesh implements Mesh {
    protected final short nSpansX;
    protected final short nSpansY;
    protected ShortBuffer indices;
    protected FloatBuffer vertices;
    protected FloatBuffer normals;
    protected FloatBuffer texCoords;
    
    public RectangularMesh(int nSpansX, int nSpansY) {
        assert nSpansX > 0 && nSpansX <= Short.MAX_VALUE;
        assert nSpansY > 0 && nSpansY <= Short.MAX_VALUE;
        
        this.nSpansX = (short)nSpansX;
        this.nSpansY = (short)nSpansY;
    }
    
    public RectangularMesh() {
        this(1, 1);
    }
    
    public RectangularMesh setRectangle(float x, float y, float w, float h, float z) {
        assert w >= 0 && h >= 0;
        
        final float spanX = w/nSpansX;
        final float spanY = h/nSpansY;
        
        getOrCreateVertices();
        
        for (int i = 0; i <= nSpansY; ++i) {
            for (int j = 0; j <= nSpansX; ++j) {
                vertices.put(x + j*spanX);
                vertices.put(y + i*spanY);
                vertices.put(z);
            }
        }
        
        vertices.rewind();
        
        return this;
    }
    
    public RectangularMesh setRectangle(float x, float y, float w, float h) {
        return setRectangle(x, y, w, h, 0);
    }
    
    public RectangularMesh setRectangle() {
        return setRectangle(-0.5f, -0.5f, 1, 1, 0);
    }
    
    public RectangularMesh setNormals(float nx, float ny, float nz) {
        getOrCreateNormals();
        
        for (int i = 0; i <= nSpansY; ++i) {
            for (int j = 0; j <= nSpansX; ++j) {
                normals.put(nx);
                normals.put(ny);
                normals.put(nz);
            }
        }
        
        normals.rewind();
        
        return this;
    }
    
    public RectangularMesh setNormals() {
        return setNormals(0, 0, 1);
    }
    
    // coordinates and sizes passed into this function are texture coordinates and sizes
    public RectangularMesh setTextureRectangle(float x, float y, float w, float h) {
        final float texSpanX = w/nSpansX;
        final float texSpanY = h/nSpansY;
        
        getOrCreateTexCoords();
        
        for (int i = 0; i <= nSpansY; ++i) {
            for (int j = 0; j <= nSpansX; ++j) {
                texCoords.put(x + j*texSpanX);
                texCoords.put(y + i*texSpanY);
            }
        }
        
        texCoords.rewind();
        
        return this;
    }
    
    protected FloatBuffer getOrCreateVertices() {
        if (null == vertices) {
            vertices = Buffers.newFloatBuffer(getVertexCount()*3); // x, y, z
            vertices.rewind();
        }
        return vertices;
    }
    
    protected FloatBuffer getOrCreateNormals() {
        if (null == normals) {
            normals = Buffers.newFloatBuffer(getVertexCount()*3); // nx, ny, nz
            normals.rewind();
        }
        return normals;
    }
    
    protected FloatBuffer getOrCreateTexCoords() {
        if (null == texCoords) {
            texCoords = Buffers.newFloatBuffer(getVertexCount()*2); // s, t
            texCoords.rewind();
        }
        return texCoords;
    }
    
    protected int getVertexCount() {
        return (nSpansX + 1)*(nSpansY + 1);
    }
    
    @Override
    public short getIndexCount() {
        final int degenerateIndexCount = (nSpansY - 1)*2;
        final int rowIndexCount = (nSpansX + 1)*2;
        final int rowCount = nSpansY;
        final int indexCount = rowIndexCount*rowCount + degenerateIndexCount;
        return (short)indexCount;
    }
    
    @Override
    public ShortBuffer getIndices() {
        if (null == indices) {
            indices = Buffers.newShortBuffer(getIndexCount());
            
            short k = 0;
            for (int i = 0; i < nSpansY; ++i) {
                // generate 1 horizontal stripe of vertices
                for (int j = 0; j <= nSpansX; ++j, ++k) {
                    indices.put(k);
                    indices.put((short)(k + nSpansX + 1));
                }
                // if needed, generate degenerate indices to advance to the next vertical span
                if (i < nSpansY - 1) {
                    indices.put((short)(k + nSpansX));
                    indices.put(k);
                }
            }
            
            indices.rewind();
        }
        
        return indices;
    }
    
    @Override
    public FloatBuffer getNormals() {
        return normals;
    }
    
    @Override
    public FloatBuffer getTexCoords() {
        return texCoords;
    }
    
    @Override
    public FloatBuffer getVertices() {
        if (null == vertices) {
            setRectangle();
        }
        return vertices;
    }
}
