// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.Background;
import com.kredatus.flockblockers.GameObjects.TinyBird;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.Helpers.InvertedTweenEquations;
import com.kredatus.flockblockers.TweenAccessors.Value;

import java.util.ArrayList;
import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import box2dLight.RayHandler;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class BgHandler {
    private  boolean endWaveBgMotion;
    // BgHandler will create all five objects that we need.
    private Background background, background2, background3, background4;
    private Random r = new Random();
    // BgHandler will use the constants below to determine
    // how fast we need to scroll and also determine
    public static Value horiz =new Value(), vert=new Value(), shake=new Value();
    // Capital letters are used by convention when naming constants.
    boolean same = false;
    /*private ArrayList<Boost> boostlist = new ArrayList<Boost>(), invboostlist = new ArrayList<Boost>(),
            flipboostlist = new ArrayList<Boost>(), invflipboostlist = new ArrayList<Boost>();*/
    public ArrayList<Vector3> boostcoords = AssetHandler.getBoostcoords();
    int w = AssetHandler.boost.getWidth();
    int h = AssetHandler.boost.getHeight();
    //private Boost tempBoost;
    //private int orgBoostnumber = AssetHandler.getBoostnumber(), coordslistsize=AssetHandler.getcoordslistsize();
    public static int bgw= AssetHandler.bgPhoenixtexture.getRegionWidth();
    public static int bgh = AssetHandler.bgPhoenixtexture.getRegionHeight(), separatorHeight=AssetHandler.bgCloudSeparatorTexture.getRegionHeight();    //height of separator is different, and there are 2 combined with eachother
    float x, y, width, height;
    public int bgNumber;

    //private TweenManager manager;
    public Timeline horizPositionBg, vertPositionBg,smallShake, bigShake;
    private TweenCallback startStoryIntroAndSpawns, backgroundStackReset, shakeCamCallback, endBirdSpawn;
    public static float camHeight, camWidth, bgStackHeight=separatorHeight+bgh+bgh;
    public static boolean isPastStoryIntro, isCameraShake, isBirdSpawning, stackJustReset;
    private OrthographicCamera cam;
    private GameRenderer renderer;
    private int bgStackStartYHeight;
    public static RayHandler rayHandler;

    public BgHandler(float camWidth, float camHeight){
        //bgStackStartYHeight= (int)(separatorHeight/2-camHeight/2);
        this.camHeight=camHeight;
        this.camWidth=camWidth;
        bgNumber = 0;//9*BirdHandler.waveTypeCnt;
        //System.out.print("Start height of bg1: "+-bgStackStartYHeight);
        horiz.setValue(0);
        vert.setValue(0);//everything is done in negative (camera goes up by that amount
        shake.setValue(0);                              //but in reality its the bg's lowering by that much

        background = new Background(horiz.getValue(), vert.getValue(), bgw, separatorHeight, AssetHandler.bgList.get(bgNumber++));
        background2 = new Background(horiz.getValue(), background.getTailY(), bgw, bgh, AssetHandler.bgList.get(bgNumber++));

        //this.manager= SplashScreen.getManager();
        //System.out.println("vert.getValue() " + vert.getValue());
        isCameraShake=false;
        setupTweens(camWidth, camHeight);
        isPastStoryIntro=true;
        //isBirdSpawning=true;

        TinyBirdHandler.addTinyBirdsNextCity(camWidth,camHeight);
    }

    private void setupTweens(final float camWidth, final float camHeight){
        //final float camHeight2=camHeight;
        startStoryIntroAndSpawns=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                System.out.println("Start Spawning");
               if (!isPastStoryIntro && GameWorld.isFirstTime){
                    isPastStoryIntro=false;
                    horizPositionBg.pause();
                    vertPositionBg.pause();
                } else {
                   isBirdSpawning=true;
               }
            }
        };

        endBirdSpawn=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                    isBirdSpawning=false;
                    System.out.println("end spawning");
            }
        };



        shakeCamCallback=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                isCameraShake=true;   //reset camera
                if (!smallShake.isStarted()){smallShake.start();}else{ smallShake.resume();}
            }
        };

        (horizPositionBg = Timeline.createSequence()
                .push(Tween.to(horiz, -1, 15f).target((camWidth)-bgw).ease(TweenEquations.easeInOutSine)))
                .repeatYoyo(Tween.INFINITY, 0).start();
//System.out.println("First easing target: "+(-bgh+camHeight/2)  /2);

        /*if (GameWorld.isFirstTime){
            (vertPositionBg = Timeline.createSequence()  //5 4.5f 4.5f 2 7 .1, 8 and 60 repeats
                    .push(Tween.to(vert,-1, 1f ).target(-bgh+(.5f*camHeight)).ease(TweenEquations.easeInOutSine)      )//midpoint
                    .push(Tween.to(vert, -1, 3f).target(  -bgh+(1.1f*camHeight)).ease(TweenEquations.easeInOutSine))                                           //0.733 camheight below
                    .push(Tween.to(vert, -1, 3f).target(  -bgh-( .1f*camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(1, 0))                          //0.733 camheight above
                    .push(Tween.to(vert, -1,2f).target(-bgh+(.5f*camHeight)).ease(TweenEquations.easeInOutSine).setCallback(endBirdSpawn)     )                                       //midpoint

                    .push(Tween.to(vert, -1, 8).target((-bgh*2)).ease(TweenEquations.easeInElastic)     )                    //top edge+bgh/50
                    .push((Tween.to(vert, -1, 0.1f).target((-bgh*2+camHeight/80)).ease(TweenEquations.easeInOutSine).repeatYoyo(1, 0)).setCallback(backgroundStackReset).setCallback(startStoryIntroAndSpawns))      )       //top edge

                    //.push(Tween.to(vert,-1,6).target(-bgh*2).ease(TweenEquations.easeInCubic)          .setCallback(backgroundStackReset))                   )
                    .repeat(Tween.INFINITY, 0).start();
        }*/

/*
        (vertPositionBg = Timeline.createSequence()  //55 20 .01, 8 and 800 repeats
                //.push(Tween.to(vert, -1, 13).target(  -bgh+(1.1f*camHeight)).ease(TweenEquations.easeInOutQuart).setCallback(startStoryIntroAndSpawns).setCallbackTriggers(TweenCallback.START)   )                 //center is 0.6 camheight below, cam is centered on -0.5 to everything
                .push((Tween.to(vert, -1, 30).target(  -bgh-(.15f*camHeight)).ease(TweenEquations.easeOutSine)).setCallback(startStoryIntroAndSpawns).setCallbackTriggers(TweenCallback.BEGIN) ) //center is 0.65 camheight  above

                .push((Tween.to(vert, -1, 20).target((-bgh*2)+(0.499f*camHeight)).ease(TweenEquations.easeInOutQuart)).setCallback(endBirdSpawn)     )  //cam center 0.001 above edge
                .push(Tween.to(vert, -1, 0.02f).target((-bgh*2)+(0.501f*camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(400, 0)         )          //cam 0.001 below edge
                .push((Tween.to(vert, -1, 10).target((-bgh*2)).ease(TweenEquations.easeInSine)).setCallback(backgroundStackReset))      )   //cam 0.03 below edge

                //.push(Tween.to(vert,-1,6).target(-bgh*2).ease(TweenEquations.easeInCubic)          .setCallback(backgroundStackReset))                   )
                .repeat(Tween.INFINITY, 0).start();
*/

        float smallShakeMaxAngle=0.1f;
        int bigShakeMaxAngle=30;
        smallShake= Timeline.createSequence()
                .push(Tween.to(shake,-1, .01f ).target((-1+2*r.nextFloat())*smallShakeMaxAngle).ease(TweenEquations.easeInOutSine)) //camera shake between cities
                .repeatYoyo(Tween.INFINITY, 0);
        bigShake=  Timeline.createSequence()
                .push(Tween.to(shake,-1, .5f ).target((-1+2*r.nextFloat())*bigShakeMaxAngle).ease(TweenEquations.easeInOutSine))
                .repeatYoyo(Tween.INFINITY, 0);


        backgroundStackReset=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                TinyBirdHandler.addVertValueToBirdsSurvivingWavePart(vert.getValue());
                TinyBirdHandler.addTinyBirdsNextCity(camWidth, camHeight);
                System.out.println("Reset stack");
                stackJustReset=true;
                if (bgNumber == AssetHandler.bgList.size()) {
                    //waveNumber+=1;
                    bgNumber = 0;
                }
                LightHandler.newBgLighting(bgNumber);
                if (background.addedY>background2.addedY){ background.reset(background2.getTailY(), bgNumber++);}//lights for new backgrounds
                else { background2.reset(background.getTailY(), bgNumber++);}
                background2.addedY = 0;
                background.addedY = 0;
                //vert.setValue(-bgStackStartYHeight);
                //cam.normalizeUp();
                renderer.setRotate(-(float) Math.atan2(cam.up.x, cam.up.y) * MathUtils.radiansToDegrees);
                isCameraShake = false;
                smallShake.pause();
                vert.setValue(0);
                //}
                //}
            }
        };
        regularVertBgMotion();
    }


    private void  regularVertBgMotion(){
        if (!FlockBlockersMain.fastTest) {
            System.out.println("First target: "+bgStackHeight+", separator height: "+separatorHeight+", bgh: "+bgh);
            (vertPositionBg = Timeline.createSequence()  //15 sec duration
                    .push((((Tween.to(vert, -1, 15).delay(1.2f).targetRelative(-bgStackHeight).ease(InvertedTweenEquations.QuartInOut2LongerMiddle).setCallback(backgroundStackReset).setCallbackTriggers(TweenCallback.END))
                    )).repeat(2,0))//complete is after repetitions done, end is after each repetition

                    .push(Tween.to(vert, -1, 5).targetRelative(vert.getValue()).setCallback(endBirdSpawn).setCallbackTriggers(TweenCallback.START)) //start spawning after wave done
                    //pause in clouds after waves over to have the effect of travelling to different bird type areas, only going horizontally
            ).setCallback(startStoryIntroAndSpawns).setCallbackTriggers(TweenCallback.START).repeat(Tween.INFINITY, 0).start();

        } else {
            System.out.println("First target: "+bgStackHeight+", separator height: "+separatorHeight+", bgh: "+bgh);
            (vertPositionBg = Timeline.createSequence()  //-1 so it happens slightly before reset with added y
                    .push((((Tween.to(vert, -1, 5).delay(1.2f).targetRelative(-bgStackHeight).ease(InvertedTweenEquations.QuartInOut2LongerMiddle).setCallback(backgroundStackReset).setCallbackTriggers(TweenCallback.END))
                    )).repeat(2,0))//complete is after repetitions done, end is after each repetition

                    .push(Tween.to(vert, -1, 5).targetRelative(vert.getValue()).setCallback(endBirdSpawn).setCallbackTriggers(TweenCallback.START)) //start spawning after wave done
                    //pause in clouds after waves over to have the effect of travelling to different bird type areas, only going horizontally
            ).setCallback(startStoryIntroAndSpawns).setCallbackTriggers(TweenCallback.START).repeat(Tween.INFINITY, 0).start();
        }
    }
    private void shakeCamera(float delta){
       // cam.normalizeUp();
        //System.out.println("Last angle: "+(-(float)Math.atan2(cam.up.x,cam.up.y)*MathUtils.radiansToDegrees) + " New Angle: "+shake.getValue());
        renderer.setRotate((-(float)Math.atan2(cam.up.x,cam.up.y)*MathUtils.radiansToDegrees) +shake.getValue() ); //subtract last angle and add next one
//+ (-1+2*r.nextFloat())
    }
//PointLight
    public void update(float delta) {
        //System.out.println("1: "+ Math.round(background.y) + " 2: "+Math.round(background2.y));
        if (isCameraShake){
            smallShake.update(delta);
            bigShake.update(delta);
            shakeCamera(delta); }

        if(isPastStoryIntro ){
            //System.out.println(" vert values:" + Math.round(vert.getValue()) + " bg1: "+background.y +" bg2: "+ background2.y + " bg1 added: "+background.addedY+" bg2 added: "+background2.addedY);
                    //stop running once done

            //if(vert.getValue()<bgStackHeight+35)backgroundStackReset();
            vertPositionBg.update(delta);
            //if end of wave close or 1 background away from ending dont end wave quickly, bgNumber multiples of 10 are wave end bg's
            //System.out.println(isBirdSpawning +" "+ BirdHandler.birdQueue.isEmpty() +" "+ BirdHandler.activeBirdQueue.isEmpty() +" "+ !(bgNumber%10==0) +" "+ !((bgNumber+1)%10==0));
            if (bgNumber>1 && !((bgNumber-1)%9==0) && !((bgNumber-2)%9==0) && isBirdSpawning && BirdHandler.birdQueue.isEmpty() && BirdHandler.activeBirdQueue.isEmpty() ) {
                //System.out.println(bgNumber);
                endWaveBgMotion = true;
                isBirdSpawning = false;
                vertPositionBg.kill();
                (vertPositionBg = Timeline.createSequence()  //-1 so it happens slightly before reset with added y
                        .push(Tween.to(vert, -1, 2).targetRelative(-(separatorHeight + bgh + bgh)-vert.getValue()).ease(TweenEquations.easeInQuint).setCallback(backgroundStackReset).setCallbackTriggers(TweenCallback.END))
                ).start();
                //System.out.println("irregular motion");
            }

            if (endWaveBgMotion && vertPositionBg.isFinished() ) {   //check if we need to keep ending wave quickly or new wave begins
                System.out.println("vert position finished bgNum now "+bgNumber);
                //vertPositionBg.pause();
                //System.out.println(BirdHandler.birdQueue+ ", ActiveBirdQueue: "+BirdHandler.activeBirdQueue+"************************************************");
                if (  ((bgNumber+1)%9==0) ){       //if last background motion before round end clouds slow down at the end
                    (vertPositionBg = Timeline.createSequence()  //-1 so it happens slightly before reset with added y
                            .push(Tween.to(vert, -1, 1.5f).targetRelative(-(separatorHeight + bgh + bgh)-vert.getValue()).ease(TweenEquations.easeOutSine).setCallback(backgroundStackReset).setCallbackTriggers(TweenCallback.END))
                    ).start();
                } else if (  !((bgNumber-2)%9==0) && !((bgNumber-3)%9==0)){ // && !((bgNumber-1)%9==0) && !((bgNumber)%9==0)    cam is at round end clouds when ((bgNumber-2)%9==0  (every 3 cities)
                        System.out.println("bgNum is "+bgNumber+" so make another tween");
                        (vertPositionBg = Timeline.createSequence()  //-1 so it happens slightly before reset with added y
                                .push(Tween.to(vert, -1, 1.5f).targetRelative(-(separatorHeight + bgh + bgh)-vert.getValue()).ease(TweenEquations.easeNone).setCallback(backgroundStackReset).setCallbackTriggers(TweenCallback.END))
                                ).start();
                    //System.out.println("irregular motion");
                } else {
                    endWaveBgMotion=false;
                    regularVertBgMotion();
                    //System.out.println("regular motion");
                }
            }


            if (background.y<background2.y){
                background.setY(vert.getValue());
                background2.setYToTail(background.getTailY());
            } else {
                background2.setY(vert.getValue());
                background.setYToTail(background2.getTailY());
            }

            horizPositionBg.update(delta);
            background.setX(horiz.getValue());
            background2.setX(horiz.getValue());

            background.update();
            background2.update();

            if (bgNumber== AssetHandler.bgList.size()) {
                //waveNumber+=1;
                bgNumber = 0;
            }

            if (background.addedY<3000 && background2.addedY<3000) {//in case the tween lags, we make sure reset stack is called back instead of whats below
                if (background.isScrolledDown()) {
                    //System.out.println("reset 1 to 2 +bgh, 1 is " + background.y + ", set to " + background2.getTailY());
                    background2.addedY += background.height + background.addedY;
                    background.reset(background2.getTailY(), bgNumber++);

                    //System.out.println("bgNumber is now " + bgNumber);
                } else if (background2.isScrolledDown()) {
                    //System.out.println("reset 2 to 1 +bgh, 2 is " + background2.y + ", set to " + background.getTailY());
                    background.addedY += background2.height + background2.addedY;
                    background2.reset(background.getTailY(), bgNumber++);

                    //System.out.println("bgNumber is now " + bgNumber);
                }
            }
        } else {
            // Update our objects (do phoenixBird intro)

            //end with spawning again, if this done then ispaststory=true
            isPastStoryIntro=true;
            horizPositionBg.resume();
            vertPositionBg.resume();
            isBirdSpawning=true;
        }
    }

    // The getters for our five instance variables
    public Background getBackground() {
        return background;
    }

    public Background getBackground2() {
        return background2;
    }

    public void setRendererAndCam(GameRenderer renderer) {
        this.renderer = renderer;
        this.cam = renderer.cam;
    }
}