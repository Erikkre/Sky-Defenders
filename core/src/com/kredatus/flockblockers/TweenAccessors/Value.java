// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.TweenAccessors;

/**
 * Created by Mr. Kredatus on 10/29/2017.
 */

public class Value {

    private float val;

    public Value(float val){
        this.val=val;
    }
    public Value(){
    }
    public float get() {
        return val;
    }

    public void set(float newVal) {
        val = newVal;
    }

}