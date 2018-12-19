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
    private boolean firing;
    public int aiUp, dmgUpCounter, penUpCounter, rofUpCounter, sprUpCounter, rotUpCounter, width, height, spr, rot=3;   //rot = rotationSpeed
    public Vector2 position;
    private float camWidth, camHeight;
    public float dmg, pen, rof;
    private int rotation, targetRot, behindRotation, spreadAngle=60;
    private Timer timer;
    private TimerTask timerTask;
    private BirdAbstractClass targetBird;
    public TextureRegion texture, projTexture;
    char turretType;
    public int lvl = 0, firingInterval, timeSinceLastShot;
    private double lastShotTime=0;
    public Turret(char turretType, Vector2 position, float camWidth, float camHeight){
        this.position =position ;
        this.camWidth =camWidth ;
        this.camHeight=camHeight;
        this.turretType= turretType;

        timer=new Timer();
        firing=false;
        turretSetup(turretType, lvl);

        if (position.x<camWidth/2) {
            texture = new TextureRegion(texture);
            texture.flip(false,true);
        }
        setTarget(BirdHandler.activeBirdQueue.peek());
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
        restartFiring();
        rofUpCounter++;
        rof*=1.2;
        restartFiring();
    }
    public void rotUp(){
        rotUpCounter++;
        rot++;
    }

    public void sprUp(){
        sprUpCounter++;
        spr++;
        restartFiring();
    }
    public void aiUp(){
        aiUp++;
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
                if (turretType!='s') {
                    if (spr==1) {
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, position, camWidth, camHeight, rotation));
                    } else if (spr==2) {
                        if (rotation >= 0 && rotation < 90) {
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(position.x - (float) (25 * Math.cos(Math.toRadians(90-(rotation-180)))), position.y - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation));
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(position.x + (float) (25 * Math.cos(Math.toRadians(90-(rotation-180)))), position.y + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation));
                        } else if (rotation>=90&&rotation<180){
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(position.x - (float) (25 * Math.cos(Math.toRadians(rotation-270))), position.y - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation));
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(position.x + (float) (25 * Math.cos(Math.toRadians(rotation-270))), position.y + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation));
                        } else if (rotation>=180&&rotation<270){
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(position.x - (float) (25 * Math.cos(Math.toRadians(90-(rotation-180)))), position.y - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation));
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(position.x + (float) (25 * Math.cos(Math.toRadians(90-(rotation-180)))), position.y + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation));
                        } else if (rotation>=270&&rotation<360){
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(position.x - (float) (25 * Math.cos(Math.toRadians(rotation-270))), position.y - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation));
                            TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(position.x + (float) (25 * Math.cos(Math.toRadians(rotation-270))), position.y + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation));
                        }
                    }
                } else {
                    for (int i=1;i<=spr;i++){
                        TargetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, position, camWidth, camHeight, (rotation-(spreadAngle/2))+(spreadAngle/(spr+1))*i));
                    }
                }
                lastShotTime=System.currentTimeMillis();
            }
        };//set task to run later using timer.schedule
    }


    private void startFiring() {
        setupFiring();
        timeSinceLastShot=(int) (System.currentTimeMillis()-lastShotTime);
        if (timeSinceLastShot < firingInterval) {
            timer.scheduleAtFixedRate(timerTask, firingInterval-timeSinceLastShot, firingInterval);
        } else {
            timer.scheduleAtFixedRate(timerTask, 0, firingInterval);
        }
        firing = true;
    }


    private void stopFiring(){
        firing = false;
        timerTask.cancel();
        //System.out.println("cancelled");
    }

    private void setRotation(float xVel, float yVel, float yDistance, float xDistance){
        float rotCompYDiff=((xVel*(Math.abs(yDistance)/(camHeight*4)))  *1.5f  )/(pen/1.5f);
        float rotCompXDiff=yVel*((Math.abs(xDistance))/camWidth)*5;   //smaller and should be constant
        targetRot = (int) (Math.toDegrees(Math.atan(yDistance / xDistance)) + rotCompYDiff + rotCompXDiff); //the further it is the more ahead we aim when vel increases
        //System.out.println("Rot due to yDiff: " + rotCompYDiff + ", Rot due to xDiff: " + rotCompXDiff);
        if        (xDistance+position.x > position.x) {
            targetRot += 180;
        } else if (yDistance+position.y > position.y) {
            targetRot += 360;
        }
    }

    private void rotateToTarget(){
        behindRotation=rotation-180;
        if (behindRotation<0){
            behindRotation+=360;
        }

        /*if ((targetRot > behindRotation && targetRot < rotation) ||
                (rotation < 180 && (targetRot > behindRotation ||
                        targetRot < rotation))  ){
            rotation-=rot;
            
        } else if ((targetRot < behindRotation && targetRot > rotation) ||
                (rotation > 180 && (targetRot < behindRotation ||
                        targetRot > rotation))  ){
            rotation+=rot;
        }*/

        if (Math.abs(rotation-targetRot)>3 ) {
            if (rotation <= 180) {
                if (targetRot >
                        rotation && targetRot < behindRotation)
                    rotation += rot;
                else
                    rotation -= rot;
            } else {
                if (targetRot <
                        rotation && targetRot > behindRotation)
                    rotation -= rot;
                else
                    rotation += rot;
            }
        }
    }

    public boolean rotationCloseToTarget(){
        return Math.abs(rotation-targetRot)<12 || rotation+(360-targetRot)<12;
    }

    public void update() {
        //System.out.println(rotation);
        if (Gdx.input.isTouched()) {   //***************************************************if tapped and not startedTapping yet***********************************************************************************************
            setRotation(0, 0, -(InputHandler.scaleY(Gdx.input.getY()) - camHeight) - position.y, InputHandler.scaleX(Gdx.input.getX()) - position.x);
            rotateToTarget();
            if (turretType=='s') System.out.println("rotation: "+rotation+" , targetRot: "+targetRot);
            if (!firing && rotationCloseToTarget()) {
                startFiring();
            }
        } else {    //****************************************************************************************************ai system****************************************************************************************************
            //System.out.println("AI system");
            if (BirdHandler.activeBirdQueue.size() > 0) {
                if ((targetBird==null||!targetBird.isAlive) && TargetHandler.targetBird!=null && TargetHandler.targetBird.y>TargetHandler.minTargetingHeight) {
                    //System.out.println("Activebirdqueue not empty, set target &");
                    setTarget(TargetHandler.targetBird);
                    setRotation(targetBird.xVel, targetBird.yVel,targetBird.y-position.y, targetBird.x-position.x);
                    rotateToTarget();

                } else if (targetBird!=null){
                    //ask haoran for a better equation
                    //rotation=Math.toDegrees(Math.atan(     (position.x-targetBird.x)/(position.y/targetBird.yVel)     ));//pen is velocity but needs to be better scaled
                    setRotation( targetBird.xVel, targetBird.yVel,targetBird.y-position.y, targetBird.x-position.x);
                    rotateToTarget();
                    if (turretType=='s') System.out.println("rotation: "+rotation+" , targetRot: "+targetRot);
                    if (!firing && rotationCloseToTarget()) {
                        startFiring();
                    }
                }
            } else if (firing) {
                stopFiring();
            }
        }
    }

    public void setTarget(BirdAbstractClass targetBird){
        targetBird=targetBird;
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
                rof = 1f;
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
                    } break;
            case ('d'):
                dmg = 4;
                pen = 2;
                spr = 1;
                rof = 0.5f;
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
                spr+=2;
            }
        }

        firingInterval=(int) ((1 / (rof / 3)) * 1000);
        width=texture.getRegionWidth();
        height=texture.getRegionHeight();
    }

    public float getRotation() {
        return rotation;
    }
}
