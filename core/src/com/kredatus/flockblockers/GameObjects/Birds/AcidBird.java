package com.kredatus.flockblockers.GameObjects.Birds;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;


public class AcidBird extends BirdAbstractClass{
    public AcidBird(float camHeight, float camWidth){
        super(camHeight, camWidth);
        yVel=4;
        health=7;
        coins=15;
        this.sizeVariance=15;
        width += -sizeVariance+ r.nextInt(sizeVariance*2);
        height += -sizeVariance+r.nextInt(sizeVariance*2);
        super.load("sprites/acid.png", 0.15f);
        animation=rightFlaps;

    }

    @Override
    public void setManager(float camWidth, float edge) {
        final Animation[] list = {rightFlaps, frontFlaps, leftFlaps, frontFlaps};

        final TweenCallback animationSwitch = new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                if (counter==2){
                    counter=0;
                }
                animation = list[counter++];
            }
        };
        (xMotion = Timeline.createSequence()
                .push(   Tween.to(this, 1, 4).target(edge).ease(TweenEquations.easeOutBack).setCallback(animationSwitch))
                .push(Tween.to(this, 1, 4).target(width/2).ease(TweenEquations.easeOutBack).setCallback(animationSwitch)))
                .repeatYoyo(Tween.INFINITY, 0).start();
    }
}
