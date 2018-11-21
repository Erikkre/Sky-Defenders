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

public class PhoenixBird extends BirdAbstractClass {


    public Animation[] animSeq;
    //public final int[] animSeqList = {0,1,2,3};
Tween second;
    public PhoenixBird(float camHeight, float camWidth){
        super();
        this.yVel=3f;
        this.diamonds=1;
        this.coinNumber=100;

        this.sizeVariance=1;
        sizeRatio=0.8f;

        animSeq = AssetHandler.phoenixAnimations;
        frontFlaps=animSeq[0];
        leftFlaps=animSeq[1];
        rightFlaps=animSeq[2];
        backFlaps=animSeq[3];
        animSeq= new Animation[]{frontFlaps,leftFlaps,frontFlaps,rightFlaps};
        height=((TextureRegion)backFlaps.getKeyFrames()[3]).getRegionHeight();
        width=((TextureRegion)backFlaps.getKeyFrames()[0]).getRegionWidth();



        //System.out.println("Height before: " + height+ " width: " + width);
        finalSizeRatio=((width-sizeVariance+r.nextInt(sizeVariance*2))*sizeRatio)/width;

        width *=finalSizeRatio;
        height *= finalSizeRatio;
        edge = (camWidth)-width/2;
        //System.out.println("Height after: " + height+ " width: " + width);
        health=5;

        animation=frontFlaps;// starting animation
        x=(width/2 + r.nextInt((int)(edge-width)));
        //System.out.println("x of bird set to " + x);
        y=-height/4;
        this.camWidth = camWidth;
        this.camHeight = camHeight;
        setManager(camWidth);
        setBoundingPoly(x,y,width,height);
    }

    @Override
    public void specificUpdate(float delta, float runTime) {
        /*if (xMotion==first&&firstxMotion){
            xMotionTimePositions.put(xMotion.getCurrentTime(), x);   //get all x positions
            System.out.print(xMotionTimePositions.toString());
            if (xMotion.getCurrentTime()==0){//this stops it immediately
                firstxMotion=false;
                System.out.println(xMotionTimePositions);
            }
        }*/


        //second.update(delta);
        if (cnt==4) {cnt=0;}
        //System.out.println("x: "+x+ " > "+(2*camWidth)/3);

        /*if (cnt==0&&x>(2*camWidth)/3) {
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
        }*/
    }

    @Override
    public void setManager(float camWidth) {
        final TweenCallback endIntro= new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {

                intro.pause();intro.kill();intro=null;
                xMotion=first;
                first.start();
            }
        };
//aseInOutQuint
        intro = Tween.to(this, 1, 1).target(edge).ease(TweenEquations.easeInOutQuint).start().setCallback(endIntro);
        xMotion=intro;


        /*final TweenCallback endfirst= new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                second.pause();
                first.start();
            }
        };*/

        first =Tween.to(this, 1, 3).target(width/2).ease(TweenEquations.easeInOutQuint).repeatYoyo(Tween.INFINITY,0);
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