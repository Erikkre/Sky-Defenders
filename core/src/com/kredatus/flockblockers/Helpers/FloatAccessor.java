package com.kredatus.flockblockers.Helpers;

import aurelienribon.tweenengine.TweenAccessor;

public class FloatAccessor implements TweenAccessor<Float>{

    @Override
    public void setValues(Float aFloat, int i, float[] floats) {
        floats[0] = aFloat;
    }

    @Override
    public int getValues(Float aFloat, int i, float[] floats) {
        floats[0] = aFloat;
        return 1;
    }
}