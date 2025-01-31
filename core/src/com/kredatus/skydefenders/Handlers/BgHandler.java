// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.skydefenders.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.kredatus.skydefenders.Birds.BirdAbstractClass;
import com.kredatus.skydefenders.SkyDefendersMain;
import com.kredatus.skydefenders.GameObjects.Background;
import com.kredatus.skydefenders.GameWorld.GameRenderer;
import com.kredatus.skydefenders.GameWorld.GameWorld;
import com.kredatus.skydefenders.Helpers.InvertedTweenEquations;
import com.kredatus.skydefenders.NonGameHandlerScreens.Loader;
import com.kredatus.skydefenders.TweenAccessors.Value;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class BgHandler {
    public static boolean endWaveBgMotion;
    // BgHandler will create all five objects that we need.
    public static Background background, background2, background3, background4;
    private Random r = new Random();
    // BgHandler will use the constants below to determine
    // how fast we need to scroll and also determine
    public static Value horiz =new Value(), vert=new Value(), shake=new Value();
    // Capital letters are used by convention when naming constants.
    boolean same = false;
    /*private ArrayList<Boost> boostlist = new ArrayList<Boost>(), invboostlist = new ArrayList<Boost>(),
            flipboostlist = new ArrayList<Boost>(), invflipboostlist = new ArrayList<Boost>();*/

    //private Boost tempBoost;
    //private int orgBoostnumber = AssetHandler.getBoostnumber(), coordslistsize=AssetHandler.getcoordslistsize();
    public static int bgw= (((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("bgPhoenix2")).getRegionWidth();
    public static int bgh = (((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("bgPhoenix2")).getRegionHeight(), separatorHeight=(((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.tA.findRegion("cloudSeparator")).getRegionHeight();    //height of separator is different, and there are 2 combined with eachother

    public static int bgNumber;

    //private TweenManager manager;
    public Timeline horizPositionBg, vertPositionBg,smallShake, bigShake;
    private TweenCallback startStoryIntroAndSpawns, backgroundStackReset, shakeCamCallback, endBirdSpawn;
    public static float camHeight, camWidth, bgStackHeight=separatorHeight+bgh+bgh;
    public static boolean isPastStoryIntro, isCameraShake, isBirdSpawning, stackJustReset, isbgVertFast, changingBalloonBrightness;
    private OrthographicCamera cam;
    private GameRenderer renderer;
    public static int bgStackStartYHeight=0;
    public static boolean isMiddleOfCloud, irregularMotion, justStarted = true;
    public int waveDuration, sideToSideMotionDuration=30;

    public static float preYVel, yVel;
    public static boolean lightsBrightening=true;

    ConcurrentLinkedQueue<BirdAbstractClass> activeBirdQueue,birdQueue;

    public GameWorld world;
    LightHandler lightHandler;  TinyBirdHandler tinyBirdHandler;

    public void deathAndRestart(){
        bgNumber=bgNumber/9;

        //vert.set(0); //everything is done in negative (camera goes up by that amount
        vert.set(0);

        if (background.y < background2.y) {
            background2.reset(0,bgNumber++);background.reset(background2.getTailY(),bgNumber++);
        } else {
            background.reset(0,bgNumber++);background2.reset(background.getTailY(),bgNumber++);
        }
        survivalBgTweens(tinyBirdHandler,lightHandler);

        //startToSurvival(tinyBirdHandler,lightHandler);
    }
    public void buyMenuToSurvival(){
        vertPositionBg.resume();
        (horizPositionBg = Timeline.createSequence()
                .push(Tween.to(horiz, -1, sideToSideMotionDuration).target((camWidth)-bgw).ease(TweenEquations.easeInOutSine)))
                .repeatYoyo(Tween.INFINITY, 0).start();
    }
    public void survivalToBuyMenu(){
        //background = new Background(horiz.get(), vert.get(), bgw, separatorHeight, Loader.bgList.get(0));
        //background2 = new Background(horiz.get(), background.getTailY(), bgw, separatorHeight, Loader.bgList.get(0));
        vertPositionBg.pause();
        (horizPositionBg = Timeline.createSequence()
                .push(Tween.to(horiz, -1, sideToSideMotionDuration/2).target((camWidth)-bgw).ease(TweenEquations.easeInOutSine)))
                .repeatYoyo(Tween.INFINITY, 0).start();
        //horiz.set(0);
        //vert.set(0);
        /*(horizPositionBg = Timeline.createSequence()
                .push(Tween.to(horiz, -1, sideToSideMotionDuration).target((camWidth)-bgw).ease(TweenEquations.easeInOutSine)))
                .repeatYoyo(Tween.INFINITY, 0).start();
        /*(vertPositionBg = Timeline.createSequence()  //-1 so it happens slightly before reset with added y
                .push(Tween.to(vert, -1, 5).target(-bgh)))
                .repeatYoyo(Tween.INFINITY,0).start();*/
    }
    public void startToSurvival(TinyBirdHandler tinyBirdHandler,LightHandler lightHandler){
        this.tinyBirdHandler=tinyBirdHandler;this.lightHandler=lightHandler;
        horiz.set(0);
        vert.set(0); //everything is done in negative (camera goes up by that amount
        shake.set(0);                              //but in reality its the bg's lowering by that much

        isCameraShake=false;
        isPastStoryIntro=true;
        background = new Background(horiz.get(), vert.get(), bgw, separatorHeight, Loader.bgList.get(bgNumber++));
        background2 = new Background(horiz.get(), background.getTailY(), bgw, bgh, Loader.bgList.get(bgNumber++));
        //System.out.println(((SkyDefendersMain)Gdx.app.getApplicationListener()).loader.gameHandler);
        survivalBgTweens(tinyBirdHandler,lightHandler);
    }



    public BgHandler(GameWorld world, float camWidth, float camHeight, int waveNumber, BirdHandler birdHandler){
        this.world=world;
        this.activeBirdQueue=birdHandler.activeBirdQueue;
        this.birdQueue=birdHandler.birdQueue;

        //bgStackStartYHeight= (int)(separatorHeight/2-camHeight/2);
        this.camHeight=camHeight;
        this.camWidth =camWidth;
                          // 0    1    2    3    4    5    6    7
        bgNumber=waveNumber*9;
        //if (bgNumber!=waveNumber * 9) bgNumber = 9 * waveNumber;
        //bgNumber = 9 * waveNumber;// "pB","tB","wB","fB","aB","nB","lB","gB"
        //System.out.print("Start height of bg1: "+-bgStackStartYHeight);


        //this.manager= SplashScreen.getManager();
        //System.out.println("vert.get() " + vert.get());

        //isBirdSpawning=true;

        //TinyBirdHandler.addTinyBirdsNextCity(camWidth,camHeight);
    }

    public void survivalBgTweens(final TinyBirdHandler tinyBirdHandler, final LightHandler lightHandler){
        //final float camHeight2=camHeight;
        startStoryIntroAndSpawns=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                //System.out.println("Start Spawning");
               if (!isPastStoryIntro && world.isFirstTime){
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
                    //System.out.println("end spawning");
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
                .push(Tween.to(horiz, -1, sideToSideMotionDuration).target((camWidth)-bgw).ease(TweenEquations.easeInOutSine)))
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
                tinyBirdHandler.addVertValueToBirdsSurvivingWavePart(vert.get());
                tinyBirdHandler.addTinyBirdsNextCity(camWidth, camHeight);
                //System.out.println("Reset stack");
                stackJustReset=true;
                if (bgNumber == Loader.bgList.size()) {
                    //waveNumber+=1;
                    bgNumber = 0;
                }
                lightHandler.newBgLighting(bgNumber);
                if (background.addedY>background2.addedY){ background.reset(background2.getTailY(), bgNumber++);}//lights for new backgrounds
                else { background2.reset(background.getTailY(), bgNumber++);}
                background2.addedY = 0;
                background.addedY = 0;
                //vert.set(-bgStackStartYHeight);
                //cam.normalizeUp();
                renderer.setRotate(-(float) Math.atan2(cam.up.x, cam.up.y) * MathUtils.radiansToDegrees);
                isCameraShake = false;
                smallShake.pause();
                vert.set(0);
            }
        };
        regularVertBgMotion();
    }

    private void  regularVertBgMotion() {
        if (!SkyDefendersMain.fastTest) {
            //System.out.println("First target: "+bgStackHeight+", separator height: "+separatorHeight+", bgh: "+bgh);
            (vertPositionBg = Timeline.createSequence()  //15 sec duration
                    .push((((Tween.to(vert, -1, 15).delay(1.2f).targetRelative(-bgStackHeight).ease(InvertedTweenEquations.QuartInOut2LongerMiddle).setCallback(backgroundStackReset).setCallbackTriggers(TweenCallback.END))
                    )).repeat(2,0))//complete is after repetitions done, end is after each repetition

                    .push(Tween.to(vert, -1, 5).targetRelative(vert.get()).setCallback(endBirdSpawn).setCallbackTriggers(TweenCallback.START)) //start spawning after wave done
                    //pause in clouds after waves over to have the effect of travelling to different bird type areas, only going horizontally
            ).setCallback(startStoryIntroAndSpawns).setCallbackTriggers(TweenCallback.START).repeat(Tween.INFINITY, 0).start();

        } else {
            //System.out.println("First target: "+bgStackHeight+", separator height: "+separatorHeight+", bgh: "+bgh);
            (vertPositionBg = Timeline.createSequence()  //-1 so it happens slightly before reset with added y
                    .push((((Tween.to(vert, -1, 5).delay(1.2f).targetRelative(-bgStackHeight).ease(InvertedTweenEquations.QuartInOut2LongerMiddle).setCallback(backgroundStackReset).setCallbackTriggers(TweenCallback.END))
                    )).repeat(2,0))//complete is after repetitions done, end is after each repetition

                    .push(Tween.to(vert, -1, 5).targetRelative(vert.get()).setCallback(endBirdSpawn).setCallbackTriggers(TweenCallback.START)) //start spawning after wave done
                    //pause in clouds after waves over to have the effect of travelling to different bird type areas, only going horizontally
            ).setCallback(startStoryIntroAndSpawns).setCallbackTriggers(TweenCallback.START).repeat(Tween.INFINITY, 0).start();
        }
    }

    private void shakeCamera(float delta){
       // cam.normalizeUp();
        //System.out.println("Last angle: "+(-(float)Math.atan2(cam.up.x,cam.up.y)*MathUtils.radiansToDegrees) + " New Angle: "+shake.get());
        renderer.setRotate((-(float)Math.atan2(cam.up.x,cam.up.y)*MathUtils.radiansToDegrees) +shake.get() ); //subtract last angle and add next one
        //+ (-1+2*r.nextFloat())
    }

    //   0                 9                       18                      27                      36                      45                        54                      63
    //      1 2  4 5  7 8     10 11  13 14  16 17     19 20  22 23  25 26     28 29  31 32  34 35     37 38  40 41  43 44       46 47  49 50  52 53     55 56  58 59  61 62     64 65  67 68  70 71
    public void update(float delta) {
        if (world.isSurvival()||world.isBuyMenu()) {
            //basically if middle of each wave start brightening, i.e. 6, if beginning of new wave start darkening
            if (lightsBrightening && ((bgNumber - 4) % 9) == 0) {
                lightsBrightening = false;
                // System.out.println("start darkening");
            } else if (!lightsBrightening && ((bgNumber + 1) % 9) == 0) {
                lightsBrightening = true;
            }//System.out.println("start brightening");}

            //System.out.println(endWaveBgMotion);
            //System.out.println("1: "+ Math.round(background.y) + " 2: "+Math.round(background2.y));
            if (isCameraShake) {
                smallShake.update(delta);
                bigShake.update(delta);
                shakeCamera(delta);
            }

            if (isPastStoryIntro) {
                //System.out.println(" vert values:" + Math.round(vert.get()) + " bg1: "+background.y +" bg2: "+ background2.y + " bg1 added: "+background.addedY+" bg2 added: "+background2.addedY);
                //stop running once done
                //if(vert.get()<bgStackHeight+35)backgroundStackReset();

                preYVel = vert.get();
                vertPositionBg.update(delta);
                if (Math.abs(preYVel - vert.get()) < bgStackHeight / 10)
                    yVel = preYVel - vert.get();


                //System.out.println(vert.get()+" "+isbgVertFast);
                if (vert.get() > 0.20 * -bgStackHeight || vert.get() < 0.67 * -bgStackHeight) {  //test this
                    if (!isbgVertFast) {
                        isbgVertFast = true;
                    }
                    if (!justStarted && (vert.get() > 0.15 * -bgStackHeight || vert.get() < 0.85 * -bgStackHeight)) {
                        if (!changingBalloonBrightness) {
                            changingBalloonBrightness = true;
                        }
                        if (vert.get() > 0.02 * -bgStackHeight || vert.get() < 0.92 * -bgStackHeight) {
                            if (!isMiddleOfCloud) {
                                isMiddleOfCloud = true;
                                //System.out.println("Middle of cloud");
                            }
                        } else if (isMiddleOfCloud) {
                            isMiddleOfCloud = false;
                            //system.out.println("Not Middle of cloud");
                        }
                    } else if (changingBalloonBrightness) {
                        changingBalloonBrightness = false;
                    }
                } else if (isbgVertFast) {
                    if (justStarted) justStarted = false;
                    isbgVertFast = false;
                }
                //System.out.println("BgVertFast = "+isbgVertFast+", vert: "+vert.get()+", greater than: "+ (0.20*-bgStackHeight)+", lower than: "+ (0.67*-bgStackHeight));

                //if end of wave close or 1 background away from ending dont end wave quickly, bgNumber multiples of 10 are wave end bg's
                //System.out.println(isBirdSpawning +" "+ BirdHandler.birdQueue.isEmpty() +" "+ BirdHandler.activeBirdQueue.isEmpty() +" "+ !(bgNumber%10==0) +" "+ !((bgNumber+1)%10==0));
                if (!endWaveBgMotion && isBirdSpawning && bgNumber > 1 && !((bgNumber - 1) % 9 == 0) && !((bgNumber - 2) % 9 == 0) && birdQueue.isEmpty() && activeBirdQueue.isEmpty()) {//stop spawning
                    //System.out.println(bgNumber);
                    endWaveBgMotion = true;
                    isBirdSpawning = false;
                    //if (!isEndingEarlyAndFast) isEndingEarlyAndFast=true;//fire burner if all birds are dead and about to end quickly(used in airship class)
                    vertPositionBg.kill();
                    (vertPositionBg = Timeline.createSequence()  //-1 so it happens slightly before reset with added y
                            .push(Tween.to(vert, -1, 2).targetRelative(-(separatorHeight + bgh + bgh) - vert.get()).ease(TweenEquations.easeInQuint).setCallback(backgroundStackReset).setCallbackTriggers(TweenCallback.END))
                    ).start();
                    //System.out.println("irregular motion");
                }   //fix this stuff up but be careful because could cause unexpected bugs. shouldnt if statement before this have an if (!endwavebgmotion) at the beginning of it?
                //*********************************


                if (endWaveBgMotion && vertPositionBg.isFinished()) {   //check if we need to keep ending wave quickly or new wave begins
                    //System.out.println("vert position finished bgNum now "+bgNumber);
                    //vertPositionBg.pause();
                    //System.out.println(BirdHandler.birdQueue+ ", ActiveBirdQueue: "+BirdHandler.activeBirdQueue+"************************************************");
                    if (((bgNumber + 1) % 9 == 0)) {       //if last background motion before round end clouds slow down at the end
                        //System.out.println("Fast moving 1");

                        (vertPositionBg = Timeline.createSequence()  //-1 so it happens slightly before reset with added y
                                .push(Tween.to(vert, -1, 1.5f).targetRelative(-(separatorHeight + bgh + bgh) - vert.get()).ease(TweenEquations.easeOutSine).setCallback(backgroundStackReset).setCallbackTriggers(TweenCallback.END))
                        ).start();
                    } else if (!((bgNumber - 2) % 9 == 0) && !((bgNumber - 3) % 9 == 0)) { // && !((bgNumber-1)%9==0) && !((bgNumber)%9==0)    cam is at round end clouds when ((bgNumber-2)%9==0  (every 3 cities)
                        //System.out.println("Fast moving 2");

                        (vertPositionBg = Timeline.createSequence()  //-1 so it happens slightly before reset with added y
                                .push(Tween.to(vert, -1, 1.5f).targetRelative(-(separatorHeight + bgh + bgh) - vert.get()).ease(TweenEquations.easeNone).setCallback(backgroundStackReset).setCallbackTriggers(TweenCallback.END))
                        ).start();
                        //System.out.println("irregular motion");
                    } else {
                        endWaveBgMotion = false;
                        regularVertBgMotion();
                        //System.out.println("regular motion");
                    }
                }


                if (background.y < background2.y) {
                    background.setY(vert.get());
                    background2.setYToTail(background.getTailY());
                } else {
                    background2.setY(vert.get());
                    background.setYToTail(background2.getTailY());
                }

                horizPositionBg.update(delta);
                background.setX(horiz.get());
                background2.setX(horiz.get());

                background.update();
                background2.update();

                if (bgNumber == Loader.bgList.size()) {
                    //waveNumber+=1;
                    bgNumber = 0;
                }

                if (background.addedY < 3000 && background2.addedY < 3000) { //in case the tween lags, we make sure reset stack is called back instead of whats below
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
                isPastStoryIntro = true;
                horizPositionBg.resume();
                vertPositionBg.resume();
                isBirdSpawning = true;
            }
        } else if (world.isBuyMenu()){

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