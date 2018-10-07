package com.kredatus.flockblockers.GlideOrDieHelpers;

import com.badlogic.gdx.math.Vector3;
import com.kredatus.flockblockers.GameObjects.Background;
import com.kredatus.flockblockers.GameWorld.GameWorld;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Mr. Kredatus on 8/31/2017.
 */

public class ScrollHandler {
    // ScrollHandler will create all five objects that we need.
    private Background background, background2, background3, background4;
    private Random r;
    // ScrollHandler will use the constants below to determine
    // how fast we need to scroll and also determine

    // Capital letters are used by convention when naming constants.
    boolean same = false;
    /*private ArrayList<Boost> boostlist = new ArrayList<Boost>(), invboostlist = new ArrayList<Boost>(),
            flipboostlist = new ArrayList<Boost>(), invflipboostlist = new ArrayList<Boost>();*/
    public ArrayList<Vector3> boostcoords = AssetLoader.getBoostcoords();
    int w = AssetLoader.boost.getWidth();
    int h = AssetLoader.boost.getHeight();
    //private Boost tempBoost;
    private int orgBoostnumber = AssetLoader.getBoostnumber(), coordslistsize=AssetLoader.getcoordslistsize();
    public int bgw = AssetLoader.bgPhoenix.getWidth();
    public int bgh = AssetLoader.bgPhoenix.getHeight();
    float x, y, width, height;
    private GameWorld gameWorld;
    // Constructor receives a float that tells us where we need to create our
    // Grass and Pipe objects.

    public ScrollHandler(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        background = new Background(-bgw/2, 0, bgw, bgh, AssetLoader.bgList.get(0));
        background2 = new Background(-bgw/2, bgh, bgw, bgh, AssetLoader.bgList.get(1));
        r = new Random();

        /* //flipworld
        background3 = new Background(0, -bgh, bgw, bgh);
        background4 = new Background(background3.getTailY(), -bgh, bgw, bgh);*/

        /*startlist(boostlist, false, false, orgBoostnumber);
        startlist(invboostlist, false, true, orgBoostnumber);
        startlist(flipboostlist, true, false, orgBoostnumber);
        startlist(invflipboostlist, true, true, orgBoostnumber); */   //start is same as restart only for boosts
    }

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

    public void update(int boostnumber) {
        // Update our objects
        background.update();
        background2.update();
                /*
        background3.update();
        background4.update();
        //System.out.println("Boost"+i+": "+boostlist.get(i).x+","+boostlist.get(i).y+" scrolled"+boostlist.get(i).isScrolledDown());

        /*
        specificupdate(boostlist, boostnumber);
        specificupdate(invboostlist, boostnumber);
        specificupdate(flipboostlist, boostnumber);
        specificupdate(invflipboostlist, boostnumber);
*/

        //System.out.println("size: "+boostlist.size()+" number: "+boostnumber);
        //System.out.println("boostnumber"+(boostlist.size()-1));

        /*
        updatelist(boostlist, false, false, boostnumber);
        updatelist(invboostlist, false, true, boostnumber);
        updatelist(flipboostlist, true, false, boostnumber);
        updatelist(invflipboostlist, true, true, boostnumber);*/
        // Check if any of the boosts are scrolled left,
        // and reset accordingly

        if (background.isScrolledDown()) {
            background.reset(background2.getTailY());
        } else if (background2.isScrolledDown()) {
            background2.reset(background.getTailY());
        }
        /*
        if (background3.isScrolledDown()) {
            background3.reset(background4.getTailY());
        } else if (background4.isScrolledDown()) {
            background4.reset(background3.getTailY());
        }*/
    }
/*
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

    /*public ArrayList getboostlist() {
        return boostlist;
    }

    public ArrayList getflipboostlist() {return flipboostlist;}

    public ArrayList getinvboostlist() {
        return invboostlist;
    }

    public ArrayList getinvflipboostlist() {
        return invflipboostlist;
    }*/

    /*public void startlist(ArrayList<Boost> boostlist, boolean flipped, boolean horinv, int boostnumber) {
        while (boostlist.size() < boostnumber) {   //first half of boosts (unflipped map passing)

            int index = r.nextInt(coordslistsize);
            if (!horinv) {    //2nd inverted half of map printed a mirror image of normal map boosts
                x = (boostcoords.get(index).x - (w / boostcoords.get(index).z / 2));
            } else {
                x = (bgw + (bgw - boostcoords.get(index).x)) - (w / boostcoords.get(index).z / 2);
            }
            y = (boostcoords.get(index).y);
            width = (w / boostcoords.get(index).z);
            height = (h / boostcoords.get(index).z);

            if (!flipped) {
                tempBoost = new Boost(x, y - (h / boostcoords.get(index).z / 2), (int) width, (int) height);
            } else {
                tempBoost = new Boost(x, -y - (h / boostcoords.get(index).z / 2), (int) width, (int) height);
            }
            for (Boost i : boostlist) {
                if (i.getX() == x) {
                    same = true;
                }
            }
            if (!same) {

                boostlist.add(tempBoost);
            }
            same = false;
        }
    }*/
/*
    public ArrayList<Boost> updatelist(ArrayList<Boost> boostlist, boolean flipped, boolean horinv, int boostnumber) {
        //System.out.println("list size : "+boostlist.size());
        //System.out.println("New number: "+boostnumber);

        for (int i = 0; i < boostnumber; i++) {

            if (boostlist.get(i).isScrolledDown()) {
                boostlist.get(i).boostReset();

                same = true;
                while (same) {   //keep going through loop until the boost that needs to be reset is not the same as another one
                    same = false;

                    int index = r.nextInt(coordslistsize);
                    if (!horinv) {  //2nd half of list is inverted map ones
                        x = (boostcoords.get(index).x - (w / boostcoords.get(index).z / 2));
                    } else {
                        x = (bgw + (bgw - boostcoords.get(index).x)) - (w / boostcoords.get(index).z / 2);
                    }
                    y = (boostcoords.get(index).y);
                    width = (w / boostcoords.get(index).z);
                    height = (h / boostcoords.get(index).z);

                    for (Boost j : boostlist) {
                        if (j.getX() == x + background2.getTailY()) {
                            same = true;
                        }
                    }
                    if (!same) {    //if boost isnt already in list add to random x, y using original coords added from rightmost background

                        if (!flipped) {
                            boostlist.set(i, new Boost(x + background2.getTailY(), y - (h / boostcoords.get(index).z / 2), (int) width, (int) height));
                        } else {
                            boostlist.set(i, new Boost(x + background2.getTailY(), -y - (h / boostcoords.get(index).z / 2), (int) width, (int) height));
                        }
                    }
                }
            }
        }
        return boostlist;
    }

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