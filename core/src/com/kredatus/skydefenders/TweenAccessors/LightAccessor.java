package com.kredatus.skydefenders.TweenAccessors;

import aurelienribon.tweenengine.TweenAccessor;
import box2dLight.Light;

    public class LightAccessor implements TweenAccessor<Light> {

        public static final int dist = 1;

        @Override
        public int getValues(Light target, int tweenType, float[] returnValues) {
            switch (tweenType) {
                case dist:
                    returnValues[0] = target.getDistance();
                    return 1;
                default:
                    assert false;
                    return -1;
            }
        }

        @Override
        public void setValues(Light target, int tweenType, float[] newValues) {
            switch (tweenType) {
                case dist:
                    target.setDistance(newValues[0]);
                    break;
                default:
                    assert false;
                    break;
            }
        }
    }
