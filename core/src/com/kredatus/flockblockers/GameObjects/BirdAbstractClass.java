package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.kredatus.flockblockers.GameWorld.GameWorld;


import java.util.Hashtable;
import java.util.Random;

import aurelienribon.tweenengine.Tween;


/**
 * Created by Erik Kredatus on 9/8/2018.
 *
 *                  Health Speed  Size  Gold             Amount/Wave
 *
 * WaterBird  =     1(S)   S      M      12(480)         40
 * NightBird  =     1(S)   S      S      8(480 / wave)   30
 * AcidBird   =     7(M)   M      M      15(300)         20
 * FireBird   =     7(M)   M      S      15(300)         20
 * ThunderBird=     7(M)   F      M      15(300)         20
 * LunarBird  =     7(M)   XF     S      50(500)         10
 * GoldBird   =     25(L)  S      L      100(500)        5
 * PhoenixBird=     100(XL)S      XL     7+Diamond       1

 Gun upgrades work as: each can be upgraded once, to upgrade to level 2 must upgrade whole turret, see .excel file (Dmg, RoF, Pen)

                                                                                                                                Damage      Fire Rate     Penetration   Spread
 (Fast Firing) knife thrower,    bow,        submachinegun,  assault rifle,    Machinegun,  Minigun, laser,  ion cannon        2           L             M
 (High Damage) spear thrower,    crossbow,   ballistae,  hunting rifle,  anti-tank sniper, cannon,      gauss cannon           4           S             L
 (Wide Spread) shuriken thrower, tripleShot, tripleCatapult, shotgun, blunderbuss, missile, Microwave emitter                  1           M             L             XL

 1 Diamond=10000 Gold
 Powerups: Climate Cooling(5 diamonds), Overclock Turrets(15 diamonds), Nuclear Bomb (25 Diamonds)
 */

public abstract class BirdAbstractClass {
    //protected GameWorld world;

    public float preX, x, y, yVel, yAcc, xVel, rotation, sizeRatio, finalSizeRatio;
    public Hashtable xMotionTimePositions;
    public double xMotionTime;
    public float width, height;
    protected float camWidth, camHeight, edge;
    public boolean isOffCam;
    public float  starty;
    protected boolean isAlive, firstxMotion=true;
    protected Random r =new Random();
    public Animation frontFlaps, backFlaps, leftFlaps, rightFlaps, animation;
    protected int sizeVariance, coins, health, diamonds, cnt=0;
    //protected Timeline xMotion;
    protected Tween intro, first, xMotion;
    public Polygon boundingPoly;

    public BirdAbstractClass() {
        isAlive=true;
        isOffCam = false;
        //this.manager=manager;
    }

    protected void setBoundingPoly(float x, float y, float width, float height){


        boundingPoly  = new Polygon(new float[]{x - width / 3, y - height / 3,          x + width / 3, y - height / 3,          x + width / 3f, y + height / 5f,          x - width / 3f, y + height / 5f});//middle of front bird is below
        boundingPoly  . setOrigin(x, y);

        System.out.print("x of poly set to" + x);

        //boundingPoly.dirty();
    }
    public abstract void setManager(float camWidth);

    //public abstract void fly(float delta) ;
    public boolean collides(Projectile projectile) {
        //if (x <= bird.x + bird.width && y-height<bird.y+bird.getHeight()/2 && y+height*2>bird.getPosition().y) {
            return Intersector.overlapConvexPolygons(boundingPoly, projectile.boundingRect);
        //}
    }

    public void update(float delta, float runTime){


        y+=yVel;
        if (isAlive) {
            preX=x;
            xMotion.update(delta);



            System.out.println("Shape x: "+x);

            xVel=x-preX;
            boundingPoly.translate(xVel, yVel);
            if (xVel>0.1) {
                rotation = (float) (Math.toDegrees(-Math.atan(-1 / xVel))) / 7 - 9;
            } else if (xVel<-0.1) {
                rotation = (float) (Math.toDegrees(-Math.atan(-1 / xVel))) / 7 + 9;
            }
            boundingPoly.setRotation(rotation);
            if (health <= 0) {
                die();
            }
            if (y > camHeight - 0) { //0 being height of top of tower where score & diamonds are
                health=0;
                GameWorld.addGold(-coins);
                //isOffCam=true;
            }
        } else {
            yVel+=yAcc;
            x+=xVel;

            if (y+height/2<0 || x+width/2< 0 || x-width/2> camWidth){
                isOffCam=true;
            }
        }
        specificUpdate(delta, runTime);
    }

    public abstract void specificUpdate(float delta, float runTime);

    private void die(){
        xMotion.kill();
        isAlive=false;
        animation=frontFlaps;
        animation.setFrameDuration(0.05f);

        yAcc=-0.8f;
        yVel=15;
        if (x>camWidth/2){   //if dying on right side fall to left and vice versa
            xVel=-3;
        } else {
            xVel=3;
        }
    }

    public final void hit(Projectile projectile){
        health-= projectile.dmg;
    }

  //  public void dead(float delta){
//
   // }

/*
    public float distanceAfterDeath() {
        return Math.abs(position.y+height/2) - (bgh - midpointY);
    }

    public boolean isFlipWorld() {
        return position.y < -bgh / 8;
    }

    public boolean isNormalWorld() {
        return position.y > bgh / 8;
    }

    public boolean isPositive() {
        return position.y >0;
    }

    public boolean isNegative() {
        return position.y <0;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getRotation() {
        return rotation;
    }

    public Circle getboundingCir() {
        return boundingCir;
    }
    */

}
