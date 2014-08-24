package etceterum.libra.graphics.gles;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public interface Mesh {
    short getIndexCount();
    // must not return null
    ShortBuffer getIndices();
    // may return null, but if non-null is returned, must contain triplets (nx, ny, nz)
    FloatBuffer getNormals();
    // may return null, but if non-null is returned, must contain pairs (s, t)
    FloatBuffer getTexCoords();
    // must not return null, must contain triplets (x, y, z)
    FloatBuffer getVertices();
}
