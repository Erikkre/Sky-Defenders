// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Birds.AcidBird;
import com.kredatus.flockblockers.GameObjects.Birds.FireBird;
import com.kredatus.flockblockers.GameObjects.Birds.GoldBird;
import com.kredatus.flockblockers.GameObjects.Birds.LunarBird;
import com.kredatus.flockblockers.GameObjects.Birds.NightBird;
import com.kredatus.flockblockers.GameObjects.Birds.PhoenixBird;
import com.kredatus.flockblockers.GameObjects.Birds.ThunderBird;
import com.kredatus.flockblockers.GameObjects.Birds.WaterBird;
import com.kredatus.flockblockers.GameObjects.Projectile;
import com.kredatus.flockblockers.GameObjects.Turret;


import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;


public class BirdHandler {
    //public  static Class[] birdList ={PhoenixBird.class,  WaterBird.class,  NightBird.class, AcidBird.class, FireBird.class, ThunderBird.class, LunarBird.class, GoldBird.class};
    public static ConcurrentLinkedQueue<BirdAbstractClass> birdQueue=new ConcurrentLinkedQueue<BirdAbstractClass>();

    public static ConcurrentLinkedQueue<BirdAbstractClass> activeBirdQueue=new ConcurrentLinkedQueue<BirdAbstractClass>();
    public static ConcurrentLinkedQueue<BirdAbstractClass> deadBirdQueue=new ConcurrentLinkedQueue<BirdAbstractClass>();

                                                //0    1    2    3    4    5    6    7
    //public static String[] birdOrderList=     {"pB","tB","wB","fB","aB","nB","lB","gB"};
    public final int[] birdNumberList=  { 1,   30,  30,  30,  15,  10,  10,  5  };
    private float[] spawnIntervals=new float[8];
    public int waveTypeCnt=5;
    public TimerTask task;
    public Timer timer;
    public final float duration = 40;
    private BgHandler bgHandler;
    private float camWidth, camHeight;
    //BirdAbstractClass birdToAdd;
    boolean taskRunning;
    public double lastBirdSpawnTime;

    public BirdHandler(BgHandler bgHandler,  float camWidth, float camHeight) {

        this.bgHandler = bgHandler;
        this.camHeight = camHeight;
        this.camWidth = camWidth;

        //public  static Class[] birdList ={new PhoenixBird(camHeight, camWidth),  WaterBird.class,  NightBird.class, AcidBird.class, FireBird.class, ThunderBird.class, LunarBird.class, GoldBird.class};

        for (int i = 0; i < 8; i++) {
            if (i==0) spawnIntervals[i]=0;
            else if (i==1||i==3) spawnIntervals[i]=0.01f;
            else spawnIntervals[i] = duration / birdNumberList[i];
        }
        timer=new Timer();
        taskRunning=false;

        //birdList.add(PhoenixBird.class);
        // post a Runnable to the rendering thread that processes the result
    }

    public void setUpTask() {
        lastBirdSpawnTime=System.currentTimeMillis();
        //birdToAdd=(BirdAbstractClass) PhoenixBird.in
        if (waveTypeCnt !=2) {//if not waterbird
            task = new TimerTask() {
                @Override
                public void run() {
                    if (birdQueue.size() > 0) {
                        activeBirdQueue.add(birdQueue.poll());  //removes it or returns null if empty
                    }
                    //System.out.println(activeBirdQueue);
                }
            };
        } else {
            task = new TimerTask() {
                @Override
                public void run() {
                    if (birdQueue.size() > 0) {
                        activeBirdQueue.add(birdQueue.poll());  //removes it or returns null if empty
                        activeBirdQueue.add(birdQueue.poll());
                        activeBirdQueue.add(birdQueue.poll());
                        activeBirdQueue.add(birdQueue.poll());
                        activeBirdQueue.add(birdQueue.poll());
                    }
                }
            };
        }
    }

    public void update() {
        if (waveTypeCnt==8){waveTypeCnt=0;}
        if ( bgHandler.isBirdSpawning ){ //(((bgHandler.getBackground().y <-camHeight/2) || (bgHandler.getBackground2().addedY!=0)) &&  (bgHandler.getBackground2().getTailY()>camHeight/2)) ){    //if halfway up bg1 or below bg2 keep the scheduleAtFixedRate timer
            if (!taskRunning) {
                //for the amount of birds in the wave,
                if (waveTypeCnt == 0) {
                    for (int i = 0; i < birdNumberList[waveTypeCnt]; i++) {
                        birdQueue.add(new PhoenixBird(camHeight, camWidth));
                    }
                } else if (waveTypeCnt == 1) {
                    for (int i = 0; i < birdNumberList[waveTypeCnt]; i++) {
                        birdQueue.add(new ThunderBird(camHeight, camWidth));
                    }
                } else if (waveTypeCnt == 2) {
                    float height = ((TextureRegion) AssetHandler.waterAnimations[3].getKeyFrames()[3]).getRegionHeight();
                    //float width  =((TextureRegion)AssetHandler.waterAnimations[3].getKeyFrames()[0]).getRegionWidth();
                    for (int i = 0; i < birdNumberList[waveTypeCnt] / 5; i++) {
                        birdQueue.add(new WaterBird(camHeight, camWidth, (camWidth / 6) * 1, (-height / 3) * 2f));
                        birdQueue.add(new WaterBird(camHeight, camWidth, (camWidth / 6) * 2, (-height / 3) * 1.5f));
                        birdQueue.add(new WaterBird(camHeight, camWidth, (camWidth / 6) * 3,          (-height / 3)    ));
                        birdQueue.add(new WaterBird(camHeight, camWidth, (camWidth / 6) * 4, (-height / 3) * 1.5f));
                        birdQueue.add(new WaterBird(camHeight, camWidth, (camWidth / 6) * 5, (-height / 3) * 2));

                        /*waterArrowHeads.add(new WaterBird(camHeight, camWidth, (camWidth/6)*3));
                        waterArrowHeads.add(new WaterBird(camHeight, camWidth, (camWidth/6)*2));
                        waterRound.add(waterArrowHeads);
                        waterArrowHeads.clear();*/
                    }
                } else if (waveTypeCnt == 3) {
                    for (int i = 0; i < birdNumberList[waveTypeCnt]; i++) {
                        birdQueue.add(new FireBird(camHeight, camWidth));
                    }
                } else if (waveTypeCnt == 4) {
                    for (int i = 0; i < birdNumberList[waveTypeCnt]; i++) {
                        birdQueue.add(new AcidBird(camHeight, camWidth));
                    }
                } else if (waveTypeCnt == 5) {
                    for (int i = 0; i < birdNumberList[waveTypeCnt]; i++) {
                        birdQueue.add(new NightBird(camHeight, camWidth));
                    }
                } else if (waveTypeCnt == 6) {
                    for (int i = 0; i < birdNumberList[waveTypeCnt]; i++) {
                        birdQueue.add(new LunarBird(camHeight, camWidth));
                    }
                } else if (waveTypeCnt == 7) {
                    for (int i = 0; i < birdNumberList[waveTypeCnt]; i++) {
                        birdQueue.add(new GoldBird(camHeight, camWidth));
                    }
                }

                setUpTask();

                if (waveTypeCnt == 0) {
                    timer.schedule(task, 4000);
                } else if (waveTypeCnt == 2) {  //if waterbird
                    timer.scheduleAtFixedRate(task, 4500, (int) ((duration / (birdNumberList[waveTypeCnt] / 5)) * 1000) / 2);   //  duration/amount of waves/2
                } else if (waveTypeCnt == 3) {  //if fire spawn all at once in blob
                    for (int i=0; i<birdNumberList[3]; i++){
                        activeBirdQueue.add(birdQueue.poll());
                    }
                } else {
                    timer.scheduleAtFixedRate(task, 4500, (int) (spawnIntervals[waveTypeCnt] * 1000));
                }
               //task.run();
                //activeBirdQueue.remove(0);
                taskRunning = true;
                //System.out.println("Timer started");
            }

        } else {
            if (taskRunning){
                //System.out.println("Timer cancelled");
                task.cancel();
                waveTypeCnt++; //nextWave when timer reset
                taskRunning=false;
            }
        }
    }

    public void pause(){
        if (waveTypeCnt!=3 && waveTypeCnt!=0 && bgHandler.isBirdSpawning) task.cancel();    //if not fire or phoenix, cancel. dont make taskRunning=false
    }

    public void resume(){
        if (waveTypeCnt!=3 && waveTypeCnt!=0 && bgHandler.isBirdSpawning) {//if not fire or phoenix, restart

            setUpTask();
            int timeSinceLastBirdSpawn= (int) (System.currentTimeMillis() - lastBirdSpawnTime);
            int spawningInterval;

            if (waveTypeCnt == 2) {  //if waterbird
                spawningInterval=(int) (((duration / (birdNumberList[waveTypeCnt] / 5)) * 1000) / 2);
                if (timeSinceLastBirdSpawn < spawningInterval) timer.scheduleAtFixedRate(task, spawningInterval-timeSinceLastBirdSpawn, spawningInterval);   //  duration/amount of waves/2
                else timer.scheduleAtFixedRate(task, 4500, spawningInterval);

            } else {    //if any bird but water fire or phoenix
                spawningInterval= (int) (spawnIntervals[waveTypeCnt] * 1000);
                if (timeSinceLastBirdSpawn < spawningInterval) timer.scheduleAtFixedRate(task, spawningInterval-timeSinceLastBirdSpawn, spawningInterval);
                else timer.scheduleAtFixedRate(task, 4500, spawningInterval);   //will never really happen because if the birds are spawning the time since the last bird should be shorter than the time it takes for it to spawn

            }
        }

    }
}
