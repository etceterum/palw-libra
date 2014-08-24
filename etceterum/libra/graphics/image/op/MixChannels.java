package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.Image;
import etceterum.libra.graphics.image.ImageOp;

// Port of JHLabs ChannelMixFilter
// https://github.com/axet/jhlabs/blob/master/src/main/java/com/jhlabs/image/ChannelMixFilter.java
// Original copyright:
/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
public final class MixChannels implements ImageOp {
    private int blueGreen, redBlue, greenRed;
    private int intoR, intoG, intoB;
    
    public MixChannels() {
        // no-op
    }

    public MixChannels setBlueGreen(int blueGreen) {
        this.blueGreen = blueGreen;
        return this;
    }

    public int getBlueGreen() {
        return blueGreen;
    }

    public MixChannels setRedBlue(int redBlue) {
        this.redBlue = redBlue;
        return this;
    }

    public int getRedBlue() {
        return redBlue;
    }

    public MixChannels setGreenRed(int greenRed) {
        this.greenRed = greenRed;
        return this;
    }

    public int getGreenRed() {
        return greenRed;
    }

    public MixChannels setIntoR(int intoR) {
        this.intoR = intoR;
        return this;
    }

    public int getIntoR() {
        return intoR;
    }

    public MixChannels setIntoG(int intoG) {
        this.intoG = intoG;
        return this;
    }

    public int getIntoG() {
        return intoG;
    }

    public MixChannels setIntoB(int intoB) {
        this.intoB = intoB;
        return this;
    }

    public int getIntoB() {
        return intoB;
    }

    @Override
    public Image execute(Image input) {
        final Image target = input;
        
        final int targetWidth = target.getWidth();
        final int targetHeight = target.getHeight();
        final int targetStride = target.getStride() << 2;
        final byte[] targetData = target.getData();
        int rowIdx = target.getOffset();
        
        for (int y = 0; y < targetHeight; ++y, rowIdx += targetStride) {
            int colIdx = rowIdx;
            for (int x = 0; x < targetWidth; ++x, colIdx += 4) {
                final int r = (int)targetData[0 + colIdx] & 0xFF;
                final int g = (int)targetData[1 + colIdx] & 0xFF;
                final int b = (int)targetData[2 + colIdx] & 0xFF;
                
                int nr = (intoR*(blueGreen*g + (0xFF - blueGreen)*b)/0xFF + (0xFF - intoR)*r)/0xFF;
                if (nr < 0) nr = 0; else if (nr > 0xFF) nr = 0xFF;
                
                int ng = (intoG*(redBlue*b + (0xFF - redBlue)*r)/0xFF + (0xFF - intoG)*g)/0xFF;
                if (ng < 0) ng = 0; else if (ng > 0xFF) ng = 0xFF;
                
                int nb = (intoB*(greenRed*r + (0xFF - greenRed)*g)/0xFF + (0xFF - intoB)*b)/0xFF;
                if (nb < 0) nb = 0; else if (nb > 0xFF) nb = 0xFF;
            
                targetData[0 + colIdx] = (byte)nr;
                targetData[1 + colIdx] = (byte)ng;
                targetData[2 + colIdx] = (byte)nb;
            }
        }
        
        return target;
    }

}
