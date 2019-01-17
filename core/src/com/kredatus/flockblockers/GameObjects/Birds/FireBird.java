// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.Handlers.AssetHandler;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class FireBird extends BirdAbstractClass {



    //public final int[] animSeqList = {0,1,2,3};
    Tween second;
    public FireBird(float camHeight, float camWidth){
        super();

        yVel=1f*globalSpeedMultiplier;
        origYVel=yVel;

        coinNumber=1;

        sizeVariance=150;
        sizeRatio=0.5f;

        animSeq = AssetHandler.fireAnimations;
        animSetup();

        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height *= finalSizeRatio;
        edge = (camWidth)-width/2;
        //System.out.println("Height after: " + height+ " width: " + width);
        health=2;
        if (FlockBlockersMain.fastTest) health*=globalHealthMultiplier;

        double temp=r.nextGaussian();
        if (temp<-0.7 || temp >0.7) animation=animSeq[r.nextInt(4)];    //less of a chance for it to be side or back of bird, within standard deviation higher chance of front
        else animation=frontFlaps;
        origFlapSpeed=animation.getFrameDuration();

        x=0;
        y=1;

        while (x<width/2||x>edge) {
            x = (float) ((camWidth / 2) + (r.nextGaussian() * ((camWidth / 4) - (width / 2)))); //gaussian is between -1 and 1 as a bellcurve around 0
        }
        while (y>0 || y<-height*4) {
            y = (float) (-height * 2 + r.nextGaussian() * (height ));
        }

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