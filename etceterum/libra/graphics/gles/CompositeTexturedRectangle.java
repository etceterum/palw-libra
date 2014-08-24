package etceterum.libra.graphics.gles;

import java.util.ArrayList;
import java.util.Iterator;

import etceterum.libra.graphics.image.Image;

public final class CompositeTexturedRectangle implements CompositeTexturedMesh {
    public static final int DEFAULT_MAX_TILE_SIZE_X = 256;
    public static final int DEFAULT_MAX_TILE_SIZE_Y = 256;
    
    private final Image image;
    private final int offsetX, offsetY;
    private final int nTilesX, nTilesY;
    private final ArrayList<TexturedRectangle> tiles;
    
    public CompositeTexturedRectangle(int offsetX, int offsetY, Image image, int maxTileSizeX, int maxTileSizeY, int nSpansPerTileX, int nSpansPerTileY) {
        this.image = image;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        final int width = image.getWidth();
        final int height = image.getHeight();
        
        int nFullTilesX = width/maxTileSizeX;
        int nFullTilesY = height/maxTileSizeY;
        int lastTileSizeX = width - nFullTilesX*maxTileSizeX;
        int lastTileSizeY = height - nFullTilesY*maxTileSizeY;
        int nLastTilesX = lastTileSizeX > 0 ? 1 : 0;
        int nLastTilesY = lastTileSizeY > 0 ? 1 : 0;
        nTilesX = nFullTilesX + nLastTilesX;
        nTilesY = nFullTilesY + nLastTilesY;
        int nTiles = nTilesX*nTilesY;
        int nLastTileSpansX = 0 == nLastTilesX ? 0 : (int)Math.max(1, maxTileSizeX/(double)lastTileSizeX);
        int nLastTileSpansY = 0 == nLastTilesY ? 0 : (int)Math.max(1, maxTileSizeY/(double)lastTileSizeY);
        
        tiles = new ArrayList<TexturedRectangle>(nTiles);
        int tileOffsetY = 0;
        
        for (int tileIdxY = 0; tileIdxY < nTilesY; ++tileIdxY, tileOffsetY += maxTileSizeY) {
            int tileOffsetX = 0;
            for (int tileIdxX = 0; tileIdxX < nTilesX; ++tileIdxX, tileOffsetX += maxTileSizeX) {
                int tileSizeX = tileIdxX < nFullTilesX ? maxTileSizeX : lastTileSizeX;
                int tileSizeY = tileIdxY < nFullTilesY ? maxTileSizeY : lastTileSizeY;
                final Image tileImage = image.getSlice(tileOffsetX, tileOffsetY, tileSizeX, tileSizeY);
                int nTileSpansX = tileIdxX < nFullTilesX ? nSpansPerTileX : nLastTileSpansX;
                int nTileSpansY = tileIdxY < nFullTilesY ? nSpansPerTileY : nLastTileSpansY;
                tiles.add(new TexturedRectangle(offsetX + tileOffsetX, offsetY + tileOffsetY, tileImage, nTileSpansX, nTileSpansY));
            }
        }
    }
    
    public int getOffsetX() {
        return offsetX;
    }
    
    public int getOffsetY() {
        return offsetY;
    }
    
    public int getWidth() {
        return image.getWidth();
    }
    
    public int getHeight() {
        return image.getHeight();
    }
    
    public void recycle(GLES10 gl) {
        for (TexturedRectangle tile: tiles) {
            tile.recycle(gl);
        }
    }
    
    @Override
    public Iterator<TexturedRectangle> getTexturedMeshIterator() {
        return tiles.iterator();
    }

    @Override
    public int getTexturedMeshCount() {
        return tiles.size();
    }

    @Override
    public void invalidateTextures() {
        for (Iterator<TexturedRectangle> it = getTexturedMeshIterator(); it.hasNext(); ) {
            it.next().invalidateTexture();
        }
    }
}
