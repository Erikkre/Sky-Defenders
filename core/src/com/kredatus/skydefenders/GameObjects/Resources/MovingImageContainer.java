// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.skydefenders.GameObjects.Resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.skydefenders.Birds.BirdAbstractClass;
import com.kredatus.skydefenders.GameObjects.Airship;
import com.kredatus.skydefenders.GameWorld.GameWorld;
import com.kredatus.skydefenders.Handlers.TargetHandler;
import com.kredatus.skydefenders.Handlers.UiHandler;
import com.kredatus.skydefenders.NonGameHandlerScreens.Loader;
import com.kredatus.skydefenders.SkyDefendersMain;
import com.kredatus.skydefenders.TweenAccessors.Value;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

public class MovingImageContainer {
    private Vector2 dest=new Vector2(), lastDest=new Vector2(), differenceVector=new Vector2();
    public float y1, x1, width, height, x, y, motion1TimePhoenix,motion2TimePhoenix,motion1Time,motion2Time;
    public Value tweenX=new Value(), tweenY=new Value();
    public  Animation animation;public TextureRegion texture;
    public boolean firstMovementEndedX=false, firstMovementEndedY=false, phoenixOrGoldBirdMovementType, airshipMoved;
    public TweenCallback endFirstMovementX, endFirstMovementY,endSecondMovementY;
    public BirdAbstractClass thisBird;
    public Tween firstXMotion, secondXMotion, firstYMotion, secondYMotion;
    public double startTime;
    public Character type;
    public Vector2 startPos;
    Random r = new Random();float goldCoinSpinRandomizationFactor,buyMenuShootOutRandomizationFactor;
    float specificAirshipResourceYdestOffset,slideMenuLeftTime;
    /*
    @Overrided
    public void act(float delta){
        //super.act(delta);
        this.setDrawable(new TextureRegionDrawable((TextureRegion) animation.getKeyFrame(GameHandler.runTime/2)));
    }*/

    public MovingImageContainer(String type, float rotation, BirdAbstractClass bird, boolean phoenixOrGoldBirdMovementType,Vector2 startPos){
        this.startPos=startPos;
        if (type.equals("gold")) {
            goldCoinSpinRandomizationFactor=r.nextFloat();
            //System.out.println(startPos);
            animation = Loader.coinAnimation;
            width = ((TextureRegion) animation.getKeyFrame(0)).getRegionWidth();height=width;
            dest=UiHandler.goldSymbol.localToStageCoordinates(new Vector2(UiHandler.goldSymbol.getWidth()/2,UiHandler.goldSymbol.getHeight()/2));
            this.type='g';
        } else if (type.equals("exp")) {
            texture = ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("exp");
            dest=UiHandler.expBar.localToStageCoordinates(new Vector2(UiHandler.expBar.getPercent()*UiHandler.expBar.getWidth(), UiHandler.expBar.getHeight()/2));
            this.type='e';
            width = texture.getRegionWidth();height = width;
        } else if (type.equals("diamond")) {
            texture = ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("diamond");
            dest = UiHandler.diamondLabel.localToStageCoordinates(new Vector2(UiHandler.diamondLabel.getWidth() / 2, UiHandler.diamondLabel.getHeight() / 2));
            this.type = 'd';
            width = texture.getRegionWidth();height = width;
            System.out.println("set dest");
        } else {//buymenu items
            if (type.equals("health")) {
                //System.out.println(startPos);
                texture = ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("health");
                this.type = 'h';
                specificAirshipResourceYdestOffset=Airship.balloonHeight.get()/2;
            } else if (type.equals("armor")) {
                texture = ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("armor");
                this.type = 'r';
                specificAirshipResourceYdestOffset=-Airship.rackHeight.get()/2;
            } else if (type.equals("fuel")) {
                texture = ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("fuel");
                this.type = 'f';
                specificAirshipResourceYdestOffset=Airship.balloonHeight.get()/10;
            } else if (type.equals("ammo")) {
                texture = ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("ammo");
                this.type = 'a';
                specificAirshipResourceYdestOffset=-Airship.rackHeight.get()/2;
            } else if (type.equals("science")) {
                texture = ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("science");
                this.type = 's';
            }

            if (this.type!='d'&&this.type!='e'&&this.type!='g') {dest.set(Airship.pos.x,Airship.pos.y+specificAirshipResourceYdestOffset+Airship.balloonBob.get());}
            width = texture.getRegionWidth()/1.5f;height = width;
        }

        if (startPos!=null ){//if bought from slide side Menus and position is set
            x=startPos.x;y=startPos.y;tweenX.set(0);tweenY.set(0);
        }

        if (bird!=null||this.type=='g'){//if dropped from birds or gold
            firstMovementEndedY=false;firstMovementEndedX=false;tweenX.set(0);tweenY.set(0);
            this.phoenixOrGoldBirdMovementType=phoenixOrGoldBirdMovementType;
            this.thisBird=bird;
        }
        if (phoenixOrGoldBirdMovementType) {
            //x+=thisBird.width/7.7f;
            x1 =  (float)(Math.cos(Math.toRadians(rotation)))*55 ;
            y1 =  (float)(Math.sin(Math.toRadians(rotation)))*55;

        } else {
            if (thisBird==null&& rotation==0){
                double rot=Math.toDegrees(Math.atan((Airship.pos.y-startPos.y) / (Airship.pos.x-startPos.x)));
                if (Airship.pos.x-startPos.x > 0) { //(xDistance+position.x > position.x) {
                    rot += 180;
                } else if ((Airship.pos.y-startPos.y) > 0) {
                    rot += 360;
                }

                x1 = -(float) (Math.cos(Math.toRadians(rot+  -35+r.nextInt(70))  ) ) * (30);
                y1 = -(float) (Math.sin(Math.toRadians(rot+  -35+r.nextInt(70))  ) ) * (30);

            } else {
                x1 = (float) (Math.cos(Math.toRadians(rotation))) * (thisBird.width / 4 + width / 1.5f);
                y1 = (float) (Math.sin(Math.toRadians(rotation))) * (thisBird.width / 4 + width / 1.5f);
            }
        }
        motion1TimePhoenix=0.7f;
        motion2TimePhoenix=1.7f;
        motion1Time=0.7f;
        motion2Time=1.7f;
        slideMenuLeftTime=0.7f;

        lastDest=dest.cpy() ;
        setupTweens(TargetHandler.resourceGather);
    }

    private void setupTweens(final Sound gatherSound) {
        final MovingImageContainer thisMovingImageContainer =this;
        endSecondMovementY=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {

                if (type=='g') {
                    GameWorld.gold+=1;
                    UiHandler.goldLabel.setText(GameWorld.gold);

                    if (!GameWorld.soundMuted) gatherSound.play(0.07f,0.50f+ 0.0007f*UiHandler.goldGatherStreak,0); //if resource collected in last 300ms set pitch 0.05f higher, else, stop setting pitch higher

                    if (System.currentTimeMillis()-UiHandler.lastGoldGatherTime<UiHandler.maxStreakInterval) UiHandler.goldGatherStreak++;
                        if (UiHandler.goldGatherFuture!=null) UiHandler.goldGatherFuture.cancel(true);
                        UiHandler.goldGatherFuture=UiHandler.timer.schedule(new Runnable() {
                            @Override
                            public void run() {
                                    ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.goldSymbol.localToStageCoordinates(new Vector2(
                                            UiHandler.goldSymbol.getWidth() / 4, UiHandler.goldSymbol.getHeight() / 7)), UiHandler.goldGatherStreak, 35, 1, 1, 1, 1);
                                    UiHandler.goldGatherStreak=1;
                             }
                        }, (long) (UiHandler.maxStreakInterval/1.5), TimeUnit.MILLISECONDS);

                    UiHandler.lastGoldGatherTime=System.currentTimeMillis();

                } else if (type=='e') {
                    UiHandler.rank.addExp(1);
                    UiHandler.expBar.setValue(UiHandler.rank.expGained);
                    UiHandler.expLabel.setText(UiHandler.rank.expGained);

                    if (!GameWorld.soundMuted) gatherSound.play(0.07f,0.55f+ 0.0010f*UiHandler.expGatherStreak,0); //if resource collected in last 200ms set pitch 0.05f higher, else, stop setting pitch higher

                    if (System.currentTimeMillis()-UiHandler.lastExpGatherTime<UiHandler.maxStreakInterval) UiHandler.expGatherStreak++;
                    if (UiHandler.expGatherFuture!=null) UiHandler.expGatherFuture.cancel(true);
                    UiHandler.expGatherFuture=UiHandler.timer.schedule(new Runnable() {
                        @Override
                        public void run() {
                            ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.expBar.localToStageCoordinates(new Vector2(
                                    UiHandler.expBar.getPercent()*UiHandler.expBar.getWidth(), UiHandler.expBar.getHeight()/7)),UiHandler.expGatherStreak,35,1,1,1,1);
                            UiHandler.expGatherStreak=1;
                        }
                    }, (long) (UiHandler.maxStreakInterval/1.5), TimeUnit.MILLISECONDS);

                    UiHandler.lastExpGatherTime=System.currentTimeMillis();

                } else if (type=='d') {
                    GameWorld.diamond +=1;
                    UiHandler.diamondLabel.setText(GameWorld.diamond);

                    if (!GameWorld.soundMuted) gatherSound.play(0.07f,0.55f+ 0.0010f*UiHandler.diamondGatherStreak,0); //if resource collected in last 200ms set pitch 0.05f higher, else, stop setting pitch higher

                    if (System.currentTimeMillis()-UiHandler.lastExpGatherTime<UiHandler.maxStreakInterval) UiHandler.diamondGatherStreak++;
                    if (UiHandler.diamondGatherFuture!=null) UiHandler.diamondGatherFuture.cancel(true);
                    UiHandler.diamondGatherFuture=UiHandler.timer.schedule(new Runnable() {
                        @Override
                        public void run() {
                            ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.diamondSymbol.localToStageCoordinates(new Vector2(
                                    UiHandler.diamondSymbol.getWidth()/4, UiHandler.diamondSymbol.getHeight()/7)),UiHandler.diamondGatherStreak,35,1,1,1,1);
                            UiHandler.diamondGatherStreak=1;
                        }
                    }, (long) (UiHandler.maxStreakInterval/1.5), TimeUnit.MILLISECONDS);

                    UiHandler.lastDiamondGatherTime=System.currentTimeMillis();

                } else if (type=='f') {
                    if (Airship.fuel<1) UiHandler.fuelLabel.setColor(1,1,1,1);
                    Airship.fuel+=1;
                    UiHandler.fuelLabel.setText(Integer.toString((int)Airship.fuel));


                    if (!GameWorld.soundMuted) gatherSound.play(0.17f,0.55f + 0.0010f*UiHandler.fuelGatherStreak,0); //if resource collected in last 200ms set pitch 0.05f higher, else, stop setting pitch higher

                    if (System.currentTimeMillis()-UiHandler.lastFuelGatherTime<UiHandler.maxStreakInterval) UiHandler.fuelGatherStreak++;
                    if (UiHandler.fuelGatherFuture!=null) UiHandler.fuelGatherFuture.cancel(true);
                    UiHandler.fuelGatherFuture=UiHandler.timer.schedule(new Runnable() {
                        @Override
                        public void run() {
                            ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.fuelSymbol.localToStageCoordinates(new Vector2(
                                    UiHandler.fuelSymbol.getWidth()/4, UiHandler.fuelSymbol.getHeight()/7)),UiHandler.fuelGatherStreak,35,1,1,1,1);
                            UiHandler.fuelGatherStreak=1;
                        }
                    }, (long) (UiHandler.maxStreakInterval/1.5), TimeUnit.MILLISECONDS);

                    UiHandler.lastFuelGatherTime=System.currentTimeMillis();

                } else if (type=='a') {
                    if (Airship.ammo<1) UiHandler.ammoLabel.setColor(1,1,1,1);
                    Airship.ammo+=1;
                    UiHandler.ammoLabel.setText(Airship.ammo);

                    if (!GameWorld.soundMuted) gatherSound.play(0.07f,0.55f+ 0.0010f*UiHandler.ammoGatherStreak,0); //if resource collected in last 200ms set pitch 0.05f higher, else, stop setting pitch higher

                    if (System.currentTimeMillis()-UiHandler.lastAmmoGatherTime<UiHandler.maxStreakInterval) UiHandler.ammoGatherStreak++;
                    if (UiHandler.ammoGatherFuture!=null) UiHandler.ammoGatherFuture.cancel(true);
                    UiHandler.ammoGatherFuture=UiHandler.timer.schedule(new Runnable() {
                        @Override
                        public void run() {
                            ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.ammoSymbol.localToStageCoordinates(new Vector2(
                                    UiHandler.ammoSymbol.getWidth()/4, UiHandler.ammoSymbol.getHeight()/7)),UiHandler.ammoGatherStreak,35,1,1,1,1);
                            UiHandler.ammoGatherStreak=1;
                        }
                    }, (long) (UiHandler.maxStreakInterval/1.5), TimeUnit.MILLISECONDS);

                    UiHandler.lastAmmoGatherTime=System.currentTimeMillis();

                } else if (type=='h'){
                    Airship.health+=1;
                    UiHandler.airshipHealthBar.setValue(Airship.health);UiHandler.airshipHealthLabel.setText(Airship.health);

                    if (!GameWorld.soundMuted) gatherSound.play(0.07f,0.55f+ 0.0010f*UiHandler.healthGatherStreak,0); //if resource collected in last 200ms set pitch 0.05f higher, else, stop setting pitch higher

                    if (System.currentTimeMillis()-UiHandler.lastHealthGatherTime<UiHandler.maxStreakInterval) UiHandler.healthGatherStreak++;
                    if (UiHandler.healthGatherFuture!=null) UiHandler.healthGatherFuture.cancel(true);
                    UiHandler.healthGatherFuture=UiHandler.timer.schedule(new Runnable() {
                        @Override
                        public void run() {
                            ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.airshipHealthBar.localToStageCoordinates(new Vector2(
                                    UiHandler.airshipHealthBar.getPercent()*UiHandler.airshipHealthBar.getWidth(), UiHandler.airshipHealthBar.getHeight()/7)),UiHandler.healthGatherStreak,35,1,1,1,1);
                            UiHandler.healthGatherStreak=1;
                        }
                    }, (long) (UiHandler.maxStreakInterval/1.5), TimeUnit.MILLISECONDS);

                    UiHandler.lastHealthGatherTime=System.currentTimeMillis();

                } else if (type=='r'){
                    Airship.armor+=1;
                    UiHandler.airshipArmorBar.setValue(Airship.armor);UiHandler.airshipArmorLabel.setText(Airship.armor);

                    if (!GameWorld.soundMuted) gatherSound.play(0.07f,0.55f+ 0.0010f*UiHandler.armorGatherStreak,0); //if resource collected in last 200ms set pitch 0.05f higher, else, stop setting pitch higher

                    if (System.currentTimeMillis()-UiHandler.lastArmorGatherTime<UiHandler.maxStreakInterval) UiHandler.armorGatherStreak++;
                    if (UiHandler.armorGatherFuture!=null) UiHandler.armorGatherFuture.cancel(true);
                    UiHandler.armorGatherFuture=UiHandler.timer.schedule(new Runnable() {
                        @Override
                        public void run() {
                            ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(UiHandler.airshipArmorBar.localToStageCoordinates(new Vector2(
                                    UiHandler.airshipArmorBar.getPercent()*UiHandler.airshipArmorBar.getWidth(), UiHandler.airshipArmorBar.getHeight()/7)),UiHandler.armorGatherStreak,35,1,1,1,1);
                            UiHandler.armorGatherStreak=1;
                        }
                    }, (long) (UiHandler.maxStreakInterval/1.5), TimeUnit.MILLISECONDS);

                    UiHandler.lastArmorGatherTime=System.currentTimeMillis();
                }

                if (thisBird==null) ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.boughtItemsList.remove(thisMovingImageContainer);
                else thisBird.dropsList.remove(thisMovingImageContainer);

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
            secondXMotion=(Tween.to(tweenX, -1, motion2TimePhoenix).target(dest.x).ease(TweenEquations.easeInCubic));

            firstYMotion=(Tween.to(tweenY, -1, motion1TimePhoenix).target(y1).ease(TweenEquations.easeNone)).setCallback(endFirstMovementY).start();
            secondYMotion=(Tween.to(tweenY, -1, motion2TimePhoenix).target(dest.y).ease(TweenEquations.easeInCubic)).setCallback(endSecondMovementY);
        } else if (startPos==null){
            firstXMotion = Tween.to(tweenX, -1, motion1Time).target(x1).ease(TweenEquations.easeInBounce).setCallback(endFirstMovementX).start();
            secondXMotion= Tween.to(tweenX, -1, motion2Time).target(dest.x).ease(TweenEquations.easeNone);

            firstYMotion = Tween.to(tweenY, -1, motion1Time).target(y1).ease(TweenEquations.easeInBounce).setCallback(endFirstMovementY).start();
            secondYMotion= Tween.to(tweenY, -1, motion2Time).target(dest.y).ease(TweenEquations.easeNone).setCallback(endSecondMovementY);
        } else {
             firstXMotion = Tween.to(tweenX, -1, slideMenuLeftTime/2f).target(x1).ease(TweenEquations.easeInSine).setCallback(endFirstMovementX).start();
             secondXMotion= Tween.to(tweenX, -1, slideMenuLeftTime).target(dest.x).ease(TweenEquations.easeNone);

             firstYMotion = Tween.to(tweenY, -1, slideMenuLeftTime/2f).target(y1).ease(TweenEquations.easeInSine).setCallback(endFirstMovementY).start();
             secondYMotion= Tween.to(tweenY, -1, slideMenuLeftTime).target(dest.y).ease(TweenEquations.easeNone).setCallback(endSecondMovementY);
         }

        startTime=System.currentTimeMillis();
    }

    public void update(float delta,Vector2 airshipPos){
        //if (type=='d') System.out.println("Dest: "+dest+", x:"+x+", y:"+y);
        if (type!='g'&&type!='e'&&type!='d') {
            dest.set(airshipPos.x,airshipPos.y+specificAirshipResourceYdestOffset+Airship.balloonBob.get());
            differenceVector=dest.cpy().sub(lastDest);
            airshipMoved=Math.abs(differenceVector.x)>0 || Math.abs(differenceVector.y)>0;
        }

        //System.out.println("lol"+(slideMenuLeftTime - (System.currentTimeMillis() - startTime)/1000d));
        if (!firstMovementEndedX) {
            if (thisBird!=null) x = tweenX.get() + thisBird.x;
            else {
                x = tweenX.get() + startPos.x; //if startPosition not null
                if (airshipMoved&&(float) (slideMenuLeftTime - (System.currentTimeMillis() - startTime)/1000d)>0.01) { //if airship moves keep moving object over 0.9s using new secondXmotion
                    double rot=Math.toDegrees(Math.atan((Airship.pos.y-startPos.y) / (Airship.pos.x-startPos.x)));
                    x1 = (float) (Math.cos(Math.toRadians(rot+  -35+r.nextInt(70))  ) ) * (30);
                    y1 = (float) (Math.sin(Math.toRadians(rot+  -35+r.nextInt(70))  ) ) * (30);

                    firstXMotion = Tween.to(tweenX, -1, (float) (slideMenuLeftTime - (System.currentTimeMillis() - startTime)/1000d)).target(x1).ease(TweenEquations.easeNone).setCallback(endFirstMovementX).start();
                }
            }
            firstXMotion.update(delta);

        } else {
            if (airshipMoved&&(float) (slideMenuLeftTime - (System.currentTimeMillis() - startTime)/1000d)>0.01) { //if airship moves keep moving object over 0.9s using new secondXmotion
                dest.set(Airship.pos.x,Airship.pos.y+specificAirshipResourceYdestOffset+Airship.balloonBob.get());

                secondXMotion = (Tween.to(tweenX, -1, (float) (slideMenuLeftTime - (System.currentTimeMillis() - startTime)/1000d)).target(dest.x).ease(TweenEquations.easeNone)).start();
            }

            x = tweenX.get();
            secondXMotion.update(delta);
        }
        if (!firstMovementEndedY) {
            if (thisBird!=null) y = tweenY.get() + thisBird.y;
            else {
                if (type!='g'&&airshipMoved&&(float) (slideMenuLeftTime - (System.currentTimeMillis() - startTime)/1000d)>0.01) { //if airship moves keep moving object over 0.9s using new secondXmotion
                    firstYMotion = Tween.to(tweenY, -1, (float) (slideMenuLeftTime - (System.currentTimeMillis() - startTime)/1000d)).target(y1).ease(TweenEquations.easeNone).setCallback(endFirstMovementY).start();
                }
                y = tweenY.get() + startPos.y; //if startPosition not null
            }
            firstYMotion.update(delta);

        } else {
            if (airshipMoved&&(float) (slideMenuLeftTime - (System.currentTimeMillis() - startTime)/1000d)>0.01) {

                secondYMotion = (Tween.to(tweenY, -1, (float) (slideMenuLeftTime - (System.currentTimeMillis() - startTime)/1000d)).target(dest.y).ease(TweenEquations.easeNone)).setCallback(endSecondMovementY).start();
            }
            y = tweenY.get();
            secondYMotion.update(delta);
        }
        lastDest = dest.cpy();
    }
    
    public void draw(float runTime, SpriteBatch batcher){
        if (type=='g') {
                batcher.draw((TextureRegion) animation.getKeyFrame(runTime+goldCoinSpinRandomizationFactor), x - width / 2, y - height / 2,
                        width, height);
        } else {
            batcher.draw(texture, x - width / 2, y - height / 2,
                    width, height);
        }
    }
}
