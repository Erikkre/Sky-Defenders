package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
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
 * "defend your sky fortress and stop the bird invasion"
 *
 * change stats with ctrl+shift+f to match spec
 * start with 1000 gold
 *
 *                  Health Speed  Size   Gold            Amount/Wave    Skin Shown          Flight Pattern          FOR TEXTURES COMBINE PHOENIX ONES AND MAKE NEW FOR EACH TYPE AND PUT IN birdsOriginal(8k wide), cut up for birdsOriginalCutForUse, shrink for actual game
 * ThunderBird=     1(S)    2     S      12(360)         30             F S B               Side to side all at once
 * WaterBird  =     1(S)    2     S      10(490)         33             B                   Wave line with slight arrow shape, 11 per wave, add 1 wave every 2 rounds
 * FireBird   =     1(S)    2     S      15(300)         35             S B F               all at once, some looking forwards some back, occasionally some go to either side sideways, all move as 1 mass
 * AcidBird   =     4(L)    12    M      15(300)         15             B S F               Side to side fast (make face front one side back the other)
 * NightBird  =     4(M)    12    M      20(600 / wave)  10             F B                 1 at a time randomly, sometimes back sometimes front, start slow end fast
 * LunarBird  =     4(M)    12    M      15(300)         10             B S                 Diagonal side to side
 * GoldBird   =     25(XL)  4     L      120(600)        5              F S                 Slowly side to side
 * PhoenixBird=     150(XXL)4     XL     200+Diamond     1              F B S               Random positions tweened to (only front-Story intro has back, side, front) then hit wall at end of wave
Only add health to phoenix each round

 1 dia=1000 go, 5000 go=1 dia		90c/100 0.9c/dia	$2/300 0.7c/dia	$5/1000 0.5c/dia	$1.59/no ads
 cost	        500	            1500	    4500	        13500	        40500	        121500          	364500	            1000 diamonds	3000 diamonds	10000 diamonds
                I	            II	        III	            IV	            V           	VI	                VII	                VIII	    IX	                X       Dmg	  RoF	  Pen         Spr
 (Fast Firing)	knife thrower	bow	        Machine Pistol  assault rifle	machinegun	    Minigun	            AA Autocannon	    Laser	    Ion cannon	        ???	    2	  1/s	  2 birds     +1 upgradeable
 (High Damage)	spear thrower	crossbow	Ballistae	    Hand Cannon	    sniper rifle	grenade launcher	anti-tank rifle	    Artillery	gauss cannon	    ???	    4	  0.1/s	  3 birds
 (Wide Spread)	dart thrower	multishot	MultiCatapult	shotgun	        blunderbuss	    Flamethrower	    Mortar	            Missile	    Microwave emitter	???	    1	  0.5/s	  2 bird	  3 shots +1 each time
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
    public ConcurrentLinkedQueue<Coin> coinList= new ConcurrentLinkedQueue<Coin>();
    public float  starty;
    public boolean isAlive, firstxMotion=true;
    protected Random r =new Random();
    public Animation frontFlaps, backFlaps, leftFlaps, rightFlaps, deathFlaps, animation;
    protected int sizeVariance, coinNumber,  diamonds, cnt=0, rotationCounter;
    //protected Timeline xMotion;
    protected Tween intro, first, xMotion;
    public Polygon boundingPoly;
    private TimerTask task;
    private BirdAbstractClass thisBird=this;
    public Animation[] animSeq;
    public int health;

    public BirdAbstractClass() {
        isAlive=true;
        isOffCam = false;
        yAcc=-0.6f;
        yVelDeath=10;
        //this.manager=manager;
    }

    protected void setBoundingPoly(float x, float y, float width, float height){
        boundingPoly  = new Polygon(new float[]{x - width / 4.5f, y - height / 4,          x + width / 4.5f, y - height / 4,          x + width / 4.5f, y + height / 6f,          x - width / 4.5f, y + height / 6f});//middle of front bird is below
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

    private  void setCoinList(float delta) {


        if (coinNumber<100) {  //if not a phoenix or goldbird
            final float rotationIncrement = 360 / coinNumber;
            for (int i=0;i<coinNumber;i++) {
                coinList.add(new Coin(x, y, rotationIncrement * rotationCounter++, thisBird, false));
            }
        } else {


            //(0.5*yAcc)
            float realYAcc = yAcc / 2;
            float determinant = (yVelDeath * yVelDeath) - (4 * (realYAcc) * y);     //a=yAcc, b=yDeathVel, distance/c = y, determinant = d = b^2 -4*a*c
            System.out.println("Acc: " + realYAcc + ", VelDeath: " + yVelDeath + ", Distance: " + y + ", CamHeight: "+camHeight);
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
                    if ((coinNumber<100 && i.secondYMotion.isFinished()) || (coinNumber>100&&i.xMotion.isFinished())) {
                        GameWorld.addGold(1);
                        coinList.remove(i);
                    }
                }
            }
        }
    }

    public abstract void specificUpdate(float delta, float runTime);

    public boolean isOffCam(){
        return y+height/2<0 || x+width/2< 0 || x-width/2> camWidth;
    }

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
