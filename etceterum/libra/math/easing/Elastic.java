package etceterum.libra.math.easing;

import etceterum.libra.math.Const;
import etceterum.libra.math.Easing;

// Based on Robert Penner's easing functions, see copyright notice and license inside Easing.java
public abstract class Elastic implements Easing {
    //////////
    
    protected double p = 0;
    protected double a = 0;
    
    protected Elastic() {
        // no-op
    }
    
    protected Elastic(double p, double a) {
        setP(p).setA(a);
    }

    //////////
    
    public Elastic setP(double value) {
        p = value;
        return this;
    }
    
    public double getP() {
        return p;
    }
    
    public Elastic setA(double value) {
        a = value;
        return this;
    }
    
    public double getA() {
        return a;
    }
    
    //////////
    
    public static final class EaseIn extends Elastic {
        private static EaseIn INSTANCE = new EaseIn();
        
        public static EaseIn getInstance() {
            return INSTANCE;
        }
        
        private EaseIn() {
            // do not instantiate me
        }
        
        public EaseIn(double p, double a) {
            super(p, a);
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            if (t == 0) {
                return b;
            }
            if ((t /= d) == 1) {
                return b + c;
            }
            final double pp = 0 == p ? d*0.3 : p;
            double aa = a;
            double s;
            if (0 == aa || aa < Math.abs(c)) {
                aa = c;
                s = pp*0.25;
            }
            else {
                s = pp/Const.TWO_PI*Math.asin(c/aa);
            }

            return -(aa*Math.pow(2, 10*(--t))*Math.sin((t*d - s)*(2*Math.PI)/pp)) + b;
        }
        
    }
    
}
