// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Birds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.Coin;
import com.kredatus.flockblockers.GameObjects.Projectile;
import com.kredatus.flockblockers.GameWorld.GameHandler;
import com.kredatus.flockblockers.Handlers.BgHandler;
import com.kredatus.flockblockers.TweenAccessors.Value;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
//import com.badlogic.gdx.ai.steer.behaviors.Arrive;

/**
 * Created by Erik Kredatus on 9/8/2018.
 *
 * "defend your sky fortress and stop the bird invasion"
 *
 * change stats with ctrl+shift+f to match spec
 * start with 1000 gold
 * damage of guns is based on size, spread = good against small, fast = good for med, high damage = good for bosses
 *                  Health Speed  Size   Gold            Amount/Wave    Skin Shown          Flight Pattern          FOR TEXTURES COMBINE PHOENIX ONES AND MAKE NEW FOR EACH TYPE AND PUT IN birdsOriginal(8k wide), cut up for birdsOriginalCutForUse, shrink for actual game
 * ThunderBird=     2(S)    1-2.5 .4     1(20)           20             F S B               Side to side all at once
 * WaterBird  =     2(S)    2     .4     1(20)           20             B                   Wave line with slight arrow shape, 11 per wave, add 1 wave every 2 rounds
 * FireBird   =     2(S)    1     .4     1(25)           25             S B F               all at once, some looking forwards some back, occasionally some go to either side sideways, all move as 1 mass
 * AcidBird   =     4(L)    5     .6    3(30)           10             B S F               Side to side fast (make face front one side back the other)
 * NightBird  =     4(M)    12    .6    4(32)           8              F B                 1 at a time randomly, sometimes back sometimes front, start slow end fast
 * LunarBird  =     4(M)    12    .6    5(35)           7              B S                 Diagonal side to side
 * GoldBird   =     15(XL)  3     .8     25(75)          3              F S                 Random Positions but stay from beginning to end
 * PhoenixBird=     60(XXL) 3     1      100||diamond    1              F B S               Random poss tweened to (only front-Story intro has back, side, front) then hit wall at end of wave
Only add health to phoenix each round   after you hit multiples of 500 gold/phoenix add chance to drop that multiple of diamonds instead
                                        i.e. 1 diamond OR 500 gold (1 Dia=1000 Go)

 1 dia=1000 go, 5000 go=1 dia		90c/100 0.9c/dia	$2/300 0.7c/dia	$5/1000 0.5c/dia	$2/no ads
 cost	        500	            1500	    4500	        13500	        40500	        121500          	364500	            1000 diamonds	3000 diamonds	10000 diamonds
                I	            II	        III	            IV	            V           	VI	                VII	                VIII	    IX	                X       Dmg	  RoF	  Pen         Spr
 (Fast Firing)	knife thrower	bow	        Machine Pistol  assault rifle	machinegun	    Minigun	            AA Autocannon	    Laser	    Ion cannon	        ???	    1	  1/s	  2 birds     +1 upgradeable (last upgrade cuz pretty op, maybe lower fire rate)
 (Damage Dealer)spear thrower	crossbow	Ballistae	    Hand Cannon	    sniper rifle	grenade launcher	anti-tank rifle	    Artillery	gauss cannon	    ???	    4	  0.1/s	  3 birds
 (Crowd Control)dart thrower	multishot	MultiCatapult	shotgun	        blunderbuss	    Flamethrower	    Mortar	            Missile	    Microwave emitter	???	    0.5   0.5/s	  2 bird	  3 shots +1 each time
                                                                                                                                                                            *=2   *=1.5   *=1.4

 Gold/s	score is gold/s/active playtime		gold is lost when bird passes through


 Gun upgrades work as: each can be upgraded once, to upgrade to level 2 must upgrade turret's main stat, each upgrade is worth .2 of gun's value and does *1.2 of the stat (so main stat is more worth upgradng)  see .excel file (Dmg, RoF, Pen)

 Powerups: Climate Cooling(10 diamonds), Overclock Turrets(30 diamonds), Nuclear Bomb (100 Diamonds)

 Birds will be targeting a hot air balloon with 5 racks of turrets under it (5 4 3 2 1 = 15 gun slots)
 */

public abstract class BirdAbstractClass {
    //protected GameWorld world;

    protected float globalSpeedMultiplier = 1.0f, globalHealthMultiplier = 7.0f;

    public float preX, preY, x, y, yVel, yAcc, xVel,yVelDeath, sizeRatio, finalSizeRatio=1, preTargetY;
    //public Hashtable xMotionTimePositions=new Hashtable();
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
    public int sizeVariance, coinNumber,  diamonds, cnt=0, rotationCounter;
    //protected Timeline xMotion;
    protected Tween introX, introY, first, firstX, firstY, currentX, currentY;
    public Polygon boundingPoly;
    private TimerTask task;
    private BirdAbstractClass thisBird=this;
    public Animation[] animSeq;

    public int origHealth, health;
    public float targetRot, rotation, rotStep, unRotStep;

    public float origYVel, origFlapSpeed, lastYSpeedAtFlapChange;

    protected boolean startRot, unRotate, rotate, fasterFlap;
    //public AnimationController animControl = AnimationController();
    protected ArrayList<Float> flashLengths=new ArrayList<Float>(),flapSpeedIntervals=new ArrayList<Float>();

    public float flapRandomFactor,currentFlashLength;

    public boolean isFlashing;
    public Value flashOpacityValue = new Value();
    public Tween flashTween, outroY;
    public TweenCallback endFlashing;

    /*TweenEquation[] tweenEquations = {TweenEquations.easeOutExpo}; /*,TweenEquations.easeOutQuint,TweenEquations.easeOutQuart,
            TweenEquations.easeOutCubic,TweenEquations.easeOutQuad,TweenEquations.easeOutSine,
            TweenEquations.easeNone};*/

    Vector2 airshipPos;
    public BirdAbstractClass(Vector2 airshipPos) {
        this.airshipPos=airshipPos;
        outroY=Tween.to(this, 2, 2).target(GameHandler.camHeight*1.2f).ease(TweenEquations.easeInExpo); //hit wall when not killed at end of spawning period

        if (FlockBlockersMain.fastTest) {globalSpeedMultiplier = 3f; globalHealthMultiplier=0.1f;}

        //if (flashTween!=null)flashTween.kill();
        endFlashing = new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                isFlashing = false;
                flashTween = null;
            }
        };

        isAlive=true;
        isOffCam = false;
        yAcc=-0.6f;
        yVelDeath=10;
        rotStep=2.1f;
        unRotStep=0.6f;
        //this.manager=manager;
        flapRandomFactor=r.nextFloat()*0.5f;
    }
    protected void flapSpeedIntervals(){
        for (float i=1.6f; i>=1;i-=0.05f){
            flapSpeedIntervals.add(origFlapSpeed/i); //fast to slow
        }
        //System.out.println(flapSpeedIntervals.toString());
    }

    protected void setBoundingPoly(float x, float y, float width, float height){
        boundingPoly  = new Polygon(new float[]{x - width / 5f, y - height / 4,          x + width / 5f, y - height / 4,          x + width / 3f, y + height / 5f,          x - width / 3f, y + height / 5f}); //trapezoid to reach bid wings
        boundingPoly  .  setOrigin(x, y);
    }

    public void update(float delta, float runTime){
        if (isAlive) {
            if (!BgHandler.isBirdSpawning&&currentY!=outroY) {
                if (currentX!=null)currentX.kill();
                currentY = outroY.start();
                if (x > camWidth / 2) {   //if dying on right side fall to left and vice versa
                    xVel = -2;
                } else {
                    xVel = 2;
                }
                animation = backFlaps;
            }
            setPositionAndVel(delta);
            //System.out.println(xVel);
            setAndRotateToTargetRot();
                                                                        //as speed goes up fraction goes down, at 0.1 (10x speed) flap at 0.5*origFlapSpeed so twice as fast
            if (yVel>origYVel&& !fasterFlap){
                fasterFlap=true;
                lastYSpeedAtFlapChange=yVel;
                animation.setFrameDuration(flapSpeedIntervals.get((int)((origYVel/yVel)*flapSpeedIntervals.size())));
                //System.out.println("Heighten"+animation.getFrameDuration());
            }  else if (yVel<lastYSpeedAtFlapChange && yVel<= origFlapSpeed && fasterFlap){
                fasterFlap=false;
                animation.setFrameDuration(origFlapSpeed);//base with yvel at original vel, we flap standard speed. but with faster vel we have shorter frame duration so faster flapping
                //System.out.println("Lower"   +animation.getFrameDuration());
            }

            if (health <= 0) {
                setCoinList(delta);
                die();
            }
            /*if (y > camHeight +height/2) { //0 being height of top of tower where score & diamonds are
                BirdHandler.activeBirdQueue.remove(this);

               System.out.println(this+" removed");
            }*/
            specificUpdate(delta, runTime);

        } else {
            //System.out.println("dead");
            if (coinNumber>=25){   //is phoenix
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
                    if (i.secondYMotion.isFinished()) {
                        ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.gameHandler.world.addGold(1);
                        coinList.remove(i);
                    }
                }
            }
        }
    }

    public void setAndRotateToTargetRot(){
        if (startRot){
            startRot=false;
            targetRot=-xVel*4+ (Math.signum(-xVel)*2);
            //System.out.println("TargetRot: "+targetRot);
            if (targetRot<0){
                targetRot+=360;
            }
            rotate=true;
        }
        if (rotate) {  //if rotation is not 0 for any reason then startRot back
            if (Math.abs(rotation - targetRot)>rotStep){
                rotation=rotationToTargetRot(rotation,targetRot,rotStep);   //moves rotation to targetRot by angleStep
            } else {
                rotation=targetRot;
                rotate=false;
                unRotate=true;
            }
        }
        if (unRotate){
            if (Math.abs(rotation)>unRotStep) {
                rotation = rotationToTargetRot(rotation, 0, unRotStep);
            } else {
                rotation=0;
                unRotate=false;
            }
        }
        boundingPoly.setRotation(rotation);
    }

    public void setPositionAndVel(float delta){
        if (currentY!=null&&currentY.isStarted()){
            preY=y;
            currentY.update(delta);
            //System.out.println("currentY tween: "+y);
            yVel=y-preY;
        } else {
            //System.out.println("y: "+y+" += "+yVel);
            y+=yVel;
        }
        if (currentX!=null&&currentX.isStarted()){
            preX=x;
            currentX.update(delta);
            //System.out.println("currentX tween: "+x);
            xVel=x-preX;
        } else {
            //System.out.println("x: "+x+" += "+xVel);
            x+=xVel;
        }
        boundingPoly.translate(xVel, yVel);
    }

    public void die(){
        if (currentX!=null) currentX.kill();
        if (currentY!=null) currentY.kill();

        isAlive=false;
        animation=deathFlaps;

        if (x>camWidth/2){   //if dying on right side fall to left and vice versa
            xVel=-2;
        } else {
            xVel=2;
        }
    }

    public boolean isOffCam(){
        return y+height/2<0 || x+width/2< 0 || x-width/2> camWidth;
    }
    public boolean isAboveCam(){
        return y-height/2>camHeight;
    }

    public abstract void setManager(float camWidth);

    public boolean collides(Polygon objectYoureHitting) {
        return Intersector.overlapConvexPolygons(boundingPoly, objectYoureHitting);
    }

    private float rotationToTargetRot(float rotation, float targetRot, float angleStep) {
        float behindRotation = rotation - 180;
        if (behindRotation < 0) {
            behindRotation += 360;
        }
            if (rotation < 180) {
                if (targetRot > rotation && targetRot < behindRotation) {
                    rotation += angleStep;
                } else {
                    rotation -= angleStep;

                    if (rotation < 0) {
                        rotation += 360;
                    }
                }
            } else {
                if (targetRot < rotation && targetRot > behindRotation) {
                    rotation -= angleStep;
                } else {
                    rotation += angleStep;
                    if (rotation > 360) {
                        rotation -= 360;
                    }
                }
        }
        return rotation;
    }
        /*
        if (isUnrotating){
            if (rotation>180&&rotation<359){
                rotation+=rotSpeed/100;
                //System.out.println("startRot left to unstartRot");
            } else if (rotation<180&&rotation>1){
                rotation-=rotSpeed/100;
                //System.out.println("startRot right to unstartRot");
            } else if (Math.abs(xVel)<0.5){
                isUnrotating=false;
            }
        }
        */

    public void setTargetRot() {
        /*if (xVel>0) {
            if (x>camWidth/3) rotation = ((float) Math.toDegrees(Math.atan(yVel / (xVel / 300))) - 90) / 1.5f; //gradually slow down
            else rotation = ((float) Math.toDegrees(Math.atan(yVel / (xVel))) - 90)/6;                      //quickly start up
        } else if (xVel<0) {
            if (x<camWidth/3) rotation = ((float) Math.toDegrees(Math.atan(yVel / (xVel / 300))) + 90) / 1.5f; //gradually slow down
            else rotation = ((float) Math.toDegrees(Math.atan(yVel / (xVel))) + 90)/6;                      //quickly start up
        }*/

        /*
        targetRot=(float) (Math.signum(-xVel)*5*Math.pow(Math.abs(xVel), 0.35)) ;   //y=5x^{0.4}
        if (targetRot<0){
            //if (targetRot<5) targetRot*=((5-targetRot)/2);
            targetRot+=360;
        }

        if (targetRot<345&&targetRot>180)      targetRot=345;
        else if (targetRot>15&&targetRot<180)  targetRot=15;
*/
        //if (Math.abs(targetRot)<5) targetRot*=(1+(5-targetRot));
        //System.out.println("Rotation target: "+targetRot+" xvel: "+xVel);
                  /*
            if (xVel>0.5) {
                rotation = (float) (Math.toDegrees(-Math.atan(-1 / xVel))) / 7 - 9.5f;
            } else if (xVel<-0.5) {
                rotation = (float) (Math.toDegrees(-Math.atan(-1 / xVel))) / 7 + 9.5f;
            }*/
    }

    private  void setCoinList(float delta) {
        if (coinNumber<25) {  //if not a phoenix or goldbird
            final float rotationIncrement = 360 / coinNumber;
            for (int i=0;i<coinNumber;i++) {
                coinList.add(new Coin(rotationIncrement * rotationCounter++, thisBird, false,  airshipPos));
            }
        } else {
            //(0.5*yAcc)
            float realYAcc = yAcc / 2;
            float determinant = (yVelDeath * yVelDeath) - (4 * (realYAcc) * y);     //a=yAcc, b=yDeathVel, distance/c = y, determinant = d = b^2 -4*a*c
            //System.out.println("Acc: " + realYAcc + ", VelDeath: " + yVelDeath + ", Distance: " + y + ", CamHeight: "+camHeight);
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
                    coinList.add(new Coin(r.nextInt(360), thisBird, true,airshipPos));   //random spurting for phoenix
                    //System.out.println("Coin added at rotation"+rotationIncrement*rotationCounter);
                }
            };
            float timerIntervals = (timeToOffCam * delta * 1.05f) / coinNumber;  //because yacc and yvel are added every frameTimeDifference, we must multiply by delta to get seconds approximation
            timer.scheduleAtFixedRate(task, 0, (int) (timerIntervals * 1000)+1);
        }
    }

    public abstract void specificUpdate(float delta, float runTime);

    public final void hit(float collisionDmg){
        health -= collisionDmg;
        isFlashing = true;
        flashOpacityValue.set(1f);//always start from white flash to distinguish from bg
        if (collisionDmg<origHealth&&health>0){
            currentFlashLength=flashLengths.get((int)((collisionDmg/origHealth)*flashLengths.size()));
            flashTween = Tween.to(flashOpacityValue, -1, currentFlashLength).target(0f).ease(TweenEquations.easeOutExpo).setCallback(endFlashing).start();
        } else {
            //currentFlashLength=flashLengths.get(flashLengths.size()-1); //else make flash black (-1f-0f)
            flashOpacityValue.set(1f);    //make a death shader effect

                    //.push(Tween.to(flashOpacityValue, -1, 0.3f).target(1f).ease(TweenEquations.easeOutExpo))
                    flashTween = Tween.to(flashOpacityValue, -1, 2f).target(-1f).ease(TweenEquations.easeOutExpo).setCallback(endFlashing).start();
        }
        //System.out.println(currentFlashLength);
    }
}