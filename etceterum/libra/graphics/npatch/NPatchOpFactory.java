package etceterum.libra.graphics.npatch;

import etceterum.libra.graphics.image.ImageOpFactory;

public interface NPatchOpFactory extends ImageOpFactory {
    @Override
    NPatchOp createOp();
}
