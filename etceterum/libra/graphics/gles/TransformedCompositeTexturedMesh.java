package etceterum.libra.graphics.gles;

import java.util.ArrayList;
import java.util.Iterator;

public class TransformedCompositeTexturedMesh implements CompositeTexturedMesh {
    private final CompositeTexturedMesh base;
    private final ArrayList<SimpleTexturedMesh<TransformedMesh>> tiles;
    
    public TransformedCompositeTexturedMesh(CompositeTexturedMesh base, MeshTransformer transformer) {
        assert null != base;
        assert null != transformer;
        
        this.base = base;
        final int count = base.getTexturedMeshCount();
        tiles = new ArrayList<SimpleTexturedMesh<TransformedMesh>>(count);
        for (Iterator<? extends TexturedMesh> baseIt = base.getTexturedMeshIterator(); baseIt.hasNext(); ) {
            final TexturedMesh baseTexturedMesh = baseIt.next();
            final TransformedMesh transformedMesh = new TransformedMesh(baseTexturedMesh, transformer);
            tiles.add(new SimpleTexturedMesh<TransformedMesh>(baseTexturedMesh.getTexture(), transformedMesh));
        }
    }
    
    public void setTransformer(MeshTransformer transformer) {
        for (SimpleTexturedMesh<TransformedMesh> tile: tiles) {
            tile.getBaseMesh().setTransformer(transformer);
        }
    }
    
    public void invalidate() {
        for (SimpleTexturedMesh<TransformedMesh> tile: tiles) {
            tile.getBaseMesh().invalidate();
        }
    }

    @Override
    public Iterator<SimpleTexturedMesh<TransformedMesh>> getTexturedMeshIterator() {
        return tiles.iterator();
    }

    @Override
    public int getTexturedMeshCount() {
        return tiles.size();
    }
    
    @Override
    public void recycle(GLES10 gl) {
        base.recycle(gl);
    }

    @Override
    public void invalidateTextures() {
        for (SimpleTexturedMesh<TransformedMesh> tile: tiles) {
            tile.invalidateTexture();
        }
    }

}
