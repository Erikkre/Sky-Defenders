// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameObjects.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Birds.BirdAbstractClass;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.Handlers.UiHandler;
import com.kredatus.flockblockers.NonGameHandlerScreens.Loader;
import com.kredatus.flockblockers.TweenAccessors.Value;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

public class MovingImageContainer {
    private Vector2 dest, lastDest, differenceVector;
    public float y1, x1, width, height, x, y, motion1TimePhoenix,motion2TimePhoenix,motion1Time,motion2Time;
    public Value tweenX=new Value(), tweenY=new Value();
    public  Animation animation;public TextureRegion texture;
    public boolean firstMovementEndedX=false, firstMovementEndedY=false, phoenixOrGoldBirdMovementType, airshipMoved;
    public TweenCallback endFirstMovementX, endFirstMovementY,endSecondMovementY;
    public BirdAbstractClass thisBird;
    public Tween firstXMotion, secondXMotion, firstYMotion, secondYMotion;
    public double startTime;
    public Character type;
    /*
    @Overrided
    public void act(float delta){
        //super.act(delta);
        this.setDrawable(new TextureRegionDrawable((TextureRegion) animation.getKeyFrame(GameHandler.runTime/2)));
    }*/

    public MovingImageContainer(String type, float rotation, BirdAbstractClass bird, boolean phoenixOrGoldBirdMovementType){
        if (type.equals("coin")) {
            animation = Loader.coinAnimation;
            width = ((TextureRegion) animation.getKeyFrame(0)).getRegionWidth();height=width;

            dest=UiHandler.goldSymbol.localToParentCoordinates(new Vector2(UiHandler.table1.getX()+UiHandler.goldSymbol.getWidth()/2, UiHandler.table1.getY()));

            this.type='c';
        } else if (type.equals("exp")) {
            texture = Loader.tA.findRegion("exp");
            width = texture.getRegionWidth();height=width;

            dest=UiHandler.lvlLabel.localToParentCoordinates(new Vector2(UiHandler.table0.getX()+UiHandler.lvlLabel.getWidth(), UiHandler.table0.getY()));

            this.type='e';
        } else if (type.equals("diamond")) {
            texture = Loader.tA.findRegion("diamond");
            width = texture.getRegionWidth();height = width;

            dest = UiHandler.diamondLabel.localToParentCoordinates(new Vector2(UiHandler.table1.getX() + UiHandler.diamondLabel.getWidth()/2, UiHandler.table1.getY()));

            this.type = 'd';
        }

        this.phoenixOrGoldBirdMovementType=phoenixOrGoldBirdMovementType;
        this.thisBird=bird;

        if (phoenixOrGoldBirdMovementType) {
            //x+=thisBird.width/7.7f;
           x1 =  (float)(Math.cos(Math.toRadians(rotation)))*55 ;
           y1 =  (float)(Math.sin(Math.toRadians(rotation)))*55;
           //tweenX.set(x);
           //tweenY.set(y);
        } else {
            x1 = (float) (Math.cos(Math.toRadians(rotation))) * (thisBird.width/4 + width/1.5f);
            y1 = (float) (Math.sin(Math.toRadians(rotation))) * (thisBird.width/4 + width/1.5f);
        }


        //lastDest=new Vector2(0,0);

        motion1TimePhoenix=0.7f;
        motion2TimePhoenix=1.7f;
        motion1Time=0.7f;
        motion2Time=1.7f;


        setupTweens();
    }

    private void setupTweens(){
        final MovingImageContainer thisMovingImageContainer =this;
        endSecondMovementY=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                if (type=='c') {
                    GameWorld.gold+=1;
                    UiHandler.goldLabel.setText(GameWorld.gold);

                    ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.goldSymbol.localToParentCoordinates(new Vector2(
                            UiHandler.table1.getX()+UiHandler.goldSymbol.getWidth()/4, UiHandler.table1.getY()+UiHandler.goldSymbol.getHeight()/7)),1,20,1,1);
                } else if (type=='e') {
                    UiHandler.rank.addExp(1);
                    UiHandler.expBar.setValue(UiHandler.rank.expGained);
                    UiHandler.expLabel.setText(UiHandler.rank.expGained);

                    ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.expStack.localToParentCoordinates(new Vector2(UiHandler.expBar.localToParentCoordinates( new Vector2(
                            UiHandler.table0.getX()+UiHandler.expBar.getPercent()*UiHandler.expBar.getWidth(), UiHandler.table0.getY()) ))),1,20,1,1);
                } else if (type=='d') {
                    GameWorld.diamonds+=1;
                    UiHandler.diamondLabel.setText(GameWorld.diamonds);

                    ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.diamondSymbol.localToParentCoordinates(new Vector2(
                            UiHandler.table1.getX()+UiHandler.diamondSymbol.getWidth()/4, UiHandler.table1.getY()+UiHandler.diamondSymbol.getHeight()/7)),1,20,1,1);
                } else if (type=='f') {
                    Airship.fuel+=1;
                    UiHandler.fuelLabel.setText(Airship.fuel);

                    ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.fuelSymbol.localToParentCoordinates(new Vector2(
                            UiHandler.table1.getX()+UiHandler.fuelSymbol.getWidth()/4, UiHandler.table1.getY()+UiHandler.fuelSymbol.getHeight()/7)),1,20,1,1);
                } else if (type=='a') {
                    Airship.ammo+=1;
                    UiHandler.ammoLabel.setText(Airship.ammo);

                    ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.ammoSymbol.localToParentCoordinates(new Vector2(
                            UiHandler.table1.getX()+UiHandler.ammoSymbol.getWidth()/4, UiHandler.table1.getY()+UiHandler.ammoSymbol.getHeight()/7)),1,20,1,1);
                } else if (type=='h'){
                    Airship.health+=1;
                    UiHandler.airshipHealthBar.setValue(Airship.health);UiHandler.airshipHealthLabel.setText(Airship.health);

                    ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.expStack.localToParentCoordinates(new Vector2(UiHandler.airshipHealthBar.localToParentCoordinates( new Vector2(
                            UiHandler.table0.getX()+UiHandler.airshipHealthBar.getPercent()*UiHandler.airshipHealthBar.getWidth(), UiHandler.table0.getY()) ))),1,20,1,1);
                } else if (type=='r'){
                    Airship.armor+=1;
                    UiHandler.airshipArmorBar.setValue(Airship.armor);UiHandler.airshipArmorLabel.setText(Airship.armor);

                    ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.expStack.localToParentCoordinates(new Vector2(UiHandler.airshipArmorBar.localToParentCoordinates( new Vector2(
                            UiHandler.table0.getX()+UiHandler.airshipArmorBar.getPercent()*UiHandler.airshipArmorBar.getWidth(), UiHandler.table0.getY()) ))),1,20,1,1);
                }
                thisBird.dropsList.remove(thisMovingImageContainer);
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

        if (phoenixOrGoldBirdMovementType) {
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
        //dest.set(airshipPos.x,airshipPos.y+ Airship.balloonHeight.get()/2f);
        //differenceVector=dest.cpy().sub(lastDest);
        //airshipMoved=Math.abs(differenceVector.x)>0 || Math.abs(differenceVector.y)>0;

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
        //lastDest = dest.cpy();
    }
    
    public void draw(float runTime, SpriteBatch batcher){
        if (type=='c') {
            if (firstMovementEndedX) {
                batcher.draw((TextureRegion) animation.getKeyFrame(runTime), x - width / 2, y - height / 2,
                        width, height);
            } else {
                batcher.draw((TextureRegion) animation.getKeyFrame(0), x - width / 2, y - height / 2,
                        width, height);
            }
        } else if (type=='e'||type=='d'){
            batcher.draw(texture, x - width / 2, y - height / 2,
                    width, height);
        }
    }
}
