package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.TweenAccessors.Value;
import com.kredatus.flockblockers.TweenAccessors.ValueAccessor;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

public class Coin {
    private Vector2 dest=new Vector2(1000,1000);
    public Timeline xMotion, yMotion;
    public float y1, x1, width, height;
    public Value x=new Value(), y=new Value();
    public Animation animation;
    public Coin(float camWidth, float camheight, float x, float y, float rotation){
        this.x.setValue(x);
        this.y.setValue(y);
        x1=-(float)(Math.cos(Math.toRadians(rotation)))*2;
        y1=-(float)(Math.sin(Math.toRadians(rotation)))*2;
        animation = AssetHandler.coinAnimation;
        width=((TextureRegion)animation.getKeyFrame(0)).getRegionWidth();
        height=width;

        setupTweens();
    }

    private void setupTweens(){
        xMotion = Timeline.createSequence()
                .push(   Tween.to(x, -1, 1).target(x1).ease(TweenEquations.easeOutSine))
                .push(   Tween.to(x, -1, 2).target(dest.x).ease(TweenEquations.easeInQuint))
                .start();
        yMotion = Timeline.createSequence()
                .push(   Tween.to(y, -1, 1).target(y1).ease(TweenEquations.easeOutSine))
                .push(   Tween.to(y, -1, 2).target(dest.y).ease(TweenEquations.easeInQuint))
                .start();
    }
    public void update(float delta){
        xMotion.update(delta);
        yMotion.update(delta);
    }
}
