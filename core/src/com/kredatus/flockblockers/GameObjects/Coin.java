package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.GameHandler;
import com.kredatus.flockblockers.TweenAccessors.Value;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

public class Coin {
    private Vector2 dest=new Vector2(GameHandler.camWidth/2,9*GameHandler.camHeight/10);
    public float y1, x1, width, height, x,y;
    public Value tweenX=new Value(), tweenY=new Value();
    public Animation animation;
    public boolean firstMovementEndedX=false, firstMovementEndedY=false, phoenixCoin;
    public TweenCallback endFirstMovementX, endFirstMovementY;
    public BirdAbstractClass thisBird;
    public Tween firstXMotion, secondXMotion, firstYMotion, secondYMotion;
    public Timeline xMotion, yMotion;

    public Coin(float x, float y, float rotation, BirdAbstractClass thisBird, boolean phoenixCoin){
        this.phoenixCoin=phoenixCoin;
        animation = AssetHandler.coinAnimation;
        width = ((TextureRegion) animation.getKeyFrame(0)).getRegionWidth();

        height = width;

        if (phoenixCoin) {
            x+=thisBird.width/7.7f;
           x1= x - (float)(Math.cos(Math.toRadians(rotation)))*150 ;
           y1= y - (float)(Math.sin(Math.toRadians(rotation)))*150;
           tweenX.setValue(x);
           tweenY.setValue(y);

       } else {
            this.thisBird=thisBird;
            x1 = (float) (Math.cos(Math.toRadians(rotation))) * (thisBird.width/4 + width/1.5f);
            y1 = (float) (Math.sin(Math.toRadians(rotation))) * (thisBird.width/4 + width/1.5f);
       }
        setupTweens();
    }

    private void setupTweens(){
        endFirstMovementX=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                firstMovementEndedX=true;
                tweenX.setValue(x);
                if (secondXMotion!=null) secondXMotion.start();
            }
        };

        endFirstMovementY=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                firstMovementEndedY=true;
                tweenY.setValue(y);
                if (secondYMotion!=null) secondYMotion.start();
            }
        };

        if (phoenixCoin) {
            xMotion = Timeline.createSequence()
                    .push((Tween.to(tweenX, -1, 0.4f).target(x1).ease(TweenEquations.easeOutSine)).setCallback(endFirstMovementX))
                    .push(Tween.to(tweenX, -1, 1.5f).target(dest.x).ease(TweenEquations.easeInQuint))
                    .start();

            yMotion = Timeline.createSequence()
                    .push((Tween.to(tweenY, -1, 0.4f).target(y1).ease(TweenEquations.easeOutSine)).setCallback(endFirstMovementY))
                    .push(Tween.to(tweenY, -1, 1.5f).target(dest.y).ease(TweenEquations.easeInQuint))
                    .start();

        } else {

            firstXMotion = Tween.to(tweenX, -1, 0.7f).target(x1).ease(TweenEquations.easeInBounce).setCallback(endFirstMovementX).start();
            secondXMotion= Tween.to(tweenX, -1, 1.7f).target(dest.x).ease(TweenEquations.easeNone);

            firstYMotion = Tween.to(tweenY, -1, 0.7f).target(y1).ease(TweenEquations.easeInBounce).setCallback(endFirstMovementY).start();
            secondYMotion= Tween.to(tweenY, -1, 1.7f).target(dest.y).ease(TweenEquations.easeNone);
        }
    }

    public void update(float delta){
        if (phoenixCoin) {
            xMotion.update(delta);
            yMotion.update(delta);
            x=tweenX.getValue();
            y=tweenY.getValue();
        } else {
            if (!firstMovementEndedX){
                x=tweenX.getValue()+thisBird.x;//+thisBird.width/9.7f; //higher number=more to the left
                firstXMotion.update(delta);
            } else {
                x=tweenX.getValue();
                secondXMotion.update(delta);
            }
            if (!firstMovementEndedY){
                y=tweenY.getValue()+thisBird.y;
                firstYMotion.update(delta);
            } else {
                y=tweenY.getValue();
                secondYMotion.update(delta);

            }
        }
    }
}
