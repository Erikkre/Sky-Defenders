package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.Handlers.AssetHandler;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class NightBird extends BirdAbstractClass {



    //public final int[] animSeqList = {0,1,2,3};
    Tween second;
    public NightBird(float camHeight, float camWidth){
        super();

        this.yVel=12;

        this.coinNumber=7;

        this.sizeVariance=50;
        sizeRatio=0.8f;

        animSeq = AssetHandler.nightAnimations;
        animSetup();


        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height *= finalSizeRatio;
        edge = (camWidth)-width/2;
        //System.out.println("Height after: " + height+ " width: " + width);
        health=1;

        animation=rightFlaps;
        x=(width/2 + r.nextInt((int)(edge-width)));

        y=-height/3;
        this.camWidth = camWidth;
        this.camHeight = camHeight;
        setManager(camWidth);
        setBoundingPoly(x,y,width,height);
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
        //second.update(delta);
        if (cnt==4) {cnt=0;}
        //System.out.println("x: "+x+ " > "+(2*camWidth)/3);

        if (cnt==0&&x>(2*camWidth)/3) {
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
    public void setManager(float camWidth) {
        final TweenCallback endIntro= new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                currentX=firstX.start();
                yVel=0;
                firstY.start();
            }
        };

        introX = Tween.to(this, 1, 1).target(edge).ease(TweenEquations.easeInOutQuint).start().setCallback(endIntro);
        currentX=introX;


        /*final TweenCallback endfirst= new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                second.pause();
                first.start();
            }
        };*/

        firstX =Tween.to(this, 1, 2).target(width/2).ease(TweenEquations.easeInOutQuint).repeatYoyo(Tween.INFINITY,0);
        //Tween.to(this, 1, 4).target(edge).ease(TweenEquations.easeOutQuint).setCallback(tweenStart).start();
        /*
        (xMotion = Timeline.createSequence()
                .push(   Tween.to(this, 1, 6).target(edge).ease(TweenEquations.easeInOutQuint))
                .push(   Tween.to(this, 1, 6).target(width/2).ease(TweenEquations.easeOutQuint)).setCallback(tweenStart)
                //.push(   Tween.to(this, 1, 4).target(edge).ease(TweenEquations.easeNone).setCallback(animationSwitch))

                //.push(   Tween.to(this, 1, 4).target(width/2).ease(TweenEquations.easeNone).setCallback(animationSwitch))    )
                .repeat( Tween.INFINITY, 0)).start();
*/
                /*.push(delay(3).setCallback(animationSwitch))
                .target(width/2).setCallback(animationSwitch).delay(3).setCallback(animationSwitch)
                .target(edge).setCallback(animationSwitch).delay(3)
                .ease(TweenEquations.easeOutBack)*/
    }
}