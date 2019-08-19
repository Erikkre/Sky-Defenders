// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.skydefenders.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.kredatus.skydefenders.Birds.AcidBird;
import com.kredatus.skydefenders.Birds.BirdAbstractClass;
import com.kredatus.skydefenders.Birds.FireBird;
import com.kredatus.skydefenders.Birds.GoldBird;
import com.kredatus.skydefenders.Birds.LunarBird;
import com.kredatus.skydefenders.Birds.NightBird;
import com.kredatus.skydefenders.Birds.PhoenixBird;
import com.kredatus.skydefenders.Birds.ThunderBird;
import com.kredatus.skydefenders.Birds.WaterBird;
import com.kredatus.skydefenders.SkyDefendersMain;
import com.kredatus.skydefenders.GameObjects.Airship;
import com.kredatus.skydefenders.GameObjects.Projectile;
import com.kredatus.skydefenders.GameWorld.GameHandler;
import com.kredatus.skydefenders.GameWorld.GameWorld;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by Erik Kredatus on 11/8/2018.
 */

public class TargetHandler {
    public  ConcurrentLinkedQueue<Projectile> projectileList=new ConcurrentLinkedQueue<Projectile>();

    public static BirdAbstractClass targetBird;
    //public static int minTargetingHeight=0;
    public float closestBirdDist=Float.POSITIVE_INFINITY,minTargetingHeight=0;
    private Airship airship;
    public Sound birdHit,balloonHit,balloonDeath,thunderBirdDeath,waterBirdDeath,fireBirdDeath,acidBirdDeath,nightBirdDeath,lunarBirdDeath,goldBirdDeath,phoenixBirdDeath;
    public static Sound resourceGather;

    public boolean balloonHitPlaying;
    public ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(30);
    public int activeQueueSize;
    float curDist;
    private Random r=new Random();

    public static ConcurrentLinkedQueue<BirdAbstractClass> activeBirdQueue,deadBirdQueue;

    public TargetHandler(BirdHandler birdHandler){
        this.activeBirdQueue=birdHandler.activeBirdQueue; this.deadBirdQueue=birdHandler.deadBirdQueue;
        AssetManager m=((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.manager;AssetHandler a=((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.assets;

        resourceGather=m.get(a.resourceGather);
        birdHit= m.get(a.birdHit);
        balloonHit=m.get(a.balloonHit);balloonDeath=m.get(a.balloonDeath);
        thunderBirdDeath=m.get(a.thunderBirdDeath);waterBirdDeath=m.get(a.waterBirdDeath);fireBirdDeath=m.get(a.fireBirdDeath);acidBirdDeath=m.get(a.acidBirdDeath);
        nightBirdDeath=m.get(a.nightBirdDeath);lunarBirdDeath=m.get(a.lunarBirdDeath);goldBirdDeath=m.get(a.goldBirdDeath);phoenixBirdDeath=m.get(a.phoenixBirdDeath);

        Sound[] list={balloonHit,birdHit,balloonDeath,thunderBirdDeath,waterBirdDeath,fireBirdDeath,acidBirdDeath,nightBirdDeath,lunarBirdDeath,goldBirdDeath,phoenixBirdDeath};
        for (Sound i:list){
            i.play(0);
        }

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
        for (BirdAbstractClass bird : activeBirdQueue) {
            bird.update(delta, runTime);


            if (activeBirdQueue.size()!=activeQueueSize){
                //System.out.println("update size");
                curDist= (float) Math.sqrt(Math.pow(Math.abs(Airship.pos.x-bird.x),2) + Math.pow(Math.abs(Airship.pos.y-Airship.rackHeight.get()-bird.y),2));
            }

            //System.out.println(curDist);
                //if (bird.collides(Airship.prelimBoundPoly1)||bird.collides(Airship.prelimBoundPoly2)) {
                //System.out.println("********************** HIT PRELIM *********************");

                if (bird.isAlive && airship.balloonHitbox!=null && (bird.collides(airship.rackHitbox) || bird.collides(airship.balloonHitbox)) ) {

                    if (Airship.health+Airship.armor<=bird.origHealth) {
                        bird.hit(bird.origHealth+1);    //lol I hope bird health is below orig
                        //Gdx.input.vibrate(100);
                        if (!GameWorld.soundMuted)balloonDeath.play(0.8f);
                        ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.world.deathAndRestart();
                    } else {
                        //Gdx.input.vibrate(10);
                        if (!GameWorld.soundMuted) balloonHit.play(0.55f,r.nextFloat()*0.6f+0.7f,1);
                        airship.hit(bird.origHealth);
                        ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.renderer.makeTransition(100, 0, 0, 0.5f);
                        bird.hit(bird.origHealth+1);    //lol I hope bird health is below orig
                    }

                    ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.followFadeAwayNumberEffect(airship,-bird.origHealth,45,1.5f,2f);



                } else if (!bird.isAlive) {
                    //System.out.println("closestBirdDist reset");
                //System.out.println("bird added to death queue because dead");
                activeBirdQueue.remove(bird);
                deadBirdQueue.add(bird);
                if (!bird.birdHitPlaying) chooseWhichBirdHitToPlay(bird,true);
                if (bird==targetBird) {targetBird.isAlive=false;closestBirdDist = Float.POSITIVE_INFINITY;break;}


                //if (bird==targetBird)targetBird=null;
            } else if (bird.isAboveCam()) {
                //System.out.println("bird removed because above cam");
                activeBirdQueue.remove(bird);
                if (bird==targetBird) {targetBird.isAlive=false;closestBirdDist = Float.POSITIVE_INFINITY;break;}
            } else if ( curDist < closestBirdDist ) {
                //System.out.println("closer bird************************************");
                closestBirdDist = curDist;
                targetBird = bird;
            }
            //System.out.println((bird.y-bird.height/2)+", camHeight: "+bird.camHeight);
        }

        if (closestBirdDist!=Float.POSITIVE_INFINITY && activeBirdQueue.size()!=activeQueueSize) {//System.out.println("activeQueueSize=activeBirdQueue.size();");
            activeQueueSize=activeBirdQueue.size();}
            //System.out.println("stop rechecking after size change");}//if last bird in queue set queue size to new one to stop rechecking after queue size change
        //previousBirdHeight=minTargetingHeight; //in case some birds are moved past lead bird before any bird dies, need to check top bird every time

        for (Projectile proj : projectileList){    //could have dead bird higher than alive bird, so need separate loader, alive, dead lists
            proj.update();
            if (proj.isGone){
                projectileList.remove(proj);
            }
            for (final BirdAbstractClass bird : activeBirdQueue) {
                if (proj.pen>0 && !bird.hitBulletList.contains(proj) && bird.collides(proj.boundingRect) && bird.health>0) {  //if bird bird is colliding with bullet proj and was not already hit before
                    if (!GameWorld.soundMuted&&!bird.birdHitPlaying) { chooseWhichBirdHitToPlay(bird,false);
                            bird.birdHitPlaying=true;timer.schedule(new Runnable() {@Override public void run() {
                                bird.birdHitPlaying=false;
                            }
                        },bird.hitSoundLengthMS, TimeUnit.MILLISECONDS);//wait specific bird call length before playing sound again
                    }
                    bird.hit(proj.dmg);
                    ((SkyDefendersMain) Gdx.app.getApplicationListener()).loader.gameHandler.uiHandler.fadeAwayNumberEffect(proj.position,-(int)Math.ceil(proj.dmg),60,1.25f,1);
                    //System.out.println("Bullet --, pen was "+proj.pen);
                    proj.pen--;

                    if (proj.pen<1){
                        //System.out.println("Bullet exhausted");
                        projectileList.remove(proj);
                    } else {
                        bird.hitBulletList.add(proj);
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
    public void chooseWhichBirdHitToPlay(BirdAbstractClass b, boolean death){
        float v=0.12f;float p;
        if (!death)p=0.9f+r.nextFloat()*0.2f;else p=1.2f+r.nextFloat()*0.3f;
        if (b instanceof ThunderBird) thunderBirdDeath.play(v,p,1);
        else if (b instanceof FireBird) fireBirdDeath.play(v,p,1);
        else if (b instanceof WaterBird) waterBirdDeath.play(v,p,1);
        else if (b instanceof AcidBird) acidBirdDeath.play(v,p,1);
        else if (b instanceof NightBird) nightBirdDeath.play(v,p,1);
        else if (b instanceof LunarBird) lunarBirdDeath.play(v,p,1);
        else if (b instanceof GoldBird) goldBirdDeath.play(v,p,1);
        else if (b instanceof PhoenixBird) phoenixBirdDeath.play(v,p,1);
    }
}