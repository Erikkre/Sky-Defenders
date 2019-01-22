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

public class NightBird extends BirdAbstractClass {

    //public final int[] animSeqList = {0,1,2,3};
    public NightBird(float camHeight, float camWidth, ArrayList flashLengths){
        super();
this.flashLengths=flashLengths;

        //yVel=10;
        origYVel=yVel;

        coinNumber=4;

        sizeVariance=50;
        sizeRatio=0.65f;

        animSeq = AssetHandler.nightAnimations;
        animSetup();


        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height *= finalSizeRatio;
        edge = (camWidth)-width/2;
        //System.out.println("Height after: " + height+ " width: " + width);
        health=4;origHealth=health;
        if (FlockBlockersMain.fastTest) health*=globalHealthMultiplier;

        animation=animSeq[r.nextInt(2)];
        origFlapSpeed=animation.getFrameDuration();
        x=(width/2 + r.nextInt((int)(edge-width)));

        y=-5*height- r.nextFloat()*height*2;
        this.camWidth = camWidth;
        this.camHeight = camHeight;
        setManager(camWidth);
        setBoundingPoly(x,y,width,height);
        origFlapSpeed=animation.getFrameDuration();
        flapSpeedIntervals();
    }

    protected void animSetup(){
        frontFlaps=animSeq[0];
        backFlaps=animSeq[3];
        deathFlaps=animSeq[4];
        animSeq= new Animation[]{frontFlaps,backFlaps};
        height=((TextureRegion)backFlaps.getKeyFrames()[3]).getRegionHeight();
        width=((TextureRegion)backFlaps.getKeyFrames()[0]).getRegionWidth();
    }

    @Override
    public void specificUpdate(float delta, float runTime) {

    }

    @Override
    public void setManager(float camWidth) {

        final TweenCallback endIntro= new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                currentY=firstY.start();
            }
        };

        currentY = Tween.to(this, 2, 2.5f/globalSpeedMultiplier).target(height).ease(TweenEquations.easeOutBack).setCallback(endIntro).start();
        firstY =Tween.to(this, 2, 2.5f/globalSpeedMultiplier).target(camHeight+height/2).ease(TweenEquations.easeInBack).delay(0.5f);

    }
}