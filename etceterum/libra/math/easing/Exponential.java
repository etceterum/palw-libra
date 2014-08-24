package etceterum.libra.math.easing;

import etceterum.libra.math.Easing;

// Based on Robert Penner's easing functions, see copyright notice and license inside Easing.java
public abstract class Exponential implements Easing {

    //////////
    
    public static final class EaseIn extends Exponential {
        private static EaseIn INSTANCE = new EaseIn();
        
        public static EaseIn getInstance() {
            return INSTANCE;
        }
        
        private EaseIn() {
            // do not instantiate me
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            return t == 0 ? b : c*Math.pow(2, 10*(t/d - 1)) + b;
        }
        
    }
    
    //////////
    
    public static final class EaseOut extends Exponential {
        private static EaseOut INSTANCE = new EaseOut();
        
        public static EaseOut getInstance() {
            return INSTANCE;
        }
        
        private EaseOut() {
            // do not instantiate me
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            return t == d ? b + c : c*(-Math.pow(2, -10*t/d) + 1) + b;
        }
        
    }
    
    //////////
    
    public static final class EaseInOut extends Exponential {
        private static EaseInOut INSTANCE = new EaseInOut();
        
        public static EaseInOut getInstance() {
            return INSTANCE;
        }
        
        private EaseInOut() {
            // do not instantiate me
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            if (t == 0) {
                return b;
            }
            if (t == d) {
                return b + c;
            }
            if ((t /= d*0.5) < 1)
            {
                return c*0.5*Math.pow(2, 10*(t - 1)) + b;
            }
            return c*0.5*(-Math.pow(2, -10*(--t)) + 2) + b;
        }
        
    }
    
    //////////

}
