package com.kredatus.flockblockers.Handlers;

import com.badlogic.gdx.Gdx;
import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Birds.AcidBird;
import com.kredatus.flockblockers.GameObjects.Birds.FireBird;
import com.kredatus.flockblockers.GameObjects.Birds.GoldBird;
import com.kredatus.flockblockers.GameObjects.Birds.LunarBird;
import com.kredatus.flockblockers.GameObjects.Birds.NightBird;
import com.kredatus.flockblockers.GameObjects.Birds.PhoenixBird;
import com.kredatus.flockblockers.GameObjects.Birds.ThunderBird;
import com.kredatus.flockblockers.GameObjects.Birds.WaterBird;
import com.kredatus.flockblockers.GameObjects.Tower;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class BirdHandler {
    //public  static Class[] birdList ={PhoenixBird.class,  WaterBird.class,  NightBird.class, AcidBird.class, FireBird.class, ThunderBird.class, LunarBird.class, GoldBird.class};

    public static ArrayList<BirdAbstractClass> activeBirdList = new ArrayList<BirdAbstractClass>();
    public BirdAbstractClass[] test;
    private BirdAbstractClass bird;
    //public static String[] birdOrderList={"pB","wB","nB","aB","fB","tB","lB","gB"};
    private final static int[] birdNumberList=  { 1,   40,  30,  20,  20,  20,  10,  5  };
    private int[] spawnIntervals=new int[8];
    private int waveTypeCnt=0;
    //public Timer[] taskList;
    private Timer task=null;
    private final int duration = 100;
    private BgHandler bgHandler;
    private static float camWidth, camHeight;
    BirdAbstractClass birdToAdd;
    public BirdHandler(BgHandler bgHandler,  float camWidth, float camHeight) {
        this.bgHandler = bgHandler;
        this.camHeight = camHeight;
        this.camWidth = camWidth;

        //public  static Class[] birdList ={new PhoenixBird(camHeight, camWidth),  WaterBird.class,  NightBird.class, AcidBird.class, FireBird.class, ThunderBird.class, LunarBird.class, GoldBird.class};



        for (int i = 0; i < 8; i++) {
            if (i!=0){
                spawnIntervals[i] = duration / birdNumberList[i];
            } else {
                spawnIntervals[0]= 1;
            }

        }

        //birdList.add(PhoenixBird.class);

                // post a Runnable to the rendering thread that processes the result

    }

    public void setUpTimer(){
        //birdToAdd=(BirdAbstractClass) PhoenixBird.in
        task=new Timer();


                task.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    if (waveTypeCnt == 0) {
                                        birdToAdd = new PhoenixBird(camHeight, camWidth);
                                    }
                                    if (waveTypeCnt == 1) {
                                        birdToAdd = new WaterBird(camHeight, camWidth);
                                    }
                                    if (waveTypeCnt == 2) {
                                        birdToAdd = new NightBird(camHeight, camWidth);
                                    }
                                    if (waveTypeCnt == 3) {
                                        birdToAdd = new AcidBird(camHeight, camWidth);
                                    }
                                    if (waveTypeCnt == 4) {
                                        birdToAdd = new FireBird(camHeight, camWidth);
                                    }
                                    if (waveTypeCnt == 5) {
                                        birdToAdd = new ThunderBird(camHeight, camWidth);
                                    }
                                    if (waveTypeCnt == 6) {
                                        birdToAdd = new LunarBird(camHeight, camWidth);
                                    }
                                    if (waveTypeCnt == 7) {
                                        birdToAdd = new GoldBird(camHeight, camWidth);
                                    }
                                    System.out.println(birdToAdd + " bird added");
                                    activeBirdList.add(birdToAdd);
                                }
                            } );
                        } catch (Exception e) {
                            System.out.println("Could not make new " + birdToAdd + " object spawn because of error " + e);
                            e.printStackTrace();
                        }
                    }
                }, spawnIntervals[waveTypeCnt] * 1000, spawnIntervals[waveTypeCnt] * 1000);


        /*
        Gdx.app.postRunnable(new Runnable() {
            public void run() {
        final BirdAbstractClass birdToAdd = (BirdAbstractClass) birdList[waveTypeCnt].getConstructor().newInstance();
                Class<?> clazz = Class.forName("com.kredatus.flockblockers.GameObjects.Birds.PhoenixBird");
        task=new Timer();
        task.scheduleAtFixedRate(new TimerTask() {
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
/*




*/

    }


    public void update(float runTime, float delta) {
        if (waveTypeCnt==8){waveTypeCnt=0;}
        if (  (((bgHandler.getBackground().y <-camHeight/2) || (bgHandler.getBackground2().addedY!=0)) &&  (bgHandler.getBackground2().getTailY()>camHeight/2)) ){    //if halfway up bg1 or below bg2 keep the scheduleAtFixedRate timer
            if (task==null){

                System.out.println("Timer set");
                setUpTimer();
            }
            //System.out.println("In loop");

            //final PhoenixBird newBird= new PhoenixBird(camHeight,camWidth);

            //final PhoenixBird newBird = new PhoenixBird(camHeight,camWidth);



        } else {
            if (task!=null){
                System.out.println("Timer nulled");
                task=null;
                waveTypeCnt++; //nextWave when timer reset
            }
        }





/*
        if        (waveTypeCnt%8==0){           //pB



            waveTypeCnt++;
        } else if (waveTypeCnt%8==1){   //wB



            waveTypeCnt++;
        }else if (waveTypeCnt%8==2){   //nB



            waveTypeCnt++;
        }else if (waveTypeCnt%8==3){   //aB



            waveTypeCnt++;
        }else if (waveTypeCnt%8==4){   //fB



            waveTypeCnt++;
        }else if (waveTypeCnt%8==5){   //tB



            waveTypeCnt++;
        }else if (waveTypeCnt%8==6){   //lB



            waveTypeCnt++;
        }else if (waveTypeCnt%8==7){   //gB



            waveTypeCnt++;
        }

*/
        for (int i =0 ; i < activeBirdList.size(); i++){
            //System.out.println(birdsList.size());
            activeBirdList.get(i).update(delta, runTime);
            if (activeBirdList.get(i).isOffCam){
                activeBirdList.remove(i);
                //i=null; experiment performance putting this before, after or even removing it, or removing the .remove
            }

        }

    }

}
