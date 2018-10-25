package com.kredatus.flockblockers.GameObjects.Birds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.Helpers.BirdAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Erik Kredatus on 9/8/2018.
 */

public class PhoenixBird extends BirdAbstractClass {
    private final float edge;
    public PhoenixBird(float camHeight, float camWidth){
        super(camHeight, camWidth);


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
        x= width/2 + r.nextInt((int)edge-width);
        y=0;
        this.camWidth = camWidth;
        this.camHeight = camHeight;
        setManager(camWidth, edge);
    }

    @Override
    public void setManager(float camWidth, float edge) {
        final Animation[] list = {frontFlaps, leftFlaps, frontFlaps, rightFlaps};
        Tween.registerAccessor(PhoenixBird.class, new BirdAccessor());

        final TweenCallback animationSwitch = new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                System.out.println("callback");
                animation = list[counter++];
                if (counter==4){
                    counter=0;
                }
            }
        };

        (xMotion = Timeline.createSequence()
                .push(   Tween.to(this, 1, 4).target(edge).ease(TweenEquations.easeOutBack).setCallback(animationSwitch))
                .push(   Tween.to(this, 1, 4).target(edge).ease(TweenEquations.easeNone).setCallback(animationSwitch))
                .push(   Tween.to(this, 1, 4).target(width/2).ease(TweenEquations.easeOutBack).setCallback(animationSwitch))
                .push(   Tween.to(this, 1, 4).target(width/2).ease(TweenEquations.easeNone).setCallback(animationSwitch))    )
                .repeat( Tween.INFINITY, 0).start();
                /*.push(delay(3).setCallback(animationSwitch))
                .target(width/2).setCallback(animationSwitch).delay(3).setCallback(animationSwitch)
                .target(edge).setCallback(animationSwitch).delay(3)
                .ease(TweenEquations.easeOutBack)*/
    }
}