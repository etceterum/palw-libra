package etceterum.libra.graphics.image;

// does not throw OutOfMemoryError, returns null if failed
public final class SafeImageFactory implements ImageFactory {
    private static final SafeImageFactory instance = new SafeImageFactory();
    
    public static SafeImageFactory getInstance() {
        return instance;
    }
    
    private SafeImageFactory() {
        // do not instantiate me
    }

    @Override
    public Image createImage(int width, int height) {
        try {
            return new Image(width, height);
        }
        catch (Throwable e) {
            // no-op
        }
        return null;
    }
}
