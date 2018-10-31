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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class BirdHandler {
    public  static Class[] birdList ={PhoenixBird.class,  WaterBird.class,  NightBird.class, AcidBird.class, FireBird.class, ThunderBird.class, LunarBird.class, GoldBird.class};
    public static ArrayList<BirdAbstractClass> activeBirdList = new ArrayList<BirdAbstractClass>();
    public BirdAbstractClass[] test;
    private BirdAbstractClass bird;
    //public static String[] birdOrderList={"pB","wB","nB","aB","fB","tB","lB","gB"};
    private final static int[] birdNumberList=  { 1,   40,  30,  20,  20,  20,  10,  5  };
    private int[] spawnIntervals=new int[8];
    private int waveTypeCnt;
    //public Timer[] taskList;
    private Timer task=new Timer();
    private final int duration = 100;
    private BgHandler bgHandler;
    private float camWidth, camHeight;
    public BirdHandler(BgHandler bgHandler, final float camWidth, final float camHeight){
        this.bgHandler=bgHandler;
        this.camHeight=camHeight;
        this.camWidth =camWidth;
//        test[0]=new PhoenixBird(camHeight,camWidth);
        for (int i=0;i<8;i++){
            spawnIntervals[i]=duration/birdNumberList[i];
        }
        //birdList.add(PhoenixBird.class);
        //birdList= {PhoenixBird.class,  WaterBird.class,  NightBird.class, AcidBird.class, FireBird.class, ThunderBird.class, LunarBird.class, GoldBird.class};
        //test[0].
    }

    public void update(float runTime, float delta) {
        /*if (waveTypeCnt==8){waveTypeCnt=0;}
        if (  (((bgHandler.getBackground().y <-camHeight/2) || (bgHandler.getBackground2().addedY!=0)) &&  (bgHandler.getBackground2().getTailY()>camHeight/2))    && !task.toString().equals("running")){    //if halfway up bg1 or below bg2 keep the scheduleAtFixedRate timer
            System.out.println("In loop");
            task=new Timer("running");
            task.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try{
                            activeBirdList.add((BirdAbstractClass) birdList[waveTypeCnt].newInstance());
                        } catch (Exception e){
                            System.out.println("Could not make new "+birdList[waveTypeCnt].toString()+" object spawn");
                        }
                    }
                }, 3, spawnIntervals[waveTypeCnt] * 1000);    //1000 for milliseconds

        } else {
            task=null;
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
