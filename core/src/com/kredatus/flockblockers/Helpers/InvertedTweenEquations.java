

package com.kredatus.flockblockers.Helpers;

import aurelienribon.tweenengine.TweenEquation;

public abstract class InvertedTweenEquations extends TweenEquation {
    private static final float PI = 3.1415927F;

    public static final InvertedTweenEquations CubicInOut = new InvertedTweenEquations() {
        public final float compute(float t) {
            //return -0.5F * ((float)Math.cos((double)(3.1415927F * t)) - 1.0F);
            //return Math.ceil(t)%2 ==1.0 || Math.ceil(t)%2 ==-1.0?  ((float)Math.acos((double)(-2.0F*(t - 0.5F))))/PI -1.5F : ((float)Math.acos((double)(-2.0F*(-t + 1.5F))))/PI -1.5F;
            //return (t *= 2.0F) < 1.0F ? (float) Math.sqrt(2.0F * t) : - 0.5F * ( t * t - 3 * t + 1.0F ) + 1.0F ;
            return 4.0F * (float)Math.pow(t - 0.5, 3) + 0.5F;//4\left(\left(x-0.5\right)\right)^3+0.5
        }
        public String toString() {
            return "InvertedTweenEquations.CUBICINOUT";
        }
    };
    public static final InvertedTweenEquations QuadInOut = new InvertedTweenEquations() {
        public final float compute(float t) {
            return t < 0.5F ? -2.0F * (float)Math.pow(t - 0.5, 2) + 0.5F : 2.0F * (float)Math.pow(t - 0.5, 2) + 0.5F;//-2\left(x-0.5\right)^2+0.5
        }
        public String toString() {
            return "InvertedTweenEquations.QuadInOut";
        }
    };
    public static final InvertedTweenEquations QuadInOutGradual = new InvertedTweenEquations() {
        public final float compute(float t) {
            return t < 0.5F ? -1.0F * (float)Math.pow(t - 0.7, 2) + 0.54F : 1.0F * (float)Math.pow(t - 0.3, 2) + 0.46F;//-2\left(x-0.5\right)^2+0.5
        }
        public String toString() {
            return "InvertedTweenEquations.QuadInOutGradual";
        }
    };
    public static final InvertedTweenEquations QuadInOutVeryGradual = new InvertedTweenEquations() {
        public final float compute(float t) {
            return t < 0.5F ? -1.164F * (float)Math.pow(t - 0.68, 2) + 0.5377F : 1.164F * (float)Math.pow(t - 0.32, 2) + 0.4623F;//-2\left(x-0.5\right)^2+0.5
        }
        public String toString() {
            return "InvertedTweenEquations.QuadInOutVeryGradual";
        }
    };
    public InvertedTweenEquations() {
    }
}

