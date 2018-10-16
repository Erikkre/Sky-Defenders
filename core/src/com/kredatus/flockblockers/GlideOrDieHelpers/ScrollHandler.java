package com.kredatus.flockblockers.GlideOrDieHelpers;

import com.badlogic.gdx.math.Vector3;
import com.kredatus.flockblockers.GameObjects.Background;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.Screens.SplashScreen;
import com.kredatus.flockblockers.TweenAccessors.Value;
import com.kredatus.flockblockers.TweenAccessors.ValueAccessor;

import java.util.ArrayList;
import java.util.Random;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class ScrollHandler {
    // ScrollHandler will create all five objects that we need.
    private Background background, background2, background3, background4;
    private Random r;
    // ScrollHandler will use the constants below to determine
    // how fast we need to scroll and also determine
    private Value beta =new Value();
    // Capital letters are used by convention when naming constants.
    boolean same = false;
    /*private ArrayList<Boost> boostlist = new ArrayList<Boost>(), invboostlist = new ArrayList<Boost>(),
            flipboostlist = new ArrayList<Boost>(), invflipboostlist = new ArrayList<Boost>();*/
    public ArrayList<Vector3> boostcoords = AssetLoader.getBoostcoords();
    int w = AssetLoader.boost.getWidth();
    int h = AssetLoader.boost.getHeight();
    //private Boost tempBoost;
    //private int orgBoostnumber = AssetLoader.getBoostnumber(), coordslistsize=AssetLoader.getcoordslistsize();
    public int bgw = AssetLoader.bgPhoenix.getWidth();
    public int bgh = AssetLoader.bgPhoenix.getHeight();
    float x, y, width, height;
    private GameWorld gameWorld;
    public int bgNumber;
    // Constructor receives a float that tells us where we need to create our
    // Grass and Pipe objects.
    private TweenManager manager;

    public ScrollHandler(GameWorld gameWorld, float camwidth, float camheight) {
        this.gameWorld = gameWorld;
        bgNumber = 0;
        System.out.println("-camwidth/2: " + -camwidth / 2);
        beta.setValue(-camwidth / 2);
        background = new Background(beta.getValue(), -camheight / 2, bgw, bgh, AssetLoader.bgList.get(bgNumber++));
        background2 = new Background(beta.getValue(), -camheight / 2 + bgh, bgw, bgh, AssetLoader.bgList.get(bgNumber++));
        r = new Random();
        this.manager= SplashScreen.getManager();
        setupTweens(camwidth);
    }

    private void setupTweens(float camwidth){
        Tween.registerAccessor(Value.class, new ValueAccessor());
        (Timeline.createSequence()
                .push(Tween.to(beta, -1, 15).target(-bgw/2).ease(TweenEquations.easeInOutSine)    )
                .push(Tween.to(beta, -1, 15).target((camwidth/2)-bgw) .ease(TweenEquations.easeInOutSine))
                .push(Tween.to(beta, -1, 15).target(-bgw/2).ease(TweenEquations.easeInOutSine)    )
                .push(Tween.to(beta, -1, 15).target(-camwidth/2)     .ease(TweenEquations.easeInOutSine)))
                .repeatYoyo(Tween.INFINITY, 0).start(manager);
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

    public void update(int boostnumber, float runTime, float delta) {
        // Update our objects
        manager.update(delta);
        background.setX(beta.getValue());
        background2.setX(beta.getValue());
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

        if (background.isScrolledDown()) {
            if (bgNumber==AssetLoader.bgList.size()){
                //waveNumber+=1;
                bgNumber=0;
            }
            background.reset(background2.getTailY(), bgNumber++);
        } else if (background2.isScrolledDown()) {
            if (bgNumber==AssetLoader.bgList.size()){
                //waveNumber+=1;
                bgNumber=0;
            }
            background2.reset(background.getTailY(), bgNumber++);
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
            AssetLoader.fire.play();
        }
    }*/

    private void addBoost(double increment) {
        gameWorld.addBoost(increment);
    }

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