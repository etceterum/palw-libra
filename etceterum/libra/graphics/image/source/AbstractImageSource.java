package etceterum.libra.graphics.image.source;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageSource;

public abstract class AbstractImageSource implements ImageSource {
    public final boolean hasImage() {
        return null != getImage();
    }
    public final int getImageWidth() {
        final Image image = getImage();
        return null == image ? 0 : image.getWidth();
    }
    public final int getImageHeight() {
        final Image image = getImage();
        return null == image ? 0 : image.getHeight();
    }
}
