package etceterum.libra.math.easing;

import etceterum.libra.math.Easing;

// Based on Robert Penner's easing functions, see copyright notice and license inside Easing.java
public abstract class TwoWay implements Easing {
	
	//////////
	
	public static final class Quadratic extends TwoWay {
		private static final Quadratic INSTANCE = new Quadratic();
		
		private Quadratic() {
			// to prevent instantiation
		}
		
		public static Quadratic getInstance() {
			return INSTANCE;
		}
		
		@Override
		public double ease(double t, double b, double c, double d) {
			t = 1 - 2*t/d;
			return t*t*c + b;
		}
		
	}
	
	//////////
	
}
