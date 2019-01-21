// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class PhoenixBird extends BirdAbstractClass {

Tween outroY;

public float targetY, targetX;//, preTargetY;
    public PhoenixBird(float camHeight, float camWidth){
        super();
        yAcc=-0.2f;
        yVelDeath=10;
        yVel=1*globalSpeedMultiplier;
        diamonds=1;
        coinNumber=100;
        origYVel=yVel;
        health=60;
        if (FlockBlockersMain.fastTest) health*=globalHealthMultiplier;

        sizeVariance=1;
        sizeRatio=1f;

        animSeq = AssetHandler.phoenixAnimations;
        animSetup();

        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio= ((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height*=finalSizeRatio;
        edge = (camWidth)-width/3;
        //System.out.println("Height after: " + height+ " width: " + width);


        animation=frontFlaps;// starting animation
        origFlapSpeed=animation.getFrameDuration();

        x=(width/3 + r.nextInt((int)(edge-(2*width)/3)));

        y=-height/3;

        this.camWidth = camWidth;

        this.camHeight = camHeight;
        setManager(camWidth);

        setBoundingPoly(x,y,width,height);
        flapSpeedIntervals();
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

        if (!BgHandler.isBirdSpawning&&currentY!=outroY){
            currentX.kill();
            currentY=outroY.start();
            if (x>camWidth/2){   //if dying on right side fall to left and vice versa
                xVel=-2;
            } else {
                xVel=2;
            }
            System.out.println("startOutro");
        } else if (BgHandler.isBirdSpawning&&currentX.isFinished()&&currentX!=introX){
            //System.out.println("Tween is finished******************************************************");
            if (x<camWidth/2) targetX=(camWidth/2+width/3)+r.nextInt(  (int)((camWidth/2-(2*width/3)))  );  //if on left go to right if on right go left
            else              targetX=width/3+r.nextInt((int)(camWidth/2-(2*width/3)));

            targetY=height/3+r.nextInt((int)(camHeight-height*3));
            /*while (Math.abs(x-targetX)<200){
                System.out.println("finding target: "+targetX+"far enough from "+x+"constraints are "+width/3+" and "+(camWidth-(width/3)));
                if (x<camWidth/2)
                targetX=width/2+r.nextInt((int)(t));
            }*/
            currentX =(Tween.to(this, 1, 2).target(targetX).ease(TweenEquations.easeOutSine)).start();
            currentY =(Tween.to(this, 2, 2).target(targetY).ease(TweenEquations.easeOutSine)).start();
            startRot=true;
        } else if (introX!=null&&introX.isFinished()){
            introX=null;
            startRot=true;
            System.out.println("startRot");
        }
    }

    @Override
        public void setManager(final float camWidth) {

            final TweenCallback endIntro= new TweenCallback() {
                @Override
                public void onEvent(int i, BaseTween<?> baseTween) {
                    currentX=firstX.start();
                    currentY=firstY.start();
                    yVel=0;
                }
            };
//aseInOutQuint
        introX = Tween.to(this, 1, 2).target(edge).ease(TweenEquations.easeOutQuart).setCallback(endIntro).start();
        currentX=introX;

        //type 1 is xmotion type 2 is y
        if (x<camWidth/2) targetX=(camWidth/2+width/3)+r.nextInt(  (int)((camWidth/2-(2*width/3)))  );
        else              targetX=width/3+r.nextInt((int)(camWidth/2-(2*width/3)));

        targetY=height/3+r.nextInt((int)(camHeight-height*3));
        firstX =(Tween.to(this, 1, 2).target(targetX).ease(TweenEquations.easeOutSine));
        //targetY=height/2+r.nextInt((int)(camHeight-height));
        firstY =(Tween.to(this, 2, 2).target(targetY).ease(TweenEquations.easeOutSine));

        outroY=Tween.to(this, 2, 2).target(camHeight+height/2).ease(TweenEquations.easeInExpo).delay(1); //hit wall when not killed and end of spawning period

    }
}