package com.kredatus.flockblockers.GameObjects.Birds;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import aurelienribon.tweenengine.TweenEquations;


/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class FlockBird extends BirdAbstractClass {
    private final float edge;
    final Animation[] animSeq = {frontFlaps, leftFlaps, frontFlaps, rightFlaps};
    final int[] animSeqList = {0,1,2,3};
    public FlockBird( float camHeight, float camWidth){
        super( camHeight, camWidth);
        this.yVel=1;
        this.diamonds=1;
        this.coins=7;
        this.health=100;
        this.sizeVariance=15;

        super.load("sprites/phoenix.png", 0.15f);

        this.width += -sizeVariance+r.nextInt(sizeVariance*2);
        this.height += -sizeVariance+r.nextInt(sizeVariance*2);

        animation=rightFlaps;
        edge = (camWidth)-width/2;
        x.setValue(width/2 + r.nextInt((int)edge-width));
        y=0;
        this.camWidth = camWidth;
        this.camHeight = camHeight;
        setManager(camWidth);
        xMotion.start();
    }

    @Override
    public void specificUpdate(float delta, float runTime) {
        if (cnt==4) {cnt=0;}
        System.out.println(x.getValue());

        if (animSeqList[cnt]==0&&x.getValue()>(5*camWidth)/6) {
            System.out.println("1");
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth();
            //edge = (camWidth)-width/2;
        } else if (animSeqList[cnt]==1&&x.getValue()<(5*camWidth)/6) {
            System.out.println("2");
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth();
            //edge = (camWidth)-width/2;
        } else if (animSeqList[cnt]==2&&x.getValue()<(camWidth)/6) {
            System.out.println("3");
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth();
            //edge = (camWidth)-width/2;
        } else if (animSeqList[cnt]==3&&x.getValue()>(camWidth)/6) {
            System.out.println("4");
            animation = animSeq[cnt++];
            width=((TextureRegion)animation.getKeyFrame(runTime)).getRegionWidth();
            //edge = (camWidth)-width/2;
        }
    }

    @Override
    public void setManager(float camWidth) {

        /*
        final TweenCallback animationSwitch = new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {

                animation = animSeq[counter++];
                if (counter==4){
                    counter=0;
                }
            }
        };*/

        (xMotion = Timeline.createSequence()
                .push(   Tween.to(this, 1, 4).target(edge).ease(TweenEquations.easeInOutQuint))
                //.push(   Tween.to(this, 1, 4).target(edge).ease(TweenEquations.easeNone).setCallback(animationSwitch))
                .push(   Tween.to(this, 1, 4).target(width/2).ease(TweenEquations.easeInOutQuint))
                //.push(   Tween.to(this, 1, 4).target(width/2).ease(TweenEquations.easeNone).setCallback(animationSwitch))    )
        ).repeat( Tween.INFINITY, 3);
                /*.push(delay(3).setCallback(animationSwitch))
                .target(width/2).setCallback(animationSwitch).delay(3).setCallback(animationSwitch)
                .target(edge).setCallback(animationSwitch).delay(3)
                .ease(TweenEquations.easeOutBack)*/
    }
}

