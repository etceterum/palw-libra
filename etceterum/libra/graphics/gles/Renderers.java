package etceterum.libra.graphics.gles;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Iterator;

public final class Renderers {
    private Renderers() {
        // prevent instantiation
    }
    
    public static void renderMesh(GLES10 gl, Mesh mesh) {
        assert null != mesh;
        
        final FloatBuffer vertices = mesh.getVertices();
        assert null != vertices;
        final FloatBuffer normals = mesh.getNormals();
        final FloatBuffer texCoords = mesh.getTexCoords();
        final ShortBuffer indices = mesh.getIndices();
        assert null != indices;
        final int indexCount = mesh.getIndexCount();
        
        gl.glEnableClientState(gl.GL_VERTEX_ARRAY());
        gl.glVertexPointer(3, gl.GL_FLOAT(), 0, vertices);
        
        if (null != normals) {
            gl.glEnableClientState(gl.GL_NORMAL_ARRAY());
            gl.glNormalPointer(gl.GL_FLOAT(), 0, normals);
        }
        
        if (null != texCoords) {
            gl.glEnableClientState(gl.GL_TEXTURE_COORD_ARRAY());
            gl.glTexCoordPointer(2, gl.GL_FLOAT(), 0, texCoords);
        }
        
        gl.glDrawElements(gl.GL_TRIANGLE_STRIP(), indexCount, gl.GL_UNSIGNED_SHORT(), indices);

        if (null != texCoords) {
            gl.glDisableClientState(gl.GL_TEXTURE_COORD_ARRAY());
        }
        
        if (null != normals) {
            gl.glDisableClientState(gl.GL_NORMAL_ARRAY());
        }
        
        gl.glDisableClientState(gl.GL_VERTEX_ARRAY());
    }
    
    public static void renderTexturedMesh(GLES10 gl, TexturedMesh texturedMesh) {
        texturedMesh.getTexture().activate(gl);
        renderMesh(gl, texturedMesh);
    }
    
    public static void renderCompositeTexturedMesh(GLES10 gl, CompositeTexturedMesh compositeTexturedMesh) {
        for (Iterator<? extends TexturedMesh> it = compositeTexturedMesh.getTexturedMeshIterator(); it.hasNext(); ) {
            renderTexturedMesh(gl, it.next());
        }
    }
}
