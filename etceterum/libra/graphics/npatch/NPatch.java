package etceterum.libra.graphics.npatch;


import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.op.Slice;

public final class NPatch {
    private static final boolean NOISY = false;
    
    private final Image image;
    private final NPatchInfo info;
    private final int[] xSpans, ySpans;
    private final int nonStretchableWidth, nonStretchableHeight;
    
    public NPatch(Image image, NPatchInfo info) {
        assert null != image;
        assert null != info;
        
        this.image = image;
        this.info = info;
        
        final int[] xDivs = info.getXDivs();
        nonStretchableWidth = calculateSpans(xDivs, xSpans = new int[xDivs.length + 2], getWidth());
        assert nonStretchableWidth <= getWidth();
        final int[] yDivs = info.getYDivs();
        nonStretchableHeight = calculateSpans(info.getYDivs(), ySpans = new int[yDivs.length + 2], getHeight());
        assert nonStretchableHeight <= getHeight();
        
        if (NOISY) {
            System.out.println("NPatch: W=" + getWidth() + "(NS:" + nonStretchableWidth + ", S:" + getStretchableWidth() + "), H=" + getHeight() + "(NS:" + nonStretchableHeight + ", S:" + getStretchableHeight() + ")");
        }
    }
    
    public int getWidth() {
        return image.getWidth();
    }
    
    public int getHeight() {
        return image.getHeight();
    }
    
    public int getStretchableWidth() {
        return getWidth() - nonStretchableWidth;
    }
    
    public int getNonStretchableWidth() {
        return nonStretchableWidth;
    }
    
    public int getStretchableHeight() {
        return getHeight() - nonStretchableHeight;
    }
    
    public int getNonStretchableHeight() {
        return nonStretchableHeight;
    }
    
    public int getPaddingLeft() {
        return info.getPaddingLeft();
    }
    
    public int getPaddingRight() {
        return info.getPaddingRight();
    }
    
    public int getPaddingTop() {
        return info.getPaddingTop();
    }
    
    public int getPaddingBottom() {
        return info.getPaddingBottom();
    }
    
    public int getHorizontalPadding() {
        return getPaddingLeft() + getPaddingRight();
    }
    
    public int getContentWidth() {
        return getScaledContentWidth(getWidth());
    }
    
    public int getVerticalPadding() {
        return getPaddingTop() + getPaddingBottom();
    }
    
    public int getContentHeight() {
        return getScaledContentHeight(getHeight());
    }
    
    public int getScaledContentWidth(int fullWidth) {
        return fullWidth - getHorizontalPadding();
    }
    
    public int getScaledContentHeight(int fullHeight) {
        return fullHeight - getVerticalPadding();
    }
    
    public Image getImage() {
        return image;
    }
    
    public NPatchInfo getInfo() {
        return info;
    }
    
    public int[] getXSpans() {
        return xSpans;
    }
    
    public int[] getYSpans() {
        return ySpans;
    }
    
    public Image getContentImage() {
        final int pl = getPaddingLeft(), pr = getPaddingRight(), pt = getPaddingTop(), pb = getPaddingBottom();
        final int w = getWidth(), h = getHeight();
        return new Slice(pl, pt, w - pl - pr, h - pt - pb).execute(image);
    }
    
    private static int calculateSpans(int[] divs, int[] spans, int size) {
        int nonStretchableSize = size;
        for (int i = 0; i < divs.length - 1; ++i) {
            final boolean stretchable = 0 == (i & 1);
            final int length = divs[i + 1] - divs[i];
            if (stretchable) {
                nonStretchableSize -= length;
            }
            spans[i + 1] = divs[i];
        }
        spans[0] = 0;
        spans[spans.length - 2] = divs[divs.length - 1];
        spans[spans.length - 1] = size;
        return nonStretchableSize;
    }
}
