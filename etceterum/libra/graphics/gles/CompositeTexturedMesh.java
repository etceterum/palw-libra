package etceterum.libra.graphics.gles;

import java.util.Iterator;

public interface CompositeTexturedMesh extends Recyclable {
    Iterator<? extends TexturedMesh> getTexturedMeshIterator();
    int getTexturedMeshCount();
    void invalidateTextures();
}
