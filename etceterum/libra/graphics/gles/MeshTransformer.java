package etceterum.libra.graphics.gles;

import java.nio.FloatBuffer;

public interface MeshTransformer {
    void transformNormal(FloatBuffer input, FloatBuffer output);
    void transformVertex(FloatBuffer input, FloatBuffer output);
}
