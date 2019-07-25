// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kredatus.flockblockers.CustomLights.CustomPointLight;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Handlers.LightHandler;
import com.kredatus.flockblockers.Handlers.TargetHandler;
import com.kredatus.flockblockers.Handlers.UiHandler;
import com.kredatus.flockblockers.NonGameHandlerScreens.Loader;
import com.kredatus.flockblockers.TweenAccessors.Value;

import java.util.ArrayList;
import java.util.Arrays;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import box2dLight.Light;
import box2dLight.PointLight;


public class Airship {  //engines, sideThrusters, armors and health are organized as lvl1-lvl5
    public  Vector2 pos, vel=new Vector2(), thrusterPosOffset, tweenTarget=new Vector2(); //vel is only used for monitoring not changing pos, lastTouchVel=new Vector2(), acc, dest, lastDest, differenceVector;
    //public boolean was

    //x and y are at middle of textures, bottom of balloonTexture,top of rack
    public static Value pipeHeight=new Value(), pipeWidth=new Value(), balloonWidth=new Value(), balloonHeight=new Value(), rackWidth=new Value(), rackHeight=new Value(), thrusterWidth=new Value(), thrusterHeight=new Value(),tW=new Value(),tH=new Value();
    protected boolean isScrolledDown;
    public float midpointY, midpointX, startY,startX;
    private boolean isAlive;
    private ArrayList<Vector2> turretPositionOffsets = new ArrayList<Vector2>();

    public static int armor, health; //slowdownSpeed;

    public static int rackLvl, burnerLvl, healthLvl, armorLvl, speedLvl;   //Levels: 1-5, rack: 1-5, engine 1-4 //mobility level decides thruster size and how fast you move on screen
    public static TextureRegion balloonTexture, rackTexture, sideThrustTexture, pipeTexture, reticleTexture,
            dragCircleTexture, dragLineTexture, aimLineTexture;    //balloonTexture is top part of hot air balloon, rack is bottom
    public static PointLight dragCircleLight;
    //turretPositionOffsets 28,31    82,31  110-136 and 137-163

    public ArrayList<Turret> turretList=new ArrayList<Turret>(13);
    public Polygon rackHitbox, balloonHitbox, prelimBoundPoly1, prelimBoundPoly2;

    public static int airshipTouchPointer=-1, camWidth, camHeight;
    public float fingerAirshipXDiff, fingerAirshipYDiff;
    //public static boolean airshipTouched;

    public float currentFlashLength;// dragSpeed;
    public boolean isFlashing;
    public Value flashOpacityValue = new Value(), rotation = new Value();
    public Tween flashTween;
    public TweenCallback endFlashing, endOfMovement,endSizeChange;
    protected ArrayList<Float> flashLengths=new ArrayList<Float>();

    public Tween movtween, burnerLightTween, rightThrusterLightTween, leftThrusterLightTween, rotationTween;
    float inputX, inputY, speed;
    public static boolean airshipTouched;

    public static float preX, preY, maxInputX;

    //public static PooledEffect burnerFire, thrusterFireLeft, thrusterFireUp;
    public static Array<PooledEffect> additiveEffects = new Array<PooledEffect>();
    public static Array<ParticleEmitter> firstEmittersOfEachEffect = new Array<ParticleEmitter>();

    //public boolean isMovingLeftAndSlowing, isMovingRightAndSlowing;
    public static Array<Light> flameLights = new Array<Light>(3);
    public float burnerOrigAlpha=0.80f, thrusterOrigAlpha=0.40f;
    public int burnerOrigDist=30, thrusterOrigDist=100;

    public static float[] airshipTint, airShipCloudTint;
    public boolean hitMaxBrightnessCloudBrightening=false;
    public static int[] healthValues=new int[]{100, 200, 350, 550, 800, 1100, 1450,1850,2300,2800}, armorValues={100, 250, 500, 850, 1300, 1850, 2500},
            speedValues={55, 65, 75, 88, 103, 119, 136};
    public TextureRegion[] rackTextures=new TextureRegion[7];
    
    public int nextTurretPosition;
    public float thrusterYposOffset;
    public void healthUp(){
        if (healthLvl<healthValues.length-1) {
            health = healthValues[++healthLvl];
        }
    }
    public void armorUp(){
        if (armorLvl<armorValues.length-1) {
            assignRackAndArmor(++armorLvl, rackLvl);
        }
    }
    public void rackUp(){
        if (rackLvl<4) {
            assignRackAndArmor(armorLvl, ++rackLvl);
            assignRackBounds();
        }
    }
    public void burnerUp(){
        if (burnerLvl<3) {
            setEmitterVal(additiveEffects.get(0).getEmitters().get(3- ++burnerLvl).getEmission(),300,false);
            setEmitterVal(additiveEffects.get(0).getEmitters().get(4+ burnerLvl).getYOffsetValue(),300,false);
        }
    }
    public void speedUp(){
        if (speedLvl<speedValues.length-1) {
            speed = speedValues[++speedLvl];
            thrusterHeight.set((int) (thrusterHeight.get() * (1 + 0.12f * speedLvl)));
            setEmitterVal(firstEmittersOfEachEffect.get(1).getSpawnHeight(), speedLvl * 2, false, false);
            setEmitterVal(firstEmittersOfEachEffect.get(1).getEmission(), firstEmittersOfEachEffect.get(1).getEmission().getHighMax() * (speedLvl/3f), false, false);

            setEmitterVal(firstEmittersOfEachEffect.get(2).getSpawnHeight(), speedLvl * 2, false, false);
            setEmitterVal(firstEmittersOfEachEffect.get(2).getEmission(), firstEmittersOfEachEffect.get(2).getEmission().getHighMax() * (speedLvl/3f), false, false);
            //System.out.println(thrusterHeight+" ssssssssssssssssss");
        }
    }
    float timeToTweenTarget;
    private int reticleRotation;
    public Value reticleSize=new Value(0.9f), dragLineOpacity=new Value(), aimLineOpacity =new Value(); private static Value balloonBob=new Value(-10f); public static Value aimLineRotation=new Value();
    public Tween reticleSizeTween= Tween.to(reticleSize,1,0.9f).target(1.3f).ease(TweenEquations.easeInOutSine).repeatYoyo(Tween.INFINITY,0).start();
    public Tween balloonBobTween= Tween.to(balloonBob,1,1f).target(10f).ease(TweenEquations.easeInOutSine).repeatYoyo(Tween.INFINITY,0).start();
    public Tween dragLineFadeout= Tween.to(dragLineOpacity,1,timeToTweenTarget*0.5f).target(0).ease(TweenEquations.easeInCubic);
    public Tween aimLineFadeout= Tween.to(aimLineOpacity,1,2).target(0).ease(TweenEquations.easeOutSine);
    public static Tween aimLineRotationSmoothing= Tween.to(aimLineRotation,1,1).target(0).ease(TweenEquations.easeInCubic);


    float preAimLineRotation, extraRot;
    float sizeTargetRatio,sizeChangeDur=2f,baseBurnerVelocityPostSizeChange;
    final float tWOrig=62,tHOrig=65;
    public Timeline sizeChangeTween;
    public String sizeChangeType;
    public void survivalToBuyMenu(){
        startX=pos.x;startY=pos.y;//need to reset for all hitboxes and turret turretPositionOffsets that use start as reference

        sizeChangeType="survivalToBuyMenu";
        changeTextureSizes(armorLvl,rackLvl,"buyMenu");
        movtween =Tween.to(pos,0,sizeChangeDur).target(camWidth/2f,camHeight/2f).ease(TweenEquations.easeInOutCubic).start();

        rotation.set(0);
        rotationTween.kill();
        //scaleFireEffects((1+finalNewTexturesSizeRatio)/2f);
    }
    public void backToSurvival(String sizeChangeType){
        if (sizeChangeType==null) this.sizeChangeType="buyMenuToSurvival";
        else this.sizeChangeType="startToSurvival";
        changeTextureSizes(armorLvl,rackLvl,"survival");
        movtween =Tween.to(pos,0,sizeChangeDur).target(camWidth/2f,camHeight/2f).ease(TweenEquations.easeInOutCubic).delay(1f).start();

        if (flameLights.size==0) setupLights(lightHandler);
        rotationTween=Tween.to(rotation,0,2).waypoint((pos.x-(camWidth-balloonWidth.get()))/25f).target(0).ease(TweenEquations.easeOutCirc).start();
        //scaleFireEffects((1+finalNewTexturesSizeRatio)/2f);
        if (sizeChangeType.equals("startToSurvival")) rackSetup();
    }
    public void rackSetup() {
        //burnerUp();burnerUp();burnerUp();rackUp();speedUp();speedUp();
        if (rackLvl!=1) rackUp();
        addTurret('c');addTurret('c');addTurret('d');addTurret('d');addTurret('d');addTurret('f');addTurret('f');addTurret('f');
        turretList.get(1).lvlUp();turretList.get(3).lvlUp();turretList.get(4).lvlUp();turretList.get(4).lvlUp();turretList.get(6).lvlUp();turretList.get(7).lvlUp();turretList.get(7).lvlUp();
        //addTurret('d');turretList.get(0).lvlUp();turretList.get(0).lvlUp();
        for (Turret i :turretList) {
            i.accUp();i.accUp();i.accUp();
            //i.rotUp();i.rotUp();i.rotUp();i.rotUp();i.rotUp();i.rotUp();
        }
        //turretList.get(1).lvlUp();turretList.get(2).lvlUp();turretList.get(2).lvlUp();//max level 3 for now
    }

    int birdStartType;
    Preferences prefs = Gdx.app.getPreferences("skyDefenders");
    BirdHandler birdHandler;TargetHandler targetHandler;LightHandler lightHandler;
    public Airship(GameWorld world, int camWidth, int camHeight, int birdStartType, BirdHandler birdHandler, TargetHandler targetHandler, LightHandler lightHandler) {

        this.lightHandler=lightHandler;
        this.birdStartType=birdStartType;
        this.targetHandler=targetHandler;
        this.birdHandler=birdHandler;
        this.camWidth =camWidth;
        this.camHeight=camHeight;


        healthLvl=prefs.getInteger("healthLvl",0);
        armorLvl=  prefs.getInteger("armorLvl",0);
        rackLvl=    prefs.getInteger("rackLvl",0);
        speedLvl=  prefs.getInteger("speedLvl",0);
        burnerLvl=prefs.getInteger("burnerLvl",0);
        loadTextures();

        loadFireEffects();
        for (int i = 3; i-burnerLvl>0; i--) {//turn off 6/8 of the burnerFire additive effect firstEmittersOfEachEffect (0-2 and 5-7) when burnerLvl==0, 4/8 when burnerLvl==1 etc
            setEmitterVal(additiveEffects.get(0).getEmitters().get(3-i).getEmission(),0,false);
            setEmitterVal(additiveEffects.get(0).getEmitters().get(4+i).getYOffsetValue(),0,false);
        }
        health=healthValues[healthLvl];
        armor=armorValues[armorLvl];
        speed=speedValues[speedLvl];
        startY=camHeight/2f; //-height;
        startX=camWidth/2f; //-balloonWidth;
        pos=new Vector2(startX, startY);

        endOfMovement = new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                tweenTarget.setZero();
            }
        };
        endFlashing = new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                isFlashing = false;
                flashTween = null;
            }
        };
        endSizeChange= new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                if (sizeChangeType.equals("startToSurvival")){
                    assignRackBounds();
                    assignBalloonBounds();
                }
                //baseBurnerVelocityPostSizeChange= additiveEffects.get(0).getEmitters().get(0).getVelocity().getHighMax();
                //System.out.println("baseBurnerVelocityPostSizeChange set to "+baseBurnerVelocityPostSizeChange);
            }
        };

        for (double i = 0.4f; i <= 6; i += 0.1f) { //  5.6/0.05=66 poss, maxes out at a 13 second flash
            flashLengths.add((float) (Math.pow(flashLengths.size(), 0.7) / 3f + 0.3f));//desmos:y=\left(x^{0.5}+0.3\right)
            //else flashLengths.add(   (float) (                (-(Math.pow(-(flashLengths.size()-25),1.32))/50) +1.8f    ));
        }
        //slowdownSpeed=20;
        //dragSpeed=10f;


        setFireColor(birdStartType);
        airshipTint=chooseColorBasedOnWave(birdStartType, true);
        airShipCloudTint=airshipTint.clone();
    }

    private void addTurret(char type){//button will upgrade turret based on position of click choosing which turretPosition on a rack diagram thats blown up on screen when you tap upgrade i.e.
        if (nextTurretPosition< turretPositionOffsets.size()) {
            turretList.add(nextTurretPosition, new Turret(type, turretPositionOffsets.get(nextTurretPosition++),this,birdHandler,targetHandler));   //turretlist(position).lvlUp or whatever upgrades you're giving
        }
    } //draw a screen with a diagram of the upgrades

    private void updateLightDistanceFromAirship() {
        thrusterYposOffset = balloonHeight.get()*0.3f;
        ((CustomPointLight)flameLights.get(0)).distanceFromAirship=new Vector2(0,pipeHeight.get()*3);
        ((CustomPointLight)flameLights.get(1)).distanceFromAirship=new Vector2( -thrusterWidth.get()/1.1f,thrusterYposOffset);
        ((CustomPointLight)flameLights.get(2)).distanceFromAirship=new Vector2(thrusterWidth.get()/1.1f,thrusterYposOffset);
    }
    private void setupLights(LightHandler lightHandler){
        dragCircleLight=LightHandler.newPointLight(lightHandler.backRayHandler, 255,255,255,1,50, new Vector2(0, -camHeight));
        //dragCircleLight.setXray(true);
        //dragCircleLight.setActive(false);
        flameLights.add(LightHandler.newPointLight(new Vector2(-thrusterWidth.get()/1.1f,thrusterYposOffset),lightHandler.foreRayHandler, 255,255,255,thrusterOrigAlpha,0, new Vector2(pos.x, pos.y)));
        flameLights.add(LightHandler.newPointLight(new Vector2(thrusterWidth.get()/1.1f,thrusterYposOffset),lightHandler.foreRayHandler, 255,255,255,thrusterOrigAlpha,0, new Vector2(pos.x, pos.y)));
        flameLights.add(LightHandler.newPointLight(new Vector2(0,pipeHeight.get()*39),lightHandler.foreRayHandler, 255,255,255,burnerOrigAlpha,  0, new Vector2(pos.x,pos.y)));//position same as bottom of burner
        //flameLights.add(LightHandler.newPointLight(LightHandler.foreRayHandler, 255,255,255,burnerOrigAlpha,0, new Vector2(pos.x+15,pos.y+balloonBob.get()+15)));
         //leftThrusterLightTween=Tween.to(flameLights.get(1), 1, 1f).target(thrusterOrigDist).repeatYoyo(2,0);
        //rightThrusterLightTween=Tween.to(flameLights.get(0), 1, 1f).target(thrusterOrigDist).repeatYoyo(2,0);
        burnerLightTween=Tween.to(flameLights.get(0), 1, 1f).target(burnerOrigDist).start();
    }

    float newTurretHeightTarget,newTurretWidthTarget,currentNewTexturesSizeRatio;

    private void loadTextures(){
        rackTextures=Loader.getRacks("rack");
        balloonTexture = Loader.tA.findRegion("balloon");
        sideThrustTexture = Loader.tA.findRegion("sideThruster");
        pipeTexture = Loader.tA.findRegion("burnerPipes");
        reticleTexture = Loader.tA.findRegion("reticle");
        dragCircleTexture=Loader.tA.findRegion("dragCirc");
        dragLineTexture=Loader.tA.findRegion("dragLine");
        aimLineTexture=Loader.tA.findRegion("aimLine");
    }
    private void changeTextureSizes(int armorLvl, int rackLvl, String buyMenuOrSurvivalSizeTarget) {
        if (buyMenuOrSurvivalSizeTarget.equals("buyMenu"))    sizeTargetRatio=1f;
        else if (buyMenuOrSurvivalSizeTarget.equals("survival"))    sizeTargetRatio=0.661f;

            newTurretHeightTarget=tHOrig * sizeTargetRatio;
            newTurretWidthTarget= tWOrig * sizeTargetRatio;

            //System.out.println("***"+newTurretWidthTarget+" "+sizeTargetRatio);


        // 167x195 is res of small balloon, 640x800 is res of big balloon. 44 is turretWidth relative to original rack width of x167
        //make tweens for all these float values from current vals to new ones, including tW and tH, and set one for startX and startY to move towards whatever current pos is
        (sizeChangeTween= Timeline.createParallel().
                push( Tween.to(balloonWidth,0,sizeChangeDur).target((int)(balloonTexture.getRegionWidth()*sizeTargetRatio)).ease(TweenEquations.easeInOutCubic)).
                push( Tween.to(balloonHeight,0,sizeChangeDur).target((int)(balloonTexture.getRegionHeight()*sizeTargetRatio)).ease(TweenEquations.easeInOutCubic)).
                push( Tween.to(thrusterWidth,0,sizeChangeDur).target((int)(sideThrustTexture.getRegionWidth()*sizeTargetRatio)).ease(TweenEquations.easeInOutCubic)).
                push( Tween.to(thrusterHeight,0,sizeChangeDur).target((int)(sideThrustTexture.getRegionHeight()*sizeTargetRatio*(1+0.12f*speedLvl))).ease(TweenEquations.easeInOutCubic)).
                push( Tween.to(rackWidth,0,sizeChangeDur).target((int)(rackTextures[0].getRegionWidth()*sizeTargetRatio)).ease(TweenEquations.easeInOutCubic)).
                push( Tween.to(rackHeight,0,sizeChangeDur).target((int) ( tHOrig*(rackLvl+1) - 20 )*sizeTargetRatio).ease(TweenEquations.easeInOutCubic)).
                push( Tween.to(pipeWidth,0,sizeChangeDur).target((int)(pipeTexture.getRegionWidth()*sizeTargetRatio)).ease(TweenEquations.easeInOutCubic)).
                push( Tween.to(pipeHeight,0,sizeChangeDur).target((int)(pipeTexture.getRegionHeight()*sizeTargetRatio)).ease(TweenEquations.easeInOutCubic)).
                push( Tween.to(tW,0,sizeChangeDur).target(newTurretWidthTarget).ease(TweenEquations.easeInOutCubic)).
                push( Tween.to(tH,0,sizeChangeDur).target(newTurretHeightTarget).ease(TweenEquations.easeInOutCubic))
        ).setCallback(endSizeChange).setCallbackTriggers(TweenCallback.COMPLETE).start();
        //rackpositions needs to be updated every time, as well as startX and startY

        assignRackAndArmor(armorLvl,rackLvl);
    }
    private void assignRackAndArmor(int armorLvl, int rackLvl) {
        rackTexture=rackTextures[armorLvl];
        rackTexture.setRegion(rackTexture, 0, 0, rackTexture.getRegionWidth(), (int) (tHOrig*(rackLvl + 1)  -  20) );

        armor=armorValues[armorLvl];
        assignRackPositions();
    }
    private void updateRackAndPositionsDuringSizeChangeTween(){

        updateLightDistanceFromAirship();
        currentNewTexturesSizeRatio=rackWidth.get()/rackTexture.getRegionWidth();

        if (sizeChangeType.equals("startToSurvival")) scaleFireEffects(1.002f);
        else if (sizeChangeType.equals("survivalToBuyMenu")) scaleFireEffects(1.0042f);
        else if (sizeChangeType.equals("buyMenuToSurvival")) scaleFireEffects(0.9958f);

        assignRackPositions();
    }
    private void assignRackPositions() {
        //System.out.println("positions assigned");
        nextTurretPosition=0;
        float leftXOfRack= -rackWidth.get()/2f;
        for (int i=0;i<=rackLvl;i++) {
            if (i<2) {
                for (int j=0;j<4;j++) {
                    //System.out.println(nextTurretPosition+"lol");
                    turretPositionOffsets.add(nextTurretPosition++,new Vector2(leftXOfRack+j*tW.get()+tW.get()/2f,- i*tH.get() - (tH.get()/2.7f)  ));// /2.8f is cuz we want all guns higher, i is for 1 pixel gap between each level
                }
            } else {
                for (int j=0;j<3;j++) {
                    //System.out.println(nextTurretPosition);
                    turretPositionOffsets.add(nextTurretPosition++, new Vector2(leftXOfRack+j*tW.get()+ tW.get(),- i*tH.get() - (tH.get()/2.7f)  ));
                }
            }
        }
        nextTurretPosition=0;
        for (Turret i : turretList){
            i.distanceFromAirship = turretPositionOffsets.get(nextTurretPosition++);//this sets nextTurretPosition to last turret
        }
        //System.out.println("left");
    }

    private void assignBalloonBounds() {
        float x = pos.x, y = pos.y+balloonBob.get(), rB = balloonWidth.get() / 2f, hB = balloonHeight.get();

        //prelimBoundPoly2 = new Polygon(new float[]{x - balloonWidth / 2f, y, x - balloonWidth / 2f, y + hB, x + balloonWidth / 2f, y + hB, x + balloonWidth / 2f, y});//left side
        //prelimBoundPoly1 = new Polygon(new float[]{x - rackWidth / 2f, y, x - rackWidth / 2f, y - rackHeight, x + rackWidth / 2f, y - rackHeight, x + rackWidth / 2f, y});

        balloonHitbox = new Polygon(new float[]{
                x, y + hB, x - rB * 0.60f, y + hB * 0.92f, x - rB * 0.95f, y + hB * 0.74f, x - rB * 0.88f, y + hB * 0.45f, x - rB * 0.15f, y-tH.get()*0.2f,  //top to bottom left of burner
                x + rB * 0.15f, y-tH.get()*0.2f, x + rB * 0.88f, y + hB * 0.45f, x + rB * 0.95f, y + hB * 0.74f, x + rB * 0.60f, y + hB * 0.92f //to top of balloon
        });
        balloonHitbox.setOrigin(startX,startY);
    }
    private void assignRackBounds() {
        float x = pos.x, y = pos.y+balloonBob.get();
        if (rackLvl==0){
            rackHitbox = new Polygon(new float[] {
                    x - tW.get() * 2, y -tH.get()*0.2f,         x - tW.get() * 2, y - 1 * tH.get() , //bottom left rack
                    x, y - (1 * tH.get()),  //tip of bottom of armor
                    x + tW.get() * 2, y - 1 * tH.get() ,     x + tW.get() * 2, y-tH.get()*0.2f ,     //bottom right of burner
            }
            );
        } else if (rackLvl==1) {
            rackHitbox = new Polygon(new float[]{
                    x - tW.get() * 2, y -tH.get()*0.2f,     x - tW.get() * 2, y - 2 * tH.get() ,//bottom left rack
                    x, y - (2 * tH.get()),  //tip of bottom f armor
                    x + tW.get() * 2, y - 2 * tH.get() ,   x + tW.get() * 2, y -tH.get()*0.2f ,     //bottom right of burner
            }
            );
        } else if (rackLvl==2) {
            rackHitbox = new Polygon(new float[]{
                    x - tW.get() * 2, y -tH.get()*0.2f,     x - tW.get() * 2, y - 2 * tH.get() ,     x - tW.get() * 1.5f, y - 3 * tH.get() ,//bottom left rack
                    x, y - (3 * tH.get()),  //tip of bottom of armor
                    x + tW.get() * 1.5f, y - 3 * tH.get() ,        x + tW.get() * 2, y - 2 * tH.get() ,    x + tW.get() * 2, y -tH.get()*0.2f,     //bottom right of burner
            }
            );
        } else if (rackLvl==3) {
            rackHitbox = new Polygon(new float[]{
                    x - tW.get() * 2, y -tH.get()*0.2f,     x - tW.get() * 2, y - 2 * tH.get() ,     x - tW.get() * 1.5f, y - 4 * tH.get() ,//bottom left rack
                    x, y - (4 * tH.get()),  //tip of bottom of armor
                    x + tW.get() * 1.5f, y - 4 * tH.get() ,          x + tW.get() * 2, y - 2 * tH.get() ,    x + tW.get() * 2, y -tH.get()*0.2f,     //bottom right of burner
            }
            );
        } else if (rackLvl==4) {
            rackHitbox = new Polygon(new float[]{
                    x - tW.get() * 2, y -tH.get()*0.2f,     x - tW.get() * 2, y - 2 * tH.get() ,     x - tW.get() * 1.5f, y - 4 * tH.get() ,      x - tW.get() * 1f, y - 5 * tH.get() ,//bottom left rack
                    x, y - (5 * tH.get()),  //tip of bottom of armor
                    x + tW.get() * 1f, y - 5 * tH.get() , x + tW.get() * 1.5f, y - 4 * tH.get() , x + tW.get() * 2, y - 2 * tH.get() , x + tW.get() * 2, y -tH.get()*0.2f,     //bottom right of burner
            }
            );
        }
        rackHitbox.setOrigin(startX,startY);
    }

    public  boolean pointerOnAirship(int pointer) {
        //System.out.println("NEW TOUCH ON AIRSHIP as y: "+y+", posY: "+pos.y+balloonBob.get()+", x: "+x+", posX: "+x);
        float y = -(InputHandler.scaleY(Gdx.input.getY(pointer))-camHeight), x = InputHandler.scaleX(Gdx.input.getX(pointer));
        return y < pos.y+balloonBob.get() + balloonHeight.get() && y > pos.y+balloonBob.get() - rackHeight.get() && x < pos.x + ((balloonWidth.get() + rackWidth.get()) / 4f) && x > pos.x - ((balloonWidth.get() + rackWidth.get()) / 4f);//average width of airship between balloon and rack
    }

    /*public boolean bothOnCam(float x, float y){
        return x > mvBnds("l") && x < mvBnds("r") && (y < mvBnds("u") && y > mvBnds("d"));
    }*/
    public boolean eitherOnCam(float x, float y) {
        return x > mvBnds("l") && x < mvBnds("r") || (y < mvBnds("u") && y > mvBnds("d"));
    }
    public boolean isOnCam(float input, String xOrY) {
        if (xOrY.equals("x")) return input > mvBnds("l") && input < mvBnds("r");
        else return input < mvBnds("u") && input > mvBnds("d");
    }

    public float mvBnds(String edge) {
        if (edge.equals("l")) return rackWidth.get()/2f;
        else if (edge.equals("r")) return camWidth  - rackWidth.get()/2f;
        else if (edge.equals("u")) return camHeight - balloonHeight.get()/2f;
        else return rackHeight.get()/1.1f;
    }

    private int getAirshipTouchPointer() {
        for (int i = 0; i < 2; i++) {
            if (pointerOnAirship(i)) {
                return i;
            }
        }
        return -1;
    }

    private void setDestAirship(){
        if ( airshipTouchPointer==-1 && Gdx.input.justTouched()) { //if new press and not pressed before
            //System.out.println(InputHandler.scaleX(Gdx.input.getX())+ " *** "+  InputHandler.scaleY(Gdx.input.getY()) );
            airshipTouchPointer=getAirshipTouchPointer();
            if (airshipTouchPointer>=0) {
                //System.out.println("AIRSHIP POINTER TOUCHED");
                airshipTouched=true;
                inputX=InputHandler.scaleX(Gdx.input.getX(airshipTouchPointer));inputY=-(InputHandler.scaleY(Gdx.input.getY(airshipTouchPointer))-camHeight);
                fingerAirshipXDiff=inputX-pos.x;fingerAirshipYDiff=inputY-pos.y+balloonBob.get();//fingerAirshipDiff doesnt change while finger is pressed which is why we get it once here
            }

        } else if (UiHandler.movPad.isTouched() || (airshipTouchPointer>=0 && Gdx.input.isTouched(airshipTouchPointer) &&
                (     Math.abs((inputX+fingerAirshipXDiff)-InputHandler.scaleX(Gdx.input.getX(airshipTouchPointer))  ) >0
                    ||Math.abs((inputY+fingerAirshipYDiff)+(InputHandler.scaleY(Gdx.input.getY(airshipTouchPointer))-camHeight)  ) >0     )     )     ) { //if (after first press) and (airship currently pressed)

            //System.out.println("AIRSHIP POINTER MOVED OR MOVPAD MOVED");
            if (UiHandler.movPad.isTouched()) {
                if (inputX == 0) inputX = pos.x;
                if (inputY == 0) inputY = pos.y+balloonBob.get();
                airshipTouchPointer = -1;
                fingerAirshipXDiff = 0;
                fingerAirshipYDiff = 0;

                //System.out.println(UiHandler.movPad.getKnobPercentX());
                if (UiHandler.movPad.getKnobPercentX() * 5 < -1.5) {
                    fireThruster(2);
                    //System.out.println("Thrust Right");
                } else if (UiHandler.movPad.getKnobPercentX() * 5 > 1.5) {
                    fireThruster(1);
                    // System.out.println("Thrust Left");
                }

                if (isOnCam(inputX + UiHandler.movPad.getKnobPercentX() * 5f,"x")||isOnCam(inputX + UiHandler.movPad.getKnobPercentX() * 150f,"x"))
                    inputX += UiHandler.movPad.getKnobPercentX() * 4f * speed/50f;
                if (isOnCam(inputY + UiHandler.movPad.getKnobPercentY() * 5f,"y")||isOnCam(inputY + UiHandler.movPad.getKnobPercentY() * 150f,"y"))
                    inputY += UiHandler.movPad.getKnobPercentY() * 4f * speed/50f;

            } else {
                inputX = InputHandler.scaleX(Gdx.input.getX(airshipTouchPointer)) - fingerAirshipXDiff;//input with finger touch difference
                inputY = -(InputHandler.scaleY(Gdx.input.getY(airshipTouchPointer)) - camHeight) - fingerAirshipYDiff;

                if (Gdx.input.getDeltaX(airshipTouchPointer) < -3) {
                    fireThruster(2);
                    //System.out.println("Thrust Right");
                } else if (Gdx.input.getDeltaX(airshipTouchPointer) > 3) {
                    fireThruster(1);
                    // System.out.println("Thrust Left");
                }
            }

            if (eitherOnCam(inputX, inputY)) {
                //System.out.println("either are on cam");
                if (!isOnCam(inputY,"y")) {
                    //System.out.println("x is on cam");
                    fingerAirshipYDiff=0;
                    if (inputY>camHeight/2) inputY=mvBnds("u");
                    else inputY=mvBnds("d");

                } else if (!isOnCam(inputX,"x")) {
                    //System.out.println("y is on cam");
                    fingerAirshipXDiff = 0;
                    if (inputX > camWidth / 2) inputX = mvBnds("r");
                    else inputX = mvBnds("l");
                }

                timeToTweenTarget = (float) (Math.sqrt(Math.pow(Math.abs(pos.x - inputX), 2) + Math.pow(Math.abs(pos.y + balloonBob.get() - inputY), 2))) / speed;
                //if (distance/speedDivisor<1.5f){//if distance is so small it takes under 1.5s to get there, take 1.5s anyways
                //    tween = Tween.to(pos, 0, 1.5f).target(inputX, inputY).ease(TweenEquations.easeOutQuint).start();
                //} else {

                movtween = Tween.to(pos, 0, timeToTweenTarget).target(inputX, inputY).ease(TweenEquations.easeOutQuint).setCallback(endOfMovement).start();

                if (!UiHandler.movPad.isTouched()) {
                    if (Math.abs((pos.x - inputX) / 10f)<30) rotationTween = Tween.to(rotation, 0, 1.5f).waypoint((pos.x - inputX) / 10f).target(0).ease(TweenEquations.easeOutCirc).start();
                    else if (pos.x-inputX>0) rotationTween = Tween.to(rotation, 0, 1.5f).waypoint(30).target(0).ease(TweenEquations.easeOutCirc).start();
                    else rotationTween = Tween.to(rotation, 0, 1.5f).waypoint(-30).target(0).ease(TweenEquations.easeOutCirc).start();

                    dragLineOpacity.set(0.4f);

                    tweenTarget.set(inputX + fingerAirshipXDiff, inputY + fingerAirshipYDiff);
                    if (timeToTweenTarget > 2) {
                        dragLineFadeout = Tween.to(dragLineOpacity, 1, timeToTweenTarget * 0.24f).target(-1).ease(TweenEquations.easeInSine).start();
                    } else {
                        dragLineFadeout = Tween.to(dragLineOpacity, 1, timeToTweenTarget * 0.21f).target(-1).ease(TweenEquations.easeInCubic).start();//no delay if very close
                    }

                } else {
                    if (Math.abs((pos.x - inputX)/2)<20) rotationTween = Tween.to(rotation, 0, .7f).waypoint((pos.x - inputX)/2).target(0).ease(TweenEquations.easeOutCirc).start();
                    else if (pos.x-inputX>0) rotationTween = Tween.to(rotation, 0, .7f).waypoint(20).target(0).ease(TweenEquations.easeOutCirc).start();
                    else rotationTween = Tween.to(rotation, 0, .7f).waypoint(-20).target(0).ease(TweenEquations.easeOutCirc).start();
                }
                //rotate to waypoint based on x distance, then back to itself
            }



            //if (inputX > balloonWidth/3f   &&  inputX<camWidth-balloonWidth/3f)pos.x=inputX;
            //if (inputY > rackHeight/3f  &&   inputY<camHeight-balloonHeight/4f) pos.y+balloonBob.get()=inputY;
            //} else if (airshipTouchPointer>=0) {//if (airship pointer not pressed and pointer not reset)
        } else if (airshipTouchPointer>=0&&!Gdx.input.isTouched(airshipTouchPointer)) {
            //System.out.println("AIRSHIP POINTER UNTOUCHED");
            //vel.set(Gdx.input.getDeltaX(airshipTouchPointer)*30,-(Gdx.input.getDeltaY(airshipTouchPointer))*30);

            airshipTouchPointer=-1;
            if (airshipTouched)airshipTouched=false;
        }
    }

    private void scaleFireEffects (float multiplier) {
        additiveEffects.get(0).scaleEffect(multiplier);
        //additiveEffects.get(0).
        additiveEffects.get(1).scaleEffect(multiplier);
        additiveEffects.get(2).scaleEffect(multiplier);
    }
    private void loadFireEffects () {
        //burnerFire=AssetHandler.burnerFirePool.obtain(); thrusterFireLeft=AssetHandler.thrusterFireLeftPool.obtain(); thrusterFireUp=AssetHandler.thrusterFireUpPool.obtain();
        additiveEffects = ((FlockBlockersMain)Gdx.app.getApplicationListener()).loader.additiveEffects;
        additiveEffects.get(0).scaleEffect(0.13f);
        additiveEffects.get(1).scaleEffect(0.30f);
        additiveEffects.get(2).scaleEffect(0.30f);
        firstEmittersOfEachEffect = Loader.firstEmittersOfEachEffect;
        //burnerFire.scaleEffect(0.3f);
        //burnerFire.start();
    }

    public static float[] chooseColorBasedOnWave (int waveNumber, boolean isBalloon) {
        if (!isBalloon){
            if (waveNumber==0) return new float[]{178/255f, 166/255f, 96/255f };
            else if (waveNumber==1) return new float[]{178/255f, 119/255f, 98/255f };
            else if (waveNumber==2) return new float[]{43/255f,  158/255f, 238/255f};
            else if (waveNumber==3) return new float[]{227/255f, 133/255f, 37/255f };
            else if (waveNumber==4) return new float[]{75/255f,  201/255f, 142/255f};
            else if (waveNumber==5) return new float[]{154/255f, 155/255f, 158/255f};
            else if (waveNumber==6) return new float[]{230/255f, 49/255f,  252/255f};
            else if (waveNumber==7) return new float[]{178/255f, 178/255f, 47/255f };
        } else {
                if (waveNumber==0)  return new float[]{255, 180, 148};
            else if (waveNumber==1) return new float[]{249, 50,  109};
            else if (waveNumber==2) return new float[]{43,  158, 238};
            else if (waveNumber==3) return new float[]{227, 133, 37 };
            else if (waveNumber==4) return new float[]{75,  201, 142};
            else if (waveNumber==5) return new float[]{154, 155, 158};
            else if (waveNumber==6) return new float[]{230, 49,  252};
            else if (waveNumber==7) return new float[]{200, 200, 50 };
        }
        return null;
    }

    public static void setFireColor(int waveNumber){
        //System.out.println("Was "+firstEmittersOfEachEffect.get(i).getTint().getColors()[0]+", "+firstEmittersOfEachEffect.get(i).getTint().getColors()[1]+", "+firstEmittersOfEachEffect.get(i).getTint().getColors()[2]);
        //{"pB","tB","wB","fB","aB","nB","lB","gB"};
        //  0    1    2    3    4    5    6    7

        float[] color =null;
        try {
            color = chooseColorBasedOnWave(waveNumber, false);
            assert color!=null;
        } catch (Exception e){
            e.printStackTrace();
        }

        for (ParticleEffectPool.PooledEffect i : additiveEffects){
            for (ParticleEmitter j : i.getEmitters()) {
                j.getTint().setColors(color);
            }
        }
        for (Light j : flameLights) {
            j.setColor(color[0],color[1],color[2],j.getColor().a);
        }
        //dragCircleLight.setColor(color[0],color[1],color[2],dragCircleLight.getColor().a);
        //System.out.println("Was "+firstEmittersOfEachEffect.get(i).getTint().getColors()[0]+", "+firstEmittersOfEachEffect.get(i).getTint().getColors()[1]+", "+firstEmittersOfEachEffect.get(i).getTint().getColors()[2]);
    }

    public void fireThruster(int i){
        firstEmittersOfEachEffect.get(i).allowCompletion();
        if (i==1) {
            setEmitterVal(firstEmittersOfEachEffect.get(i).getAngle(), 180 + rotation.get(), true, true);
            leftThrusterLightTween=Tween.to(flameLights.get(1), 1, 1.5f).target(0).waypoint(thrusterOrigDist).repeatYoyo(0,0).ease(TweenEquations.easeOutQuint).start();
        }
        else if(i==2) {
            setEmitterVal(firstEmittersOfEachEffect.get(i).getAngle(), 0 + rotation.get(), true, true);//thrust right
            rightThrusterLightTween=Tween.to(flameLights.get(2), 1, 1.5f).target(0).waypoint(thrusterOrigDist).repeatYoyo(0,0).ease(TweenEquations.easeOutQuint).start();
        }
        additiveEffects.get(i).start();
    }

    public void setBurnerLightTarget(float target, TweenEquation eq, boolean fast) {
        if (fast) burnerLightTween=Tween.to(flameLights.get(0), 1, 0.25f).target(target).ease(eq).start();
        else burnerLightTween=Tween.to(flameLights.get(0), 1, 1f).target(target).ease(eq).start();
    }

    public void fastBurner() {
        //System.out.println("8, "+firstEmittersOfEachEffect.get(0).getEmission().getHighMax());
        if (firstEmittersOfEachEffect.get(0).getEmission().getHighMax() != 500||firstEmittersOfEachEffect.get(0).getVelocity().getHighMax() != 70) {
            for (ParticleEmitter i : additiveEffects.get(0).getEmitters()) {
                setEmitterVal(i.getEmission(), 500, false, false);
                setEmitterVal(i.getVelocity(), 70, true, false);//always change vel based on airship vel
            }
            additiveEffects.get(0).start();
            setBurnerLightTarget(burnerOrigDist*5,TweenEquations.easeOutElastic, false);
        }
        //setEmitterVal(i.getAngle(), 90 - rotation.get() * 10, true, true);
    }

    public void burnerOnOff() {
            if (vel.y >= 0) {   //if moving up
                //System.out.println("1, "+firstEmittersOfEachEffect.get(0).isComplete());

                for (ParticleEmitter i : additiveEffects.get(0).getEmitters()) {
                    setEmitterVal(i.getVelocity(), 40*currentNewTexturesSizeRatio+(vel.y*5*currentNewTexturesSizeRatio), true, false);//always change vel based on airship vel
                }

                if (vel.y > 1) {   //if moving up fastish and burner set to low (might want to leave out last condition)
                    for (ParticleEmitter i : additiveEffects.get(0).getEmitters()) {
                        setEmitterVal(i.getEmission(), 200 + vel.y * 200, false, false);
                    }
                    additiveEffects.get(0).start();
                    setBurnerLightTarget((vel.y)/1.3f*(burnerOrigDist)+burnerOrigDist, TweenEquations.easeOutElastic, false);
                    //System.out.println("2, "+firstEmittersOfEachEffect.get(0).getEmission().getHighMax());
                } else if (vel.y < 1 && (firstEmittersOfEachEffect.get(0).getEmission().getHighMax() != 200|| firstEmittersOfEachEffect.get(0).isComplete())) {    //if moving slow and burner not set to low, reset
                    for (ParticleEmitter i : additiveEffects.get(0).getEmitters()) {
                        setEmitterVal(i.getEmission(), 200, false, false);
                    }
                    additiveEffects.get(0).start();
                    setBurnerLightTarget( burnerOrigDist, TweenEquations.easeOutElastic, false);
                    //System.out.println("3, "+firstEmittersOfEachEffect.get(0).getEmission().getHighMax());
                }

            } else if (vel.y < -2.5 && !firstEmittersOfEachEffect.get(0).isComplete()) { //if descending let current burner anim finish then turn it off
                for (ParticleEmitter i : additiveEffects.get(0).getEmitters()) {
                    i.allowCompletion();
                }
                if (getLightDist("burner")!=0 && burnerLightTween.getTargetValues()[0]!=0){
                    setBurnerLightTarget( 0, TweenEquations.easeOutCirc, true);
                }
                //System.out.println("4, "+firstEmittersOfEachEffect.get(0).getEmission().getHighMax());
            } else if (vel.y >= -2.5f ) {//if stopped falling go back to flame
                //System.out.println("5, "+firstEmittersOfEachEffect.get(0).getEmission().getHighMax());
                if (firstEmittersOfEachEffect.get(0).getEmission().getHighMax() != 200|| firstEmittersOfEachEffect.get(0).isComplete()) {
                    //System.out.println("6, "+firstEmittersOfEachEffect.get(0).getEmission().getHighMax());
                    for (ParticleEmitter i : additiveEffects.get(0).getEmitters()) {
                        setEmitterVal(i.getEmission(), 200, false, false);
                    }
                    additiveEffects.get(0).start();
                }
                    setBurnerLightTarget(burnerOrigDist, TweenEquations.easeOutElastic, false);
            }
    }

    public int getLightDist(String burnerOrleftThrustOrRightThrust){
        if (burnerOrleftThrustOrRightThrust.equals("burner")){
            return (int) flameLights.get(0).getDistance();
        } else if (burnerOrleftThrustOrRightThrust.equals("leftThrust")) {
            return (int) flameLights.get(1).getDistance();
        } else if (burnerOrleftThrustOrRightThrust.equals("rightThrust")) {
            return (int) flameLights.get(2).getDistance();
        }
        return 0;
    }

    /*public void setLightDist(String burnerOrThrusterLeftOrThrusterRight, int newDist){
        if (burnerOrThrusterLeftOrThrusterRight.equals("burner")){
            if (!BgHandler.isbgVertFast&&newDist>burnerOrigDist*2){
                newDist=burnerOrigDist*2;
            }
            flameLights.get(0).setDistance(newDist); flameLights.get(3).setDistance(newDist);
        } else if (burnerOrThrusterLeftOrThrusterRight.equals("thrusterLeft")) {
            flameLights.get(1).setDistance(newDist);
        } else if (burnerOrThrusterLeftOrThrusterRight.equals("thrusterRight")) {
            flameLights.get(2).setDistance(newDist);
        }
    }*/

    /*public void changeLightAlpha(String burnerOrThruster, float newAlpha){
        Light i=null, j=null;
        if (burnerOrThruster.equals("burner")){
            i = flameLights.get(0); j=flameLights.get(3);
        } else if (burnerOrThruster.equals("thruster")) {
            i = flameLights.get(0); j = flameLights.get(1);
        }
            i.setColor(i.getColor().r,i.getColor().g,i.getColor().b,newAlpha);
            j.setColor(j.getColor().r,j.getColor().g,j.getColor().b,newAlpha);
    }*/

    public void setEmitterVal(ParticleEmitter.RangedNumericValue val, float newVal, boolean retainLowMinMax) {
        if (retainLowMinMax) {
            float amplitude = (val.getLowMax() - val.getLowMin()) / 2f;
            float h1 = newVal + amplitude;
            float h2 = newVal - amplitude;
            val.setLow(h1, h2);
        } else {
            val.setLow(newVal);
        }
    }
    public void setEmitterVal(ParticleEmitter.ScaledNumericValue val, float newVal, boolean retainHighMinMax, boolean changeLowToo) {
            if (retainHighMinMax) {
                float amplitude = (val.getHighMax() - val.getHighMin()) / 2f;
                float h1 = newVal - amplitude;
                float h2 = newVal + amplitude;
                val.setHigh(h1, h2);
            } else {
                val.setHigh(newVal);
            }
            if (changeLowToo) val.setLow(newVal);
    }

    public void update(float delta) {
        if(sizeChangeTween!=null && !sizeChangeTween.isFinished()){sizeChangeTween.update(delta); updateRackAndPositionsDuringSizeChangeTween();}

        aimLineFadeout.update(delta);
        aimLineRotationSmoothing.update(delta);

        reticleSizeTween.update(delta);
        balloonBobTween.update(delta);

        for (ParticleEmitter i : additiveEffects.get(0).getEmitters()) {
            setEmitterVal(i.getAngle(), 90 - rotation.get()*2f, true, true);//always change angle of burner fire based on airship rot
        }

        //System.out.println("isMovingRightAndSlowing: "+isMovingRightAndSlowing+", velX: "+vel.x);
        setDestAirship();
        //System.out.print(pos.toString());
        if (balloonHitbox!=null) {//check if hitbox properly assigned
            rackHitbox.setRotation(rotation.get());
            balloonHitbox.setRotation(rotation.get());
            rackHitbox.setPosition(pos.x - startX, pos.y+balloonBob.get()+ tH.get()/2 - startY);
            balloonHitbox.setPosition(pos.x - startX, pos.y+balloonBob.get()+ tH.get()/2 - startY);
        }
        //System.out.print(BgHandler.isbgVertFast);
        if (BgHandler.isbgVertFast||BgHandler.endWaveBgMotion) {
            fastBurner();
            //System.out.println("very fast");
        } /*else if (firstEmittersOfEachEffect.get(0).getEmission().getHighMax() == 2000){ //if past fastBurning stage, change emission to 200
            for (ParticleEmitter i : additiveEffects.get(0).getEmitters()) {
                setEmitterVal(i.getEmission(), 200, false, false);
            }
            setBurnerLightTarget(burnerOrigDist, TweenEquations.easeOutElastic, false);
            //System.out.println("not very fast and reset");
        }*/

        if (burnerLightTween.isStarted()) burnerLightTween.update(delta);
        if (leftThrusterLightTween!=null && !leftThrusterLightTween.isFinished()) leftThrusterLightTween.update(delta);
        if (rightThrusterLightTween!=null && !rightThrusterLightTween.isFinished()) rightThrusterLightTween.update(delta);

        if (!BgHandler.isbgVertFast&&!BgHandler.endWaveBgMotion) {
            burnerOnOff();//if not moving quickly
            //system.out.println("burner change");
        }

        //0 is burner, 1 is thrustLeft, 2 is thrustRight
        if (!movtween.isFinished()) { //if moving
            dragLineFadeout.update(delta);

            //if (vel.y>-2&&firstEmittersOfEachEffect.get(0).isComplete()) firstEmittersOfEachEffect.get(0).reset();


            additiveEffects.get(1).setPosition(
                    xOffsetDueToRotation(pos.x - thrusterWidth.get() / 2f + 2,
                            (thrusterWidth.get() / 2f - 2), -(thrusterYposOffset + thrusterHeight.get() / 2f - firstEmittersOfEachEffect.get(1).getSpawnHeight().getHighMax()/2f)),

                    yOffsetDueToRotation(pos.y+balloonBob.get() + thrusterYposOffset + thrusterHeight.get() / 2f - firstEmittersOfEachEffect.get(1).getSpawnHeight().getHighMax()/2f,
                            (thrusterWidth.get() / 2f + 2),-(thrusterYposOffset + thrusterHeight.get() / 2f - firstEmittersOfEachEffect.get(1).getSpawnHeight().getHighMax()/2f )));
            //adding a bit of vel for straying thrusters

            additiveEffects.get(2).setPosition(
                    xOffsetDueToRotation(pos.x + thrusterWidth.get() / 2f + 2,
                            -(thrusterWidth.get() / 2f - 2), -(thrusterYposOffset + thrusterHeight.get() / 2f - firstEmittersOfEachEffect.get(2).getSpawnHeight().getHighMax()/2f)),

                    yOffsetDueToRotation(pos.y+balloonBob.get() + thrusterYposOffset + thrusterHeight.get() / 2f - firstEmittersOfEachEffect.get(2).getSpawnHeight().getHighMax()/2f,
                            -(thrusterWidth.get() / 2f + 2), -(thrusterYposOffset + thrusterHeight.get() / 2f - firstEmittersOfEachEffect.get(2).getSpawnHeight().getHighMax()/2f))
            );
            //adding a bit of vel for straying thrusters
            preX=pos.x;
            preY=pos.y+balloonBob.get();
            movtween.update(delta);
            rotationTween.update(delta);
            vel.set(pos.x-preX, pos.y+balloonBob.get()-preY);

            /*if ((vel.x<=0&&tween.getTargetValues()[0]-pos.x<0)||(vel.x>=0&&tween.getTargetValues()[0]-pos.x>0)){
                rotation.get() += -Math.signum(vel.x)*((Math.abs(vel.x*2)-Math.abs(rotation.get()))/1.5f);
            }*/

            //System.out.println("Velocity change, vel: "+vel.x+", preVel: "+(tween.getTargetValues()[0]-pos.x));
            //float temp = vel.x/(2f*(speed/60f));
            //-Math.signum(vel.x)*(temp*temp); //exponent of 2 //(float) (-Math.signum(xVel)*Math.pow(Math.abs(xVel),1.5));//-xVel*2f;

            for (Light i: flameLights){
                if (i instanceof CustomPointLight)  i.setPosition (
                        xOffsetDueToRotation(pos.x                  + ((CustomPointLight) i).distanceFromAirship.x, -((CustomPointLight) i).distanceFromAirship.x,-((CustomPointLight) i).distanceFromAirship.y),
                        yOffsetDueToRotation(pos.y+balloonBob.get() + ((CustomPointLight) i).distanceFromAirship.y, -((CustomPointLight) i).distanceFromAirship.x,-((CustomPointLight) i).distanceFromAirship.y)
                );
            }


            //checkBordersAndSlowdown(); not using velocity

            for (int i = 0; i <= burnerLvl; i++) {
                //change originX x100 multiplier for burnerFire rotation,
                setEmitterVal(additiveEffects.get(0).getEmitters().get(3-i).getYOffsetValue(),yOffsetDueToRotation(pos.y+pipeHeight.get()*3,((pipeWidth.get() * (i+1))-pipeWidth.get()/2f)*150,-(pos.y+pipeHeight.get()*3))/180f,false);
                setEmitterVal(additiveEffects.get(0).getEmitters().get(4+i).getYOffsetValue(),yOffsetDueToRotation(pos.y+pipeHeight.get()*3,(-(pipeWidth.get() * (i+1))+pipeWidth.get()/2f)*150,-(pos.y+pipeHeight.get()*3))/180f,false);
            }
        } else {
            flameLights.get(0).setPosition (
                    xOffsetDueToRotation(pos.x                 +((CustomPointLight) flameLights.get(0)).distanceFromAirship.x,-((CustomPointLight) flameLights.get(0)).distanceFromAirship.x,-((CustomPointLight) flameLights.get(0)).distanceFromAirship.y),
                    yOffsetDueToRotation(pos.y+balloonBob.get()+((CustomPointLight) flameLights.get(0)).distanceFromAirship.y,-((CustomPointLight) flameLights.get(0)).distanceFromAirship.x,-((CustomPointLight) flameLights.get(0)).distanceFromAirship.y) );
            if (!rotationTween.isFinished()) { //finish rotation
                rotationTween.update(delta);
            }
        }

        for (Turret i : turretList) {//update turret position no matter what
            i.update();

            i.pos.set( pos.x + i.distanceFromAirship.x + i.posOffset.x, pos.y+balloonBob.get() + i.distanceFromAirship.y );
        }
        additiveEffects.get(0).setPosition(pos.x- ((additiveEffects.get(0).getEmitters().get(0).getSpawnWidth().getHighMax()) * (burnerLvl+1))/8f+ (float)Math.pow(pipeWidth.get()/10f,4), pos.y+balloonBob.get() + pipeHeight.get()*1.45f); //update burner position no matter what
        //System.out.println(additiveEffects.get(0).getEmitters().get(0).getSpawnWidth().getHighMax()/8f);
    }
    public void drawReticle(SpriteBatch batcher) {
        if (turretList.size() > 0 && !UiHandler.aimPad.isTouched()) {
            if (BgHandler.changingBalloonBrightness) batcher.setColor(airShipCloudTint[0] / 255f, airShipCloudTint[1] / 255f, airShipCloudTint[2] / 255f, 1);
            else batcher.setColor(airshipTint[0] / 255f, airshipTint[1] / 255f, airshipTint[2] / 255f, 1);
            //System.out.println("Turretlist size: " + turretList.size());
            Turret turretAimer = turretList.get(0);
            if (turretAimer.gunTargetPointer != -1&&!pointerOnAirship(turretAimer.gunTargetPointer)) {    //if using finger to aim
                if (!UiHandler.aimPad.isTouched()) UiHandler.aimPad.calculatePositionAndValue(UiHandler.aimPad.getX()+(turretAimer.lastFingerPosition.x-pos.x)*10,UiHandler.aimPad.getY()+(turretAimer.lastFingerPosition.y-pos.y)*10,false);

                batcher.draw(reticleTexture, turretAimer.lastFingerPosition.x - reticleTexture.getRegionWidth() / 3f,
                        turretAimer.lastFingerPosition.y - reticleTexture.getRegionWidth() / 3f,
                        reticleTexture.getRegionWidth() / 3f, reticleTexture.getRegionWidth() / 3f, reticleTexture.getRegionWidth() / 1.5f, reticleTexture.getRegionHeight() / 1.5f,reticleSize.get(),reticleSize.get(),reticleRotation--);

            } else if (turretAimer.targetBird != null) {                                                          //if ai is engaged
                if (!UiHandler.aimPad.isTouched()) UiHandler.aimPad.calculatePositionAndValue(UiHandler.aimPad.getX()+(turretAimer.targetBird.x-pos.x)*10,UiHandler.aimPad.getY()+(turretAimer.targetBird.y-pos.y)*10,false);

                batcher.draw(reticleTexture, turretAimer.targetBird.x - turretAimer.targetBird.width / 3f, turretAimer.targetBird.y - turretAimer.targetBird.width / 3f,
                        turretAimer.targetBird.width/3f, turretAimer.targetBird.width/3f, turretAimer.targetBird.width/1.5f, turretAimer.targetBird.width/1.5f,reticleSize.get(),reticleSize.get(), reticleRotation--);
                    //System.out.println("Draw reticle with width " + turretAimer.targetBird.width);
                if (aimLineOpacity.get()>0){
                    float targetRot=turretList.get(0).targetRot;
                    if (preAimLineRotation>targetRot+180)extraRot+=360;
                    else if (preAimLineRotation<targetRot-180)extraRot-=360;
                    aimLineRotationSmoothing = Tween.to(aimLineRotation, 1, 0.1f).ease(TweenEquations.easeOutCirc).target(targetRot+180+extraRot).start();
                    preAimLineRotation=targetRot;
                }
            }
            batcher.setColor(Color.WHITE);
        }
    }

    public void draw(SpriteBatch batcher, float delta) {
        if (UiHandler.aimPad.isTouched()){
            aimLineOpacity.set(0.4f);
            aimLineFadeout = Tween.to(aimLineOpacity,1,2).target(0).ease(TweenEquations.easeOutSine).start();

            float targetRot=turretList.get(0).targetRot;
            if (preAimLineRotation>targetRot+180)extraRot+=360;
            else if (preAimLineRotation<targetRot-180)extraRot-=360;
            aimLineRotationSmoothing = Tween.to(aimLineRotation, 1, 0.1f).ease(TweenEquations.easeOutCirc).target(targetRot+180+extraRot).start();
            preAimLineRotation=targetRot;
        } //else if (extraRot!=0) extraRot=0;

        Color c = batcher.getColor();
        batcher.setColor(c.r, c.g, c.b, aimLineOpacity.get());

        batcher.draw(aimLineTexture, pos.x, (pos.y - rackHeight.get() / 2 + balloonBob.get()) - aimLineTexture.getRegionHeight() / 2f,
                0, aimLineTexture.getRegionHeight() / 2f,
                aimLineTexture.getRegionWidth(), aimLineTexture.getRegionHeight(), 1, 1, aimLineRotation.get()); //taken from turret
        batcher.setColor(Color.WHITE);


        if (!movtween.isFinished() && !UiHandler.movPad.isTouched()) {
            Color l=dragCircleLight.getColor();
            dragCircleLight.setColor(l.r, l.g, l.b, dragLineOpacity.get());
            if (dragLineOpacity.get()>0) {
                dragCircleLight.setPosition(tweenTarget.x, tweenTarget.y);

                float xDist = tweenTarget.x - pos.x, yDist = (tweenTarget.y - (pos.y + balloonBob.get() + balloonHeight.get() / 2 )), width = (float) Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2)) - dragCircleTexture.getRegionWidth() / 2f;
                float rotation = (float) Math.toDegrees(Math.atan(yDist / xDist));
                /*if        (xDist > 0) { //(xDistance+position.x > position.x) {
                    rotation += 180;
                } else if (yDist > 0) {
                    rotation += 360;
                }*/
                if (xDist <= 0) rotation += 180;
                //System.out.println(rotation);

                c = batcher.getColor();
                batcher.setColor(c.r, c.g, c.b, dragLineOpacity.get());

                batcher.draw(dragLineTexture, pos.x, (pos.y + balloonHeight.get() / 2 + balloonBob.get()) - dragLineTexture.getRegionHeight() / 2,
                        0, dragLineTexture.getRegionHeight() / 2,//Math.abs(xDist)/2f, dragLineTexture.getRegionWidth()/2,//dragLineTexture.getRegionWidth()/2f, dragLineTexture.getRegionHeight()/2f,
                        Math.abs(width), dragLineTexture.getRegionHeight(), 1, 1, rotation);//taken from turret

                batcher.draw(dragCircleTexture, tweenTarget.x - dragCircleTexture.getRegionWidth() / 2, tweenTarget.y - dragCircleTexture.getRegionHeight() / 2,
                        dragCircleTexture.getRegionWidth() / 2, dragCircleTexture.getRegionHeight() / 2,
                        dragCircleTexture.getRegionWidth(), dragCircleTexture.getRegionHeight(), 1, 1, rotation);//full height at top, half height at sides*/
                batcher.setColor(Color.WHITE);
            }
        }

        //Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        //burnerFire.setEmittersCleanUpBlendFunction(false);//can use this to make all textures ghostly, see what blending function actually enables that

            additiveEffects.get(0).draw(batcher, delta);

        if (BgHandler.changingBalloonBrightness) {
            batcher.setColor(airShipCloudTint[0] / 255f, airShipCloudTint[1] / 255f, airShipCloudTint[2] / 255f, 1);

            if (!BgHandler.isMiddleOfCloud) {
                if (!hitMaxBrightnessCloudBrightening ) { //if still getting brighter
                    //System.out.println("Getting brighter");
                    airShipCloudTint[0] += (255 - airShipCloudTint[0]) / 30f;
                    airShipCloudTint[1] += (255 - airShipCloudTint[1]) / 30f;
                    airShipCloudTint[2] += (255 - airShipCloudTint[2]) / 30f;
                } else if (!Arrays.equals(airShipCloudTint,airshipTint)) {   //if past max point and getting darker and brighter than original
                    //System.out.println("Getting darker");
                    airShipCloudTint[0] -= (airShipCloudTint[0]-airshipTint[0]) / 30f;
                    airShipCloudTint[1] -= (airShipCloudTint[1]-airshipTint[1]) / 30f;
                    airShipCloudTint[2] -= (airShipCloudTint[2]-airshipTint[2]) / 30f;
                }
            } else {    //if is moving fast and in the middle of the cloud
                if (!hitMaxBrightnessCloudBrightening) {//so we only do this block once
                    hitMaxBrightnessCloudBrightening=true;
                    //System.out.println("hit middle of cloud");
                    if ((BgHandler.bgNumber-1)%9==0) {    //if changing waves, change colors
                        //System.out.println("New color target");
                        //System.out.println("Bgnumber: "+(bgNumber-2)+", ");
                        setFireColor((BgHandler.bgNumber-1)/9);
                        airshipTint=Airship.chooseColorBasedOnWave((BgHandler.bgNumber-1)/9, true);
                        airShipCloudTint[0]=255f;airShipCloudTint[1]=255f;airShipCloudTint[2]=255f;
                    }
                }
            }
        } else {
            if (hitMaxBrightnessCloudBrightening){ //if stopped going fast and had hit max brightness
                //System.out.println("Not going fast, isFast= "+BgHandler.isbgVertFast);
                hitMaxBrightnessCloudBrightening=false;
                //System.out.println("Done getting darker");
            }
            if (airShipCloudTint[0]!=airshipTint[0]){airShipCloudTint=airshipTint.clone();
            //System.out.println("airship cloud tint");
                }
            //System.out.println("Batcher r set to: "+ (airshipTint[0] / 255f));
            //System.out.println("*******************************************************************************************************************");
            batcher.setColor(airshipTint[0] / 255f, airshipTint[1] / 255f, airshipTint[2] / 255f, 1);
        }

        batcher.draw(balloonTexture, pos.x-(balloonWidth.get())/2f, pos.y+balloonBob.get(),
                balloonWidth.get()/2f, 0, balloonWidth.get(), balloonHeight.get(), 1, 1, rotation.get());

        if (burnerLvl>0) {//if theres 4 pipes or up to 8 with higher burner levels
            for (float i = 2; i < burnerLvl + 2; i++) {
                batcher.draw(pipeTexture, pos.x - (pipeWidth.get() * i), pos.y+balloonBob.get() + 2+currentNewTexturesSizeRatio*2,
                        (pipeWidth.get() * i), -(int)(pipeHeight.get()/2.5f), pipeWidth.get(), pipeHeight.get(), 1, 1, rotation.get()); //originX and Y work as: starting with (0,0) being at the bottom left corner of wherever the image currently is,
                                                                                                                                    // set the point of rotation (irrelevant to world or camera coordinates)
                batcher.draw(pipeTexture, pos.x + (pipeWidth.get() * (i-1)), pos.y+balloonBob.get() + 2+currentNewTexturesSizeRatio*2,
                        -(pipeWidth.get() * (i-1)), -(int)(pipeHeight.get()/2.5f), pipeWidth.get(), pipeHeight.get(), 1, 1, rotation.get());
            }
        }

        c=batcher.getColor();
        //************************* 30% of the tint ****************************
        batcher.setColor((c.r+2f)/3f,(c.g+2f)/3f,(c.b+2f)/3f,c.a);
        batcher.draw(sideThrustTexture, pos.x-thrusterWidth.get()/2f, pos.y+balloonBob.get()+thrusterYposOffset,
                thrusterWidth.get()/2f, -thrusterYposOffset, thrusterWidth.get(), thrusterHeight.get(), 1, 1, rotation.get());

        //************************* 16.66% of the tint ****************************
        c=batcher.getColor();
        batcher.setColor((c.r+1f)/2f,(c.g+1f)/2f,(c.b+1f)/2f,c.a);

        batcher.draw(rackTexture, pos.x-rackWidth.get()/2f, pos.y+balloonBob.get()-rackHeight.get(),
                rackWidth.get()/2f, rackHeight.get(), rackWidth.get(), rackHeight.get(),1,1,rotation.get());


        batcher.setColor(Color.WHITE);//draw balloon and pipes, then set color back to normal


        if (!additiveEffects.get(1).isComplete()) {
            additiveEffects.get(1).draw(batcher, delta);
        }
        if (!additiveEffects.get(2).isComplete()) {
            additiveEffects.get(2).draw(batcher, delta);
        }

        //for (int i=0;i<mobilityLvl+1;i++){ //starting at bottom of balloon, draw different number of thrusters

        if (rotation.get() ==0) {
            for (Turret i : turretList) {
                i.draw(batcher,i.pos.x - i.width / 2f, i.pos.y - i.height / 2f,currentNewTexturesSizeRatio);
            }
        } else {
            for (Turret i : turretList) {
                i.draw(batcher, xOffsetDueToRotation(i.pos.x, -i.distanceFromAirship.x, -i.distanceFromAirship.y) - i.width / 2f,
                        yOffsetDueToRotation(i.pos.y, -i.distanceFromAirship.x, -i.distanceFromAirship.y) - i.height / 2f,
                        currentNewTexturesSizeRatio);
            }
        }
    }

    public void hit(int collisionDmg) {
        health-=collisionDmg;
        isFlashing = true;
        flashOpacityValue.set(1f);//always start from white flash to distinguish from bg
        if (collisionDmg<healthValues[healthLvl]&&health>0){
            currentFlashLength=flashLengths.get((collisionDmg/healthValues[healthLvl])*flashLengths.size());
            flashTween = Tween.to(flashOpacityValue, -1, currentFlashLength).target(0f).ease(TweenEquations.easeOutExpo).setCallback(endFlashing).start();
        } else {
            //currentFlashLength=flashLengths.get(flashLengths.size()-1); //else make flash black (-1f-0f)
            flashOpacityValue.set(1f);    //make a death shader effect

            //.push(Tween.to(flashOpacityValue, -1, 0.3f).target(1f).ease(TweenEquations.easeOutExpo))
            flashTween = Tween.to(flashOpacityValue, -1, 2f).target(-1f).ease(TweenEquations.easeOutExpo).setCallback(endFlashing).start();
        }
        //System.out.println(currentFlashLength);
    }

    public float xOffsetDueToRotation(float x, float originX, float originY ){ //modified .draw from spritebatch, for using the equation that moves the turret's x,y with the rotation of the airship, so they can turn on the spot and stay on the racks
        float worldOriginX = x + originX;
        float fx = -originX;
        float fy = -originY;

        float u = MathUtils.cosDeg(rotation.get());
        float v = MathUtils.sinDeg(rotation.get());
        float x1 = u * fx - v * fy;

        return x1 + worldOriginX;
    }
    public float yOffsetDueToRotation(float y, float originX, float originY ){
        float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;

        float u = MathUtils.cosDeg(rotation.get());
        float v = MathUtils.sinDeg(rotation.get());
        float y1 = v * fx + u * fy;

        return y1+ worldOriginY;
    }
}