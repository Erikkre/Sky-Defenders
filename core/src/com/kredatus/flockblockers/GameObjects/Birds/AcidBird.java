// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BgHandler;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

/*
 * Created by Erik Kredatus on 9/8/2018.
 */

public class AcidBird extends BirdAbstractClass {

    int angle=25;
    public AcidBird(float camHeight, float camWidth){
        super();

        rotStep=1.4f;
        unRotStep=1.0f;

        yVel=5;
        origYVel=yVel;

        coinNumber=3;

        sizeVariance=40;
        sizeRatio=0.65f;

        animSeq = AssetHandler.acidAnimations;
        animSetup();


        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height *= finalSizeRatio;
        edge = (camWidth)-width/2;
        //System.out.println("Height after: " + height+ " width: " + width);
        health=4;



        x=0;
        while (x<width/2||x>edge) {
            x = (float) ((camWidth / 2) + (r.nextGaussian() * ((camWidth / 4) - (width / 2)))); //gaussian is between -1 and 1 as a bellcurve around 0
        }

        y=-height/3 - r.nextFloat()*height*2;
        this.camWidth = camWidth;
        this.camHeight = camHeight;
        setManager(camWidth);
        setBoundingPoly(x,y,width,height);
        flapSpeedIntervals();
    }

    protected void animSetup(){
        frontFlaps=animSeq[0];
        leftFlaps=animSeq[1];
        rightFlaps=animSeq[2];
        backFlaps=animSeq[3];
        deathFlaps=animSeq[4];
        height=((TextureRegion)backFlaps.getKeyFrames()[3]).getRegionHeight();
        width=((TextureRegion)backFlaps.getKeyFrames()[0]).getRegionWidth();
    }

    @Override
    public void specificUpdate(float delta, float runTime) {
        if (BgHandler.isBirdSpawning&&currentX.isFinished()) {
            if (x>camWidth/2) {currentX = Tween.to(this, 1, 1f + r.nextFloat()).target(width / 2+r.nextInt((int)(width/2))).ease(TweenEquations.easeInOutSine).start(); targetRot=angle; rotate=true;}
            else {currentX = Tween.to(this, 1, 1f + r.nextFloat()).target(edge-r.nextInt((int)(width/2))).ease(TweenEquations.easeInOutSine).start(); targetRot=360-angle; rotate=true;}
        }

        if (cnt==4) {cnt=0;}
        if (cnt==0&&x>(2*camWidth)/3 ) {
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth()*finalSizeRatio;
            //edge = (camWidth)-width/2;
        } else if (cnt==1&&x<(2*camWidth)/3) {
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth()*finalSizeRatio;
            //edge = (camWidth)-width/2;
        } else if (cnt==2&&x<(camWidth)/3) {
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth()*finalSizeRatio;
            //edge = (camWidth)-width/2;
        } else if (cnt==3&&x>(camWidth)/3) {
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth()*finalSizeRatio;
            //edge = (camWidth)-width/2;
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
        animSeq= new Animation[]{frontFlaps,leftFlaps,frontFlaps,rightFlaps};

        currentX = Tween.to(this, 1, 0.7f).target(firstTarget).ease(TweenEquations.easeInOutSine).start();
        origFlapSpeed=animation.getFrameDuration();

    }
}