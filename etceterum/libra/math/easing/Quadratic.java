package etceterum.libra.math.easing;

import etceterum.libra.math.Easing;

// Based on Robert Penner's easing functions, see copyright notice and license inside Easing.java
public abstract class Quadratic implements Easing {

    //////////
    
    public static final class EaseIn extends Quadratic {
        private static EaseIn INSTANCE = new EaseIn();
        
        public static EaseIn getInstance() {
            return INSTANCE;
        }
        
        private EaseIn() {
            // do not instantiate me
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            return c*(t /= d)*t + b;
        }
        
    }
    
    //////////
    
    public static final class EaseOut extends Quadratic {
        private static EaseOut INSTANCE = new EaseOut();
        
        public static EaseOut getInstance() {
            return INSTANCE;
        }
        
        private EaseOut() {
            // do not instantiate me
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            return -c*(t /= d)*(t - 2) + b;
        }
        
    }
    
    //////////
    
    public static final class EaseInOut extends Quadratic {
        private static EaseInOut INSTANCE = new EaseInOut();
        
        public static EaseInOut getInstance() {
            return INSTANCE;
        }
        
        private EaseInOut() {
            // do not instantiate me
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            if ((t /= d*0.5) < 1) {
                return c * 0.5 * t * t + b;
            }
            return -c*0.5*((--t)*(t - 2) - 1) + b;
        }
        
    }
    
    //////////

}
