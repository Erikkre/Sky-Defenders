// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.GameWorld.GameHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Handlers.TargetHandler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Erik Kredatus on 9/9/2018.
 */

public class Turret {
    public boolean firing, targetAquired;
    private int[] rotList =new int[] {1,2,5, 10, 20}, accList=new int[] {28, 22, 16, 10, 2};    //rotation rates, accuracy disparity in degrees (divide by 2), so highest innacuracy is 14 lowest is 1 degree off from target
    public int dmgUpCounter, penUpCounter, rofUpCounter, sprUpCounter, rotUpCounter, accUpCounter,  spr, acc=accList[0], rot=rotList[0];   //rot = rotationSpeed
    public static int width,height;
    public Vector2 pos, origPosition;
    private float camWidth, camHeight;
    public float dmg, pen, rof;
    private int rotation, targetRot, behindRotation, spreadAngle=50;
    private Timer timer;
    private TimerTask timerTask;
    public BirdAbstractClass targetBird;
    public TextureRegion texture, projTexture;
    char turretType;
    public int lvl = 0, firingInterval, timeSinceLastShot, gunTargetPointer=-1;
    private double lastShotTime=0;

    public boolean firingStoppedByGamePause;
    public Vector2 lastFingerPosition=new Vector2();

    public Turret(char turretType, Vector2 pos){
        this.pos = pos;
        this.origPosition= pos.cpy();
        this.camWidth = GameHandler.camWidth;
        this.camHeight=GameHandler.camHeight;
        this.turretType= turretType;

        timer=new Timer();
        firing=false;
        turretSetup(turretType, lvl);
        height=texture.getRegionHeight();width=texture.getRegionWidth();
        /*if (position.x<camWidth/2) {
            texture = new TextureRegion(texture);
            texture.flip(false,true);
            rotation=180;
        }*/
        targetBird=BirdHandler.activeBirdQueue.peek();
        setupFiring();
    }

    public void restartFiring(){
        if (firing){
            stopFiring();
            startFiring();
        }
    }
    public void dmgUp(){
        dmgUpCounter++;
        dmg*=1.2;
        restartFiring();
    }
    public void penUp(){
        penUpCounter++;
        pen*=1.2;
        restartFiring();
    }
    public void rofUp(){
        rofUpCounter++;
        rof*=1.2;
        firingInterval=(int) ((1 / (rof / 3)) * 1000);
        restartFiring();
    }
    public void rotUp(){
        rot=rotList[++rotUpCounter];
    }

    public void sprUp(){
        sprUpCounter++;
        spr++;
        restartFiring();
    }
    public void accUp(){
        acc=accList[++accUpCounter];
        restartFiring();
    }
    public void lvlUp(){
        turretSetup(turretType,++lvl);
        restartFiring();
    }

    private void setupFiring() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //System.out.println("Added pen of "+pen);
                //System.out.println("*******************************************Last shot time: "+lastShotTime+"**********************************************************");
                if (turretType != 's') {
                    if (spr == 1) {
                        TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, pos, camWidth, camHeight, rotation, acc));
                    } else if (spr == 2) {
                        if (rotation >= 0 && rotation < 90) {
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) (25 * Math.cos(Math.toRadians(90 - (rotation - 180)))), pos.y - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc));
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x + (float) (25 * Math.cos(Math.toRadians(90 - (rotation - 180)))), pos.y + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc));
                        } else if (rotation >= 90 && rotation < 180) {
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) (25 * Math.cos(Math.toRadians(rotation - 270))), pos.y - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc));
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x + (float) (25 * Math.cos(Math.toRadians(rotation - 270))), pos.y + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc));
                        } else if (rotation >= 180 && rotation < 270) {
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) (25 * Math.cos(Math.toRadians(90 - (rotation - 180)))), pos.y - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc));
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x + (float) (25 * Math.cos(Math.toRadians(90 - (rotation - 180)))), pos.y + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc));
                        } else if (rotation >= 270 && rotation < 360) {
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) (25 * Math.cos(Math.toRadians(rotation - 270))), pos.y - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc));
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x + (float) (25 * Math.cos(Math.toRadians(rotation - 270))), pos.y + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc));
                        }
                    }
                } else {
                    for (int i = 1; i <= spr; i++) {
                        TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, pos, camWidth, camHeight, (rotation - (spreadAngle / 2)) + (spreadAngle / (spr + 1)) * i, acc));
                    }
                }
                lastShotTime = System.currentTimeMillis();
            }
        };//set task to run later using timer.schedule
    }

    public void startFiring() {
        setupFiring();
        if (!firingStoppedByGamePause) timeSinceLastShot=(int) (System.currentTimeMillis()-lastShotTime);
        else timeSinceLastShot=(int) ((System.currentTimeMillis()-GameHandler.timeOfResume)+(GameHandler.timeOfPause-lastShotTime));    //gets time since last shot in game time without real life pause
        //System.out.println("Time since last shot: "+timeSinceLastShot+", firing interval: "+firingInterval);
        if (timeSinceLastShot < firingInterval) {
            timer.scheduleAtFixedRate(timerTask, firingInterval-timeSinceLastShot, firingInterval);
        } else {
            timer.scheduleAtFixedRate(timerTask, 0, firingInterval);
        }
        firing = true;
    }

    public void stopFiring(){
        firing = false;
        timerTask.cancel();
        //System.out.println("cancelled");
    }

    private void setRotation(float xVel, float yVel, float yDistance, float xDistance){
        float rotCompYDiff=((xVel*(Math.abs(yDistance)/(camHeight*4)))  *1.5f  )/(pen/1.5f);
        float rotCompXDiff=yVel*((Math.abs(xDistance))/camWidth)*5;   //smaller and should be constant
        targetRot = (int) (Math.toDegrees(Math.atan(yDistance / xDistance)) + rotCompYDiff + rotCompXDiff); //the further it is the more ahead we aim when vel increases
        //System.out.println("Rot due to yDiff: " + rotCompYDiff + ", Rot due to xDiff: " + rotCompXDiff);
        if        (xDistance > 0) { //(xDistance+position.x > position.x) {
            targetRot += 180;
        } else if (yDistance > 0) {
            targetRot += 360;
        }
    }

    private void rotateToTarget(){
        behindRotation=rotation-180;
        if (behindRotation<0){
            behindRotation+=360;
        }
        //1st case is if targetRot and rot are not 1 at 270-360 and 1 at 0-90 degrees, 2nd is rot at 0-90 targetRot at 270-360, 3rd is rot at 270-360 and targetRot at 0-90. rotlist is degree step of the turn, so if target within next degree of turn just do else part of the if statement below
        targetAquired=Math.abs(rotation-targetRot)<rotList[rotUpCounter] || (rotation<=90&&(targetRot>=270&&targetRot<=360)    &&rotation-targetRot<0 && rotation-targetRot+360<rotList[rotUpCounter]) || (targetRot<=90&&(rotation>=270&&rotation<=360)   &&targetRot-rotation<0 && targetRot-rotation+360<rotList[rotUpCounter]);
        if (!targetAquired) {
            if (rotation < 180) {
                if (targetRot > rotation && targetRot < behindRotation) {
                    rotation += rot;
                } else {
                    rotation -= rot;
                    if (rotation < 0) {
                        rotation += 360;
                    }
                }
            } else {
                if (targetRot < rotation && targetRot > behindRotation) {
                    rotation -= rot;
                } else {
                    rotation += rot;
                    if (rotation > 360) {
                        rotation -= 360;
                    }
                }
            }
        } else {   //if close enough
            rotation=targetRot;
        }
    }

    public void update() {
        if (Gdx.input.justTouched()  && gunTargetPointer==-1 ) {   //airShip updates first so takes the spot

            //System.out.println("touched");
            if (Airship.airshipTouchPointer >= 0) {
                for (int i = 0; i <= 1; i++) {
                    if (i!=Airship.airshipTouchPointer&&!Airship.pointerOnAirship(i)&&Gdx.input.isTouched(i)) {
                        gunTargetPointer = i;
                        //System.out.println("Pointer "+i+" "+ Airship.pointerOnAirship(i));
                        //if (targetBird!=null)targetBird=null;
                            //so that reticle knows to stop going to targeted bird but finger instead
                        //System.out.println("Set and GunTargetPointer set to: " + gunTargetPointer);
                        break;
                    }
                }
            } else {
                gunTargetPointer = 0;
                //if (targetBird!=null)targetBird=null;

                //System.out.println("Not set and GunTargetPointer set to: " + gunTargetPointer);
            }
        }

        if (gunTargetPointer>=0&&Gdx.input.isTouched(gunTargetPointer)&&!Airship.pointerOnAirship(gunTargetPointer)){
            lastFingerPosition.set(InputHandler.scaleX(Gdx.input.getX(gunTargetPointer)),-(InputHandler.scaleY(Gdx.input.getY(gunTargetPointer)) - camHeight));
            setRotation(0, 0,  lastFingerPosition.y - pos.y,lastFingerPosition.x  - pos.x);
            rotateToTarget();
            //if (turretType=='s') System.out.println("rotation: "+rotation+" , targetRot: "+targetRot);
            if (!firing && targetAquired) {
                startFiring();
            }
        } else if (gunTargetPointer>=0&&(!Gdx.input.isTouched(gunTargetPointer))) {//IF NOT TOUCHED OR IF THE GUNTARGET WAS SET TO 1 AND THE ONLY LIBGDX POINTER USED IS THE AIRSHIP ONE THAT'S SET TO 0
            gunTargetPointer=-1;                                                   //So when you check for .isTouched(1) it will return false and make gunTarget=-1 again, skipping to the ai system until justTouched happens again
            //System.out.println("GunTargetPointer set to: "+gunTargetPointer+" because "+(!Gdx.input.isTouched(gunTargetPointer))+" and "+(Airship.airshipTouchPointer==gunTargetPointer));

            //System.out.println("Set Bird if closer*****************************************");
            BirdAbstractClass target=null;
            double distance;
            if (targetBird!=null){
                if (BirdHandler.activeBirdQueue.size()>1) {//need min 2 birds to switch between
                    double minDistance=camHeight*3f;
                    for (BirdAbstractClass i : BirdHandler.activeBirdQueue) {//if theres a bird closer to the reticle when we drop it than the current targetBird would be
                        distance=Math.sqrt(Math.pow(lastFingerPosition.x - i.x, 2) + (Math.pow(lastFingerPosition.y - i.y, 2)));
                        if (distance<minDistance) {minDistance=distance;target=i;}
                    }
                    if (minDistance < Math.sqrt(Math.pow(lastFingerPosition.x - targetBird.x, 2) + (Math.pow(lastFingerPosition.y - targetBird.y, 2)))) {
                        targetBird = target;
                        //System.out.println("Change target");
                    }
                }
            }
        } else {//AI SYSTEM
            //System.out.println("TargetBird: "+targetBird);
            if (BirdHandler.activeBirdQueue.size() > 0) {
                if ((targetBird==null||!targetBird.isAlive) && TargetHandler.targetBird!=null) {

                    //System.out.println("1");
                    targetBird=TargetHandler.targetBird;
                    setRotation(targetBird.xVel, targetBird.yVel,targetBird.y- pos.y, targetBird.x- pos.x);
                    rotateToTarget();
                } else if (targetBird!=null&&targetBird.isAlive&&BirdHandler.activeBirdQueue.contains(targetBird)){
                    //System.out.println("2 "+ BirdHandler.activeBirdQueue);
                    //ask haoran for a better equation
                    //rotation=Math.toDegrees(Math.atan(     (position.x-targetBird.x)/(position.y/targetBird.yVel)     ));//pen is vel but needs to be better scaled
                    setRotation( targetBird.xVel, targetBird.yVel,targetBird.y- pos.y, targetBird.x- pos.x);
                    rotateToTarget();
                    //if (turretType=='s') System.out.println("rotation: "+rotation+" , targetRot: "+targetRot);
                    if (!firing && targetAquired) {
                        startFiring();
                    }
                } else if (firing) {
                    //System.out.print("Stop firing 1");
                    stopFiring();
                    targetBird=null;
                }
            } else if (firing) {
                //System.out.print("Stop firing 2");
                stopFiring();
                targetBird=null;
            }
        }
    }

    private void turretSetup(char turretType, int lvl){
        switch (turretType) {
            case ('f'): //fast firing
                dmg = 1f;
                pen = 1;
                spr = 1;
                rof = 1.5f; //was 0.5f //(1/(0.02*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5))*1000 is ms between shots
                /*
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
                    } break;*/
                texture=AssetHandler.f0;projTexture=AssetHandler.f0Proj;break;
            case ('s'):
                dmg = 0.3f;
                pen = 1;
                spr = 3;
                rof = 1f;/*
                    switch (lvl) {
                        case(0):texture=AssetHandler.f0;projTexture=AssetHandler.f0Proj;break;  //beware of slight changes
                        case(1):texture=AssetHandler.s1;projTexture=AssetHandler.s1Proj;break;
                        case(2):texture=AssetHandler.s2;projTexture=AssetHandler.s2Proj;break;
                        case(3):texture=AssetHandler.s3;projTexture=AssetHandler.s3Proj;break;
                        case(4):texture=AssetHandler.s4;projTexture=AssetHandler.s4Proj;break;
                        case(5):texture=AssetHandler.s5;projTexture=AssetHandler.s5Proj;break;
                        case(6):texture=AssetHandler.s6;projTexture=AssetHandler.s6Proj;break;
                        case(7):texture=AssetHandler.s7;projTexture=AssetHandler.s7Proj;break;
                        case(8):texture=AssetHandler.s8;projTexture=AssetHandler.s8Proj;break;
                        case(9):texture=AssetHandler.s9;projTexture=AssetHandler.s9Proj;break;
                    } break;*/
            texture=AssetHandler.f0;projTexture=AssetHandler.f0Proj;break;
            case ('d'):
                dmg = 4;
                pen = 4;
                spr = 1;
                rof = 0.5f;/*
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
                    } break;*/
                texture=AssetHandler.f0;projTexture=AssetHandler.f0Proj;break;
        }

        for (int i=0;i<lvl;i++){
            dmg*=1.4;
            if (turretType=='d'){rof*=1.06;} else {rof*=1.4;}//if (turretType=='f'){rof*=1.4;} else {;}
            rof*= (turretType!='d' ? 1.2 : 1.06);//if not d then rof=1.2 else rof=1.06

            pen*= (turretType!='d' ? 1.4 : 1.25);
            if (turretType=='s') {spr+=3; spreadAngle+=1;}
        }

        firingInterval=(int) ((1 / (rof / 3)) * 1000);
        //System.out.println("firingInterval set as: "+firingInterval);
        //width=54;//texture.getRegionWidth();
        //height=54;//=texture.getRegionHeight();
    }

    public float getRotation() {
        return rotation;
    }
}
