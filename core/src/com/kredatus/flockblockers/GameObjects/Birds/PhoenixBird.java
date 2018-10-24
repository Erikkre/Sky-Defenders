package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class PhoenixBird extends BirdAbstractClass {
    public PhoenixBird(float camHeight, float camWidth){
        super(camHeight, camWidth);
        this.yVel=1;
        this.diamonds=1;
        this.coins=7;
        this.health=100;
        this.sizeVariance=15;
        this.width += -sizeVariance+r.nextInt(sizeVariance*2);
        this.height += -sizeVariance+r.nextInt(sizeVariance*2);
        super.load("sprites/phoenix.png", 0.15f);
        animation=rightFlaps;

    }

    @Override
    public void setManager(float camWidth, float edge) {
        final Animation[] list = {rightFlaps, frontFlaps, leftFlaps, frontFlaps};

        final TweenCallback animationSwitch = new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                if (counter==3){
                    counter=0;
                }
                animation = list[counter++];
            }
        };
        xMotion = (Tween.to(x, -1, 2).waypoint(edge).setCallback(animationSwitch)).delay(3).setCallback(animationSwitch).target(width/2).setCallback(animationSwitch).delay(3).setCallback(animationSwitch)
                .ease(TweenEquations.easeOutBack).repeatYoyo(Tween.INFINITY, 0).start();
    }
}

