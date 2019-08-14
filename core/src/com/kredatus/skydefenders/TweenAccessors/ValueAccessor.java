// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.skydefenders.TweenAccessors;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by Mr. Kredatus on 10/29/2017.
 */

public class ValueAccessor implements TweenAccessor<Value> {

    @Override
    public int getValues(Value target, int tweenType, float[] returnValues) {
        returnValues[0] = target.get();
        return 1;
    }

    @Override
    public void setValues(Value target, int tweenType, float[] newValues) {
        target.set(newValues[0]);
    }
}