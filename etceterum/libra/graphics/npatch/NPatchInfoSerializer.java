package etceterum.libra.graphics.npatch;

public final class NPatchInfoSerializer {
    private static final int INT32_SIZE = Integer.SIZE/8; 
    
    // port of Res_png_9patch::serializedSize()
    public int getSerializedSize(NPatchInfo npi) {
        assert null != npi;
        
        // The size of this struct is 32 bytes on the 32-bit target system
        // 4 * int8_t
        // 4 * int32_t
        // 3 * pointer
        return 32 + (npi.getNumXDivs() + npi.getNumYDivs() + npi.getNumColors())*INT32_SIZE;
    }
    
    // Res_png_9patch::serialize()
    public byte[] serialize(NPatchInfo npi) {
        assert null != npi;
        
        final byte[] chunk = new byte[getSerializedSize(npi)];
        final int[] xDivs = npi.getXDivs();
        final int[] yDivs = npi.getYDivs();
        final int[] colors = npi.getColors();
        
        int i = 0;
        chunk[i++] = (byte)(npi.getWasDeserialized() ? 1 : 0);
        chunk[i++] = (byte)xDivs.length;
        chunk[i++] = (byte)yDivs.length;
        chunk[i++] = (byte)colors.length;
        
        setInt(chunk, i, npi.getPaddingLeft()); 
        i += INT32_SIZE;
        setInt(chunk, i, npi.getPaddingRight()); 
        i += INT32_SIZE;
        setInt(chunk, i, npi.getPaddingTop()); 
        i += INT32_SIZE;
        setInt(chunk, i, npi.getPaddingBottom()); 
        i += INT32_SIZE;
        
        System.arraycopy(xDivs, 0, chunk, i, xDivs.length);
        i += INT32_SIZE*xDivs.length;
        System.arraycopy(yDivs, 0, chunk, i, yDivs.length);
        i += INT32_SIZE*yDivs.length;
        System.arraycopy(colors, 0, chunk, i, colors.length);
        
        return chunk;
    }
    
    // Res_png_9patch::deserialize()
    public NPatchInfo deserialize(byte[] chunk) {
        assert null != chunk;
        
        int i = 0;
        final boolean wasDeserialized = 0 != chunk[i++];
        final int numXDivs = getUByte(chunk[i++]);
        final int numYDivs = getUByte(chunk[i++]);
        final int numColors = getUByte(chunk[i++]);
        final int paddingLeft = getInt(chunk, i);
        i += INT32_SIZE;
        final int paddingRight = getInt(chunk, i);
        i += INT32_SIZE;
        final int paddingTop = getInt(chunk, i);
        i += INT32_SIZE;
        final int paddingBottom = getInt(chunk, i);
        i += INT32_SIZE;
        
        final int[] xDivs = new int[numXDivs];
        System.arraycopy(chunk, i, xDivs, 0, INT32_SIZE*xDivs.length);
        i += INT32_SIZE*xDivs.length;
        final int[] yDivs = new int[numYDivs];
        System.arraycopy(chunk, i, yDivs, 0, INT32_SIZE*yDivs.length);
        i += INT32_SIZE*yDivs.length;
        final int[] colors = new int[numColors];
        System.arraycopy(chunk, i, colors, 0, INT32_SIZE*colors.length);
        
        return new NPatchInfo(xDivs, yDivs, colors, paddingLeft, paddingRight, paddingTop, paddingBottom, wasDeserialized);
    }
    
    private static void setInt(byte[] chunk, int offset, int value) {
        chunk[0 + offset] = (byte)(value >> 24);
        chunk[1 + offset] = (byte)(value >> 16);
        chunk[2 + offset] = (byte)(value >> 8);
        chunk[3 + offset] = (byte)value;
    }
    
    private static int getInt(byte[] chunk, int offset) {
        return (chunk[0 + offset] << 24) | (chunk[1 + offset] << 16) | (chunk[2 + offset] << 8) | chunk[3 + offset]; 
    }
    
    private static int getUByte(byte b) {
        return b >= 0 ? b : 256 + b;
    }
    
    /*
    // http://www.libpng.org/pub/png/book/chapter08.html#png.ch08.div.2
    private static final int PNG_1 = 0x89504E47; // { 137, 'P', 'N', 'G' }
    private static final int PNG_2 = 0x0D0A1A0A; // { CR, LF, ^Z, LF }
    
    private NPatchSerializer() {
        // prevent instantiation
    }
    
    public NPatchInfo deserializeNPatch(InputStream png) throws IOException, NPatchException {
        assert null != png;
        
        if (PNG_1 != readInt(png) || PNG_2 != readInt(png)) {
            throw new NPatchException("Invalid PNG signature");
        }
        
        for (;;) {
            final int 
        }
    }
    
    private static int readInt(InputStream is) throws IOException {
        final int v1 = is.read();
        final int v2 = is.read();
        final int v3 = is.read();
        final int v4 = is.read();
        
        return ((v1 & 0xFF) << 24) | ((v2 & 0xFF) << 16) | ((v3 & 0xFF) << 8) | (v4 & 0xFF);
    }
    */
}
