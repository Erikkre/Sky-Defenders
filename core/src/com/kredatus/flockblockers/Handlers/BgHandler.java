package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
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
    private Random r;
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
    public int bgw = AssetHandler.bgPhoenix.getWidth();
    public int bgh = AssetHandler.bgPhoenix.getHeight();
    float x, y, width, height;
    public int bgNumber;

    //private TweenManager manager;
    public Timeline horizPosBg, vertPosBg,smallShake, bigShake;
    private TweenCallback startStoryIntro, bg2ToBg1Tail, shakeCamCallback;
    private float camHeight;
    private boolean isPastStoryIntro, isCameraShake;
    private OrthographicCamera cam;
    private GameRenderer renderer;
    public BgHandler(float camWidth, float camHeight){

        this.camHeight=camHeight;
        bgNumber = 0;

        horiz.setValue(0);
        vert.setValue(0);
        shake.setValue(0);
        background = new Background(horiz.getValue(), vert.getValue(), bgw, bgh, AssetHandler.bgList.get(bgNumber++));
        background2 = new Background(horiz.getValue(), background.getTailY(), bgw, bgh, AssetHandler.bgList.get(bgNumber++));
        r = new Random();
        //this.manager= SplashScreen.getManager();
        //System.out.println("vert.getValue() " + vert.getValue());
        isCameraShake=false;
        setupTweens(camWidth, camHeight);
        isPastStoryIntro=true;
    }

    private void setupTweens(float camWidth, float camHeight){

        //final float camHeight2=camHeight;
        startStoryIntro=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
               if (!isPastStoryIntro & GameWorld.isFirstTime){
                    isPastStoryIntro=false;
                    horizPosBg.pause();
                    vertPosBg.pause();
                }
            }
        };

        bg2ToBg1Tail=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                System.out.println("Reset bg2y to bg1y+3500, tail");
                background2.addedY=0;
                background2.reset(background.getTailY(), bgNumber++);
                //cam.normalizeUp();
                renderer.setRotate(-(float)Math.atan2(cam.up.x,cam.up.y)*MathUtils.radiansToDegrees);
                isCameraShake=false;
                smallShake.pause();
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
                .push(Tween.to(horiz, -1, 20).target((camWidth)-bgw).ease(TweenEquations.easeInOutSine)))
                .repeatYoyo(Tween.INFINITY, 0).start();
//System.out.println("First easing target: "+(-bgh+camHeight/2)  /2);

        (vertPosBg = Timeline.createSequence()  //10 9 9 4.5 6.5 1, 8 and 15 repeats
                .push((Tween.to(vert,-1, 5f ).target(-bgh+(.5f*camHeight)).ease(TweenEquations.easeInOutSine)).               setCallback(startStoryIntro))//midpoint
                .push(Tween.to(vert, -1, 4.5f).target(  -bgh+(1.1f*camHeight)).ease(TweenEquations.easeInOutSine))                                           //0.733 camheight below
                .push(Tween.to(vert, -1, 4.5f).target(  -bgh-( .1f*camHeight)).ease(TweenEquations.easeInOutSine).repeatYoyo(8, 0))                          //0.733 camheight above
                .push(Tween.to(vert, -1,2.25f).target(-bgh+(.5f*camHeight)).ease(TweenEquations.easeInOutSine))                                            //midpoint

                .push(Tween.to(vert, -1, 3.25f).target((-bgh*2)).ease(TweenEquations.easeInElastic).setCallback(shakeCamCallback)     )                    //top edge+bgh/50
                .push((Tween.to(vert, -1, 0.5f).target((-bgh*2+camHeight/80)).ease(TweenEquations.easeInOutSine).repeatYoyo(15, 0)).setCallback(bg2ToBg1Tail))      )       //top edge

                //.push(Tween.to(vert,-1,6).target(-bgh*2).ease(TweenEquations.easeInCubic)          .setCallback(bg2ToBg1Tail))                   )
                .repeat(Tween.INFINITY, 0).start();


        float smallShakeMaxAngle=1f;
        int bigShakeMaxAngle=30;
        smallShake= Timeline.createSequence()
                .push(Tween.to(shake,-1, .03f ).target((-1+2*r.nextFloat())*smallShakeMaxAngle).ease(TweenEquations.easeInOutSine)) //camera shake between cities
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
        if (isCameraShake){
            smallShake.update(delta);
            bigShake.update(delta);
            shakeCamera(delta); }

        if(isPastStoryIntro ){

            //System.out.print("bg1y: "+background.y + " bg2y: "+background2.y);
    //        System.out.println(" vert values:" +vert.getValue() + " addY bg1: "+background.addedY +" addY bg2: "+ background2.addedY);
            
                    //stop running once done

            
            vertPosBg.update(delta);

            if (background.y<background2.y){
                background.setY(vert.getValue());
                background2.setY(background.getTailY());
            } else {
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
                System.out.println("reset bg1y to bg2y +3500");
                background.reset(background2.getTailY(), bgNumber++);
                background2.addedY=bgh;
            }


        } else {
            // Update our objects
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
    /*public Background getBackground3() {
        return background3;
    }

    public Background getBackground4() {
        return background4;
    }*/

/*
    public void onRestart() {
        /*
        background.onRestart(0, 0);
        background2.onRestart(background.getTailY(), 0);
        background3.onRestart(0, -bgh);
        background4.onRestart(background3.getTailY(), -bgh);

        boostlist.clear();
        flipboostlist.clear();
        invboostlist.clear();
        invflipboostlist.clear();
        startlist(boostlist, false, false, orgBoostnumber);
        startlist(invboostlist, false, true, orgBoostnumber);
        startlist(flipboostlist, true, false, orgBoostnumber);
        startlist(invflipboostlist, true, true, orgBoostnumber);
    }*/
}