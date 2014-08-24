package etceterum.libra.graphics.image.op;

import etceterum.libra.graphics.image.ImageOpFactory;

public interface SourceOpFactory extends ImageOpFactory {
    @Override
    SourceOp createOp();
}
