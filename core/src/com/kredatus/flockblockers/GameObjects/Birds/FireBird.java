// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class FireBird extends BirdAbstractClass {



    //public final int[] animSeqList = {0,1,2,3};
    boolean newBirdOverlaps;
    public FireBird(float camHeight, float camWidth, ArrayList flashLengths){
        super();
        this.flashLengths=flashLengths;

        yVel=1f*globalSpeedMultiplier;
        origYVel=yVel;

        coinNumber=1;

        sizeVariance=100;
        sizeRatio=0.4f;

        animSeq = AssetHandler.fireAnimations;
        animSetup();

        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height *= finalSizeRatio;
        edge = (camWidth)-width/2;
        //System.out.println("Height after: " + height+ " width: " + width);
        health=2;origHealth=health;
        if (FlockBlockersMain.fastTest) health*=globalHealthMultiplier;

        //double temp=r.nextGaussian();
        //if (temp<-0.7 || temp >0.7) animation=animSeq[r.nextInt(4)];    //less of a chance for it to be side or back of bird, within standard deviation higher chance of front
        //else
        animation=frontFlaps;
        origFlapSpeed=animation.getFrameDuration();

        x=0;
        y=1;
        this.camWidth = camWidth;
        this.camHeight = camHeight;


        newXWithinWidth();  //starting x, y coords
        newYWithinHeight();

        FireBird[] fireBirdList = new FireBird[0];
        if (!BirdHandler.birdQueue.isEmpty()) fireBirdList =  BirdHandler.birdQueue.toArray(fireBirdList);

        do  {
            newBirdOverlaps=false;
            for (FireBird i : fireBirdList) {   //check each already spawned bird for overlap
                if (x > i.x - width / 2.5f && x < i.x + width / 2.5f && y > i.y - height / 2.5f && y < i.y + height / 2.5f) {
                    newBirdOverlaps = true; //if within x+-width/3 or y+-height/3, randomly change y or x
                    int xOrYchange=r.nextInt(2);
                    if (xOrYchange==0) {x=0; newXWithinWidth(); }
                    else               {y=1; newYWithinHeight();}
                    break;  //restart
                }
            }
        } while (newBirdOverlaps);

        setManager(camWidth);
        setBoundingPoly(x,y,width,height);
        flapSpeedIntervals();
    }

    private void newXWithinWidth(){
        while ((x<width/2||x>edge) ) {
            x = (float) ((camWidth / 2) + (r.nextGaussian() * ((camWidth / 4) - (width / 2)))); //gaussian is between -1 and 1 as a bellcurve around 0
        }
    }

    private void newYWithinHeight() {
        while (y > 0 || y < -height * 4) {
            y = (float) (-height * 2 + r.nextGaussian() * (height));
        }
    }
    private void animSetup(){
        frontFlaps=animSeq[0];
        leftFlaps=animSeq[1];
        rightFlaps=animSeq[2];
        backFlaps=animSeq[3];
        deathFlaps=animSeq[4];
        animSeq= new Animation[]{frontFlaps,leftFlaps,frontFlaps,rightFlaps};
        height=((TextureRegion)backFlaps.getKeyFrames()[3]).getRegionHeight();
        width=((TextureRegion)backFlaps.getKeyFrames()[0]).getRegionWidth();
    }

    @Override
    public void specificUpdate(float delta, float runTime) {

    }

    @Override
    public void setManager(float camWidth) {

    }
}