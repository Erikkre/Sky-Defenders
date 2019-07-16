// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Birds;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Screens.Loader;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class FireBird extends BirdAbstractClass {



    //public final int[] animSeqList = {0,1,2,3};
    boolean newBirdOverlaps;
    public FireBird(Vector2 airshipPos,float camHeight, float camWidth, ArrayList flashLengths, ConcurrentLinkedQueue<BirdAbstractClass> birdQueue){
        super(airshipPos);
        this.flashLengths=flashLengths;

        yVel=0.85f*globalSpeedMultiplier;
        origYVel=yVel;

        expNumber=1;
        coinNumber=1;

        sizeVariance=100;
        sizeRatio=0.3f;

        animSeq = Loader.fireAnims;
        animSetup();

        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height *= finalSizeRatio;
        edge = (camWidth)-width/2;
        //System.out.println("Height after: " + height+ " width: " + width);
        health=2;


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

        BirdAbstractClass[] fireBirdList = new BirdAbstractClass[0];
        if (!birdQueue.isEmpty()) fireBirdList =  birdQueue.toArray(fireBirdList);

        do  {
            newBirdOverlaps=false;
            for (BirdAbstractClass i : fireBirdList) {   //check each already spawned bird for overlap
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
        postInitSetup();
    }

    private void newXWithinWidth(){
        while ((x<width/2||x>edge) ) {
            x = (float) ((camWidth / 2) + (r.nextGaussian() * ((camWidth / 4) - (width / 2)))); //gaussian is between -1 and 1 as a bellcurve around 0
        }
    }

    private void newYWithinHeight() {
        while (y > -height * 2 || y < -height * 6) {
            y = (float) (-height * 4 + r.nextGaussian() * (height));
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