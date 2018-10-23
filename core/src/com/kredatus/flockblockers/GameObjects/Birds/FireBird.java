package com.kredatus.flockblockers.GameObjects.Birds;


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

public class FireBird extends BirdAbstractClass {
    FireBird(float delta, float camheight, float camwidth, TweenManager manager){
        super(delta, camheight, camwidth, manager);
        this.coins=30;
        this.health=7;
        this.sizeVariance=15;
        int variance = r.nextInt(sizeVariance*2);
        this.width = width-sizeVariance+variance;
        this.height = height-sizeVariance+variance;
        super.load("sprites/fire.png", 0.15f);
        animation=rightFlaps;

    }

    @Override
    public void setManager(float delta, float camwidth, TweenManager manager, float edge) {
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

        (Tween.to(x, -1, 10).waypoint(edge).setCallback(animationSwitch)).delay(3).setCallback(animationSwitch).target(-edge).setCallback(animationSwitch).delay(3).setCallback(animationSwitch)
                .ease(TweenEquations.easeOutBack).repeatYoyo(Tween.INFINITY, 0).start(manager);
    }
}


