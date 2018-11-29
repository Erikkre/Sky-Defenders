package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Handlers.TargetHandler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Erik Kredatus on 9/9/2018.
 */

public class Turret {
    private boolean firing, startedTapping;
    private boolean[] firingSpeedLevelCheck = new boolean[10];
    private int[] tapSpeedLevels = {10000, 300, 255, 215, 190, 175, 160, 155, 151, 147, 0}, firingSpeedLevels=new int[tapSpeedLevels.length-1];  //10 levels //#'s represent time in ms between each tap, each number is bottom point of 5 different tap speed levels i.e. infinity-200, 200-175, ... 155-0. used if tapped and startedTapping
    public int width, height, baseFiringSpeedLevel, lastFiringSpeedLevel, firingSpdDecCounter=firingSpeedLevels.length-1;
    public Vector2 position;
    private float camWidth, camHeight;
    public float dmg, pen, spr, rof;
    private float rotation;
    private Timer timer;
    private TimerTask timerTask;
    private BirdAbstractClass targetBird;
    public TextureRegion texture, projTexture;
    private double lastTapTime, timeSinceLastTap=1000, lastShotTime;

    public Turret(char turretType, int lvl, Vector2 position, float camWidth, float camHeight){
        this.position = position ;
        this.camWidth = camWidth ;
        this.camHeight= camHeight;
        timer=new Timer();
        firing=false;
        turretSetup(turretType, lvl);

        if (position.x<camWidth/2) {
            texture = new TextureRegion(texture);
            texture.flip(false,true);
        }
        setTarget(BirdHandler.activeBirdQueue.peek());
        setupFiring();

        baseFiringSpeedLevel = (int) ((1 / (rof)) * 1000);
        firingSpeedLevels[0]=baseFiringSpeedLevel;
        for (int i=1;i<firingSpeedLevels.length;i++){
            firingSpeedLevels[i]=(int)(firingSpeedLevels[i-1]*0.9);
        }
        System.out.println("Firing intervals: "+firingSpeedLevels[0]+", "+firingSpeedLevels[1]+", "+firingSpeedLevels[2]+", "+firingSpeedLevels[3]+", "+firingSpeedLevels[4]+", "+firingSpeedLevels[5]+", "+firingSpeedLevels[6]+", "+firingSpeedLevels[7]+", "+firingSpeedLevels[8]+", "+firingSpeedLevels[9]);
    }

    private void setupFiring() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //System.out.println("Added pen of "+pen);

                lastShotTime=System.currentTimeMillis();
                System.out.println("*******************************************Last shot time: "+lastShotTime+"**********************************************************");
                TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, position, camWidth, camHeight, rotation));
            }
        };//set task to run later using timer.schedule
    }

    private void setRotation(float xVel, float yVel, float yDistance, float xDistance){
        float rotCompYDiff=((xVel*(Math.abs(yDistance)/(camHeight*4)))  *1.5f  )/(pen/1.5f);
        float rotCompXDiff=yVel*((Math.abs(xDistance))/camWidth)*5;   //smaller and should be constant
        rotation = (float) Math.toDegrees(Math.atan(yDistance / xDistance)) + rotCompYDiff + rotCompXDiff; //the further it is the more ahead we aim when vel increases
        //System.out.println("Rot due to yDiff: " + rotCompYDiff + ", Rot due to xDiff: " + rotCompXDiff);
        if        (xDistance+position.x > position.x) {
            rotation += 180;
        } else if (yDistance+position.y > position.y) {
            rotation += 360;
        }
    }
    //private void setupManualFiring

    public void update() {
        if (Gdx.input.justTouched()  && !startedTapping) {   //if tapped and not startedTapping yet
            System.out.println("tapped and not startedTapping yet");
            startedTapping = true;
            setRotation(0, 0, -(InputHandler.scaleY(Gdx.input.getY()) - 1920) - position.y, InputHandler.scaleX(Gdx.input.getX()) - position.x);

            if (!firing) {
                setupFiring();
                timer.scheduleAtFixedRate(timerTask, 0, firingSpeedLevels[0]);
                firing = true;
                System.out.println("firing");
            }
            lastFiringSpeedLevel=firingSpeedLevels[0];
            firingSpeedLevelCheck[0]=true;   //assume we start tapping >200ms between taps and set interval to slowest
            lastTapTime=System.currentTimeMillis();

        } else if (Gdx.input.justTouched() && startedTapping) {    //if tapped and startedTapping
            firingSpdDecCounter=firingSpeedLevels.length-1; //reset
            timeSinceLastTap=System.currentTimeMillis()-lastTapTime;
            lastTapTime=System.currentTimeMillis();
            System.out.println("tapped and startedTapping, Last tap interval: "+timeSinceLastTap);

            for (int i=0;i<firingSpeedLevels.length;i++) {
                if (timeSinceLastTap < tapSpeedLevels[i] && timeSinceLastTap > tapSpeedLevels[i + 1] && !firingSpeedLevelCheck[i]) {
                    System.out.println("Set different tap interval with timeSinceLastTap " + timeSinceLastTap + " < tapSpeedLevel " + tapSpeedLevels[i] + " && firingSpeedLevelCheck at " + i + " is " + firingSpeedLevelCheck[i]);
                    for (int j = 0; j < firingSpeedLevels.length; j++) {
                        firingSpeedLevelCheck[j] = false;
                    }
                    firingSpeedLevelCheck[i] = true;
                    timerTask.cancel();
                    setupFiring();
                    System.out.println("Scheduling to fire shot in " + (int) (((lastFiringSpeedLevel - (System.currentTimeMillis() - lastShotTime)) / lastFiringSpeedLevel) * firingSpeedLevels[i]) + " ms or " + lastFiringSpeedLevel + " - (" + System.currentTimeMillis() + " - " + lastShotTime + ")) with interval of " + firingSpeedLevels[i]);
                    if ((int) (((lastFiringSpeedLevel - (System.currentTimeMillis() - lastShotTime)) / lastFiringSpeedLevel) * firingSpeedLevels[i]) >= 0) {
                        timer.scheduleAtFixedRate(timerTask, (int) (((lastFiringSpeedLevel - (System.currentTimeMillis() - lastShotTime)) / lastFiringSpeedLevel) * firingSpeedLevels[i]), firingSpeedLevels[i]);
                    } else {
                        timer.scheduleAtFixedRate(timerTask, firingSpeedLevels[i] / 2, firingSpeedLevels[i]);
                    }
                    lastFiringSpeedLevel = firingSpeedLevels[i];  //set last firing interval to new one

                    //fraction of time of last interval time passed*new interval, i.e. if last interval was 3s and last shot was 2s ago and new interval is 6s then fire in 2s not 1. i.e. (3-2 or 1)/3*6=2
                    firing = true;
                    break;
                }
            }
            
            setRotation(0, 0, -(InputHandler.scaleY(Gdx.input.getY()) - 1920) - position.y, InputHandler.scaleX(Gdx.input.getX()) - position.x);
        } else if (startedTapping) {    //if not tapped and startedTapping, test why bullets arent slowing down
            System.out.println("if "+firingSpdDecCounter+" > 0 && "+ (System.currentTimeMillis() - lastTapTime)+">"+tapSpeedLevels[firingSpdDecCounter]);
                if (firingSpdDecCounter>0 && System.currentTimeMillis() - lastTapTime > tapSpeedLevels[firingSpdDecCounter]) {   //from 0ms to 155 to 200

                    lastFiringSpeedLevel = firingSpeedLevels[firingSpdDecCounter--];
                    for (int j = 0; j < firingSpeedLevels.length; j++) {
                        firingSpeedLevelCheck[j] = false;
                    }
                    firingSpeedLevelCheck[firingSpdDecCounter] = true;
                    timerTask.cancel();
                    setupFiring();
                    System.out.println("Decrementing speed, Scheduling to fire shot in " + (int) (((lastFiringSpeedLevel - (System.currentTimeMillis() - lastShotTime)) / lastFiringSpeedLevel) * firingSpeedLevels[firingSpdDecCounter]) + " ms or " + lastFiringSpeedLevel + " - (" + System.currentTimeMillis() + " - " + lastShotTime + ")) with interval of " + firingSpeedLevels[firingSpdDecCounter]);
                    if ((int) (((lastFiringSpeedLevel - (System.currentTimeMillis() - lastShotTime)) / lastFiringSpeedLevel) * firingSpeedLevels[firingSpdDecCounter]) >= 0) {
                        timer.scheduleAtFixedRate(timerTask, (int) (((lastFiringSpeedLevel - (System.currentTimeMillis() - lastShotTime)) / lastFiringSpeedLevel) * firingSpeedLevels[firingSpdDecCounter]), firingSpeedLevels[firingSpdDecCounter]);
                    } else {
                        timer.scheduleAtFixedRate(timerTask, firingSpeedLevels[firingSpdDecCounter] / 2, firingSpeedLevels[firingSpdDecCounter]);
                    }
                    //firing = true;
                } else if (System.currentTimeMillis()-lastTapTime>2000){
                    firingSpdDecCounter=firingSpeedLevels.length-1;
                startedTapping=false;
                System.out.println("Not tapping anymore");
                if (firing) {
                    System.out.println("Stop Firing manually");
                    firing = false;
                    timerTask.cancel();
                    //System.out.println("cancelled");
                }
            }
        } else {    //ai system
            System.out.println("AI system");
            if (BirdHandler.activeBirdQueue.size() > 0) {
                if ((targetBird==null||!targetBird.isAlive) && TargetHandler.targetBird!=null && TargetHandler.targetBird.y>0) {
                    System.out.println("Activebirdqueue not empty, set target &");
                    setTarget(TargetHandler.targetBird);
                    setRotation(targetBird.xVel, targetBird.yVel,targetBird.y-position.y, targetBird.x-position.x);
                    if (!firing){
                        System.out.println("Not firing so set up firing schedule");
                        setupFiring();
                        timer.scheduleAtFixedRate(timerTask, 0, firingSpeedLevels[0]);
                        firing = true;
                        //System.out.println("firing");
                    }
                } else if (targetBird!=null){
                    //ask haoran for a better equation
                    //rotation=Math.toDegrees(Math.atan(     (position.x-targetBird.x)/(position.y/targetBird.yVel)     ));//pen is velocity but needs to be better scaled
                    setRotation( targetBird.xVel, targetBird.yVel,targetBird.y-position.y, targetBird.x-position.x);
                    System.out.println("rotating to bird"); //*****DEBUG***** gun aims at bird but doesnt shoot, stuck outside of loop somewhere
                }
            } else if (firing) {
                System.out.println("Activebirdqueue empty ai stop firing");
                firing = false;
                timerTask.cancel();
                //System.out.println("cancelled");
            }
        }
    }

    public void setTarget(BirdAbstractClass targetBird){
        this.targetBird=targetBird;
    }

    private void turretSetup(char turretType, int lvl){
        switch (turretType) {
            case ('f'): //fast firing
                dmg = 2;
                pen = 1;
                spr = 1;
                rof = 2f; //was 0.5f //(1/(0.02*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5))*1000 is ms between shots
                switch (lvl) {
                    case(0):texture=AssetHandler.f0;projTexture=AssetHandler.f0Proj;break;
                    case(1):texture=AssetHandler.f1;projTexture=AssetHandler.f1Proj;break;
                    case(2):texture=AssetHandler.f2;projTexture=AssetHandler.f2Proj;break;
                    case(3):texture=AssetHandler.f3;projTexture=AssetHandler.f3Proj;break;
                    case(4):texture=AssetHandler.f4;projTexture=AssetHandler.f4Proj;break;
                    case(5):texture=AssetHandler.f5;projTexture=AssetHandler.f5Proj;break;
                    case(6):texture=AssetHandler.f6;projTexture=AssetHandler.f6Proj;break;
                    case(7):texture=AssetHandler.f7;projTexture=AssetHandler.f7Proj;break;
                    case(8):texture=AssetHandler.f8;projTexture=AssetHandler.f8Proj;break;
                    case(9):texture=AssetHandler.f9;projTexture=AssetHandler.f9Proj;break;
                } break;
            case ('s'):
                dmg = 1;
                pen = 1;
                spr = 3;
                rof = 0.018f;
                switch (lvl) {
                    case(0):texture=AssetHandler.s0;projTexture=AssetHandler.s0Proj;break;
                    case(1):texture=AssetHandler.s1;projTexture=AssetHandler.s1Proj;break;
                    case(2):texture=AssetHandler.s2;projTexture=AssetHandler.s2Proj;break;
                    case(3):texture=AssetHandler.s3;projTexture=AssetHandler.s3Proj;break;
                    case(4):texture=AssetHandler.s4;projTexture=AssetHandler.s4Proj;break;
                    case(5):texture=AssetHandler.s5;projTexture=AssetHandler.s5Proj;break;
                    case(6):texture=AssetHandler.s6;projTexture=AssetHandler.s6Proj;break;
                    case(7):texture=AssetHandler.s7;projTexture=AssetHandler.s7Proj;break;
                    case(8):texture=AssetHandler.s8;projTexture=AssetHandler.s8Proj;break;
                    case(9):texture=AssetHandler.s9;projTexture=AssetHandler.s9Proj;break;
                } break;
            case ('d'):
                dmg = 4;
                pen = 2;
                spr = 1;
                rof = 0.01f;
                switch (lvl) {
                    case(0):texture=AssetHandler.d0;projTexture=AssetHandler.d0Proj;break;
                    case(1):texture=AssetHandler.d1;projTexture=AssetHandler.d1Proj;break;
                    case(2):texture=AssetHandler.d2;projTexture=AssetHandler.d2Proj;break;
                    case(3):texture=AssetHandler.d3;projTexture=AssetHandler.d3Proj;break;
                    case(4):texture=AssetHandler.d4;projTexture=AssetHandler.d4Proj;break;
                    case(5):texture=AssetHandler.d5;projTexture=AssetHandler.d5Proj;break;
                    case(6):texture=AssetHandler.d6;projTexture=AssetHandler.d6Proj;break;
                    case(7):texture=AssetHandler.d7;projTexture=AssetHandler.d7Proj;break;
                    case(8):texture=AssetHandler.d8;projTexture=AssetHandler.d8Proj;break;
                    case(9):texture=AssetHandler.d9;projTexture=AssetHandler.d9Proj;break;
                } break;
        }

        for (int i=0;i<lvl;i++){
            dmg*=2;
            rof*=1.5f;
            pen*=1.4f;
            if (turretType=='s'){
                spr+=1;
            }
        }
        width=texture.getRegionWidth();
        height=texture.getRegionHeight();
    }

    public float getRotation() {
        return rotation;
    }
}
