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
    private boolean[] firingSpeeds = {false,false,false,false,false};
    public int width, height, firingInterval;
    public Vector2 position;
    private float camWidth, camHeight;
    public float dmg, pen, spr, rof;
    private float rotation;
    private Timer timer;
    private TimerTask timerTask;
    private BirdAbstractClass targetBird;
    public TextureRegion texture, projTexture;
    private double lastTapTime, lastTapInterval=1000, lastShotTime;

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
        firingInterval = (int) ((1 / (rof)) * 1000);
    }

    private void setupFiring() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //System.out.println("Added pen of "+pen);
                lastShotTime=System.currentTimeMillis();
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
            setupFiring();
            if (!firing) {
                timer.scheduleAtFixedRate(timerTask, 0, firingInterval);
                firing = true;
                System.out.println("firing");
            }
            lastTapTime=System.currentTimeMillis();
        } else if (Gdx.input.justTouched() && startedTapping) {    //if tapped and startedTapping
            System.out.println("tapped and startedTapping");

            lastTapInterval=System.currentTimeMillis()-lastTapTime;
            System.out.println("Last tap interval: "+lastTapInterval);

            if (lastTapInterval>200 && !firingSpeeds[0]){
                for (boolean i : firingSpeeds){
                    i=false;
                }
                firingSpeeds[0]=true;
            } else if (lastTapInterval>180 && !firingSpeeds[1]){
                for (boolean i : firingSpeeds){
                    i=false;
                }
                firingSpeeds[1]=true;
            } else if (lastTapInterval>170 && !firingSpeeds[2]){
                for (boolean i : firingSpeeds){
                    i=false;
                }
                firingSpeeds[2]=true;
            } else if (lastTapInterval>160 && !firingSpeeds[3]){
                for (boolean i : firingSpeeds){
                    i=false;
                }
                firingSpeeds[3]=true;
            } else if (lastTapInterval>150 && !firingSpeeds[4]){
                for (boolean i : firingSpeeds){
                    i=false;
                }
                firingSpeeds[4]=true;
            }
            lastTapTime=System.currentTimeMillis();
            setRotation(0, 0, -(InputHandler.scaleY(Gdx.input.getY()) - 1920) - position.y, InputHandler.scaleX(Gdx.input.getX()) - position.x);
        } else if (startedTapping) {    //if not tapped and startedTapping
            //System.out.println("System time: "+System.currentTimeMillis()+" , Last tap time: "+lastTapTime);
            if (System.currentTimeMillis()-lastTapTime>500){
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
            if (BirdHandler.activeBirdQueue.size() > 0) {

                if ((targetBird==null||!targetBird.isAlive) && TargetHandler.targetBird!=null && TargetHandler.targetBird.y>0) {
                    setTarget(TargetHandler.targetBird);
                    setRotation(targetBird.xVel, targetBird.yVel,targetBird.y-position.y, targetBird.x-position.x);
                    if (!firing){
                        setupFiring();
                        timer.scheduleAtFixedRate(timerTask, 0, firingInterval);
                        firing = true;
                        //System.out.println("firing");
                    }
                } else if (targetBird!=null){
                    //ask haoran for a better equation
                    //rotation=Math.toDegrees(Math.atan(     (position.x-targetBird.x)/(position.y/targetBird.yVel)     ));//pen is velocity but needs to be better scaled
                    setRotation( targetBird.xVel, targetBird.yVel,targetBird.y-position.y, targetBird.x-position.x);
                    //System.out.println(targetBird.health);
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
                rof = 0.3f; //(1/(0.02*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5*1.5))*1000 is ms between shots
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
                rof = 0.18f;
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
                rof = 0.1f;
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
