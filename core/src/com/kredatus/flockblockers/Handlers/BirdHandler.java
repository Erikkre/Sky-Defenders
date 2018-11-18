package com.kredatus.flockblockers.Handlers;


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
    public ConcurrentLinkedQueue<BirdAbstractClass> birdQueue=new ConcurrentLinkedQueue<BirdAbstractClass>();

    public static ConcurrentLinkedQueue<BirdAbstractClass> activeBirdQueue=new ConcurrentLinkedQueue<BirdAbstractClass>();
    public static ConcurrentLinkedQueue<BirdAbstractClass> deadBirdQueue=new ConcurrentLinkedQueue<BirdAbstractClass>();

    //public static String[] birdOrderList={"pB","wB","nB","aB","fB","tB","lB","gB"};
    private final static int[] birdNumberList=  { 1,   40,  30,  20,  20,  20,  10,  5  };
    private float[] spawnIntervals=new float[8];
    private int waveTypeCnt=0;
    public TimerTask task;
    private Timer timer;
    private final float duration = 40;
    private BgHandler bgHandler;
    private float camWidth, camHeight;
    //BirdAbstractClass birdToAdd;
    boolean taskRunning;
    public BirdHandler(BgHandler bgHandler,  float camWidth, float camHeight) {

        this.bgHandler = bgHandler;
        this.camHeight = camHeight;
        this.camWidth = camWidth;

        //public  static Class[] birdList ={new PhoenixBird(camHeight, camWidth),  WaterBird.class,  NightBird.class, AcidBird.class, FireBird.class, ThunderBird.class, LunarBird.class, GoldBird.class};

        for (int i = 0; i < 8; i++) {
            if (i!=0){
                spawnIntervals[i] = duration / birdNumberList[i];
            } else {
                spawnIntervals[0]=0;
            }
        }
        timer=new Timer();
        taskRunning=false;

        //birdList.add(PhoenixBird.class);
        // post a Runnable to the rendering thread that processes the result
    }

    public void setUpTask() {
        //birdToAdd=(BirdAbstractClass) PhoenixBird.in
        task = new TimerTask() {
            @Override
            public void run() {
                if (birdQueue.size() > 0) {
                    activeBirdQueue.add(birdQueue.poll());  //removes it or returns null if empty
                }
                //System.out.println(activeBirdQueue);
            }
        };
    }
                //timer.scheduleAtFixedRate(task, 0, (int)(spawnIntervals[waveTypeCnt] * 1000));
                //task.run();
        /*
        Gdx.app.postRunnable(new Runnable() {
            public void run() {
        final BirdAbstractClass birdToAdd = (BirdAbstractClass) birdList[waveTypeCnt].getConstructor().newInstance();
                Class<?> clazz = Class.forName("com.kredatus.flockblockers.GameObjects.Birds.PhoenixBird");
        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    //PhoenixBird newBird= new PhoenixBird(camHeight,camWidth);
                    //Class<?> clazz = Class.forName("com.kredatus.flockblockers.GameObjects.Birds.PhoenixBird");

                    activeBirdList.add(birdToAdd);
                } catch (Exception e) {
                    System.out.println("Could not make new " + birdList[waveTypeCnt].toString() + " object spawn because of error" + e);
                    e.printStackTrace();
                }
            }
        }, spawnIntervals[waveTypeCnt] * 1000, spawnIntervals[waveTypeCnt] * 1000);    //1000 for milliseconds
            }
        });
*/


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
                        birdQueue.add(new WaterBird(camHeight, camWidth));
                    }
                } else if (waveTypeCnt == 2) {
                    for (int i = 0; i < birdNumberList[waveTypeCnt]; i++) {
                        birdQueue.add(new NightBird(camHeight, camWidth));
                    }
                } else if (waveTypeCnt == 3) {
                    for (int i = 0; i < birdNumberList[waveTypeCnt]; i++) {
                        birdQueue.add(new AcidBird(camHeight, camWidth));
                    }
                } else if (waveTypeCnt == 4) {
                    for (int i = 0; i < birdNumberList[waveTypeCnt]; i++) {
                        birdQueue.add(new FireBird(camHeight, camWidth));
                    }
                } else if (waveTypeCnt == 5) {
                    for (int i = 0; i < birdNumberList[waveTypeCnt]; i++) {
                        birdQueue.add(new ThunderBird(camHeight, camWidth));
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
                    timer.schedule(task, 5000);
                } else {
                    timer.scheduleAtFixedRate(task, 4500, (int) (spawnIntervals[waveTypeCnt] * 1000));
                }
               //task.run();
                //activeBirdQueue.remove(0);
                taskRunning = true;
                System.out.println("Timer started");
            }

        } else {
            if (taskRunning){
                System.out.println("Timer cancelled");
                task.cancel();
                waveTypeCnt++; //nextWave when timer reset
                taskRunning=false;
            }
        }


    }
}
