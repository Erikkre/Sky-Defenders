// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.kredatus.flockblockers.Birds.BirdAbstractClass;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.GameObjects.Projectile;
import com.kredatus.flockblockers.GameWorld.GameHandler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Erik Kredatus on 11/8/2018.
 */

public class TargetHandler {
    public  ConcurrentLinkedQueue<Projectile> projectileList=new ConcurrentLinkedQueue<Projectile>();

    public static BirdAbstractClass targetBird;
    //public static int minTargetingHeight=0;
    private float closestBirdDist=Float.POSITIVE_INFINITY,minTargetingHeight=0;
    private Airship airship;
    public Sound birdHit,balloonHit;
    public boolean balloonHitPlaying;
    public int activeQueueSize;
    float curDist;

    public static ConcurrentLinkedQueue<BirdAbstractClass> activeBirdQueue,deadBirdQueue;

    public TargetHandler(BirdHandler birdHandler){
        this.activeBirdQueue=birdHandler.activeBirdQueue; this.deadBirdQueue=birdHandler.deadBirdQueue;

        birdHit= ((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.manager.get(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.assets.birdHit);
        balloonHit=((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.manager.get(((FlockBlockersMain) Gdx.app.getApplicationListener()).loader.assets.balloonHit);;
    }
    public void setAirship(Airship airship){
        this.airship=airship;
    }
    public boolean isOnScreen(BirdAbstractClass b){
        return b.y>-b.height/2&&b.y< GameHandler.camHeight+b.height/2;
    }
    public void update(float delta, float runTime) {

        //if (activeBirdQueue.size()!=activeQueueSize) closestBirdDist = Float.POSITIVE_INFINITY;
        //System.out.println(BirdHandler.activeBirdQueue);
        //System.out.println(activeQueueSize+" "+targetBird);
        for (BirdAbstractClass i : activeBirdQueue) {
            i.update(delta, runTime);


            if (activeBirdQueue.size()!=activeQueueSize){
                //System.out.println("update size");
                curDist= (float) Math.sqrt(Math.pow(Math.abs(airship.pos.x-i.x),2) + Math.pow(Math.abs(airship.pos.y-airship.rackHeight.get()-i.y),2));
            }

            //System.out.println(curDist);
            //if (i.collides(airship.prelimBoundPoly1)||i.collides(airship.prelimBoundPoly2)) {
                //System.out.println("********************** HIT PRELIM *********************");

            if (i.isAlive && airship.balloonHitbox!=null && (i.collides(airship.rackHitbox) || i.collides(airship.balloonHitbox)) ) {
                    //System.out.println("********************** HIT REAL *********************");
                    airship.hit(i.health);
                    i.hit(i.origHealth+1);    //lol I hope bird health is below orig
                    if (!balloonHitPlaying){
                        balloonHit.play(0.55f);
                        balloonHitPlaying=true;
                            //System.out.println("made true");
                            Timer timer=new Timer();
                            timer.schedule(new TimerTask(){
                            @Override
                            public void run(){
                                balloonHitPlaying=false;
                                //System.out.println("made false");
                            }
                        },450);
                    }
            } else if (!i.isAlive) {
                activeBirdQueue.remove(i);
                deadBirdQueue.add(i);

                if (i==targetBird) {targetBird.isAlive=false;closestBirdDist = Float.POSITIVE_INFINITY;break;}
                    //System.out.println("closestBirdDist reset");}

                //if (i==targetBird)targetBird=null;
            } else if (i.isAboveCam()) {
                activeBirdQueue.remove(i);
                if (i==targetBird) {targetBird.isAlive=false;closestBirdDist = Float.POSITIVE_INFINITY;break;}
            } else if ( curDist < closestBirdDist ) {
                //System.out.println("closer bird************************************");
                closestBirdDist = curDist;
                targetBird = i;
            }
        }

        if (closestBirdDist!=Float.POSITIVE_INFINITY&& activeBirdQueue.size()!=activeQueueSize) {activeQueueSize=activeBirdQueue.size();}
            //System.out.println("stop rechecking after size change");}//if last bird in queue set queue size to new one to stop rechecking after queue size change
        //previousBirdHeight=minTargetingHeight; //in case some birds are moved past lead bird before any bird dies, need to check top bird every time

        for (Projectile i : projectileList){    //could have dead bird higher than alive bird, so need separate loader, alive, dead lists
            i.update();
            if (i.isGone){
                projectileList.remove(i);
            }
            for (BirdAbstractClass j : activeBirdQueue) {
                if (i.pen>0 && !j.hitBulletList.contains(i) && j.collides(i.boundingRect) && j.health>0) {  //if bird j is colliding with bullet i and was not already hit before
                    j.hit(i.dmg);

                    //System.out.println("Bullet --, pen was "+i.pen);
                    i.pen--;
                    birdHit.play(0.05f);
                    if (i.pen<1){
                        //System.out.println("Bullet exhausted");
                        projectileList.remove(i);
                    } else {
                        j.hitBulletList.add(i);
                    }

                }
            }
        }
        for (BirdAbstractClass i : deadBirdQueue){
            i.update(delta, runTime);
            if (i.isOffCam()&&i.dropsList==null) {
                deadBirdQueue.remove(i);

            }
        }
    }
}