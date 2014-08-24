package etceterum.libra.graphics.image.source;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageSource;

public class Animation<Source extends ImageSource> implements ImageSource {
    private Source source;
    private int startX, startY;
    private int x, y, width, height;
    private int stepX, stepY;
    
    public Animation() {
        reset();
    }
    
    public Animation(Source source) {
        reset().setSource(source);
    }
    
    public Animation<Source> reset() {
        return resetSource().resetStartX().resetStartY().resetX().resetY().resetWidth().resetHeight().resetStepX().resetStepY();
    }
    
    public Animation<Source> resetSource() {
        return setSource(null);
    }
    
    public Animation<Source> setSource(Source source) {
        this.source = source;
        return this;
    }
    
    public Source getSource() {
        return source;
    }
    
    public Animation<Source> resetStartX() {
        return setStartX(0);
    }
    
    public Animation<Source> setStartX(int startX) {
        this.startX = startX < 0 ? 0 : startX;
        return this;
    }
    
    public int getStartX() {
        return startX;
    }
    
    public Animation<Source> resetStartY() {
        return setStartY(0);
    }
    
    public Animation<Source> setStartY(int startY) {
        this.startY = startY < 0 ? 0 : startY;
        return this;
    }
    
    public int getStartY() {
        return startY;
    }
    
    public Animation<Source> resetX() {
        return setX(startX);
    }
    
    public Animation<Source> setX(int x) {
        this.x = x;
        return this;
    }
    
    public int getX() {
        return x;
    }
    
    public Animation<Source> resetY() {
        return setY(startY);
    }
    
    public Animation<Source> setY(int y) {
        this.y = y;
        return this;
    }
    
    public int getY() {
        return y;
    }
    
    public Animation<Source> resetWidth() {
        return setWidth(getImageWidth());
    }
    
    public Animation<Source> setWidth(int width) {
        final int imageWidth = getImageWidth();
        this.width = width < 0 ? 0 : width > imageWidth ? imageWidth : width;
        return this;
    }
    
    public int getWidth() {
        return width;
    }
    
    public Animation<Source> resetHeight() {
        return setHeight(getImageHeight());
    }
    
    public Animation<Source> setHeight(int height) {
        final int imageHeight = getImageHeight();
        this.height = height < 0 ? 0 : height > imageHeight ? imageHeight : height;
        return this;
    }
    
    public int getHeight() {
        return height;
    }

    public Animation<Source> resetStepX() {
        return setStepX(0);
    }
    
    public Animation<Source> setStepX(int stepX) {
        this.stepX = stepX;
        return this;
    }
    
    public int getStepX() {
        return stepX;
    }
    
    public Animation<Source> resetStepY() {
        return setStepY(0);
    }
    
    public Animation<Source> setStepY(int stepY) {
        this.stepY = stepY;
        return this;
    }
    
    public int getStepY() {
        return stepY;
    }
    
    
    public Animation<Source> start() {
        return resetX().resetY();
    }
    
    public Animation<Source> step() {
        if (null != source) {
            final Image image = source.getImage();
            if (null != image) {
                final int imageWidth = image.getWidth(), imageHeight = image.getHeight();
                x += stepX;
                if (x < 0) {
                    x = Math.max(imageWidth - width, 0);
                } else if (x + width >= imageWidth) {
                    x = 0;
                }
                y += stepY;
                if (y < 0) {
                    y = Math.max(imageHeight - height, 0);
                } else if (y + height >= imageHeight) {
                    y = 0;
                }
            }
        }
        return this;
    }

    @Override
    public Image getImage() {
        if (null == source) {
            return null;
        }
        final Image image = source.getImage();
        return null == image ? null : image.getSlice(x, y, width, height);
    }
    
    public int getImageWidth() {
        if (null == source) {
            return 0;
        }
        final Image image = source.getImage();
        return null == image ? 0 : image.getWidth();
    }
    
    public int getImageHeight() {
        if (null == source) {
            return 0;
        }
        final Image image = source.getImage();
        return null == image ? 0 : image.getHeight();
    }
    
}
