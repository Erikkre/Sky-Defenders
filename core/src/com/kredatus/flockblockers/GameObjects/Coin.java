package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.TweenAccessors.Value;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

public class Coin {

    private Vector2 dest=new Vector2(540,1800);
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

           x1= x - (float)(Math.cos(Math.toRadians(rotation)))*150;
           y1= y - (float)(Math.sin(Math.toRadians(rotation)))*150;
           tweenX.setValue(x);
           tweenY.setValue(y);
       } else {
            this.thisBird=thisBird;
           x1 = (float) (Math.cos(Math.toRadians(rotation))) * thisBird.width/3;
           y1 = (float) (Math.sin(Math.toRadians(rotation))) * thisBird.width/3;
       }
        setupTweens();
    }

    private void setupTweens(){
        if (phoenixCoin) {
            xMotion = Timeline.createSequence()
                    .push(Tween.to(tweenX, -1, 0.4f).target(x1).ease(TweenEquations.easeOutSine))
                    .push(Tween.to(tweenX, -1, 1.3f).target(dest.x).ease(TweenEquations.easeInQuint))
                    .start();

            yMotion = Timeline.createSequence()
                    .push(Tween.to(tweenY, -1, 0.4f).target(y1).ease(TweenEquations.easeOutSine))
                    .push(Tween.to(tweenY, -1, 1.3f).target(dest.y).ease(TweenEquations.easeInQuint))
                    .start();
        } else {
            endFirstMovementX=new TweenCallback() {
                @Override
                public void onEvent(int i, BaseTween<?> baseTween) {
                    firstMovementEndedX=true;
                    tweenX.setValue(x);
                    secondXMotion.start();
                }
            };
            endFirstMovementY=new TweenCallback() {
                @Override
                public void onEvent(int i, BaseTween<?> baseTween) {
                    firstMovementEndedY=true;
                    tweenY.setValue(y);
                    secondYMotion.start();
                }
            };

            firstXMotion = Tween.to(tweenX, -1, 0.4f).target(x1).ease(TweenEquations.easeInBounce).setCallback(endFirstMovementX).start();
            secondXMotion= Tween.to(tweenX, -1, 1.3f).target(dest.x).ease(TweenEquations.easeNone);

            firstYMotion = Tween.to(tweenY, -1, 0.4f).target(y1).ease(TweenEquations.easeInBounce).setCallback(endFirstMovementY).start();
            secondYMotion= Tween.to(tweenY, -1, 1.3f).target(dest.y).ease(TweenEquations.easeNone);
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
                x=tweenX.getValue()+thisBird.x+thisBird.width/7.6f;
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
