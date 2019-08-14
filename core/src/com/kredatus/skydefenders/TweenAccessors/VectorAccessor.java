// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.skydefenders.TweenAccessors;


import com.badlogic.gdx.math.Vector2;
import aurelienribon.tweenengine.TweenAccessor;

public class VectorAccessor implements TweenAccessor<Vector2>{

    public static final int XY = 0, X = 1, Y = 2;

    @Override
    public int getValues(Vector2 target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case XY:
                returnValues[0] = target.x;
                returnValues[1] = target.y;
                return 2;
            case X:
                returnValues[0] = target.x;
                return 1;
            case Y:
                returnValues[0] = target.y;
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Vector2 target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case XY:
                target.set(newValues[0],newValues[1]);
                break;
            case X:
                target.x = newValues[0];
                break;
            case Y:
                target.y = newValues[1];
                break;
            default:
                assert false;
                break;
        }
    }
}