// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.Handlers.AssetHandler;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class WaterBird extends BirdAbstractClass {

    //public final int[] animSeqList = {0,1,2,3};
    Tween second;
    public WaterBird(float camHeight, float camWidth, float xPosition, float yPosition, ArrayList flashLengths){
        super();
this.flashLengths=flashLengths;
        yVel=2*globalSpeedMultiplier;
        coinNumber=1;
        origYVel=yVel;

        sizeVariance=100;
        sizeRatio=0.5f;

        animSeq = AssetHandler.waterAnimations;
        animSetup();

        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height *= finalSizeRatio;
        edge = (camWidth)-width/2;
        //System.out.println("Height after: " + height+ " width: " + width);
        health=2;origHealth=health;
        if (FlockBlockersMain.fastTest) health*=globalHealthMultiplier;

        animation=backFlaps;
        origFlapSpeed=animation.getFrameDuration();

        x=xPosition;
        y=yPosition;

        this.camWidth = camWidth;
        this.camHeight = camHeight;

        setBoundingPoly(x,y,width,height);
        flapSpeedIntervals();
    }

    protected void animSetup(){
        backFlaps=animSeq[3];
        deathFlaps=animSeq[4];
        //animSeq= new Animation[]{frontFlaps,leftFlaps,frontFlaps,rightFlaps};
        height=((TextureRegion)backFlaps.getKeyFrames()[3]).getRegionHeight();
        width=((TextureRegion)backFlaps.getKeyFrames()[0]).getRegionWidth();
    }
    @Override
    public void specificUpdate(float delta, float runTime) {
        //second.update(delta);

    }

    @Override
    public void setManager(float camWidth) {
    }
}