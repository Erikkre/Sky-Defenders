package com.kredatus.flockblockers.Handlers;

import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Birds.PhoenixBird;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class BirdHandler {
    public static ArrayList<BirdAbstractClass> birdsList = new ArrayList<BirdAbstractClass>();
    private BirdAbstractClass bird;
    public static String[] birdOrderList={"pB","wB","nB","aB","fB","tB","lB","gB"};
    private static int[] birdNumberList=  { 1,   40,  30,  20,  20,  20,  10,  5  };
    private int[] spawnIntervals;
    private int waveTypeCnt;
    //public Timer[] taskList;
    private Timer task;
    private final int duration = 100;
    private BgHandler bgHandler;
    private float camWidth, camHeight;
    public BirdHandler(BgHandler bgHandler, final float camWidth, final float camHeight){
        this.bgHandler=bgHandler;
        this.camHeight=camHeight;
        this.camWidth =camWidth;
        //if (bgHandler.getBackground().texture==AssetHandler.bgPhoenixtexture2||bgHandler.getBackground2().texture==AssetHandler.bgPhoenixtexture2){

        //}
        //Probably want to spawn birds based off distance not time cuz lag

        for (int i=0;i<8;i++){
            spawnIntervals[i]=duration/birdNumberList[i];
        }
  



        birdsList.add(bird);
    }

    public void update(float runTime, float delta) {
        if (  (((bgHandler.getBackground().y<-camHeight/2) || (bgHandler.getBackground2().addedY!=0)) &&  (bgHandler.getBackground2().getTailY()>camHeight/2))    && task == null){    //if halfway up bg1 or below bg2 keep the scheduleAtFixedRate timer
            if (task==null) {
                task.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        bird = new PhoenixBird(camHeight, camWidth);
                    }
                }, 0, spawnIntervals[waveTypeCnt++] * 1000);
            }
        } else {

        }






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


        for (int i =0 ; i < birdsList.size(); i++){
            //System.out.println(birdsList.size());
            birdsList.get(i).update(delta, runTime);
            if (birdsList.get(i).isOffCam){
                birdsList.remove(i);
                //i=null; experiment performance putting this before, after or even removing it, or removing the .remove
            }

        }

    }

}
