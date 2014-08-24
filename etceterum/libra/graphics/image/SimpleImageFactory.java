package etceterum.libra.graphics.image;

// simple proxy for new Image(...)
public final class SimpleImageFactory implements ImageFactory {
    private static final SimpleImageFactory instance = new SimpleImageFactory();
    
    public static SimpleImageFactory getInstance() {
        return instance;
    }
    
    private SimpleImageFactory() {
        // do not instantiate me
    }

    @Override
    public Image createImage(int width, int height) {
        return new Image(width, height);
    }

}
