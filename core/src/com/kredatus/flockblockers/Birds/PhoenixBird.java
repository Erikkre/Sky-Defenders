// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Birds;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BgHandler;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class PhoenixBird extends BirdAbstractClass {

public float targetY, targetX;//, preTargetY;
    public PhoenixBird(float camHeight, float camWidth, ArrayList flashLengths){
        super();
        this.flashLengths=flashLengths;
        yAcc=-0.2f;
        yVelDeath=10;
        yVel=1*globalSpeedMultiplier;
        diamonds=1;
        coinNumber=100;
        origYVel=yVel;
        health=10;origHealth=health;
        if (FlockBlockersMain.fastTest) health*=globalHealthMultiplier;

        sizeVariance=1;
        sizeRatio=0.9f;

        animSeq = AssetHandler.phoenixAnimations;
        animSetup();

        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio= ((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height*=finalSizeRatio;
        edge = (camWidth)-width/3;
        //System.out.println("Height after: " + height+ " width: " + width);


        animation=rightFlaps;// starting animation
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
        //animSeq= new Animation[]{frontFlaps,leftFlaps,frontFlaps,rightFlaps};
        height=((TextureRegion)backFlaps.getKeyFrames()[3]).getRegionHeight();
        width=((TextureRegion)backFlaps.getKeyFrames()[0]).getRegionWidth();
    }
    @Override
    public void specificUpdate(float delta, float runTime) {
        if ( ((xVel>0 && xVel<2.5 && x>camWidth/2)||(xVel<0 && xVel>-2.5 && x<camWidth/2))&& animation!= frontFlaps) animation=frontFlaps;
        else if (xVel>1.5&&x<camWidth/2)animation=rightFlaps;
        else if (xVel<-1.5&&x>camWidth/2) animation=leftFlaps;

            //System.out.println("startOutro");
        if (BgHandler.isBirdSpawning&&currentX.isFinished()&&currentX!=introX){
            //System.out.println("Tween is finished******************************************************");
            if (x<camWidth/2) {targetX=(camWidth/2+width/3)+r.nextInt(  (int)((camWidth/2-(2*width/3)))  ); }  //if on left go to right if on right go left
            else              {targetX=width/3+r.nextInt((int)(camWidth/2-(2*width/3)));                    }

            targetY=height/3+r.nextInt((int)(camHeight-height*2));
            /*while (Math.abs(x-targetX)<200){
                System.out.println("finding target: "+targetX+"far enough from "+x+"constraints are "+width/3+" and "+(camWidth-(width/3)));
                if (x<camWidth/2)
                targetX=width/2+r.nextInt((int)(t));
            }*/
            currentX =(Tween.to(this, 1, 2).target(targetX).ease(TweenEquations.easeInOutSine)).delay(1).start();
            currentY =(Tween.to(this, 2, 2).target(targetY).ease(TweenEquations.easeOutSine)).delay(1).start();
            startRot=true;
        } else if (introX!=null&&introX.isFinished()){
            introX=null;
            startRot=true;
            //System.out.println("startRot");
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

        targetY=height/3+r.nextInt((int)(camHeight-height*2));
        firstX =(Tween.to(this, 1, 2).target(targetX).ease(TweenEquations.easeInOutSine));
        //targetY=height/2+r.nextInt((int)(camHeight-height));
        firstY =(Tween.to(this, 2, 2).target(targetY).ease(TweenEquations.easeOutSine));



    }
}