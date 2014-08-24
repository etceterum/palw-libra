package etceterum.libra.graphics.image.quantization;

public interface Selector {
    boolean select(byte[] rgb, int offset);
}
