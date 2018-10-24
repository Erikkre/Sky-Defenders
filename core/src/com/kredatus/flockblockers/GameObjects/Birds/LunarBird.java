package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class LunarBird extends BirdAbstractClass {
    public LunarBird(float delta, float camheight, float camwidth, TweenManager manager){
        super(delta, camheight, camwidth);
        this.yVel=2;
        this.coins=50;
        this.health=7;
        this.width = width-sizeVariance+r.nextInt(sizeVariance*2);
        this.height = height-sizeVariance+r.nextInt(sizeVariance*2);
        super.load("sprites/lunar.png", 0.15f);
        animation=rightFlaps;

    }

    @Override
    public void setManager(float delta, float camwidth, float edge) {
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
        xMotion = (Tween.to(x, -1, 10).waypoint(edge).setCallback(animationSwitch)).delay(3).setCallback(animationSwitch).target(-edge).setCallback(animationSwitch).delay(3).setCallback(animationSwitch)
                .ease(TweenEquations.easeOutBack).repeatYoyo(Tween.INFINITY, 0).start();
    }
}


