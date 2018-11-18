package com.kredatus.flockblockers.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Handlers.AssetHandler;
import com.kredatus.flockblockers.Handlers.BirdHandler;
import com.kredatus.flockblockers.Handlers.InputHandler;
import com.kredatus.flockblockers.Handlers.TurretHandler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Erik Kredatus on 9/9/2018.
 */

public class Turret {
    private boolean firing;
    public int width, height ;
    public Vector2 position;
    private float camWidth, camHeight;
    public float dmg, pen, spr, rof;
    private float rotation;
    private Timer timer;
    private TimerTask timerTask;
    private BirdAbstractClass targetBird;
    public TextureRegion texture, projTexture;

    public Turret(char turretType, int lvl, Vector2 position, float camWidth, float camHeight){
        this.position = position ;
        this.camWidth = camWidth ;
        this.camHeight= camHeight;

        timer=new Timer();
        firing=false;
        turretSetup(turretType, lvl);

        if (position.x<camWidth/2){
            texture =  new TextureRegion(texture);
            texture.flip(false,true);
        }
        setTarget(BirdHandler.activeBirdQueue.peek());
        setupFiring();
    }

    private void setupFiring() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                //System.out.println("Added pen of "+pen);
                TurretHandler.projectileList.add(new Projectile(projTexture, dmg, pen, position, camWidth, camHeight, rotation));
            }
        };//set task to run later using timer.schedule
    }

    private void setRotation(float xVel, float yVel, float yDistance, float xDistance){
        float rotCompYDiff=((xVel*(Math.abs(yDistance)/(camHeight)))  *1.5f  )/(pen/1.5f);
        float rotCompXDiff=yVel*((Math.abs(xDistance))/camWidth)*5;   //smaller and should be constant
        rotation = (float) Math.toDegrees(Math.atan(yDistance / xDistance)) + rotCompYDiff + rotCompXDiff; //the further it is the more ahead we aim when vel increases
        System.out.println("Rot due to yDiff: " + rotCompYDiff + ", Rot due to xDiff: " + rotCompXDiff);
        if        (xDistance+position.x > position.x) {
            rotation += 180;
        } else if (yDistance+position.y > position.y) {
            rotation += 360;
        }
    }

    public void update(float delta) {
        if (Gdx.input.isTouched()) {
            setRotation(0, 0, -(InputHandler.scaleY(Gdx.input.getY())-1920)-position.y, InputHandler.scaleX(Gdx.input.getX())-position.x);
            if (!firing) {
                setupFiring();
                timer.scheduleAtFixedRate(timerTask, (int) (((1 / (rof / 3)) * 1000)/3), (int) ((1 / (rof / 3)) * 1000));
                firing = true;
                //System.out.println("firing");
            }
        } else {    //ai system
            if (BirdHandler.activeBirdQueue.size() > 0) {

                if ((targetBird==null||!targetBird.isAlive) &&TurretHandler.targetBird!=null) {
                    setTarget(TurretHandler.targetBird);
                    setRotation(targetBird.xVel, targetBird.yVel,targetBird.y-position.y, targetBird.x-position.x);
                    if (!firing){
                        setupFiring();
                        timer.scheduleAtFixedRate(timerTask, 0, (int) ((1 / (rof / 3)) * 1000));
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
                rof = 1;
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
                rof = 0.5f;
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
