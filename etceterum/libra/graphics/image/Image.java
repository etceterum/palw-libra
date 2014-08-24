package etceterum.libra.graphics.image;

import java.nio.ByteBuffer;

/**
 * Pixels are stored in the RGBA format, i.e. in each quad the first byte is Red, second is Green, third is Blue and fourth is Alpha
 */
public final class Image {
    public static final ImageFactory DEFAULT_FACTORY = SimpleImageFactory.getInstance();
    public static int BYTES_PER_PIXEL = 4;
    private final byte[] data;
    private final int width, height;
    private final int offset, stride;
    private final ByteBuffer buffer;

    /**
     * Wraps the given array of RGBA pixels in an Image object
     * @param data Pixels data, already in RGBA format
     * @param width Width of the image in pixels
     * @param height Height of the image in pixels
     * @param offset Offset, in pixels, of the image's data from data[0]
     * @param stride Stride, in pixels, between two adjacent rows of the image
     */
    private Image(byte[] data, int width, int height, int offset, int stride) {
        assert width >= 0;
        assert height >= 0;
        assert offset >= 0;
        assert stride >= 0;
        
        /*
        final boolean isEmpty = 0 == width || 0 == height;
        if (isEmpty) {
            assert null == data || 0 == data.length;
            this.data = new byte[0];
            this.width = this.height = this.offset = this.stride = 0;
        }
        else {
            assert null != data;
            assert offset < data.length;
            assert stride >= width;
            assert (offset + stride*height)*BYTES_PER_PIXEL <= data.length;
            
            this.data = data;
            this.width = width;
            this.height = height;
            this.offset = offset;
            this.stride = stride;
        }
        */
        this.data = null == data ? new byte[0] : data;
        this.width = width;
        this.height = height;
        this.offset = offset;
        this.stride = stride;
        
        buffer = 0 == offset && stride == width && stride*height*BYTES_PER_PIXEL == data.length ? ByteBuffer.wrap(data) : null;
    }
    
    public Image(byte[] data, int width, int height) {
        this(data, width, height, 0, width);
    }
    
    Image(int width, int height) {
        this(new byte[width*height*BYTES_PER_PIXEL], width, height);
    }
    
    public boolean isNormal() {
        //return 0 == offset && stride == width && stride*height*BYTES_PER_PIXEL == data.length;
        return null != buffer;
    }
    
    public Image getNormal(ImageFactory factory) {
        if (isNormal()) {
            return this;
        }
        return getClone(factory);
    }
    
    public Image getClone(ImageFactory factory) {
        final int sourceWidth = width, sourceHeight = height;
        final int targetWidth = width, targetHeight = height;
        final ImageFactory targetFactory = null == factory ? DEFAULT_FACTORY : factory;
        final Image target = targetFactory.createImage(targetWidth, targetHeight);
        if (null == target) {
            return null;
        }
        int sourceIdx = getOffset()*BYTES_PER_PIXEL;
        int targetIdx = 0;
        final int rowLength = sourceWidth*BYTES_PER_PIXEL;
        final int sourceStride = stride*BYTES_PER_PIXEL;
        final int targetStride = targetWidth*BYTES_PER_PIXEL;
        final byte[] sourceData = data;
        final byte[] targetData = target.getData();
        
        for (int row = 0; row < sourceHeight; ++row, sourceIdx += sourceStride, targetIdx += targetStride) {
            System.arraycopy(sourceData, sourceIdx, targetData, targetIdx, rowLength);
        }
        
        return target;
    }
    
    public ByteBuffer getBuffer() {
        //final Image proxy = getNormal();
        //return null == proxy.data ? null : ByteBuffer.wrap(proxy.data);
        return buffer;
    }
    
    public Image getSlice(int sliceX, int sliceY, int sliceWidth, int sliceHeight) {
        final int sourceWidth = width, sourceHeight = height;
        
        final int targetX = sliceX < 0 ? 0 : sliceX > sourceWidth ? sourceWidth : sliceX;
        final int targetY = sliceY < 0 ? 0 : sliceY > sourceHeight ? sourceHeight : sliceY;
        final int targetWidth = sliceWidth < 0 || targetX + sliceWidth > sourceWidth ? sourceWidth - targetX : sliceWidth;
        final int targetHeight = sliceHeight < 0 || targetY + sliceHeight > sourceHeight ? sourceHeight - targetY : sliceHeight;
        
        if (0 == targetX && 0 == targetY && sourceWidth == targetWidth && sourceHeight == targetHeight) {
            return this;
        }
        
        final int targetOffset = offset + targetY*stride + targetX;
        final int targetStride = stride;
        
        return new Image(data, targetWidth, targetHeight, targetOffset, targetStride);
    }
    
    public byte[] getData() {
        return data;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    // in pixels
    public int getOffset() {
        return offset;
    }
    
    // in pixels
    public int getStride() {
        return stride;
    }
    
    public int getBytesPerPixel() {
        return BYTES_PER_PIXEL;
    }
}
