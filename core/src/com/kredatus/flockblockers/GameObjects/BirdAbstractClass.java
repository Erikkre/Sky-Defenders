package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

import com.kredatus.flockblockers.GameWorld.GameWorld;



import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import aurelienribon.tweenengine.Tween;
//import com.badlogic.gdx.ai.steer.behaviors.Arrive;

/**
 * Created by Erik Kredatus on 9/8/2018.
 *
 * start with 1000 gold
 *                  Health Speed  Size  Gold             Amount/Wave    Skin Shown      Flight Pattern
 *
 * WaterBird  =     1(S)   S      M      10(490)         49             BACK            Wave line with slight arrow shape, 7 per wave
 * NightBird  =     1(S)   S      M      20(600 / wave)  30             FRONT           1 at a time randomnly                  Thunderbird for nightMap
 * AcidBird   =     7(L)   M      M      15(300)         20             SIDE, FRONT     Side to side slowly
 * FireBird   =     4(M)   M      S      15(300)         30             SIDE, FRONT     Side to Side slowly all at once           nightbird for firemap
 * ThunderBird=     7(L)   F      M      15(300)         20             SIDE, FRONT     Side to side quickly               lunarbird for thundermap
 * LunarBird  =     7(L)   XF     M      120(600)        5              BACK            Diagonal fast                      firebird for lunarmap
 * GoldBird   =     25(XL)  S     L      120(600)        5              FRONT           Slowly side to side
 * PhoenixBird=     150(XXL)S     XL     200+Diamond     1              FRONT,BACK,SIDE       Random positions tweened to (only front-Story intro has back, side, front) then out of map on end of time


 1 dia=1000 go, 5000 go=1 dia		90c/1000 0.09c/dia	$2/3000 0.07c/dia	$5/10000 0.05c/dia	90c/no ads
 cost	500	1500	4500	13500	40500	121500	364500	1000 diamonds	3000 diamonds	10000 diamonds
                I	            II	        III	            IV	            V           	VI	                VII	                VIII	    IX	                X       Dmg	  RoF	  Pen         Spr
 (Fast Firing)	knife thrower	bow	        Machine Pistol  assault rifle	machinegun	    Minigun	            AA Autocannon	    Laser	    Ion cannon	        ???	    2	  1/s	  2 birds     +1 upgradeable
 (High Damage)	spear thrower	crossbow	Ballistae	    Hand Cannon	    sniper rifle	grenade launcher	anti-tank rifle	    Artillery	gauss cannon	    ???	    4	  0.1/s	  3 birds
 (Wide Spread)	dart thrower	tripleshot	MultiCatapult	shotgun	        blunderbuss	    Flamethrower	    Mortar	            Missile	    Microwave emitter	???	    1	  0.5/s	  2 bird	  3 shots +1 each time
                                                                                                                                                                            *=2   *=1.5   *=1.4

 Gold/s	score is gold/s/active playtime		gold is lost when bird passes through


 Gun upgrades work as: each can be upgraded once, to upgrade to level 2 must upgrade turret's main stat, each upgrade is worth .2 of gun's value and does *1.2 of the stat (so main stat is more worth upgradng)  see .excel file (Dmg, RoF, Pen)

 Powerups: Climate Cooling(10 diamonds), Overclock Turrets(30 diamonds), Nuclear Bomb (100 Diamonds)
 */

public abstract class BirdAbstractClass {
    //protected GameWorld world;

    public float preX, x, y, yVel, yAcc, xVel,yVelDeath, rotation, sizeRatio, finalSizeRatio=1;
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
    public Animation frontFlaps, backFlaps, leftFlaps, rightFlaps, deathFlaps, animation;
    protected int sizeVariance, coinNumber, health, diamonds, cnt=0, rotationCounter;
    //protected Timeline xMotion;
    protected Tween intro, first, xMotion;
    public Polygon boundingPoly;
    private TimerTask task;
    private BirdAbstractClass thisBird=this;
    public Animation[] animSeq;

    public BirdAbstractClass() {
        isAlive=true;
        isOffCam = false;
        yAcc=-0.6f;
        yVelDeath=10;
        //this.manager=manager;
    }

    protected void animSetup(){
        frontFlaps=animSeq[0];
        leftFlaps=animSeq[1];
        rightFlaps=animSeq[2];
        backFlaps=animSeq[3];
        deathFlaps=animSeq[4];
        animSeq= new Animation[]{frontFlaps,leftFlaps,frontFlaps,rightFlaps};
        height=((TextureRegion)backFlaps.getKeyFrames()[3]).getRegionHeight();
        width=((TextureRegion)backFlaps.getKeyFrames()[0]).getRegionWidth();
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
            if (diamonds==1){   //is phoenix
                width*=0.996;
                height*=0.996;
            } else {
                width*=0.985;
                height*=0.985;
            }
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
        animation=deathFlaps;

        if (x>camWidth/2){   //if dying on right side fall to left and vice versa
            xVel=-2;
        } else {
            xVel=2;
        }
    }

    public final void hit(Projectile projectile){
        health-= projectile.dmg;
    }

}
