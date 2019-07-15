// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Birds;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Screens.Loader;

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
    public NightBird(Vector2 airshipPos, float camHeight, float camWidth, ArrayList flashLengths){
        super(airshipPos);
this.flashLengths=flashLengths;

        //yVel=10;
        origYVel=yVel;

        expNumber=4;
        coinNumber=4;

        sizeVariance=50;
        sizeRatio=0.65f;

        animSeq = Loader.nightAnims;
        animSetup();


        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height *= finalSizeRatio;
        edge = (camWidth)-width/2;
        //System.out.println("Height after: " + height+ " width: " + width);
        health=4;origHealth=health;


        animation=animSeq[r.nextInt(2)];
        origFlapSpeed=animation.getFrameDuration();
        x=(width/2 + r.nextInt((int)(edge-width)));

        y=-5*height- r.nextFloat()*height*2;
        this.camWidth = camWidth;
        this.camHeight = camHeight;
        setManager(camWidth);


        postInitSetup();
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