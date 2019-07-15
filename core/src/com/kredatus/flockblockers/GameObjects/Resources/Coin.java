// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameObjects.Resources;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kredatus.flockblockers.Birds.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.GameWorld.GameHandler;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.Handlers.UiHandler;
import com.kredatus.flockblockers.Screens.Loader;
import com.kredatus.flockblockers.TweenAccessors.Value;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

public class Coin extends Image {
    private Vector2 dest, lastDest, differenceVector;
    public float y1, x1, width, height, x, y, motion1TimePhoenix,motion2TimePhoenix,motion1Time,motion2Time;
    public Value tweenX=new Value(), tweenY=new Value();
    public  Animation animation= Loader.coinAnimation;
    public boolean firstMovementEndedX=false, firstMovementEndedY=false, phoenixCoin, airshipMoved;
    public TweenCallback endFirstMovementX, endFirstMovementY,endSecondMovementY;
    public BirdAbstractClass thisBird;
    public Tween firstXMotion, secondXMotion, firstYMotion, secondYMotion;
    public double startTime;

    Vector2 airshipPos;

    public Coin(){
        super();
    }
    @Override
    public void act(float delta){
        //super.act(delta);
        this.setDrawable(new TextureRegionDrawable((TextureRegion) animation.getKeyFrame(GameHandler.runTime/2)));
    }

    public Coin(float rotation, BirdAbstractClass thisBird, boolean phoenixCoin, Vector2 airshipPos){
        this.airshipPos=airshipPos;
        this.phoenixCoin=phoenixCoin;
        width = ((TextureRegion) animation.getKeyFrame(0)).getRegionWidth();

        height = width;

        this.thisBird=thisBird;

        if (phoenixCoin) {
            //x+=thisBird.width/7.7f;
           x1 =  (float)(Math.cos(Math.toRadians(rotation)))*55 ;
           y1 =  (float)(Math.sin(Math.toRadians(rotation)))*55;
           //tweenX.set(x);
           //tweenY.set(y);
        } else {
            x1 = (float) (Math.cos(Math.toRadians(rotation))) * (thisBird.width/4 + width/1.5f);
            y1 = (float) (Math.sin(Math.toRadians(rotation))) * (thisBird.width/4 + width/1.5f);
        }

        System.out.println(UiHandler.goldSymbol.getY());
        dest=UiHandler.goldSymbol.localToParentCoordinates(new Vector2(UiHandler.table0.getX()+UiHandler.goldSymbol.getWidth()/2, UiHandler.table0.getY()));
        lastDest=new Vector2(0,0);

        motion1TimePhoenix=0.7f;
        motion2TimePhoenix=1.7f;
        motion1Time=0.7f;
        motion2Time=1.7f;

        setupTweens();
    }

    private void setupTweens(){
        final Coin thisCoin=this;
        endSecondMovementY=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                GameWorld.addGold(1);
                thisBird.coinList.remove(thisCoin);
            }
        };

        endFirstMovementX=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                firstMovementEndedX=true;
                tweenX.set(x);
                if (secondXMotion!=null) secondXMotion.start();
                startTime=System.currentTimeMillis();
            }
        };

        endFirstMovementY=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                firstMovementEndedY=true;
                tweenY.set(y);
                if (secondYMotion!=null) secondYMotion.start();
                startTime=System.currentTimeMillis();
            }
        };

        if (phoenixCoin) {
            firstXMotion=(Tween.to(tweenX, -1, motion1TimePhoenix).target(x1).ease(TweenEquations.easeNone)).setCallback(endFirstMovementX).start();
            secondXMotion=(Tween.to(tweenX, -1, motion2TimePhoenix).target(dest.x).ease(TweenEquations.easeNone));

            firstYMotion=(Tween.to(tweenY, -1, motion1TimePhoenix).target(y1).ease(TweenEquations.easeNone)).setCallback(endFirstMovementY).start();
            secondYMotion=(Tween.to(tweenY, -1, motion2TimePhoenix).target(dest.y).ease(TweenEquations.easeNone));
        } else {
            firstXMotion = Tween.to(tweenX, -1, motion1Time).target(x1).ease(TweenEquations.easeInBounce).setCallback(endFirstMovementX).start();
            secondXMotion= Tween.to(tweenX, -1, motion2Time).target(dest.x).ease(TweenEquations.easeNone);

            firstYMotion = Tween.to(tweenY, -1, motion1Time).target(y1).ease(TweenEquations.easeInBounce).setCallback(endFirstMovementY).start();
            secondYMotion= Tween.to(tweenY, -1, motion2Time).target(dest.y).ease(TweenEquations.easeNone);
        }
        secondYMotion.setCallback(endSecondMovementY);
        startTime=System.currentTimeMillis();
    }

    public void update(float delta){
        dest.set(airshipPos.x,airshipPos.y+ Airship.balloonHeight.get()/2f);
        differenceVector=dest.cpy().sub(lastDest);
        airshipMoved=Math.abs(differenceVector.x)>0 || Math.abs(differenceVector.y)>0;

        //dont need to do this for firstMovementX
        if (!firstMovementEndedX) {
            x = tweenX.get() + thisBird.x;//+thisBird.width/9.7f; //higher number=more to the left
            firstXMotion.update(delta);
        } else {
            if (airshipMoved&&(float) (0.9 - (System.currentTimeMillis() - startTime)/1000d)>0) {
                secondXMotion = (Tween.to(tweenX, -1, (float) (0.9 - (System.currentTimeMillis() - startTime)/1000d)).target(dest.x).ease(TweenEquations.easeNone)).start();
            }
            x = tweenX.get();
            secondXMotion.update(delta);
        }
        if (!firstMovementEndedY) {
            y = tweenY.get() + thisBird.y;
            firstYMotion.update(delta);
        } else {
            if (airshipMoved&&(float) (0.9 - (System.currentTimeMillis() - startTime)/1000d)>0) {
                secondYMotion = (Tween.to(tweenY, -1, (float) (0.9 - (System.currentTimeMillis() - startTime)/1000d)).target(dest.y).ease(TweenEquations.easeNone)).start();
            }
            y = tweenY.get();
            secondYMotion.update(delta);
        }
        lastDest = dest.cpy();
    }
}
