// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.skydefenders.Birds;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.skydefenders.Handlers.BgHandler;
import com.kredatus.skydefenders.NonGameHandlerScreens.Loader;

import java.util.ArrayList;
import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class ThunderBird extends BirdAbstractClass {

    //public final int[] animSeqList = {0,1,2,3};
    Random r = new Random();
    Tween second;
    int angle=35;
    public ThunderBird(Vector2 airshipPos, float camHeight, float camWidth, ArrayList flashLengths){
        super(airshipPos);
this.flashLengths=flashLengths;

        rotStep=1.4f;
        unRotStep=1.9f;

        yVel=  (0.6f*globalSpeedMultiplier); // + r.nextFloat()*.5f);
        origYVel=yVel;

        expNumber=1;
        coinNumber=1;

        sizeVariance=100;
        sizeRatio=0.5f;

        animSeq = Loader.thunderAnims;
        animSetup();

        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height *= finalSizeRatio;
        edge = (camWidth)-width/2;
        //System.out.println("Height after: " + height+ " width: " + width);
        health=2;


        animation=frontFlaps;
        origFlapSpeed=animation.getFrameDuration();

        x=(width/2 + r.nextInt((int)(edge-width)));
        y=-height/3 - r.nextFloat()*height*2;
        this.camWidth = camWidth;
        this.camHeight = camHeight;

        setManager(camWidth);
        postInitSetup();
    }

    protected void animSetup(){
        frontFlaps=animSeq[0];
        leftFlaps=animSeq[1];
        rightFlaps=animSeq[2];
        backFlaps=animSeq[3];
        deathFlaps=animSeq[4];
        //animSeq= new Animation[]{frontFlaps,leftFlaps,frontFlaps,rightFlaps};
        height=((TextureRegion)frontFlaps.getKeyFrames()[3]).getRegionHeight();
        width=((TextureRegion)frontFlaps.getKeyFrames()[0]).getRegionWidth();
    }

    @Override
    public void specificUpdate(float delta, float runTime) {
        //second.update(delta);
        if ( ((xVel>0 && xVel<2.5 && x>camWidth/2)||(xVel<0 && xVel>-2.5 && x<camWidth/2))&& animation!= frontFlaps) animation=frontFlaps;
        else if (xVel>1.5&&x<camWidth/2)animation=rightFlaps;
        else if (xVel<-1.5&&x>camWidth/2) animation=leftFlaps;
         if (BgHandler.isBirdSpawning&&currentX.isFinished()&&currentX!=introX) {
            if (x>camWidth/2) {currentX = Tween.to(this, 1, 0.7f + r.nextFloat()).target(width / 2+r.nextInt((int)width)).ease(TweenEquations.easeInOutSine).start(); targetRot=angle; rotate=true;}
            else {currentX = Tween.to(this, 1, 0.7f + r.nextFloat()).target(edge-r.nextInt((int)width)).ease(TweenEquations.easeInOutSine).start(); targetRot=360-angle; rotate=true;}
         }
         /*
        if (cnt==4) {cnt=0;}
        //System.out.println("x: "+x+ " > "+(2*camWidth)/3);

        if (cnt==0&&x>edge-width) {
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth()*finalSizeRatio;
            //edge = (camWidth)-width/2;
        } else if (cnt==1&&x<edge-width) {
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth()*finalSizeRatio;
            //edge = (camWidth)-width/2;
        } else if (cnt==2&&x<width / 2+width) {
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth()*finalSizeRatio;
            //edge = (camWidth)-width/2;
        } else if (cnt==3&&x>width / 2+width) {
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth()*finalSizeRatio;
            //edge = (camWidth)-width/2;
        }*/
    }

    @Override
    public void setManager(final float camWidth) {
        final TweenCallback endIntro= new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                currentX=firstX.start();
                if (x> camWidth /2) { targetRot=angle; rotate=true;}
                else { targetRot=360-angle; rotate=true;}
            }
        };

        introX = Tween.to(this, 1, 0.7f + r.nextFloat()).target(edge-r.nextInt((int)width)).ease(TweenEquations.easeInOutSine).start().setCallback(endIntro);
        currentX=introX;


        /*final TweenCallback endfirst= new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                second.pause();
                first.start();
            }
        };*/

        firstX =Tween.to(this, 1, 1).target(width/2).ease(TweenEquations.easeInOutSine);

    }
}