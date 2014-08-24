package etceterum.libra;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public final class Buffers {
    private Buffers() {
        // don't
    }
    
    public static FloatBuffer newFloatBuffer(int valueCount) {
        final ByteBuffer bb = ByteBuffer.allocateDirect(valueCount*(Float.SIZE/8));
        bb.order(ByteOrder.nativeOrder());
        return bb.asFloatBuffer();
    }
    
    public static ShortBuffer newShortBuffer(int valueCount) {
        final ByteBuffer bb = ByteBuffer.allocateDirect(valueCount*(Short.SIZE/8));
        bb.order(ByteOrder.nativeOrder());
        return bb.asShortBuffer();
    }
    
    public static FloatBuffer wrapFloatBuffer(float[] values) {
        final FloatBuffer fb = newFloatBuffer(values.length);
        for (int i = 0; i != values.length; ++i) {
            fb.put(values[i]);
        }
        fb.rewind();
        return fb;
    }
    
    public static ShortBuffer wrapShortBuffer(short[] values) {
        final ShortBuffer sb = newShortBuffer(values.length);
        for (int i = 0; i != values.length; ++i) {
            sb.put(values[i]);
        }
        sb.rewind();
        return sb;
    }
}
