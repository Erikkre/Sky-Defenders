// Copyright (c) 2019 Erik Kredatus. All rights reserved.
package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.kredatus.flockblockers.Birds.AcidBird;
import com.kredatus.flockblockers.Birds.BirdAbstractClass;
import com.kredatus.flockblockers.Birds.FireBird;
import com.kredatus.flockblockers.Birds.GoldBird;
import com.kredatus.flockblockers.Birds.LunarBird;
import com.kredatus.flockblockers.Birds.NightBird;
import com.kredatus.flockblockers.Birds.PhoenixBird;
import com.kredatus.flockblockers.Birds.ThunderBird;
import com.kredatus.flockblockers.Birds.WaterBird;
import com.kredatus.flockblockers.FlockBlockersMain;
import com.kredatus.flockblockers.GameObjects.Airship;
import com.kredatus.flockblockers.GameWorld.GameWorld;
import com.kredatus.flockblockers.NonGameHandlerScreens.Loader;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BirdHandler {
    //public  static Class[] birdList ={PhoenixBird.class,  WaterBird.class,  NightBird.class, AcidBird.class, FireBird.class, ThunderBird.class, LunarBird.class, GoldBird.class};
    public  ConcurrentLinkedQueue<BirdAbstractClass> birdQueue=new ConcurrentLinkedQueue<BirdAbstractClass>();

    public  ConcurrentLinkedQueue<BirdAbstractClass> activeBirdQueue=new ConcurrentLinkedQueue<BirdAbstractClass>();
    public  ConcurrentLinkedQueue<BirdAbstractClass> deadBirdQueue=new ConcurrentLinkedQueue<BirdAbstractClass>();

                                                //0    1    2    3    4    5    6    7
    //public static String[] birdOrderList=     {"pB","tB","wB","fB","aB","nB","lB","gB"};
    public final int[] birdNumberList=          { 1,   20,  20,  25,  10,  8,   7,   3  };
    private float[] spawnIntervals=new float[8];
    public static int waveNumber;
    public TimerTask task;
    public Timer timer;
    public float duration = 45 ;
    private BgHandler bgHandler;
    private float camWidth, camHeight;
    //BirdAbstractClass birdToAdd;
    boolean taskRunning;
    public double lastBirdSpawnTime;
    private ArrayList<Float> flashLengths=new ArrayList<Float>();
    Vector2 airshipPos;

    public BirdHandler( float camWidth, float camHeight, int waveNumber) {
        this.waveNumber =waveNumber;
        //this.bgHandler = bgHandler;
        this.camHeight = camHeight;
        this.camWidth  = camWidth ;

        //public  static Class[] birdList ={new PhoenixBird(camHeight, camWidth),  WaterBird.class,  NightBird.class, AcidBird.class, FireBird.class, ThunderBird.class, LunarBird.class, GoldBird.class};

        for (int i = 0; i < 8; i++) {
            if (i==0||i==3) spawnIntervals[i]=0;    //if phoenix or fire spawn immediately (0 is like null, it throws error if used, cant have 0ms interval)
            else if (i==1) spawnIntervals[i]=0.55f; //if thunder
            else if ((i==5||i==6)&&spawnIntervals[i]>8) spawnIntervals[i]=8;
            else spawnIntervals[i] = duration / birdNumberList[i];

            if ( FlockBlockersMain.fastTest) spawnIntervals[i] = 0.01f;
        }
        timer=new Timer();
        taskRunning=false;
        //birdList.add(PhoenixBird.class);
        // post a Runnable to the rendering thread that processes the result

        flashLengths();
    }
    public void setAirshipPos(Airship airship){
        this.airshipPos=airship.pos;
    }
    private void flashLengths() {
        for (double i = 0.4f; i <= 6; i += 0.1f) { //  5.6/0.05=66 poss, maxes out at a 13 second flash
            flashLengths.add((float) (Math.pow(flashLengths.size(), 0.7) / 3f + 0.3f));//desmos:y=\left(x^{0.5}+0.3\right)
            //else flashLengths.add(   (float) (                (-(Math.pow(-(flashLengths.size()-25),1.32))/50) +1.8f    ));
        }
    }

    public void setUpTask() {
        lastBirdSpawnTime=System.currentTimeMillis();
        //birdToAdd=(BirdAbstractClass) PhoenixBird.in
        if (waveNumber !=2) {//if not waterbird
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
        if ( bgHandler.isBirdSpawning ){ //(((bgHandler.getBackground().y <-camHeight/2) || (bgHandler.getBackground2().addedY!=0)) &&  (bgHandler.getBackground2().getTailY()>camHeight/2)) ){    //if halfway up bg1 or below bg2 keep the scheduleAtFixedRate timer
            if (!taskRunning) {
                //for the amount of birds in the wave,
                if (waveNumber == 0) {
                    for (int i = 0; i < birdNumberList[waveNumber]; i++) {
                        birdQueue.add(new PhoenixBird(airshipPos,camHeight, camWidth, flashLengths));
                    }
                    //System.out.println("add birds");
                } else if (waveNumber == 1) {
                    for (int i = 0; i < birdNumberList[waveNumber]; i++) {
                        birdQueue.add(new ThunderBird(airshipPos,camHeight, camWidth, flashLengths));
                    }
                    //System.out.println("add birds");
                } else if (waveNumber == 2) {
                    float height = ((TextureRegion) Loader.waterAnims[3].getKeyFrames()[3]).getRegionHeight();
                    //float width  =((TextureRegion)AssetHandler.waterAnimations[3].getKeyFrames()[0]).getRegionWidth();
                    for (int i = 0; i < birdNumberList[waveNumber] / 5; i++) {
                        birdQueue.add(new WaterBird(airshipPos,camHeight, camWidth,  (camWidth / 6) * 1, (-height / 3) * 2f,  flashLengths));
                        birdQueue.add(new WaterBird(airshipPos,camHeight, camWidth,  (camWidth/ 6) * 2, (-height / 3) * 1.5f, flashLengths));
                        birdQueue.add(new WaterBird(airshipPos,camHeight, camWidth,  (camWidth/ 6) * 3,          (-height / 3),        flashLengths));
                        birdQueue.add(new WaterBird(airshipPos,camHeight, camWidth,  (camWidth/ 6) * 4, (-height / 3) * 1.5f, flashLengths));
                        birdQueue.add(new WaterBird(airshipPos,camHeight, camWidth,  (camWidth/ 6) * 5, (-height / 3) * 2,    flashLengths));

                        /*waterArrowHeads.add(new WaterBird(camHeight, camWidth, flashLengths, (camWidth, flashLengths/6)*3));
                        waterArrowHeads.add(new WaterBird(camHeight, camWidth, flashLengths, (camWidth, flashLengths/6)*2));
                        waterRound.add(waterArrowHeads);
                        waterArrowHeads.clear();*/
                    }
                } else if (waveNumber == 3) {
                    for (int i = 0; i < birdNumberList[waveNumber]; i++) {
                        birdQueue.add(new FireBird(airshipPos,camHeight, camWidth, flashLengths, birdQueue));
                    }
                } else if (waveNumber == 4) {
                    for (int i = 0; i < birdNumberList[waveNumber]; i++) {
                        birdQueue.add(new AcidBird(airshipPos,camHeight, camWidth, flashLengths));
                    }
                } else if (waveNumber == 5) {
                    for (int i = 0; i < birdNumberList[waveNumber]; i++) {
                        birdQueue.add(new NightBird(airshipPos,camHeight, camWidth, flashLengths));
                    }
                } else if (waveNumber == 6) {
                    for (int i = 0; i < birdNumberList[waveNumber]; i++) {
                        birdQueue.add(new LunarBird(airshipPos,camHeight, camWidth, flashLengths));
                    }
                } else if (waveNumber == 7) {
                    for (int i = 0; i < birdNumberList[waveNumber]; i++) {
                        birdQueue.add(new GoldBird(airshipPos, camHeight, camWidth, flashLengths));
                    }
                }
                setUpTask();

                if (waveNumber == 2) {  //if waterbird
                    if (!FlockBlockersMain.fastTest) timer.scheduleAtFixedRate(task, 2000, (int) ((duration / (birdNumberList[waveNumber] / 5)) * 1000) / 2);   //  duration/amount of waves/2
                    else timer.scheduleAtFixedRate(task,0,1);
                } else if (waveNumber == 3 || waveNumber == 0) {  //if fire or phoenix spawn all at once in blob
                    for (int i = 0; i<birdNumberList[waveNumber]; i++){
                        activeBirdQueue.add(birdQueue.poll());
                    }
                } else {
                    timer.scheduleAtFixedRate(task, 2000, (int) (spawnIntervals[waveNumber] * 1000));
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
                waveNumber++; //nextWave when timer reset
                if (waveNumber==8){
                    waveNumber=0;
                    UiHandler.roundLabel.setText("Round "+ ++GameWorld.round);
                }
                UiHandler.waveLabel.setText("Wave "+waveNumber+"/8");
                taskRunning=false;
            }
        }
    }

    public void pause(){
        if (waveNumber !=3 && waveNumber !=0 && BgHandler.isBirdSpawning && taskRunning) task.cancel();    //if not fire or phoenix, cancel. dont make taskRunning=false
    }

    public void resume(){
        if (waveNumber !=3 && waveNumber !=0 && BgHandler.isBirdSpawning) {//if not fire or phoenix, restart
            setUpTask();
            int timeSinceLastBirdSpawn= (int) (System.currentTimeMillis() - lastBirdSpawnTime);
            int spawningInterval;

            if (waveNumber == 2) {  //if waterbird
                spawningInterval=(int) (((duration / (birdNumberList[waveNumber] / 5)) * 1000) / 2);
                if (timeSinceLastBirdSpawn < spawningInterval) timer.scheduleAtFixedRate(task, spawningInterval-timeSinceLastBirdSpawn, spawningInterval);   //  duration/amount of waves/2
                else timer.scheduleAtFixedRate(task, 2000, spawningInterval);
            } else {    //if any bird but water fire or phoenix
                spawningInterval= (int) (spawnIntervals[waveNumber] * 1000);
                if (timeSinceLastBirdSpawn < spawningInterval) timer.scheduleAtFixedRate(task, spawningInterval-timeSinceLastBirdSpawn, spawningInterval);
                else timer.scheduleAtFixedRate(task, 2000, spawningInterval);   //will never really happen because if the birds are spawning the time since the last bird should be shorter than the time it takes for it to spawn
            }
        }
    }
}
