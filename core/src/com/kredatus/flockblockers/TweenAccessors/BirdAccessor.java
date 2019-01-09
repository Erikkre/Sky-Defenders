// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.TweenAccessors;

import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

import aurelienribon.tweenengine.TweenAccessor;

public class BirdAccessor implements TweenAccessor<BirdAbstractClass>{
    private final int xMotion=1;
    private final int yMotion=2;


    @Override
    public void setValues(BirdAbstractClass birdTarget, int tweenType, float[] newValues) {
        switch (tweenType){
            case xMotion: birdTarget.x=newValues[0]; //System.out.println("birdX: "+birdTarget.x+"set to: "+newValues[0])         birdTarget.boundingPoly.setPosition(birdTarget.x-birdTarget.width, birdTarget.y);//System.out.println(" set to "+newValues[0]);
            break;
            case yMotion: birdTarget.y=newValues[0];//System.out.println("birdY: "+birdTarget.y+"set to: "+newValues[0]);
            break;
            default: assert false; break;
        }
    }

    @Override
    public int getValues(BirdAbstractClass birdTarget, int tweenType, float[] newValues) {

        switch (tweenType) {
            case (xMotion):
                newValues[0] = birdTarget.x; //System.out.println("start x: "+newValues[0]+" set to "+birdTarget.x);
                return 1;
            case (yMotion):
                newValues[0] = birdTarget.y; //System.out.println("start y: "+newValues[0]+" set to "+birdTarget.y);
                return 1;
            default: assert false; return 0;
        }
    }
}