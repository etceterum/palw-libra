package etceterum.libra.math.easing;

import etceterum.libra.math.Easing;

// Based on Robert Penner's easing functions, see copyright notice and license inside Easing.java
public abstract class Back implements Easing {
    
    protected double s = 1.70158;
    
    protected Back() {
        // no-op
    }
    
    protected Back(double s) {
        setS(s);
    }
    
    public Back setS(double s) {
        this.s = s;
        return this;
    }
    
    public double getS() {
        return s;
    }

    //////////
    
    public static final class EaseIn extends Back {
        private static EaseIn INSTANCE = new EaseIn();
        
        public static EaseIn getInstance() {
            return INSTANCE;
        }
        
        private EaseIn() {
            // do not instantiate me
        }
        
        public EaseIn(double s) {
            super(s);
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            return c*(t /= d)*t*((s + 1)*t - s) + b;
        }
        
    }
    
    //////////
    
    public static final class EaseOut extends Back {
        private static EaseOut INSTANCE = new EaseOut();
        
        public static EaseOut getInstance() {
            return INSTANCE;
        }
        
        private EaseOut() {
            // do not instantiate me
        }
        
        public EaseOut(double s) {
            super(s);
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            return c*((t = t/d - 1)*t*((s + 1)*t + s) + 1) + b;
        }
        
    }
    
    //////////
    
    public static final class EaseInOut extends Back {
        private static EaseInOut INSTANCE = new EaseInOut();
        
        public static EaseInOut getInstance() {
            return INSTANCE;
        }
        
        private EaseInOut() {
            // do not instantiate me
        }
        
        public EaseInOut(double s) {
            super(s);
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            if ((t /= d*0.5) < 1) {
                return c*0.5*(t*t*(((s *= (1.525)) + 1)*t - s)) + b;
            }
            return c*0.5*((t -= 2)*t*(((s *= (1.525)) + 1)*t + s) + 2) + b;
        }
        
    }
    
    //////////

}
