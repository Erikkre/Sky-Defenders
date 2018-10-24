package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.math.Vector3;
import com.kredatus.flockblockers.GameObjects.Background;
import com.kredatus.flockblockers.Screens.SplashScreen;
import com.kredatus.flockblockers.TweenAccessors.Value;
import com.kredatus.flockblockers.TweenAccessors.ValueAccessor;

import java.util.ArrayList;
import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class BgHandler {
    // BgHandler will create all five objects that we need.
    private Background background, background2, background3, background4;
    private Random r;
    // BgHandler will use the constants below to determine
    // how fast we need to scroll and also determine
    private Value horiz =new Value(), vert=new Value();
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
    // Constructor receives a float that tells us where we need to create our
    // Grass and Pipe objects.
    private TweenManager manager;
    public Timeline horizPosBg, vertPosBg;
    private TweenCallback pastStoryIntro, bg2ToBg1Tail;
    private float camHeight;
    private boolean isPastStoryIntro;

    public BgHandler(float camWidth, float camHeight){
        this.camHeight=camHeight;
        bgNumber = 0;

        horiz.setValue(0);
        vert.setValue(0);
        background = new Background(horiz.getValue(), vert.getValue(), bgw, bgh, AssetHandler.bgList.get(bgNumber++));
        background2 = new Background(horiz.getValue(), background.getTailY(), bgw, bgh, AssetHandler.bgList.get(bgNumber++));
        r = new Random();
        this.manager= SplashScreen.getManager();
        System.out.println("vert.getValue() " + vert.getValue());
        setupTweens(camWidth, camHeight);
    }

    private void setupTweens(float camWidth, float camHeight){
        //final float camHeight2=camHeight;
        pastStoryIntro=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
               /* if (!isPastStoryIntro & gameWorld.isFirstTime){
                    isPastStoryIntro=true;
                    horizPosBg.pause();
                    vertPosBg.pause();
                }*/
            }
        };

        bg2ToBg1Tail=new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                System.out.println("Reset bg2y to bg1y+3500, tail");
                background2.addedY=0;
                background2.reset(background.getTailY(), bgNumber++);
            }
        };

        Tween.registerAccessor(Value.class, new ValueAccessor());
        (horizPosBg = Timeline.createSequence()
                .push(Tween.to(horiz, -1, 10).target(-bgw/2+camWidth/2).ease(TweenEquations.easeInSine)    )
                .push(Tween.to(horiz, -1, 10).target((camWidth)-bgw) .ease(TweenEquations.easeNone))
                .push(Tween.to(horiz, -1, 10).target(-bgw/2+camWidth/2).ease(TweenEquations.easeNone)    )
                .push(Tween.to(horiz, -1, 10).target(0)     .ease(TweenEquations.easeOutSine)))
                .repeatYoyo(Tween.INFINITY, 0);
System.out.println("First easing target: "+(-bgh+camHeight/2 )  /2);
        (vertPosBg = Timeline.createSequence()  //7 8 2 2 2 2
                //.push(Tween.to(vert, -1, 3).target((-bgh)  /2).ease(TweenEquations.easeInCubic)    )
                .push((Tween.to(vert, -1, 3).target(-bgh+camHeight/2 ) .ease(TweenEquations.easeOutBack)).               setCallback(pastStoryIntro))
                .push(Tween.to(vert, -1, 1).target(-bgh+camHeight).ease(TweenEquations.easeInOutSine))
                .push(Tween.to(vert, -1, 3).target(-bgh).ease(TweenEquations.easeInOutSine).repeatYoyo(1, 0))
                .push(Tween.to(vert, -1, 2).target(-bgh+camHeight/2).ease(TweenEquations.easeInOutSine))
                .push(Tween.to(vert, -1, 2).target(-bgh*2+bgh/50+camHeight/2)     .ease(TweenEquations.easeInElastic))
                .push(Tween.to(vert, -1, 2).target(-bgw*2-bgh/50+camHeight/2).ease(TweenEquations.easeInOutSine).repeatYoyo(2, 0))
                .push((Tween.to(vert,-1,2).target(-bgw*2).ease(TweenEquations.easeInCubic))           .setCallback(bg2ToBg1Tail))                   )
                .repeat(Tween.INFINITY, 0);

    }

        /* //flipworld
        background3 = new Background(0, -bgh, bgw, bgh);
        background4 = new Background(background3.getTailY(), -bgh, bgw, bgh);*/

        /*startlist(boostlist, false, false, orgBoostnumber);
        startlist(invboostlist, false, true, orgBoostnumber);
        startlist(flipboostlist, true, false, orgBoostnumber);
        startlist(invflipboostlist, true, true, orgBoostnumber); */   //start is same as restart only for boosts

    /*
    public void remove(ArrayList<Boost> boostlist, int i, int boostnumber){
        if (boostlist.size() > boostnumber && boostlist.get(i).isScrolledDown()){
            boostlist.remove(i);}
    }

    public void specificupdate(ArrayList<Boost> boostlist, int boostnumber){
        for (int i = 0; i < boostlist.size(); i++) {
            boostlist.get(i).update();
            remove(boostlist, i, boostnumber);
        }
    }*/

    public void update(float runTime, float delta) {
/*if (!vertPosBg.isStarted()){
            vertPosBg.start(manager);
            horizPosBg.start(manager);
        }*/
//        System.out.print("bg1y: "+background.y + " bg2y: "+background2.y);
//        System.out.println(" vert values:" +vert.getValue() + " addY bg1: "+background.addedY +" addY bg2: "+ background2.addedY);
        if(isPastStoryIntro ){
                //stop running once done

        } else {
            // Update our objects
        }
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

        /*
        background3.update();
        background4.update();
        //System.out.println("Boost"+i+": "+boostlist.get(i).x+","+boostlist.get(i).y+" scrolled"+boostlist.get(i).isScrolledDown());

        specificupdate(boostlist, boostnumber);
        specificupdate(invboostlist, boostnumber);
        specificupdate(flipboostlist, boostnumber);
        specificupdate(invflipboostlist, boostnumber);

        //System.out.println("size: "+boostlist.size()+" number: "+boostnumber);
        //System.out.println("boostnumber"+(boostlist.size()-1));

        updatelist(boostlist, false, false, boostnumber);
        updatelist(invboostlist, false, true, boostnumber);
        updatelist(flipboostlist, true, false, boostnumber);
        updatelist(invflipboostlist, true, true, boostnumber);*/
        // Check if any of the boosts are scrolled left,
        // and reset accordingly
        if (bgNumber== AssetHandler.bgList.size()){
            //waveNumber+=1;
            bgNumber=0;
        }


        if (background.isScrolledDown()) {
            System.out.println("reset bg1y to bg2y +3500");
            background.reset(background2.getTailY(), bgNumber++);
            background2.addedY=bgh;
        }
    }

        /*
        if (background3.isScrolledDown()) {
            background3.reset(background4.getTailY());
        } else if (background4.isScrolledDown()) {
            background4.reset(background3.getTailY());
        }

    // Return true if ANY boost hits the bird.
    public boolean collides(Glider glider, int boostnumber) {
        for (int i = 0; i < boostnumber; i++) {
            if (boostlist.get(i).collides(glider) || invboostlist.get(i).collides(glider) || invflipboostlist.get(i).collides(glider) || flipboostlist.get(i).collides(glider)) {
                collideboost(boostlist, glider, i);
                collideboost(invboostlist, glider, i);
                collideboost(flipboostlist, glider, i);
                collideboost(invflipboostlist, glider, i);
                return true;
            }
        }
        return false;
    }

    /*
    private void collideboost(ArrayList<Boost> boostlist, Glider glider, int i) {
        if (!boostlist.get(i).isScored()
                && boostlist.get(i).getX() + (boostlist.get(i).getWidth() / 2) < glider.getPosition().x + glider.getWidth()) {
            boostlist.get(i).isScrolledDown = true;   //boost gets restarted if player hits it
            boostlist.get(i).setScored(true);
            System.out.println(Math.pow(boostlist.get(i).width, 1.4) / 250);
            addBoost( Math.pow(boostlist.get(i).width, 1.4) / 250);
            AssetHandler.fire.play();
        }
    }*/



    // The getters for our five instance variables
    public Background getBackground() {
        return background;
    }

    public Background getBackground2() {
        return background2;
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