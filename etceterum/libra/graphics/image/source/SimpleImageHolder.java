package etceterum.libra.graphics.image.source;

import etceterum.libra.graphics.image.Image;

public final class SimpleImageHolder extends AbstractImageSource implements ImageHolder {
    private Image image;
    
    public SimpleImageHolder() {
        resetImage();
    }
    
    public SimpleImageHolder(Image image) {
        setImage(image);
    }
    
    @Override
    public void resetImage() {
        setImage(null);
    }
    
    public SimpleImageHolder setImage(Image image) {
        this.image = image;
        return this;
    }
    
    @Override
    public Image getImage() {
        return image;
    }
}
