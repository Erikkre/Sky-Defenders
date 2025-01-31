// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.skydefenders.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.skydefenders.Birds.BirdAbstractClass;
import com.kredatus.skydefenders.GameWorld.GameHandler;
import com.kredatus.skydefenders.GameWorld.GameWorld;
import com.kredatus.skydefenders.Handlers.BirdHandler;
import com.kredatus.skydefenders.Handlers.InputHandler;
import com.kredatus.skydefenders.Handlers.TargetHandler;
import com.kredatus.skydefenders.Handlers.UiHandler;
import com.kredatus.skydefenders.SkyDefendersMain;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Erik Kredatus on 9/9/2018.
 */

public class Turret {
    public boolean firing, targetAquired;
    private int[] rotList =new int[] {1,2,3, 5, 7, 10, 13, 17, 22, 30}, accList=new int[] {28, 22, 16, 10, 2};    //rotation rates, accuracy disparity in degrees (divide by 2), so highest innacuracy is 14 lowest is 1 degree off from target
    public int dmgUpCounter, penUpCounter, rofUpCounter, sprUpCounter, rotUpCounter, accUpCounter,  spr, acc=accList[0], rot=rotList[0];   //rot = rotationSpeed
    public int width,height;
    public Vector2 pos, distanceFromAirship;
    private float camWidth, camHeight;
    public float dmg, pen, rof;
    public int rotation, targetRot, behindRotation, spreadAngle=50;
    private Timer timer;
    private TimerTask timerTask;
    public BirdAbstractClass targetBird;
    public TextureRegion[] texture = new TextureRegion[1];
    public TextureRegion projTexture;
    char turretType;
    public int lvl = 0, firingInterval, timeSinceLastShot=0, gunTargetPointer=-1;
    private double lastShotTime=0;

    public boolean firingStoppedByGamePause,stopTheFiringUpdateMethod;
    public Vector2 lastFingerPosition=new Vector2();
    //public boolean turretIsProjectile;
    public boolean projRotates,preThrowSpin,flipSpinDir;
    public float barrelLengthFromPos, rotAdded,  spinStartSpeed=0, preThrowActionDur=600;

    public boolean turretPullsBack, pullBackThenThrow, flipVel;
    public Vector2 origVel= new Vector2(0.3f,0), vel=new Vector2(), posOffset=new Vector2();
    public float pullBackScale=4f;
    public Sound sound;

    ConcurrentLinkedQueue<BirdAbstractClass> activeBirdQueue;
    Airship airship;TargetHandler targetHandler;
    public float rotCompYDiff,rotCompXDiff;
    public void draw(SpriteBatch batcher, float xPos, float yPos, float scale) {
        if (texture.length>1 && firingInterval-(System.currentTimeMillis()-lastShotTime)<400) {//if there are multiple frames and if 400ms or less before shot draw loaded turret
            batcher.draw(texture[1], xPos, yPos,
                    width / 2f - posOffset.x , height / 2f , width, height, scale, scale, rotation);

        } else if ( !((projRotates||turretPullsBack)&&System.currentTimeMillis()-lastShotTime<400)) {//if not right after pullbackthrow or preThrowSpin, draw
            batcher.draw(texture[0], xPos, yPos,
                    width / 2f - posOffset.x , height / 2f , width, height, scale, scale, rotation);
        }

        if (preThrowSpin) {
            if (!flipSpinDir){rotation+=rotAdded;rotAdded-=2;if (rotAdded<-15){flipSpinDir=true;}}
            else {rotation+=rotAdded;rotAdded+=1.5;}

        } else if (pullBackThenThrow){
            //System.out.println(posOffset.x);//check length of pullback is long enough then stop and shoot spear
            if (!flipVel){
                posOffset.add(vel);
                //vel.scl(1.00005f); //x1.01 faster each time
                if (posOffset.x>5) {flipVel=true;}
            } else {
                //System.out.println(vel);
                vel.scl(0.97f);
                posOffset.add(vel);//vel.scl(1.0001f);
            }
        }
    }

    public Turret(char turretType, Vector2 distanceFromAirship, Airship airship, BirdHandler birdHandler, TargetHandler targetHandler){
        this.targetHandler=targetHandler;
        this.airship=airship;
        this.activeBirdQueue=birdHandler.activeBirdQueue;
        this.pos = new Vector2(airship.pos.x+ distanceFromAirship.x,airship.pos.y- distanceFromAirship.y);
        this.distanceFromAirship = distanceFromAirship;
        this.camWidth = GameHandler.camWidth;
        this.camHeight=GameHandler.camHeight;
        this.turretType= turretType;

        timer=new Timer();
        firing=false;
        turretSetup(turretType, lvl);
        /*if (position.x<camWidth/2) {
            texture = new TextureRegion(texture);
            texture.flip(false,true);
            rotation=180;
        }*/
        targetBird=activeBirdQueue.peek();
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

    private void setupFiring() {//NEED TO ALIGN BARREL TO MIDDLE OF HEIGHT OF TEXTURE EVERY TIME TO MAKE BULLETS EXIT BARRELS EXACTLY RIGHT
        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (airship.ammo > 0) {
                    Airship.ammo--;UiHandler.totalAmmoNum--;

                    try {
                        if (!GameWorld.soundMuted) sound.play(0.5f);
                    } catch (Exception e) {
                    }
                    //System.out.println("*******************************************Last shot time: "+lastShotTime+"**********************************************************");
                    if (projRotates) {
                        rotation = targetRot;
                    }
                    if (turretType != 'c') {
                        if (spr == 1) {
                            targetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) ((barrelLengthFromPos) * Math.cos(Math.toRadians(rotation))), pos.y - (float) ((barrelLengthFromPos) * Math.sin(Math.toRadians(rotation)))), camWidth, camHeight, rotation, acc, projRotates, posOffset.x));
                        } else if (spr == 2) {
                            if (rotation >= 0 && rotation < 90) {
                                targetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) ((barrelLengthFromPos) * Math.cos(Math.toRadians(rotation))) - (float) (25 * Math.cos(Math.toRadians(90 - (rotation - 180)))), pos.y - (float) ((barrelLengthFromPos) * Math.sin(Math.toRadians(rotation))) - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc, projRotates, posOffset.x));
                                targetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) ((barrelLengthFromPos) * Math.cos(Math.toRadians(rotation))) + (float) (25 * Math.cos(Math.toRadians(90 - (rotation - 180)))), pos.y - (float) ((barrelLengthFromPos) * Math.sin(Math.toRadians(rotation))) + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc, projRotates, posOffset.x));
                            } else if (rotation >= 90 && rotation < 180) {
                                targetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) ((barrelLengthFromPos) * Math.cos(Math.toRadians(rotation))) - (float) (25 * Math.cos(Math.toRadians(rotation - 270))), pos.y - (float) ((barrelLengthFromPos) * Math.sin(Math.toRadians(rotation))) - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc, projRotates, posOffset.x));
                                targetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) ((barrelLengthFromPos) * Math.cos(Math.toRadians(rotation))) + (float) (25 * Math.cos(Math.toRadians(rotation - 270))), pos.y - (float) ((barrelLengthFromPos) * Math.sin(Math.toRadians(rotation))) + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc, projRotates, posOffset.x));
                            } else if (rotation >= 180 && rotation < 270) {
                                targetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) ((barrelLengthFromPos) * Math.cos(Math.toRadians(rotation))) - (float) (25 * Math.cos(Math.toRadians(90 - (rotation - 180)))), pos.y - (float) ((barrelLengthFromPos) * Math.sin(Math.toRadians(rotation))) - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc, projRotates, posOffset.x));
                                targetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) ((barrelLengthFromPos) * Math.cos(Math.toRadians(rotation))) + (float) (25 * Math.cos(Math.toRadians(90 - (rotation - 180)))), pos.y - (float) ((barrelLengthFromPos) * Math.sin(Math.toRadians(rotation))) + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc, projRotates, posOffset.x));
                            } else if (rotation >= 270 && rotation < 360) {
                                targetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) ((barrelLengthFromPos) * Math.cos(Math.toRadians(rotation))) - (float) (25 * Math.cos(Math.toRadians(rotation - 270))), pos.y - (float) ((barrelLengthFromPos) * Math.sin(Math.toRadians(rotation))) - (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc, projRotates, posOffset.x));
                                targetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) ((barrelLengthFromPos) * Math.cos(Math.toRadians(rotation))) + (float) (25 * Math.cos(Math.toRadians(rotation - 270))), pos.y - (float) ((barrelLengthFromPos) * Math.sin(Math.toRadians(rotation))) + (float) (15 * Math.sin(Math.toRadians(90 - rotation)))), camWidth, camHeight, rotation, acc, projRotates, posOffset.x));
                            }
                        }
                    } else {
                        for (int i = 1; i <= spr; i++) {
                            targetHandler.projectileList.add(new Projectile(projTexture, dmg, pen, new Vector2(pos.x - (float) ((barrelLengthFromPos) * Math.cos(Math.toRadians(rotation))), pos.y - (float) ((barrelLengthFromPos) * Math.sin(Math.toRadians(rotation)))), camWidth, camHeight, (rotation - (spreadAngle / 2f)) + (spreadAngle / (spr + 1f)) * i, acc, projRotates, posOffset.x));
                        }
                    }
                    lastShotTime = System.currentTimeMillis();
                }

                if (airship.ammo==0){
                    UiHandler.ammoLabel.setColor(1,0,0,1);
                    for (Turret i: airship.turretList) {
                        //i.stopTheFiringUpdateMethod=true;
                        i.stopFiring();
                    }
                }
            }
        };//set task to run later using timer.schedule
    }

    public void startFiring() {
        setupFiring();
        if (!firingStoppedByGamePause) timeSinceLastShot=(int) (System.currentTimeMillis()-lastShotTime);
        else timeSinceLastShot=(int) ((System.currentTimeMillis()-GameHandler.timeOfResume)+(GameHandler.timeOfPause-lastShotTime));    //gets time since last shot in game time without real life pause
        //System.out.println("Time since last shot: "+timeSinceLastShot+", firing interval: "+firingInterval);

        try {
            if (projRotates || turretPullsBack) {
                timer.scheduleAtFixedRate(timerTask, (int) preThrowActionDur, firingInterval);
            } else if (timeSinceLastShot < firingInterval) {
                timer.scheduleAtFixedRate(timerTask, firingInterval - timeSinceLastShot, firingInterval);
            } else {
                timer.scheduleAtFixedRate(timerTask, 0, firingInterval);
            }
            firing = true;
        } catch (IllegalStateException e) {
            //e.printStackTrace();
            //timer=new Timer();
        }
    }

    public void stopFiring() {
        firing = false;
        timerTask.cancel();
        if (preThrowSpin) preThrowSpin=false;
        if (pullBackThenThrow) pullBackThenThrow=false;
        //System.out.println("cancelled");
    }

    private void setRotation(float xVel, float yVel, float yDistance, float xDistance, boolean aimPadAiming) {
        if (!aimPadAiming) {
            rotCompYDiff = ((xVel * (Math.abs(yDistance) / (camHeight * 4))) * 1.5f) / (pen / 1.5f);
            rotCompXDiff = yVel * ((Math.abs(xDistance)) / camWidth) * 5;   //smaller and should be constant
            targetRot = (int) (Math.toDegrees(Math.atan(yDistance / xDistance)) + rotCompYDiff + rotCompXDiff); //the further it is the more ahead we aim when vel increases
            //System.out.println("Rot due to yDiff: " + rotCompYDiff + ", Rot due to xDiff: " + rotCompXDiff);

        } else {
            targetRot = (int) Math.toDegrees(Math.atan(yDistance / xDistance));
        }
        if (xDistance > 0) { //(xDistance+position.x > position.x) {
            targetRot += 180;
        } else if (yDistance > 0) {
            targetRot += 360;
        }
    }

    private void rotateToTarget() {
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
        if (!stopTheFiringUpdateMethod) {
            /*if (Gdx.input.justTouched() && gunTargetPointer == -1 && !UiHandler.isTouched) {   //airShip updates first so takes the spot

                //System.out.println("touched");
                if (Airship.airshipTouchPointer >= 0) {
                    for (int i = 0; i <= 1; i++) {
                        if (i != Airship.airshipTouchPointer && !airship.pointerOnAirship(i) && Gdx.input.isTouched(i)) {
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
            */
            if (((SkyDefendersMain)Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.aimPad.isTouched() ) {//|| (gunTargetPointer >= 0 && Gdx.input.isTouched(gunTargetPointer) && !airship.pointerOnAirship(gunTargetPointer))) {
                if (projRotates) {
                    if (firingInterval - (System.currentTimeMillis() - lastShotTime) < preThrowActionDur) {//if half a second before throw time
                        if (!preThrowSpin) {
                            preThrowSpin = true;
                            rotAdded = spinStartSpeed;
                            flipSpinDir = false;
                        }
                    } else {
                        if (preThrowSpin) preThrowSpin = false;
                    }
                } else if (turretPullsBack) {
                    if (firingInterval - (System.currentTimeMillis() - lastShotTime) < preThrowActionDur + 400) {//if half a second before throw time
                        if (!pullBackThenThrow) {
                            pullBackThenThrow = true;
                            posOffset.setZero();
                            vel.set(origVel);
                            flipVel = false;
                        }
                    } else {
                        if (pullBackThenThrow) {
                            pullBackThenThrow = false;
                            posOffset.setZero();
                        }
                    }
                }

                /*if (!UiHandler.aimPad.isTouched()) {
                    lastFingerPosition.set(InputHandler.scaleX(Gdx.input.getX(gunTargetPointer)), -(InputHandler.scaleY(Gdx.input.getY(gunTargetPointer)) - camHeight));
                    setRotation(0, 0, lastFingerPosition.y - pos.y, lastFingerPosition.x - pos.x, false);
                } else*/
                setRotation(0, 0, ((SkyDefendersMain)Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.aimPad.getKnobPercentY(), ((SkyDefendersMain)Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.aimPad.getKnobPercentX(), true);

                if (!preThrowSpin) rotateToTarget();

                //if (turretType=='s') System.out.println("rotation: "+rotation+" , targetRot: "+targetRot);
                if (!firing && (targetAquired || projRotates)) {
                    startFiring();
                }
            } else if (Gdx.input.justTouched()&&activeBirdQueue.size()>1&&targetBird!=null) {//switching between by directly pressing birds
                //if (gunTargetPointer >= 0 && (!Gdx.input.isTouched(gunTargetPointer))) {//IF NOT TOUCHED OR IF THE GUNTARGET WAS SET TO 1 AND THE ONLY LIBGDX POINTER USED IS THE AIRSHIP ONE THAT'S SET TO 0
                //gunTargetPointer = -1;                                                   //So when you check for .isTouched(1) it will return false and make gunTarget=-1 again, skipping to the ai system until justTouched happens again
                //System.out.println("GunTargetPointer set to: "+gunTargetPointer+" because "+(!Gdx.input.isTouched(gunTargetPointer))+" and "+(Airship.airshipTouchPointer==gunTargetPointer));

                //System.out.println("Set Bird if closer*****************************************");

                    //BirdAbstractClass target = null;
                    //double distance;

                        //need min 2 birds to switch between
                            //double minDistance = camHeight * 3f;
                for (BirdAbstractClass i : activeBirdQueue) {//if theres a bird closer to the reticle when we drop it than the current targetBird would be
                    //distance = Math.sqrt(Math.pow(lastFingerPosition.x - i.x, 2) + (Math.pow(lastFingerPosition.y - i.y, 2)));
                    if (i != targetBird &&
                    InputHandler.scaleX(Gdx.input.getX()) > i.x - i.width / 2 && InputHandler.scaleX(Gdx.input.getX()) < i.x + i.width / 2 &&
                            InputHandler.scaleY(Gdx.input.getY()) > i.y - i.height / 2 && InputHandler.scaleY(Gdx.input.getY()) < i.y + i.height / 2) {
                        targetBird = i;
                        break;
                    }
                }
                            //if (minDistance < Math.sqrt(Math.pow(lastFingerPosition.x - targetBird.x, 2) + (Math.pow(lastFingerPosition.y - targetBird.y, 2)))) {
                                 //targetBird= target;
                                //System.out.println("Change target");
                            //}

                     //else if (firing) {
                        //stopFiring();
                    //}

                //if (preThrowSpin) preThrowSpin = false;
                //if (pullBackThenThrow) pullBackThenThrow = false;
            } else {    //AI SYSTEM
                //System.out.println("TargetBird: "+targetBird);

                if (activeBirdQueue.size() > 0) {
                    if ((targetBird == null || !targetBird.isAlive) && targetHandler.targetBird != null) {

                        //System.out.println("if targetBird == null || !targetBird.isAlive) && targetHandler.targetBird != null");
                        targetBird = targetHandler.targetBird;
                        setRotation(targetBird.xVel, targetBird.yVel, targetBird.y - pos.y, targetBird.x - pos.x, false);
                        if (!preThrowSpin) rotateToTarget();

                    } else if (targetBird != null && targetBird.isAlive && activeBirdQueue.contains(targetBird)) {
                        //System.out.println("targetBird != null && targetBird.isAlive && activeBirdQueue.contains(targetBird)");
                        if (projRotates) {
                            if (firingInterval - (System.currentTimeMillis() - lastShotTime) < preThrowActionDur) {//if half a second before throw time
                                if (!preThrowSpin) {
                                    preThrowSpin = true;
                                    rotAdded = spinStartSpeed;
                                    flipSpinDir = false;
                                }
                            } else {
                                if (preThrowSpin) preThrowSpin = false;
                            }
                        } else if (turretPullsBack) {
                            if (firingInterval - (System.currentTimeMillis() - lastShotTime) < preThrowActionDur + 400) {//if half a second before throw time
                                if (!pullBackThenThrow) {
                                    pullBackThenThrow = true;
                                    vel.set(origVel);
                                    flipVel = false;
                                }
                            } else {
                                if (pullBackThenThrow) {
                                    pullBackThenThrow = false;
                                    posOffset.setZero();
                                }
                            }
                        }
                        //`System.out.println("2 "+ BirdHandler.activeBirdQueue);
                        //ask haoran for a better equation
                        //rotation=Math.toDegrees(Math.atan(     (position.x-targetBird.x)/(position.y/targetBird.yVel)     ));//pen is vel but needs to be better scaled
                        setRotation(targetBird.xVel, targetBird.yVel, targetBird.y - pos.y, targetBird.x - pos.x, false);
                        if (!preThrowSpin) rotateToTarget();
                        //if (turretType=='s') System.out.println("rotation: "+rotation+" , targetRot: "+targetRot);
                        if (!firing && (targetAquired || projRotates)) {
                            startFiring();
                        }
                    } else if (firing) {
                        //System.out.println("targetBird == null");
                        //System.out.print("Stop firing 1");
                        stopFiring();
                        targetBird = null;
                    }
                } else if (firing) {
                    //System.out.print("Stop firing 2");
                    stopFiring();
                    targetBird = null;
                }
            }
        }
    }

    private void turretSetup(char turretType, int lvl) {
        texture[0]=((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.turret(turretType,lvl,false);projTexture= ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.turret(turretType,lvl,true);
        sound=((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.turretSound(turretType,lvl);
        barrelLengthFromPos=0;//reset barrel length so by default projectile spawns from middle of turret position instead of from end of barrel
        projRotates=false;
        turretPullsBack=false;
        if (projTexture==null) {projTexture = texture[0];
            }    //if texture has multiple anims dont worry because it turret is not thrown

        height=texture[0].getRegionHeight();width=texture[0].getRegionWidth();
        switch (turretType) {
            case ('c'):
                dmg = 0.3f;
                pen = 1;
                spr = 2;
                rof = 1.1f;

                if (lvl==0||lvl==1) projRotates=true;
                break;
            case ('d'):
                dmg = 4;
                pen = 4;
                spr = 1;
                rof = 0.7f;

                if (lvl==0) projRotates=true;
                else if (lvl==1) turretPullsBack=true;
                else if (lvl==2) {texture=((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.turret(turretType,lvl,false).split(texture[0].getRegionWidth()/2,texture[0].getRegionHeight())[0];height=texture[0].getRegionHeight();width=texture[0].getRegionWidth();}
                break;
            case ('f'): //fast firing
                dmg = 1f;
                pen = 1;
                spr = 1;
                rof = 1.5f; //was 0.5f //(1/(0.02*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5))*1000 is ms between shots

                if (lvl==0) {barrelLengthFromPos=width/2f; }//blowgun barrel
                else if (lvl==1) projRotates=true;
                else if (lvl==2) {texture=((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.turret(turretType,lvl,false).split(texture[0].getRegionWidth()/2,texture[0].getRegionHeight())[0];height=texture[0].getRegionHeight();width=texture[0].getRegionWidth();}
                break;
        }

        for (int i=0;i<lvl;i++){
            dmg*=1.4;

            rof*= (turretType!='d' ? 1.2 : 1.06);//if not d then rof=1.2 else rof=1.06

            pen*= (turretType!='d' ? 1.4 : 1.25);
            if (turretType=='c') {spr+=1; spreadAngle+=1;}
        }

        firingInterval=(int) ((1 / (rof / 3)) * 1000);
        //System.out.println("firingInterval set as: "+firingInterval);
        //width=54;//texture[0].getRegionWidth();
        //height=54;//=texture[0].getRegionHeight();
    }
}