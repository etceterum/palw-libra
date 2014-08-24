package etceterum.libra.graphics.image.source;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageSource;

public final class ImageCacher<Source extends ImageSource> extends AbstractImageSource implements ImageHolder {
    private Source source;
    private final SimpleImageHolder holder = new SimpleImageHolder();
    private boolean cached = false;
    
    @Override
    public void resetImage() {
        holder.resetImage();
        cached = false;
    }

    @Override
    public Image getImage() {
        if (cached) {
            return holder.getImage();
        }
        final Image image = source.getImage();
        holder.setImage(image);
        cached = true;
        return image;
    }
}
