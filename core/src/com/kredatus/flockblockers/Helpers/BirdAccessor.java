package com.kredatus.flockblockers.Helpers;

import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

import aurelienribon.tweenengine.TweenAccessor;
/*
public class BirdAccessor implements TweenAccessor<BirdAbstractClass>{
    private final int xMotion=1;

    @Override
    public void setValues(BirdAbstractClass birdTarget, int tweenType, float[] newValues) {
        System.out.print(birdTarget.x);
        switch (tweenType){
            case (xMotion): birdTarget.x=newValues[0]; System.out.println(" set to "+newValues[0]);
        }
    }

    @Override
    public int getValues(BirdAbstractClass birdTarget, int tweenType, float[] newValues) {
        switch (tweenType) {
            case (xMotion):
                //System.out.print(birdTarget.x);
                newValues[0] = birdTarget.x; //System.out.println(" get to "+newValues[0]);
                return 1;
            default: assert false; return 0;
        }
    }
}*/