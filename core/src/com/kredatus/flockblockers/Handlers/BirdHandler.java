package com.kredatus.flockblockers.Handlers;

import com.kredatus.flockblockers.GameObjects.BirdAbstractClass;
import com.kredatus.flockblockers.GameObjects.Birds.PhoenixBird;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class BirdHandler {

    public static ArrayList<BirdAbstractClass> birdsList = new ArrayList<BirdAbstractClass>();
    public static String[] birdOrderList={"pB","wB","nB","aB","fB","tB","lB","gB"};
    public static int[] birdNumberList=  { 1,   40,  30,  20,  20,  20,  10,  5  };
    public int[] spawnIntervals;
    public int waveNumber;
    public Timer[] taskList;
    public final int duration = 100;
    public BirdHandler(BgHandler bgHandler, float camWidth, float camHeight){

        //if (bgHandler.getBackground().texture==AssetHandler.bgPhoenixtexture2||bgHandler.getBackground2().texture==AssetHandler.bgPhoenixtexture2){

        //}

        //Probably want to spawn birds based off distance not time cuz lag


        for (int i=0;i<8;i++){
            spawnIntervals[i]=duration/birdNumberList[i];
        }
        for (Timer i:taskList)
            i.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // Your database code here
                }
            }, 2 * 60 * 1000, 2 * 60 * 1000);
        PhoenixBird bird = new PhoenixBird( camHeight,camWidth);
        birdsList.add(bird);
    }

    public void update(float runTime, float delta) {
        if (waveNumber%8==0){           //pB



            waveNumber++;
        } else if (waveNumber%8==1){   //wB



            waveNumber++;
        }else if (waveNumber%8==2){   //nB



            waveNumber++;
        }else if (waveNumber%8==3){   //aB



            waveNumber++;
        }else if (waveNumber%8==4){   //fB



            waveNumber++;
        }else if (waveNumber%8==5){   //tB



            waveNumber++;
        }else if (waveNumber%8==6){   //lB



            waveNumber++;
        }else if (waveNumber%8==7){   //gB



            waveNumber++;
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
