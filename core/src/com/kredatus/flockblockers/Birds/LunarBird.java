// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Birds;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Screens.Loader;

import java.util.ArrayList;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.equations.Circ;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class LunarBird extends BirdAbstractClass {
    private final int angle = 45;
    private int camHeightSeventh=1;
    float randomizedTime, waitingTime = 0.9f, minDashTime = 0.7f; //dashtime is up to 0.33f greater than minDashTime
    private Tween wait1SecondX, wait1SecondY;
    private TweenEquation tweenEquation = Circ.INOUT;
    public LunarBird(Vector2 airshipPos, float camHeight, float camWidth, ArrayList flashLengths){
        super(airshipPos);
        this.flashLengths=flashLengths;

        rotStep=5f;
        unRotStep=3f;
        yVel=10*globalSpeedMultiplier;
        origYVel=yVel;

        expNumber=5;
        coinNumber=5;

        sizeVariance=50;
        sizeRatio=0.65f;

        animSeq = Loader.lunarAnims;
        animSetup();

        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height *= finalSizeRatio;
        edge = (camWidth)-width/2;

        //System.out.println("Height after: " + height+ " width: " + width);
        health=4;origHealth=health;


        animation=rightFlaps;
        origFlapSpeed=animation.getFrameDuration();
        x=(width/2 + r.nextInt((int)(edge-width)));

        y=-height/3 - r.nextFloat()*height*2;
        this.camWidth = camWidth;
        this.camHeight = camHeight;
        setManager(camWidth);
        postInitSetup();
    }

    private void animSetup(){
        leftFlaps =animSeq[1];
        rightFlaps=animSeq[2];
        backFlaps =animSeq[3];
        frontFlaps=animSeq[0];
        deathFlaps=animSeq[4];

        height=((TextureRegion)frontFlaps.getKeyFrames()[3]).getRegionHeight();
        width=((TextureRegion)frontFlaps.getKeyFrames()[0]).getRegionWidth();
    }

    @Override
    public void specificUpdate(float delta, float runTime) {
        if (currentX.isFinished()){

            if (currentX==wait1SecondX) {
                randomizedTime= minDashTime + r.nextFloat()/3;
                if (x>camWidth/2) { currentX = Tween.to(this, 1, randomizedTime).target(width / 2+r.nextInt((int)(width/2))).ease(tweenEquation).start(); targetRot=angle; rotate=true;}
                else              { currentX = Tween.to(this, 1, randomizedTime).target(edge-r.nextInt((int)(width/2))).ease(tweenEquation).start(); targetRot=360-angle; rotate=true;}

            } else {
                currentX = wait1SecondX = Tween.to(this, 0, waitingTime).start();
            }
        }
        if ( ((xVel>0 && xVel<3.5 && x>camWidth/2)||(xVel<0 && xVel>-3.5 && x<camWidth/2))&& animation!= frontFlaps) animation=frontFlaps;
        else if (xVel>1&&x<camWidth/2)animation=rightFlaps;
        else if (xVel<-1&&x>camWidth/2) animation=leftFlaps;

        if (currentY.isFinished()){
            
            if (currentY==wait1SecondY) {
                currentY = Tween.to(this, 2, randomizedTime).target((camHeight/7)*camHeightSeventh++).ease(tweenEquation).start();

            } else {
                currentY = wait1SecondY = Tween.to(this, 0, waitingTime).start();
            }
        }
    }

    @Override
    public void setManager(final float camWidth) {

        float[] tempList = {width/2, edge};
        float firstTarget =tempList[r.nextInt(2)];

        if (firstTarget==width/2){
            targetRot=angle; rotate=true;
            animation=leftFlaps;
            cnt=2;
        } else {
            targetRot=360-angle; rotate=true;
            animation=rightFlaps;
        }

        currentX = Tween.to(this, 1, minDashTime).target(firstTarget).ease(tweenEquation).start();
        currentY = Tween.to(this, 2, minDashTime).target((camHeight/7)*camHeightSeventh++).ease(tweenEquation).start();
        origFlapSpeed=animation.getFrameDuration();
    }
}