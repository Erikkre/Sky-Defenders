// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.brashmonkey.spriter.TweenedAnimation;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.Background;
import com.kredatus.flockblockers.GameWorld.GameRenderer;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.Screens.SplashScreen;
import com.kredatus.flockblockers.TweenAccessors.Value;

import java.util.ArrayList;
import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;


/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class BgHandler {
    // BgHandler will create all five objects that we need.
    private Background background, background2, background3, background4;
    private Random r = new Random();
    // BgHandler will use the constants below to determine
    // how fast we need to scroll and also determine
    private Value horiz =new Value(), vert=new Value(), shake=new Value();
    // Capital letters are used by convention when naming constants.
    boolean same = false;
    /*private ArrayList<Boost> boostlist = new ArrayList<Boost>(), invboostlist = new ArrayList<Boost>(),
            flipboostlist = new ArrayList<Boost>(), invflipboostlist = new ArrayList<Boost>();*/
    public ArrayList<Vector3> boostcoords = AssetHandler.getBoostcoords();
    int w = AssetHandler.boost.getWidth();
    int h = AssetHandler.boost.getHeight();
    //private Boost tempBoost;
    //private int orgBoostnumber = AssetHandler.getBoostnumber(), coordslistsize=AssetHandler.getcoordslistsize();
    public int bgw;
    public int bgh, separatorHeight;    //height of separator is different, and there are 2 combined with eachother
    float x, y, width, height;
    public int bgNumber;

    //private TweenManager manager;
    public Timeline horizPosBg, vertPosBg,smallShake, bigShake;
    private TweenCallback startStoryIntroAndSpawns, bg2ToBg1Tail, shakeCamCallback, endBirdSpawn;
    private float camHeight;
    public static boolean isPastStoryIntro, isCameraShake, isBirdSpawning;
    private OrthographicCamera cam;
    private GameRenderer renderer;
    public BgHandler(float camWidth, float camHeight){

        this.camHeight=camHeight;
        bgNumber = 0;

        horiz.setValue(0);
        vert.setValue(0);
        shake.setValue(0);
        background = new Background(horiz.getValue(), vert.getValue(), AssetHandler.bgList.get(bgNumber).getRegionWidth(), AssetHandler.bgList.get(bgNumber).getRegionHeight(), AssetHandler.bgList.get(bgNumber++));
        background2 = new Background(horiz.getValue(), background.getTailY(), AssetHandler.bgList.get(bgNumber).getRegionWidth(), AssetHandler.bgList.get(bgNumber).getRegionHeight(), AssetHandler.bgList.get(bgNumber++));

        //this.manager= SplashScreen.getManager();
        //System.out.println("vert.getValue() " + vert.getValue());
        isCameraShake=false;
        setupTweens(camWidth, camHeight);
        isPastStoryIntro=true;
    }

    private void setupTweens(float camWidth, float camHeight){
        //final float camHeight2=camHeight;
        startStoryIntroAndSpawns=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {;
               if (!isPastStoryIntro && GameWorld.isFirstTime){
                    isPastStoryIntro=false;
                    horizPosBg.pause();
                    vertPosBg.pause();
                } else {
                   isBirdSpawning=true;
                   //System.out.println("start spawning");
               }
            }
        };

        endBirdSpawn=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                    isBirdSpawning=false;
                    //System.out.println("end spawning");
            }
        };

        bg2ToBg1Tail=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                    //System.out.println("Reset 2 to 1+bgh");
                    background2.addedY = 0;
                    if (bgNumber == AssetHandler.bgList.size()) {
                        //waveNumber+=1;
                        bgNumber = 0;
                    }
                    background2.reset(background.getTailY(), bgNumber++);
                    vert.setValue(0);
                    //cam.normalizeUp();
                    renderer.setRotate(-(float) Math.atan2(cam.up.x, cam.up.y) * MathUtils.radiansToDegrees);
                    isCameraShake = false;
                    smallShake.pause();
                    //}
                //}
            }
        };

        shakeCamCallback=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                isCameraShake=true;   //reset camera
                if (!smallShake.isStarted()){smallShake.start();}else{ smallShake.resume();}
            }
        };

        (horizPosBg = Timeline.createSequence()
                .push(Tween.to(horiz, -1, 25).target((camWidth)-bgw).ease(TweenEquations.easeInOutSine)))
                .repeatYoyo(Tween.INFINITY, 0).start();
//System.out.println("First easing target: "+(-bgh+camHeight/2)  /2);

        if (GameWorld.isFirstTime){
            (vertPosBg = Timeline.createSequence()  //5 4.5f 4.5f 2 7 .1, 8 and 60 repeats
                    .push(Tween.to(vert,-1, 1f ).target(-bgh+(.5f*camHeight)).ease(TweenEquations.easeInOutSine)      )//midpoint
                    .push(Tween.to(vert, -1, 3f).target(  -bgh+(1.1f*camHeight)).ease(TweenEquations.easeInOutSine))                                           //0.733 camheight below
                    .push(Tween.to(vert, -1, 3f).target(  -bgh-( .1f*camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(1, 0))                          //0.733 camheight above
                    .push(Tween.to(vert, -1,2f).target(-bgh+(.5f*camHeight)).ease(TweenEquations.easeInOutSine).setCallback(endBirdSpawn)     )                                       //midpoint

                    .push(Tween.to(vert, -1, 8).target((-bgh*2)).ease(TweenEquations.easeInElastic)     )                    //top edge+bgh/50
                    .push((Tween.to(vert, -1, 0.1f).target((-bgh*2+camHeight/80)).ease(TweenEquations.easeInOutSine).repeatYoyo(1, 0)).setCallback(bg2ToBg1Tail).setCallback(startStoryIntroAndSpawns))      )       //top edge

                    //.push(Tween.to(vert,-1,6).target(-bgh*2).ease(TweenEquations.easeInCubic)          .setCallback(bg2ToBg1Tail))                   )
                    .repeat(Tween.INFINITY, 0).start();
        }

/*
        (vertPosBg = Timeline.createSequence()  //55 20 .01, 8 and 800 repeats
                //.push(Tween.to(vert, -1, 13).target(  -bgh+(1.1f*camHeight)).ease(TweenEquations.easeInOutQuart).setCallback(startStoryIntroAndSpawns).setCallbackTriggers(TweenCallback.START)   )                 //center is 0.6 camheight below, cam is centered on -0.5 to everything
                .push((Tween.to(vert, -1, 30).target(  -bgh-(.15f*camHeight)).ease(TweenEquations.easeOutSine)).setCallback(startStoryIntroAndSpawns).setCallbackTriggers(TweenCallback.BEGIN) ) //center is 0.65 camheight  above

                .push((Tween.to(vert, -1, 20).target((-bgh*2)+(0.499f*camHeight)).ease(TweenEquations.easeInOutQuart)).setCallback(endBirdSpawn)     )  //cam center 0.001 above edge
                .push(Tween.to(vert, -1, 0.02f).target((-bgh*2)+(0.501f*camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(400, 0)         )          //cam 0.001 below edge
                .push((Tween.to(vert, -1, 10).target((-bgh*2)).ease(TweenEquations.easeInSine)).setCallback(bg2ToBg1Tail))      )   //cam 0.03 below edge

                //.push(Tween.to(vert,-1,6).target(-bgh*2).ease(TweenEquations.easeInCubic)          .setCallback(bg2ToBg1Tail))                   )
                .repeat(Tween.INFINITY, 0).start();

*/
        if (!FlockBlockersMain.fastTest) {
            (vertPosBg = Timeline.createSequence()  //5 13 7   0.02 0.5 , 3 and 1 repeats
                    .push((Tween.to(vert, -1, 5).target(-bgh - (.1f * camHeight)).ease(TweenEquations.easeOutExpo)).setCallback(startStoryIntroAndSpawns))//0.6 camheight  above, decide whether quad quint, circ or expo, all out
                    .push((Tween.to(vert, -1, 13).target(-bgh + (1.2f * camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(3, 0)).setCallback(endBirdSpawn))                         //0.7 camheight below    //actual amount of times repeated is 1+count, so odd counts end at origin

                    .push(Tween.to(vert, -1, 7).target((-bgh * 2) + (0.499f * camHeight)).ease(TweenEquations.easeInOutSine))  //cam center 0.001 above edge, decide wether ease inoutsine
                    .push((Tween.to(vert, -1, 0.02f).target((-bgh * 2) + (0.501f * camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(1, 0)))  //         ) //cam 0.001 below edge
                    .push((Tween.to(vert, -1, 0.5f).target((-bgh * 2)).ease(TweenEquations.easeInSine)).setCallback(bg2ToBg1Tail)))   //cam 0.03 below edge

                    .repeat(Tween.INFINITY, 0).start();
        } else {
            (vertPosBg = Timeline.createSequence()  //2 4  6   0.02 0.5 . 3 and 1 repeats
                    .push((Tween.to(vert, -1, 2).target(-bgh - (.1f * camHeight)).ease(TweenEquations.easeOutExpo)).setCallback(startStoryIntroAndSpawns))//0.6 camheight  above, decide whether quad quint, circ or expo, all out
                    .push((Tween.to(vert, -1, 4).target(-bgh + (1.2f * camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(3, 0)).setCallback(endBirdSpawn))                         //0.7 camheight below    //actual amount of times repeated is 1+count, so odd counts end at origin

                    .push(Tween.to(vert, -1, 6).target((-bgh * 2) + (0.499f * camHeight)).ease(TweenEquations.easeInOutSine))  //cam center 0.001 above edge, decide wether ease inoutsine
                    .push((Tween.to(vert, -1, 0.02f).target((-bgh * 2) + (0.501f * camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(1, 0)))//.setCallback(startStoryIntroAndSpawns)         ) //cam 0.001 below edge
                    .push((Tween.to(vert, -1, 0.5f).target((-bgh * 2)).ease(TweenEquations.easeInSine)).setCallback(bg2ToBg1Tail)))   //cam 0.03 below edge

                    .repeat(Tween.INFINITY, 0).start();
        }

        float smallShakeMaxAngle=0.1f;
        int bigShakeMaxAngle=30;
        smallShake= Timeline.createSequence()
                .push(Tween.to(shake,-1, .01f ).target((-1+2*r.nextFloat())*smallShakeMaxAngle).ease(TweenEquations.easeInOutSine)) //camera shake between cities
                .repeatYoyo(Tween.INFINITY, 0);
        bigShake=  Timeline.createSequence()
                .push(Tween.to(shake,-1, .5f ).target((-1+2*r.nextFloat())*bigShakeMaxAngle).ease(TweenEquations.easeInOutSine))
                .repeatYoyo(Tween.INFINITY, 0);
    }

    private void shakeCamera(float delta){
       // cam.normalizeUp();
        //System.out.println("Last angle: "+(-(float)Math.atan2(cam.up.x,cam.up.y)*MathUtils.radiansToDegrees) + " New Angle: "+shake.getValue());
        renderer.setRotate((-(float)Math.atan2(cam.up.x,cam.up.y)*MathUtils.radiansToDegrees) +shake.getValue() ); //subtract last angle and add next one
//+ (-1+2*r.nextFloat())
    }

    public void update(float delta) {
        //System.out.println("1: "+ Math.round(background.y) + " 2: "+Math.round(background2.y));

        if (isCameraShake){
            smallShake.update(delta);
            bigShake.update(delta);
            shakeCamera(delta); }

        if(isPastStoryIntro ){


    //        System.out.println(" vert values:" +vert.getValue() + " addY bg1: "+background.addedY +" addY bg2: "+ background2.addedY);
            
                    //stop running once done

            if (isBirdSpawning && BirdHandler.birdQueue.isEmpty() && BirdHandler.activeBirdQueue.isEmpty()){
                //vertPosBg.pause();
                //System.out.println(BirdHandler.birdQueue+ ", ActiveBirdQueue: "+BirdHandler.activeBirdQueue+"************************************************");
                if (!FlockBlockersMain.fastTest) {
                    (vertPosBg = Timeline.createSequence()  //5 13 7   0.02 0.5 , 3 and 1 repeats

                            .push((Tween.to(vert, -1, 7).target((-bgh * 2) + (0.499f * camHeight)).ease(TweenEquations.easeInOutSine)).setCallback(endBirdSpawn).setCallbackTriggers(TweenCallback.START))  //cam center 0.001 above edge, decide wether ease inoutsine
                            .push(Tween.to(vert, -1, 0.02f).target((-bgh * 2) + (0.501f * camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(1, 0))//.setCallback(startStoryIntroAndSpawns)         ) //cam 0.001 below edge
                            .push((Tween.to(vert, -1, 0.5f).target((-bgh * 2)).ease(TweenEquations.easeInSine)).setCallback(bg2ToBg1Tail))   //cam 0.03 below edge

                            .push((Tween.to(vert, -1, 5).target(-bgh - (.1f * camHeight)).ease(TweenEquations.easeOutExpo)).setCallback(startStoryIntroAndSpawns))//0.6 camheight  above, decide whether quad quint, circ or expo, all out
                            .push(Tween.to(vert, -1, 13).target(-bgh + (1.2f * camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(3, 0))    )                   //0.7 camheight below    //actual amount of times repeated is 1+count, so odd counts end at origin
                            .repeat(Tween.INFINITY, 0).start();

                } else {
                    (vertPosBg = Timeline.createSequence()   //2  4 6   0.02 0.5 . 3 and 1 repeats

                            .push((Tween.to(vert, -1, 6).target((-bgh * 2) + (0.499f * camHeight)).ease(TweenEquations.easeInOutSine)).setCallback(endBirdSpawn).setCallbackTriggers(TweenCallback.START))  //cam center 0.001 above edge, decide wether ease inoutsine
                            .push(Tween.to(vert, -1, 0.02f).target((-bgh * 2) + (0.501f * camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(1, 0))//.setCallback(startStoryIntroAndSpawns)         ) //cam 0.001 below edge
                            .push((Tween.to(vert, -1, 0.5f).target((-bgh * 2)).ease(TweenEquations.easeInSine)).setCallback(bg2ToBg1Tail))   //cam 0.03 below edge

                            .push((Tween.to(vert, -1, 2).target(-bgh - (.1f * camHeight)).ease(TweenEquations.easeOutExpo)).setCallback(startStoryIntroAndSpawns))//0.6 camheight  above, decide whether quad quint, circ or expo, all out
                            .push(Tween.to(vert, -1, 4).target(-bgh + (1.2f * camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(3, 0))    )                   //0.7 camheight below    //actual amount of times repeated is 1+count, so odd counts end at origin
                            .repeat(Tween.INFINITY, 0).start();

                }

                isBirdSpawning=false;
            }
            vertPosBg.update(delta);


            if (background.y<background2.y){
                //System.out.println("background.y<background2.y");
                background.setY(vert.getValue());
                background2.setY(background.getTailY());
            } else {
                //System.out.println("background2.y<background.y");
                background2.setY(vert.getValue());
                background.setY(background2.getTailY());
            }

            horizPosBg.update(delta);
            background.setX(horiz.getValue());
            background2.setX(horiz.getValue());
            //}

            background.update();
            background2.update();

            if (bgNumber== AssetHandler.bgList.size()) {
                //waveNumber+=1;
                bgNumber = 0;
            }
            if (background.isScrolledDown()) {
                    //System.out.println("reset 1 to 2 +bgh, 1 is "+background.y+", 1 bgh is "+background.getHeight());
                    background.reset(background2.getTailY(), bgNumber++);
                    background2.addedY = background.height;
            }
        } else {
            // Update our objects (do phoenixBird intro)

            //end with spawning again, if this done then ispaststory=true
            isPastStoryIntro=true;
            horizPosBg.resume();
            vertPosBg.resume();
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