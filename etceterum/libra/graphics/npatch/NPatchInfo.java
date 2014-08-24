package etceterum.libra.graphics.npatch;

// http://stackoverflow.com/questions/6510467/android-compiling-9-patch-files-to-be-used-outside-of-the-drawable-folder
// https://scm.sipfoundry.org/rep/sipX/main/sipXmediaLib/contrib/android/android_2_0_headers/frameworks/base/include/utils/ResourceTypes.h
public final class NPatchInfo {
    // The 9 patch segment is not a solid color
    public static final int NO_COLOR            = 0x00000001;
    // The 9 patch segment is completely transparent.
    public static final int TRANSPARENT_COLOR   = 0x00000000;
    
    private final int[] xDivs;
    private final int[] yDivs;
    private final int[] colors;
    private final int paddingLeft, paddingRight;
    private final int paddingTop, paddingBottom;
    private final boolean wasDeserialized;
    
    public NPatchInfo(int[] xDivs, int[] yDivs, int[] colors, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom, boolean wasDeserialized) {
        assert null != xDivs && xDivs.length >= 2;
        assert null != yDivs && yDivs.length >= 2;
        assert null != colors && colors.length >= 1;
        assert paddingLeft >= 0;
        assert paddingRight >= 0;
        assert paddingTop >= 0;
        assert paddingBottom >= 0;
        
        this.xDivs = xDivs;
        this.yDivs = yDivs;
        this.colors = colors;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.wasDeserialized = wasDeserialized;
    }
    
    public int getNumXDivs() {
        return xDivs.length;
    }
    
    public int getNumYDivs() {
        return yDivs.length;
    }
    
    public int getNumColors() {
        return colors.length;
    }
    
    public int getPaddingLeft() {
        return paddingLeft;
    }
    
    public int getPaddingRight() {
        return paddingRight;
    }
    
    public int getPaddingTop() {
        return paddingTop;
    }
    
    public int getPaddingBottom() {
        return paddingBottom;
    }
    
    public boolean getWasDeserialized() {
        return wasDeserialized;
    }
    
    public int[] getXDivs() {
        return xDivs;
    }
    
    public int[] getYDivs() {
        return yDivs;
    }
    
    public int[] getColors() {
        return colors;
    }
}
