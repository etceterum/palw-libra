// Copyright 2012 Et Ceterum, LLC
// http://etceterum.com
//  
// (ek): the compileSourceImage() method is a port of do_9patch() and various supporting routines from Android source code
// (the actual code used as the source was found at:
// http://gitorious.org/0xdroid/frameworks_base/blobs/771532a9bbbe36268fa58d275d8f6567c28aa5d4/tools/aapt/Images.cpp)
//
// Original copyright:
// Copyright 2006 The Android Open Source Project

package etceterum.libra.graphics.npatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.op.Crop;

public final class NPatchCompiler {
    private static final int    TICK_START      = 0;
    private static final int    TICK_INSIDE_1   = 1;
    private static final int    TICK_OUTSIDE_1  = 2;
    
    private static final int    BPP             = Image.BYTES_PER_PIXEL;
    private static final byte   BYTE_FF         = (byte)0xFF;
    
    // (ek): enables debug printouts if set to true:
    private final boolean noisy;
    
    public NPatchCompiler() {
        this(false);
    }
    
    public NPatchCompiler(boolean noisy) {
        this.noisy = noisy;
    }

    public NPatchInfo compileSource(Image image) throws NPatchException {
        assert null != image;
        return compileSource(image.getData(), image.getWidth(), image.getHeight(), image.getOffset(), image.getStride());
    }
    
    private NPatchInfo compileSource(byte[] rgba, int w, int h, int offset, int stride) throws NPatchException {
        // Validate size...
        if (w < 3 || h < 3) {
            throw new NPatchException("Image must be at least 3x3 (1x1 without frame) pixels");
        }
        
        final boolean transparent = 0 == rgba[3 + offset];
        
        // Validate frame...
        if (!transparent && (rgba[0 + offset] != BYTE_FF || rgba[1 + offset] != BYTE_FF || rgba[2 + offset] != BYTE_FF || rgba[3 + offset] != BYTE_FF)) {
            throw new NPatchException("Must have one-pixel frame that is either transparent or white");
        }
        
        // Find left and right of sizing areas...
        final int[] xDivs = getHorizontalTicks(rgba, offset, w, transparent, true, true);
        assert xDivs.length >= 2 && 0 == (xDivs.length & 1);
        
        // Find top and bottom of sizing areas...
        final int[] yDivs = getVerticalTicks(rgba, offset, stride, h, transparent, true, true);
        assert yDivs.length >= 2 && 0 == (yDivs.length & 1);
        
        // Find left and right of padding area...
        final int[] xPadding = getHorizontalTicks(rgba, offset + stride*(h - 1), w, transparent, false, false);
        int paddingLeft, paddingRight;
        // If padding is not yet specified, take values from size.
        if (0 == xPadding.length) {
            paddingLeft = xDivs[0];
            paddingRight = w - 2 - xDivs[1];
        }
        else {
            assert 2 == xPadding.length;
            paddingLeft = xPadding[0];
            // Adjust value to be correct!
            paddingRight = w - 2 - xPadding[1];
        }
        
        // Find top and bottom of padding area...
        final int[] yPadding = getVerticalTicks(rgba, offset + w - 1, stride, h, transparent, false, false);
        int paddingTop, paddingBottom;
        // If padding is not yet specified, take values from size.
        if (0 == yPadding.length) {
            paddingTop = yDivs[0];
            paddingBottom = h - 2 - yDivs[1];
        }
        else {
            assert 2 == yPadding.length;
            paddingTop = yPadding[0];
            // Adjust value to be correct!
            paddingBottom = h - 2 - yPadding[1];
        }
        
        if (noisy) {
            System.out.println("Size ticks: x0=" + xDivs[0] + ", x1=" + xDivs[1] + ", y0=" + yDivs[0] + ", y1=" + yDivs[1]);
            System.out.println("Padding ticks: l=" + paddingLeft + ", r=" + paddingRight + ", t=" + paddingTop + ", b=" + paddingBottom);
        }
        
        final int numXDivs = xDivs.length, numYDivs = yDivs.length;
        if (noisy) {
            System.out.println("numXDivs=" + numXDivs + ", numYDivs=" + numYDivs);
        }

        // (ek): The code below is modified from the original taking into account the fact that,
        //       in the original code, the image has at this point been modified to remove the 1px border
        //       around it; we do not modify the original image and instead provide a separate method
        //       for border removal
        final int w2 = w - 2, h2 = h - 2;
        
        // Figure out the number of rows and columns in the N-patch
        int numCols = numXDivs + 1;
        if (xDivs[0] == 0) { // Column 1 is stretchable
            --numCols;
        }
        if (xDivs[numXDivs - 1] == w2) {
            --numCols;
        }
        int numRows = numYDivs + 1;
        if (yDivs[0] == 0) { // Row 1 is stretchable
            --numRows;
        }
        if (yDivs[numYDivs - 1] == h2) {
            --numRows;
        }
        
        if (noisy) {
            System.out.println("numCols=" + numCols + ", numRows=" + numRows);
        }
        
        final int numColors = numRows*numCols;
        final int[] colors = new int[numColors];
        
        // Fill in color information for each patch.
        
        int top = 0, left, right, bottom;

        // The first row always starts with the top being at y=0 and the bottom
        // being either yDivs[1] (if yDivs[0]=0) of yDivs[0].  In the former case
        // the first row is stretchable along the Y axis, otherwise it is fixed.
        // The last row always ends with the bottom being bitmap.height and the top
        // being either yDivs[numYDivs-2] (if yDivs[numYDivs-1]=bitmap.height) or
        // yDivs[numYDivs-1]. In the former case the last row is stretchable along
        // the Y axis, otherwise it is fixed.
        //
        // The first and last columns are similarly treated with respect to the X
        // axis.
        //
        // The above is to help explain some of the special casing that goes on the
        // code below.

        boolean hasColor = false;
        int colorIndex = 0;
        
        // The initial yDiv and whether the first row is considered stretchable or
        // not depends on whether yDiv[0] was zero or not.
        for (int j = (yDivs[0] == 0 ? 1 : 0); j <= numYDivs && top < h2; ++j) {
            bottom = j == numYDivs ? h2 : yDivs[j];
            left = 0;
            // The initial xDiv and whether the first column is considered
            // stretchable or not depends on whether xDiv[0] was zero or not.
            for (int i = xDivs[0] == 0 ? 1 : 0; i <= numXDivs && left < w2; ++i) {
                right = i == numXDivs ? w2 : xDivs[i]; 
                final int c = getColor(rgba, offset, stride, left + 1, top + 1, right, bottom);
                colors[colorIndex++] = c;
                if (noisy) {
                    if (c != NPatchInfo.NO_COLOR) {
                        hasColor = true;
                    }
                }
                left = right;
            }
            top = bottom;
        }
        
        assert colorIndex == numColors;
        
        if (noisy && hasColor) {
            System.out.println("Colors:");
            for (int i = 0; i < numColors; ++i) {
                System.out.print(String.format(" #%08x", colors[i]));
            }
            System.out.println();
        }
        
        
        return new NPatchInfo(xDivs, yDivs, colors, paddingLeft, paddingRight, paddingTop, paddingBottom, false);
    }
    
    public Image stripSourceBorder(Image image) throws NPatchException {
        assert null != image;
        
        final int w = image.getWidth(), h = image.getHeight();
        // Validate size...
        if (w < 3 || h < 3) {
            throw new NPatchException("Image must be at least 3x3 (1x1 without frame) pixels");
        }
        
        //return image.createSubimage(1, 1, w - 2, h - 2);
        return new Crop(1).execute(image);
    }
    
    // port of get_horizontal_ticks()
    private int[] getHorizontalTicks(byte[] rgba, int start, int width, boolean transparent, boolean required, boolean multipleAllowed) throws NPatchException {
        ArrayList<Integer> divs = new ArrayList<Integer>(width);
        int state = TICK_START;
        boolean found = false;
        
        for (int i = 1; i < width - 1; ++i) {
            if (isTick(rgba, start + i, transparent)) {
                if (TICK_START == state || (TICK_OUTSIDE_1 == state && multipleAllowed)) {
                    assert 0 == (divs.size() & 1);
                    divs.add(i - 1);
                    found = true;
                    state = TICK_INSIDE_1;
                }
                else if (TICK_OUTSIDE_1 == state) {
                    // outLeft = i;
                    throw new NPatchException("Can't have more than one marked region along edge");
                }
            }
            else {
                if (TICK_INSIDE_1 == state) {
                    // We're done with this div.  Move on to the next.
                    assert 1 == (divs.size() & 1);
                    divs.add(i - 1);
                    state = TICK_OUTSIDE_1;
                }
            }
        }
        
        if (required && !found) {
            throw new NPatchException("No marked region found along edge");
        }
        
        if (1 == (divs.size() & 1)) {
            divs.add(width - 2);
        }
        
        return intListToArray(divs);
    }
    
    private int[] getVerticalTicks(byte[] rgba, int start, int stride, int height, boolean transparent, boolean required, boolean multipleAllowed) throws NPatchException {
        ArrayList<Integer> divs = new ArrayList<Integer>(height);
        int state = TICK_START;
        boolean found = false;
        
        for (int i = 1; i < height - 1; ++i) {
            if (isTick(rgba, start + i*stride, transparent)) {
                if (TICK_START == state || (TICK_OUTSIDE_1 == state && multipleAllowed)) {
                    assert 0 == (divs.size() & 1);
                    divs.add(i - 1);
                    found = true;
                    state = TICK_INSIDE_1;
                }
                else if (TICK_OUTSIDE_1 == state) {
                    // outLeft = i;
                    throw new NPatchException("Can't have more than one marked region along edge");
                }
            }
            else {
                if (TICK_INSIDE_1 == state) {
                    // We're done with this div.  Move on to the next.
                    assert 1 == (divs.size() & 1);
                    divs.add(i - 1);
                    state = TICK_OUTSIDE_1;
                }
            }
        }
        
        if (required && !found) {
            throw new NPatchException("No marked region found along edge");
        }
        
        if (1 == (divs.size() & 1)) {
            divs.add(height - 2);
        }
        
        return intListToArray(divs);
    }
    
    // port of is_tick()
    private boolean isTick(byte[] rgba, int pixelIndex, boolean transparent) throws NPatchException {
        final int i = pixelIndex*BPP;
        final int r = rgba[i], g = rgba[i + 1], b = rgba[i + 2], a = rgba[i + 3];
        if (transparent) {
            if (0 == a) {
                return false;
            }
            if (BYTE_FF != a) {
                throw new NPatchException("Frame pixels must be either solid or transparent (not intermediate alphas)");
            }
            if (0 != r || 0 != g || 0 != b) {
                throw new NPatchException("Ticks in transparent frame must be black");
            }
            return true;
        }
        if (BYTE_FF != a) {
            throw new NPatchException("White frame must be a solid color (no alpha)");
        }
        if (BYTE_FF == r && BYTE_FF == g && BYTE_FF == b) {
            return false;
        }
        if (0 != r || 0 != g || 0 != b) {
            throw new NPatchException("Ticks in white frame must be black");
        }
        return true;
    }
    
    private int getColor(byte[] rgba, int offset, int stride, int left, int top, int right, int bottom) {
        int index = (offset + top*stride + left)*BPP;
        final byte r = rgba[index], g = rgba[1 + index], b = rgba[2 + index], a = rgba[3 + index];

        if (left > right || top > bottom) {
            return NPatchInfo.TRANSPARENT_COLOR;
        }

        while (top <= bottom) {
            for (int i = left; i <= right; i++) {
                index = (offset + top*stride + i)*BPP;
                final byte aa = rgba[3 + index]; 
                if (a == 0) {
                    if (aa != 0) {
                        return NPatchInfo.NO_COLOR;
                    }
                } 
                else {
                    final byte rr = rgba[index], gg = rgba[1 + index], bb = rgba[2 + index];
                    if (rr != r || gg != g || bb != b || aa != a) {
                        return NPatchInfo.NO_COLOR;
                    }
                }
            }
            ++top;
        }

        if (a == 0) {
            return NPatchInfo.TRANSPARENT_COLOR;
        }
        
        // (ek): Note: N-patch (9-patch) color format (returned below) is ARGB 
        //       which is different from the format of the input Image (RGBA)
        return (((int)a & 0xFF) << 24) | (((int)r & 0xFF) << 16) | (((int)g & 0xFF) << 8) | ((int)b & 0xFF);
    }
    
    private int[] intListToArray(List<Integer> list) {
        final int[] array = new int[list.size()];
        final Iterator<Integer> listIt = list.iterator();
        for (int i = 0; i != array.length; ++i) {
            array[i] = listIt.next();
        }
        return array;
    }
}
