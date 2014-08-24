package etceterum.libra.math.easing;

import etceterum.libra.math.Const;
import etceterum.libra.math.Easing;

// Based on Robert Penner's easing functions, see copyright notice and license inside Easing.java
public abstract class Sine implements Easing {

    //////////
    
    public static final class EaseIn extends Sine {
        private static EaseIn INSTANCE = new EaseIn();
        
        public static EaseIn getInstance() {
            return INSTANCE;
        }
        
        private EaseIn() {
            // do not instantiate me
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            return -c*Math.cos(t/d*Const.HALF_PI) + c + b;
        }
        
    }
    
    //////////
    
    public static final class EaseOut extends Sine {
        private static EaseOut INSTANCE = new EaseOut();
        
        public static EaseOut getInstance() {
            return INSTANCE;
        }
        
        private EaseOut() {
            // do not instantiate me
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            return c*Math.sin(t/d*Const.HALF_PI) + b;
        }
        
    }
    
    //////////
    
    public static final class EaseInOut extends Sine {
        private static EaseInOut INSTANCE = new EaseInOut();
        
        public static EaseInOut getInstance() {
            return INSTANCE;
        }
        
        private EaseInOut() {
            // do not instantiate me
        }
        
        @Override
        public double ease(double t, double b, double c, double d) {
            return -c*0.5*(Math.cos(Math.PI*t/d) - 1) + b;
        }
        
    }
    
    //////////

}
