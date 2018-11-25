package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

import com.kredatus.flockblockers.GameObjects.Birds.PhoenixBird;
import com.kredatus.flockblockers.GameWorld.GameWorld;



import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import aurelienribon.tweenengine.Tween;
//import com.badlogic.gdx.ai.steer.behaviors.Arrive;

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

    public float preX, x, y, yVel, yAcc, xVel,yVelDeath, rotation, sizeRatio, finalSizeRatio;
    public Hashtable xMotionTimePositions=new Hashtable();
    public double xMotionTime;
    public float width, height;
    protected float camWidth, camHeight, edge;
    public boolean isOffCam, isColliding;
    public ArrayList<Projectile> hitBulletList = new ArrayList<Projectile>(30);
    public ConcurrentLinkedQueue<Coin> coinList;
    public float  starty;
    public boolean isAlive, firstxMotion=true;
    protected Random r =new Random();
    public Animation frontFlaps, backFlaps, leftFlaps, rightFlaps, animation;
    protected int sizeVariance, coinNumber, health, diamonds, cnt=0, rotationCounter;
    //protected Timeline xMotion;
    protected Tween intro, first, xMotion;
    public Polygon boundingPoly;
    private TimerTask task;
    BirdAbstractClass thisBird=this;
    public BirdAbstractClass() {
        isAlive=true;
        isOffCam = false;
        yAcc=-0.5f;
        yVelDeath=15;
        //this.manager=manager;


    }

    protected void setBoundingPoly(float x, float y, float width, float height){
        boundingPoly  = new Polygon(new float[]{x - width / 3, y - height / 3,          x + width / 3, y - height / 3,          x + width / 3f, y + height / 5f,          x - width / 3f, y + height / 5f});//middle of front bird is below
        boundingPoly  .  setOrigin(x, y);
    }

    public abstract void setManager(float camWidth);

    public boolean collides(Projectile projectile) {
        return Intersector.overlapConvexPolygons(boundingPoly, projectile.boundingRect);
        //}
    }

    public void setRotation() {
        if (xVel>0) {
            if (x>camWidth/3) rotation = ((float) Math.toDegrees(Math.atan(yVel / (xVel / 300))) - 90) / 1.5f; //gradually slow down
            else rotation = ((float) Math.toDegrees(Math.atan(yVel / (xVel))) - 90)/6;                      //quickly start up
        } else if (xVel<0) {
            if (x<camWidth/3) rotation = ((float) Math.toDegrees(Math.atan(yVel / (xVel / 300))) + 90) / 1.5f; //gradually slow down
            else rotation = ((float) Math.toDegrees(Math.atan(yVel / (xVel))) + 90)/6;                      //quickly start up
        }
        if (isAlive) {
            boundingPoly.setRotation(rotation);
        }
                  /*
            if (xVel>0.5) {
                rotation = (float) (Math.toDegrees(-Math.atan(-1 / xVel))) / 7 - 9.5f;
            } else if (xVel<-0.5) {
                rotation = (float) (Math.toDegrees(-Math.atan(-1 / xVel))) / 7 + 9.5f;
            }*/
    }

    public void setCoinList(float delta) {
        coinList = new ConcurrentLinkedQueue<Coin>();

        if (diamonds!=1) {  //if not a phoenix
            final float rotationIncrement = 360 / coinNumber;
            for (int i=0;i<coinNumber;i++) {
                coinList.add(new Coin(x, y, rotationIncrement * rotationCounter++, thisBird, false));
            }
        } else {

            //(0.5*yAcc)
            float realYAcc = yAcc / 2;
            float determinant = (yVelDeath * yVelDeath) - (4 * (realYAcc) * y);     //a=yAcc, b=yDeathVel, distance/c = y, determinant = d = b^2 -4*a*c
            //System.out.println("Acc: " + realYAcc + ", VelDeath: " + yVelDeath + ", Distance: " + y);
            float timeToOffCam;
            if (determinant >= 0) {    // if
                double root1 = (-yVelDeath + Math.sqrt(determinant)) / (2 * realYAcc);
                double root2 = (-yVelDeath - Math.sqrt(determinant)) / (2 * realYAcc);
                //System.out.print("Root1: " + root1 + ", Root2: " + root2);
                if ((root2 <= root1 || root1 < 0) && root2 > 0) {
                    timeToOffCam = (float) root2;
                } else if ((root1 < root2 || root2 < 0) && root1 > 0) {
                    timeToOffCam = (float) root1;
                } else {
                    throw new RuntimeException();
                }
            } else {
                throw new RuntimeException();
            }
            //timeToOffCam=2;
            //System.out.println("Time to hit offCam: " + timeToOffCam);


            Timer timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    if (rotationCounter > coinNumber) {
                        task.cancel();
                    }
                    rotationCounter++;
                    coinList.add(new Coin(x, y, r.nextInt(360), thisBird, true));   //random spurting for phoenix
                    //System.out.println("Coin added at rotation"+rotationIncrement*rotationCounter);
                }
            };

            float timerIntervals = (timeToOffCam * delta * 1.05f) / coinNumber;  //because yacc and yvel are added every frameTimeDifference, we must multiply by delta to get seconds approximation
            timer.scheduleAtFixedRate(task, 0, (int) (timerIntervals * 1000));
        }
    }

    public void update(float delta, float runTime){
        setRotation();
        if (isAlive) {
            y+=yVel;
            preX=x;
            xMotion.update(delta);
            xVel=x-preX;
            boundingPoly.translate(xVel, yVel);

            if (health <= 0) {
                setCoinList(delta);
                die();
            }
            if (y > camHeight - 0) { //0 being height of top of tower where score & diamonds are
                GameWorld.addGold(-coinNumber);
                die();
            }
            specificUpdate(delta, runTime);
        } else {
            width*=0.993;
            height*=0.993;
            y+=yVelDeath;
            yVelDeath+=yAcc;
            x+=xVel;
            if (coinList!=null){
                for (Coin i : coinList){
                    i.update(delta);
                    if ((diamonds!=1 && i.secondYMotion.isFinished()) || (diamonds==1&&i.xMotion.isFinished())) {
                        GameWorld.addGold(1);
                        coinList.remove(i);
                    }
                }
            }
            if (y+height/2<0 || x+width/2< 0 || x-width/2> camWidth){
                isOffCam=true;
            }
        }
    }

    public abstract void specificUpdate(float delta, float runTime);

    private void die(){
        xMotion.kill();

        isAlive=false;
        animation=frontFlaps;
        animation.setFrameDuration(0.03f);
        rotation=0;
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
